package com.github.masonm.wiremock.extension;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeResponseTransformer
    extends ResponseTransformer
    implements CompositeExtension<ResponseTransformer> {

    private static final Map<UUID, ResponseTransformer> extensions = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "composite-response-transformer";
    }

    @Override
    public Map<UUID, ResponseTransformer> getExtensions() {
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
