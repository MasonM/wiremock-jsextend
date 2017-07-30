package com.github.masonm.wiremock.extension;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeResponseDefinitionTransformer
    extends ResponseDefinitionTransformer
    implements CompositeExtension<ResponseDefinitionTransformer> {

    private static final Map<String, ResponseDefinitionTransformer> extensions = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "composite-response-definition-transformer";
    }

    @Override
    public Map<String, ResponseDefinitionTransformer> getExtensions() {
        return extensions;
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        for (ResponseDefinitionTransformer extension : extensions.values()) {
            responseDefinition = extension.transform(request, responseDefinition, files, parameters);
        }
        return responseDefinition;
    }
}
