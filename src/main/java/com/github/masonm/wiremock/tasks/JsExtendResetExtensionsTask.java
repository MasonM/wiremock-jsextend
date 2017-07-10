package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.model.JsExtendExtensionRegistry;
import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class JsExtendResetExtensionsTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        JsExtendExtensionRegistry.getInstance().deleteExtensions();
        return ResponseDefinition.okEmptyJson();
    }
}
