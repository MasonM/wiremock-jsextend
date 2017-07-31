package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.JsExtension;
import com.github.masonm.wiremock.model.JsExtendType;
import com.github.masonm.wiremock.model.JsExtensionFactory;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import javax.script.ScriptException;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendCreateExtensionTask extends JsExtendTask {
    private final JsExtensionFactory extensionFactory;

    public JsExtendCreateExtensionTask(JsExtensionFactory extensionFactory) {
        this.extensionFactory = extensionFactory;
    }

    @Override
    public ResponseDefinition doExecute(JsExtendType jsExtendType, Request request, PathParams pathParams) {
        String javascript = request.getBodyAsString();
        if (javascript == null || javascript.length() == 0) {
            return jsonResponse("Must supply the Javascript for the extension in the request body", HTTP_BAD_REQUEST);
        }

        Map<UUID, JsExtension> jsExtensions = jsExtendType.getCompositeExtension().getExtensions();

        try {
            JsExtension extension = extensionFactory.createNew(jsExtendType, javascript);
            jsExtensions.put(extension.getSpec().getId(), extension);
        } catch (ScriptException ex) {
            return jsonResponse("Script execution error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }

        return ResponseDefinition.created();
    }
}
