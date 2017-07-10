package com.github.masonm.wiremock.model;

import com.github.masonm.wiremock.model.JsExtendExtensionRegistry;
import com.github.masonm.wiremock.model.JsExtendUserExtension;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsExtendRequestMatcherExtensionAdapter extends RequestMatcherExtension {
    @Override
    public String getName() {
        return "jsextend-requestmatch";
    }

    @Override
    public MatchResult match(Request request, Parameters parameters) {
        List<JsExtendUserExtension> extensions = JsExtendExtensionRegistry
            .getInstance()
            .getExtensionsOfType(RequestMatcherExtension.class);

        List<MatchResult> results = new ArrayList<>(extensions.size());

        for (JsExtendUserExtension extension : extensions) {
            try {
                MatchResult matchResult = (MatchResult) extension
                    .getInvocable()
                    .invokeFunction("match", request, parameters);
                results.add(matchResult);
            } catch (ScriptException |NoSuchMethodException e) {
                return throwUnchecked(e, MatchResult.class);
            }
        }

        return MatchResult.aggregate(results);
    }
}
