package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.JsExtension;
import com.github.masonm.wiremock.model.JsExtendType;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendGetExtensionTask extends JsExtendTask {
    @Override
    public ResponseDefinition doExecute(JsExtendType jsExtendType, Request request, PathParams pathParams) {
        UUID id;
        try {
            id = getIdFromParams(pathParams);
        } catch (IllegalArgumentException ex) {
            return jsonResponse(ex.getMessage(), HTTP_BAD_REQUEST);
        }

        Map<String, JsExtension> jsExtensions = jsExtendType.getCompositeExtension().getExtensions();
        if (!jsExtensions.containsKey(id)) {
            return ResponseDefinition.notFound();
        }

        return ResponseDefinition.okForJson(jsExtensions.get(id).getSpec());
    }
}
