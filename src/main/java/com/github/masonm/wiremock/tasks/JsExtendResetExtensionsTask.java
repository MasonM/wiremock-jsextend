package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.CompositeExtension;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class JsExtendResetExtensionsTask extends JsExtendTask {
    @Override
    public ResponseDefinition doExecute(CompositeExtension compositeExtension, Request request, PathParams pathParams) {
        compositeExtension.getExtensions().clear();
        return ResponseDefinition.ok();
    }
}
