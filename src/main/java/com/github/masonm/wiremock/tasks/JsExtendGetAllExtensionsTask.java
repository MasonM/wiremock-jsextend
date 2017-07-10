package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.model.JsExtendExtensionRegistry;
import com.github.masonm.wiremock.model.JsExtendUserExtension;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import java.util.Collection;

public class JsExtendGetAllExtensionsTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        Collection<JsExtendUserExtension> extensions = JsExtendExtensionRegistry.getInstance().getExtensions();
        return ResponseDefinitionBuilder.jsonResponse(extensions);
    }
}
