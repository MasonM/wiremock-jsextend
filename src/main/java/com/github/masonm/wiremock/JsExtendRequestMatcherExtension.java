package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;

import javax.script.Invocable;
import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsExtendRequestMatcherExtension extends RequestMatcherExtension implements JsExtendUserExtension {
    private String name;
    private String javascript;
    private Invocable matcher;

    public JsExtendRequestMatcherExtension(String name, String javascript, Invocable matcher) {
        this.name = name;
        this.javascript = javascript;
        this.matcher = matcher;
    }

    public MatchResult match(Request request, Parameters parameters) {
        try {
            return (MatchResult) matcher.invokeFunction("match", request, parameters);
        } catch (ScriptException|NoSuchMethodException e) {
            return throwUnchecked(e, MatchResult.class);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getJavascript() {
        return javascript;
    }
}
