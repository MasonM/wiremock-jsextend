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
    "javascript": "function transform(request, response) { return Response.Builder.like(response).but().body(\"TRANSFORMED!\").build(); }"
}' http://localhost:8080/__admin/extensions > /dev/null

# Test transformer
$ curl -s http://localhost:8080/test
TRANSFORMED!
```

# Caveats/Limitations

* This extension allows arbitrary code execution. **Do not enable unless you fully understand the security implications.**
* Only response definition transformers, response transformers, and custom request matchers are supported.
* Due to limitations in Wiremock, transformer extensions are always global. Transformer parameters can be used as a workaround.
* Due to limitations in Wiremock, you cannot give a unique name for request matcher extensions. Matcher extensions are used by passing "jsextend-requestmatch" as the name, which will match the request against each extension. The match results are then aggregated and returned. As with transformer extensions, a parameter can be used as a workaround.
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
* `GET /__admin/extension/{id}` - Retrieves extension given by the ID. Example:
* `DELETE /__admin/extension/{id}` - Delete extension with the given ID.
* `PUT /__admin/extension/{id}` - Replace extension with the given ID by the supplied JSON object.
