package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

public class JsExtendResponseTransformerExtensionAdapter extends ResponseTransformer {
    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        Iterable<JsExtendResponseTransformerExtension> transformers = JsExtendExtensionRegistry
            .getInstance()
            .getExtensionsOfType(JsExtendResponseTransformerExtension.class);

        for (ResponseTransformer transformer : transformers) {
            response = transformer.transform(request, response, files, parameters);
        }
        return response;
    }
}
