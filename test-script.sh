#!/bin/bash

set -e

reset() {
        curl -s -X DELETE http://localhost:8080/__admin/extensions > /dev/null
        curl -s -X DELETE http://localhost:8080/__admin/mappings > /dev/null
}

addExtension() {
        JSON="{ \"type\": \"${1}\", \"javascript\": \"${2//$'\n'/'\n'}\" }"
        curl -s -d "${JSON}" http://localhost:8080/__admin/extensions | sed "s/^/\t/"
}

testRequest() {
        curl -s http://localhost:8080/test | sed "s/^/\t/"
}

echo "Testing with ResponseDefinitionTransformer"
reset

echo -e "\tCreate test stub mapping"
curl -s -d '{ "request": { "method": "ANY" }, "response": { "body": "Hello" } }' http://localhost:8080/__admin/mappings > /dev/null

echo -e "\tCreate transformer:"
addExtension 'ResponseDefinitionTransformer' 'function transform(request, responseDefinition, files, pathParams) {
        return new ResponseDefinition(201, \"TRANSFORMED!\");
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest

echo -e "\n\n\tCreate another transformer:"
addExtension 'ResponseDefinitionTransformer' 'function transform(request, responseDefinition, files, pathParams) {
        return new ResponseDefinition(201, responseDefinition.getBody() + \" AGAIN!\");
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest



echo -e "\n\nTesting with ResponseTransformer"
reset

echo -e "\tCreate test stub mapping"
curl -s -d '{ "request": { "method": "ANY" }, "response": { "body": "Hello" } }' http://localhost:8080/__admin/mappings > /dev/null

echo -e "\tCreate transformer:"
addExtension 'ResponseTransformer' 'function transform(request, response, files, pathParams) {
        return Response.Builder.like(response).but().body(\"TRANSFORMED!\").build();
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest

echo -e "\n\n\tCreate another transformer:"
addExtension 'ResponseTransformer' 'function transform(request, response, files, pathParams) {
        return Response.Builder.like(response).but().body(\"TRANSFORMED AGAIN!\").build();
}'
echo -e "\n\n\tIssuing request for test stub mapping:"
testRequest



echo -e "\n\nTesting with RequestMatcherExtension"
reset

echo -e "\tCreate test stub mapping"
curl -s -d '{
        "request": {
                "customMatcher": {
                        "name": "jsextend-requestmatch",
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
addExtension 'RequestMatcherExtension' 'function match(request, parameters) {
        var queryParam = parameters.getString(\"queryParam\");
        return MatchResult.of(request.queryParameter(queryParam).isPresent());
}'
echo -e "\n\n\tIssuing matching request:"
curl -s http://localhost:8080/test?MATCH_PARAM=foo | sed "s/^/\t/"

echo -e "\n\n\tIssuing non-matching request:"
curl -I -s http://localhost:8080/test?MATCH_PARAM2=foo | sed "s/^/\t/"
