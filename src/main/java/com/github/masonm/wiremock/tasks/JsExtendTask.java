package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.*;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public abstract class JsExtendTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        String type = pathParams.get("type");
        if (type == null || type.length() == 0) {
            return jsonResponse("Must supply extension type", HTTP_BAD_REQUEST);
        }

        CompositeExtension compositeExtension;
        try {
            compositeExtension = getCompositeExtension(type);
        } catch (ClassNotFoundException ex) {
            return jsonResponse("Invalid type", HTTP_BAD_REQUEST);
        }

        return doExecute(compositeExtension, request, pathParams);
    }

    protected abstract ResponseDefinition doExecute(CompositeExtension compositeExtension, Request request, PathParams pathParams);

    private CompositeExtension getCompositeExtension(String type) throws ClassNotFoundException {
        switch (type) {
            case "RequestMatcherExtension":
                return new CompositeRequestMatcherExtension();
            case "ResponseTransformer":
                return new CompositeResponseTransformer();
            case "ResponseDefinitionTransformer":
                return new CompositeResponseDefinitionTransformer();
            case "StubMappingTransformer":
                return new CompositeStubMappingTransformer();
            default:
                throw new ClassNotFoundException();
        }
    }
}
