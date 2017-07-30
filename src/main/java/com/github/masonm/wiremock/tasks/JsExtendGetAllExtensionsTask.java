package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.CompositeExtension;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;

public class JsExtendGetAllExtensionsTask extends JsExtendTask {
    @Override
    public ResponseDefinition doExecute(CompositeExtension compositeExtension, Request request, PathParams pathParams) {
        return jsonResponse(compositeExtension.getExtensions());
    }
}
