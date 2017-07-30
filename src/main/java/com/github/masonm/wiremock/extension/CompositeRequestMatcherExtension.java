package com.github.masonm.wiremock.extension;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeRequestMatcherExtension
    extends RequestMatcherExtension
    implements CompositeExtension<RequestMatcherExtension> {

    private static final Map<String, RequestMatcherExtension> extensions = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "composite-request-matcher-extension";
    }

    @Override
    public Map<String, RequestMatcherExtension> getExtensions() {
        return extensions;
    }

    @Override
    public MatchResult match(Request request, Parameters parameters) {
        List<MatchResult> results = new ArrayList<>(extensions.size());

        for (RequestMatcherExtension extension : extensions.values()) {
            results.add(extension.match(request, parameters));
        }

        return MatchResult.aggregate(results);
    }
}
