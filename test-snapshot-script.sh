#!/bin/bash

set -e

PROXY_BASE_URL="http://www.wiremock.org"
SNAPSHOT_REQUEST_JSON=$1

if [[ -z $SNAPSHOT_REQUEST_JSON ]]; then
        SNAPSHOT_REQUEST_JSON='{
                "outputFormat": "full",
                "persist": false,
                "extractBodyCriteria": {
                        "textSizeThreshold": "2000"
                }
        }'
fi

echo "Launching Wiremock and setting up proxying"
java -jar build/libs/wiremock*standalone*.jar 1>/dev/null 2>/dev/null & 
WIREMOCK_PID=$!
trap "kill $WIREMOCK_PID" exit

echo -n "Waiting for Wiremock to start up."
until $(curl --output /dev/null --silent --head http://localhost:8080); do
	echo -n '.'
	sleep 1
done


echo -e "done\nCreating proxy mapping"
curl -s -d '{
	"request": { "urlPattern": ".*" },
	"response": {
		"proxyBaseUrl": "'${PROXY_BASE_URL}'"
	}
}' http://localhost:8080/__admin/mappings > /dev/null

curl -X DELETE -s http://localhost:8080/__admin/extensions/stub-mapping-transformer > /dev/null
curl -X DELETE -s http://localhost:8080/__admin/requests > /dev/null

curl -s -d ' 
function transform(stubMapping) {
	var request = stubMapping.getRequest();
	if (!request.getQueryParameters()) {
		var uri = URI.create(request.getUrl());
		var newRequest = RequestPatternBuilder.like(request);
		newRequest.withUrlPattern(WireMock.urlPathEqualTo(uri.getPath()));

		Urls.splitQuery(uri).forEach(function(key, queryPattern) {
			if (!queryPattern.isSingleValued()) {
				return;
			}
			newRequest.withQueryParam(key, WireMock.equalTo(queryPattern.values()[0]));
		});

		stubMapping.setRequest(newRequest.build());
	}
	return stubMapping;
}
' http://localhost:8080/__admin/extensions/stub-mapping-transformer


echo "Making requests"
curl -s 'http://localhost:8080/robots.txt?foo=bar' > /dev/null

echo "Calling snapshot API with '${SNAPSHOT_REQUEST_JSON}'"
curl -s -X POST -d "${SNAPSHOT_REQUEST_JSON}" http://localhost:8080/__admin/recordings/snapshot
