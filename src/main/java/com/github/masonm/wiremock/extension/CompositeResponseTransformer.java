package com.github.masonm.wiremock.extension;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import java.util.HashMap;
import java.util.Map;

public class CompositeResponseTransformer
    extends ResponseTransformer
    implements CompositeExtension<ResponseTransformer> {

    private final Map<String, ResponseTransformer> extensions = new HashMap<>();

    @Override
    public String getName() {
        return "composite-response-transformer";
    }

    @Override
    public Map<String, ResponseTransformer> getExtensions() {
        return extensions;
    }

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        for (ResponseTransformer extension : extensions.values()) {
            response = extension.transform(request, response, files, parameters);
        }
        return response;
    }
}
