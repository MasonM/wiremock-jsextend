package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsExtendRequestMatcherExtensionAdapter extends RequestMatcherExtension {
    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public MatchResult match(Request request, Parameters parameters) {
        Collection<JsExtendRequestMatcherExtension> matchers = JsExtendExtensionRegistry
            .getInstance()
            .getExtensionsOfType(JsExtendRequestMatcherExtension.class);

        List<MatchResult> results = new ArrayList<>(matchers.size());

        for (RequestMatcherExtension matcher : matchers) {
            results.add(matcher.match(request, parameters));
        }

        return MatchResult.aggregate(results);
    }
}
