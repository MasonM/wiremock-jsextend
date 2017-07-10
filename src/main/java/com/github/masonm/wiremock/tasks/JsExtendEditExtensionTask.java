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
import java.util.UUID;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendEditExtensionTask implements AdminTask {
    private final JsExtendUserExtensionFactory extensionFactory;

    public JsExtendEditExtensionTask(JsExtendUserExtensionFactory extensionFactory) {
        this.extensionFactory = extensionFactory;
    }

    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        String idString = pathParams.get("id");
        UUID id = UUID.fromString(idString);
        if (JsExtendExtensionRegistry.getInstance().getExtension(id) == null) {
            return ResponseDefinition.notFound();
        }

        JsExtendExtensionSpec spec = Json.read(request.getBodyAsString(), JsExtendExtensionSpec.class);
        spec.setId(id);

        JsExtendUserExtension extension;
        try {
            extension = extensionFactory.createNew(spec);
        } catch (ScriptException ex) {
            return ResponseDefinitionBuilder.jsonResponse("Error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }

        JsExtendExtensionRegistry.getInstance().addExtension(extension);

        return ResponseDefinition.okForJson(extension);
    }
}
