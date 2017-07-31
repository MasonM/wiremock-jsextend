package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.model.JsExtendType;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendRemoveExtensionTask extends JsExtendTask {
    @Override
    public ResponseDefinition doExecute(JsExtendType jsExtendType, Request request, PathParams pathParams) {
        String name = pathParams.get("name");
        if (name == null || name.length() == 0) {
            return jsonResponse("Must supply extension name", HTTP_BAD_REQUEST);
        }

        jsExtendType.getCompositeExtension().getExtensions().remove(name);
        return ResponseDefinition.ok();
    }
}
