package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.model.JsExtendExtensionSpec;
import com.github.masonm.wiremock.model.JsExtendExtensionRegistry;
import com.github.masonm.wiremock.model.JsExtendUserExtension;
import com.github.masonm.wiremock.model.JsExtendUserExtensionFactory;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import javax.script.ScriptException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class JsExtendCreateExtensionTask implements AdminTask {
    private final JsExtendUserExtensionFactory extensionFactory;

    public JsExtendCreateExtensionTask(JsExtendUserExtensionFactory extensionFactory) {
        this.extensionFactory = extensionFactory;
    }

    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        JsExtendExtensionSpec spec = Json.read(request.getBodyAsString(), JsExtendExtensionSpec.class);

        JsExtendUserExtension extension;
        try {
            extension = extensionFactory.createNew(spec);
        } catch (ScriptException ex) {
            return ResponseDefinitionBuilder.jsonResponse("Error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }

        JsExtendExtensionRegistry.getInstance().addExtension(extension);

        return ResponseDefinitionBuilder.jsonResponse(extension, HTTP_OK);
    }
}
