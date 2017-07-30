package com.github.masonm.wiremock.tasks;

import com.github.masonm.wiremock.extension.CompositeExtension;
import com.github.masonm.wiremock.extension.JsExtension;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.jsonResponse;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class JsExtendGetExtensionTask extends JsExtendTask {
    @Override
    public ResponseDefinition doExecute(CompositeExtension compositeExtension, Request request, PathParams pathParams) {
        String name = pathParams.get("name");
        if (name == null || name.length() == 0) {
            return jsonResponse("Must supply extension name", HTTP_BAD_REQUEST);
        }

        if (!compositeExtension.getExtensions().containsKey(name)) {
            return ResponseDefinition.notFound();
        }

        JsExtension extension = (JsExtension) compositeExtension.getExtensions().get(name);
        return ResponseDefinition.okForJson(extension.getSpec());
    }
}
