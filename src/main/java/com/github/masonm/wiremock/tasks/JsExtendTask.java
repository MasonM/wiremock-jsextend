package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.model.JsExtendType;
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

        String enumName = type.toUpperCase().replace("-", "_");
        JsExtendType typeInfo;
        try {
            typeInfo = JsExtendType.valueOf(enumName);
        } catch (IllegalArgumentException ex) {
            return jsonResponse("Invalid type", HTTP_BAD_REQUEST);
        }

        return doExecute(typeInfo, request, pathParams);
    }

    protected abstract ResponseDefinition doExecute(JsExtendType jsExtendType, Request request, PathParams pathParams);
}
