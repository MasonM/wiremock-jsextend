package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.JsExtension;
import com.github.masonm.wiremock.model.*;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendPutExtensionTask implements AdminTask {
    private final JsExtensionFactory extensionFactory;

    public JsExtendPutExtensionTask() {
        this.extensionFactory = new JsExtensionFactory();
    }

    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        String name = pathParams.get("name");
        if (name == null || name.length() == 0) {
            return jsonResponse("Must supply non-empty extension name", HTTP_BAD_REQUEST);
        }

        String type = pathParams.get("type");
        if (type == null || type.length() == 0) {
            return jsonResponse("Must supply extension type", HTTP_BAD_REQUEST);
        }

        String javascript = request.getBodyAsString();
        if (javascript == null || javascript.length() == 0) {
            return jsonResponse("Must supply the Javascript for the extension in the request body", HTTP_BAD_REQUEST);
        }

        try {
            JsExtension extension = extensionFactory.createNew(name, type, javascript);
            new CompositeExtensionLookup(admin.getOptions(), type)
                .getJsExtensions()
                .put(name, extension);
        } catch (ClassNotFoundException ex) {
            return jsonResponse("Invalid type", HTTP_BAD_REQUEST);
        } catch (ScriptException ex) {
            return jsonResponse("Error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }

        return ResponseDefinition.created();
    }
}
