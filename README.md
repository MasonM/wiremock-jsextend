# Overview

wiremock-jsextend is a "meta-extension" for [WireMock](http://wiremock.org) that lets you create and manage extensions written in Javascript using the [Nashorn JavaScript engine](http://www.n-k.de/riding-the-nashorn/) (with a fallback to Rhino for Java 7). It adds API endpoints to create, list, edit, and delete extensions. Example:

```sh
# Stub mapping for testing
$ curl -s -d '{
    "request": { "url": "/test" },
    "response": { "body": "TESTING" }
}' http://localhost:8080/__admin/mappings > /dev/null

# Create response transformer
$ curl -s -d '{
    "type": "ResponseTransformer",
    "javascript": "function transform(request, response) {'\
'       return Response.Builder.like(response).but().body(\"TRANSFORMED!\").build();'\
'   }"
}' http://localhost:8080/__admin/extensions > /dev/null

# Test transformer
$ curl -s http://localhost:8080/test
TRANSFORMED!
```

# Caveats/Limitations

* This extension allows arbitrary code execution. **Do not use this unless you fully understand the security implications.**
* Only response definition transformers, response transformers, and custom request matchers are supported.
* Due to limitations in Wiremock, transformer extensions are always global. Transformer parameters can be used as a workaround.
* Due to limitations in Wiremock, you cannot name request matcher extensions. See the [Request Matching](#request-matching) section below.
* No load testing has been done yet, so I have no idea how well this will work with a large number of requests.

# Building

Run `gradle jar` to build the JAR without dependencies or `gradle fatJar` to build a standalone JAR.
These will be placed in `build/libs/`.

# Running

Standalone server:
```sh
java -jar build/libs/wiremock-jsextend-0.1a-standalone.jar
```

With WireMock standalone JAR:
```sh
java \
        -cp wiremock-standalone.jar:build/libs/wiremock-snapshot-0.3a.jar \
        com.github.tomakehurst.wiremock.standalone.WireMockServerRunner \
        --extensions="com.github.masonm.wiremock.extension.JsExtendApiExtension,com.github.masonm.wiremock.extension.JsExtendRequestMatcherExtensionAdapter,com.github.masonm.wiremock.extension.JsExtendResponseDefinitionTransformerExtensionAdapter,com.github.masonm.wiremock.extension.JsExtendResponseTransformerExtensionAdapter"
```

Programmatically in Java:
```java
new WireMockServer(
    wireMockConfig().extensions(
        "com.github.masonm.wiremock.extension.JsExtendApiExtension",
        "com.github.masonm.wiremock.extension.JsExtendRequestMatcherExtensionAdapter",
        "com.github.masonm.wiremock.extension.JsExtendResponseDefinitionTransformerExtensionAdapter",
        "com.github.masonm.wiremock.extension.JsExtendResponseTransformerExtensionAdapter"
    )
)
```

# Usage

## API Endpoints

The extension adds the following admin API endpoints:
* `POST /__admin/extensions` - Create new extension defined by a JSON object and returns registered extension with ID. The POSTed JSON must be in the form:
```json
{
  "type": "ResponseTransformer|ResponseDefinitionTransformer|RequestMatcherExtension",
  "javascript": "function transform(..) OR function match(...)"
}
```
* `GET /__admin/extensions` - Returns array of registered extensions. Example:
* `DELETE /__admin/extensions` - Deletes all registered extensions.
* `GET /__admin/extensions/{id}` - Retrieves extension given by the ID. Example:
* `DELETE /__admin/extensions/{id}` - Delete extension with the given ID.
* `PUT /__admin/extensions/{id}` - Replace extension with the given ID by the supplied JSON object.

## Javascript API

Extensions must consist of a single Javascript function with a prototype matching the corresponding Java class:
* [ResponseTransformer](https://github.com/tomakehurst/wiremock/blob/42a18081701390b034a7ceb1a5281a2858afa68b/src/main/java/com/github/tomakehurst/wiremock/extension/ResponseTransformer.java#L25). Example:
    ```javascript
    function transform(request, response, files, parameters) {
        return Response.Builder.like(response).but().body("TRANSFORMED!").build();
    }
    ```
* [ResponseDefinitionTransformer](https://github.com/tomakehurst/wiremock/blob/42a18081701390b034a7ceb1a5281a2858afa68b/src/main/java/com/github/tomakehurst/wiremock/extension/ResponseTransformer.java#L25). Example:
    ```javascript
    function transform(request, responseDefinition, files, parameters) {
            return new ResponseDefinition(201, "TRANSFORMED!");
    }
    ```
* [RequestMatcherExtension](https://github.com/tomakehurst/wiremock/blob/42a18081701390b034a7ceb1a5281a2858afa68b/src/main/java/com/github/tomakehurst/wiremock/matching/RequestMatcherExtension.java#L32). Example:
    ```javascript
    function match(request, parameters) {
        var queryParam = parameters.getString("queryParam");
        return MatchResult.of(request.queryParameter(queryParam).isPresent());
    }
    ```

If you're using Java 8, then `RequestMatcherExtension`, `ResponseDefinition`, `ResponseDefinitionBuilder`, `Response`, and `MatchResult` are already imported for you. To import other classes, [use the Java.type() extension](http://www.n-k.de/riding-the-nashorn/#_invoking_java_methods_from_javascript).

## Request Matching

Due to Wiremock limitations, it's not possible to specify the name for a new request matcher. To use a request matcher, pass `"jsextend-requestmatch"` as the name, along with any parameters you want to use. This will cause wiremock-jsextend to match the request against every matcher you've registered, in the order they were registered. The results are  aggregated using `MatchResult.aggregate()` and returned. You can use a parameter to ensure only one of your matchers if used, e.g.:
```sh
# Define request matcher that returns MatchResult.exactMatch() unless the parameter "urlHasFoo" is present
$ curl -s -d '{
    "type": "RequestMatcherExtension",
    "javascript": "function match(request, parameters) {'\
'        if (!parameters || !parameters.containsKey(\"urlHasFoo\")) {'\
'            return MatchResult.exactMatch();'\
'        }'\
'        return MatchResult.of(request.getUrl().indexOf(\"foo\") != -1);'\
'    }"
}' http://localhost:8080/__admin/extensions > /dev/null

# Define test stub mapping that passes the "urlHasFoo" parameter
$ curl -s -d '{
    "request": {
        "customMatcher": {
            "name": "jsextend-requestmatch",
            "parameters": {
                "urlHasFoo": true
            }
        }
    },
    "response": { "body": "MATCHED!" }
}' http://localhost:8080/__admin/mappings > /dev/null

# Test it!
$ curl -s http://localhost:8080/foo
MATCHED!

$ curl -I -s http://localhost:8080/bar
HTTP/1.1 404 Not Found
Cache-Control: must-revalidate,no-cache,no-store
Content-Type: text/html; charset=ISO-8859-1
Content-Length: 286
Server: Jetty(9.2.13.v20150730)
```
