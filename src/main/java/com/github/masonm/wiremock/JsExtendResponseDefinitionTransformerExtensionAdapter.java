package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class JsExtendResponseDefinitionTransformerExtensionAdapter extends ResponseDefinitionTransformer {
    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        Iterable<JsExtendResponseDefinitionTransformerExtension> transformers = JsExtendExtensionRegistry
            .getInstance()
            .getExtensionsOfType(JsExtendResponseDefinitionTransformerExtension.class);

        for (ResponseDefinitionTransformer transformer : transformers) {
            responseDefinition = transformer.transform(request, responseDefinition, files, parameters);
        }
        return responseDefinition;
    }
}
