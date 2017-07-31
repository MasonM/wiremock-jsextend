package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.model.JsExtendType;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class JsExtendResetExtensionsTask extends JsExtendTask {
    @Override
    public ResponseDefinition doExecute(JsExtendType jsExtendType, Request request, PathParams pathParams) {
        jsExtendType.getCompositeExtension().getExtensions().clear();
        return ResponseDefinition.ok();
    }
}
