package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.model.JsExtendExtensionRegistry;
import com.github.masonm.wiremock.model.JsExtendUserExtension;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import java.util.UUID;

public class JsExtendRemoveExtensionTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        String idString = pathParams.get("id");
        UUID id = UUID.fromString(idString);
        JsExtendUserExtension extension = JsExtendExtensionRegistry.getInstance().removeExtension(id);
        return extension != null ? ResponseDefinition.okEmptyJson() : ResponseDefinition.notFound();
    }
}
