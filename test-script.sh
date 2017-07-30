#!/bin/bash

set -e

reset() {
        curl -s -X DELETE http://localhost:8080/__admin/extensions/StubMappingTransformer > /dev/null
        curl -s -X DELETE http://localhost:8080/__admin/extensions/RequestMatcherExtension > /dev/null
        curl -s -X DELETE http://localhost:8080/__admin/extensions/ResponseTransformer > /dev/null
        curl -s -X DELETE http://localhost:8080/__admin/extensions/ResponseDefinitionTransformer > /dev/null
        curl -s -X DELETE http://localhost:8080/__admin/mappings > /dev/null
}

addExtension() {
        curl -s -X PUT -d "${3}" http://localhost:8080/__admin/extensions/$1/$2
        echo "${3}" | sed "s/^/\t\t/"
}

testRequest() {
        curl -s http://localhost:8080/test | sed "s/^/\t/"
}

echo "Testing with ResponseDefinitionTransformer"
reset

echo -e "\tCreate test stub mapping"
curl -s -d '{ "request": { "method": "ANY" }, "response": { "body": "FOO" } }' http://localhost:8080/__admin/mappings > /dev/null

echo -e "\tCreate transformer:"
addExtension 'ResponseDefinitionTransformer' 'transformer' 'function transform(request, responseDefinition) {
        return new ResponseDefinition(201, responseDefinition.getBody() + " 1st ResponseDefinitionTransformer.");
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest

echo -e "\n\n\tCreate another transformer:"
addExtension 'ResponseDefinitionTransformer' 'transformer2' 'function transform(request, responseDefinition) {
        return new ResponseDefinition(201, responseDefinition.getBody() + " 2nd ResponseDefinitionTransformer.");
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest



echo -e "\n\nTesting with ResponseTransformer"
reset

echo -e "\tCreate test stub mapping"
curl -s -d '{ "request": { "method": "ANY" }, "response": { "body": "Hello" } }' http://localhost:8080/__admin/mappings > /dev/null

echo -e "\tCreate transformer:"
addExtension 'ResponseTransformer' 'transformer' 'function transform(request, response) {
        return Response.Builder.like(response).but().body(response.getBodyAsString() + " 1st ResponseTransformer.").build();
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest

echo -e "\n\n\tCreate another transformer:"
addExtension 'ResponseTransformer' 'transformer2' 'function transform(request, response) {
        return Response.Builder.like(response).but().body(response.getBodyAsString() + " 2nd ResponseTransformer.").build();
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest



echo -e "\n\nTesting with RequestMatcherExtension"
reset

echo -e "\tCreate test stub mapping"
curl -s -d '{
        "request": {
                "customMatcher": {
                        "name": "composite-request-matcher-extension",
                        "parameters": {
                                "queryParam": "MATCH_PARAM"
                        }
                }
        },
        "response": {
                "body": "MATCHED"
        }
}' http://localhost:8080/__admin/mappings > /dev/null

echo -e "\tCreate matcher:"
addExtension 'RequestMatcherExtension' 'matcher' 'function match(request, parameters) {
        var queryParam = parameters.getString("queryParam");
        return MatchResult.of(request.queryParameter(queryParam).isPresent());
}'
echo -e "\n\n\tIssuing matching request:"
curl -s http://localhost:8080/test?MATCH_PARAM=foo | sed "s/^/\t/"

echo -e "\n\n\tIssuing non-matching request:"
curl -I -s http://localhost:8080/test?MATCH_PARAM2=foo | sed "s/^/\t/"
