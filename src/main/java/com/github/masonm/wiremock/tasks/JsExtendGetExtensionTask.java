package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.JsExtension;
import com.github.masonm.wiremock.model.CompositeExtensionLookup;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendGetExtensionTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        String type = pathParams.get("type");
        if (type == null || type.length() == 0) {
            return jsonResponse("Must supply extension type", HTTP_BAD_REQUEST);
        }

        String name = pathParams.get("name");
        if (name == null || name.length() == 0) {
            return jsonResponse("Must supply extension name", HTTP_BAD_REQUEST);
        }

        try {
            Map<String, JsExtension> extensions = new CompositeExtensionLookup(admin.getOptions(), type).getJsExtensions();

            if (!extensions.containsKey(name)) {
                return ResponseDefinition.notFound();
            }

            return ResponseDefinition.okForJson(extensions.get(name).getSpec());
        } catch (ClassNotFoundException ex) {
            return jsonResponse("Invalid type", HTTP_BAD_REQUEST);
        }
    }
}
