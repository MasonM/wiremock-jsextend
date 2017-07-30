package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.CompositeExtension;
import com.github.masonm.wiremock.extension.JsExtension;
import com.github.masonm.wiremock.model.JsExtensionFactory;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendPutExtensionTask extends JsExtendTask {
    private final JsExtensionFactory extensionFactory;

    public JsExtendPutExtensionTask() {
        this.extensionFactory = new JsExtensionFactory();
    }

    @Override
    public ResponseDefinition doExecute(CompositeExtension compositeExtension, Request request, PathParams pathParams) {
        String name = pathParams.get("name");
        if (name == null || name.length() == 0) {
            return jsonResponse("Must supply non-empty extension name", HTTP_BAD_REQUEST);
        }

        String javascript = request.getBodyAsString();
        if (javascript == null || javascript.length() == 0) {
            return jsonResponse("Must supply the Javascript for the extension in the request body", HTTP_BAD_REQUEST);
        }

        String extensionType = compositeExtension.getClass().getSuperclass().getSimpleName();
        JsExtension extension;

        try {
            extension = extensionFactory.createNew(name, extensionType, javascript);
        } catch (ScriptException ex) {
            return jsonResponse("Error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }
        compositeExtension.getExtensions().put(name, extension);

        return ResponseDefinition.created();
    }
}
