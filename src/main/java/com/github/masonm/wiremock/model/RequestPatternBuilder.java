package com.github.masonm.wiremock.model;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.*;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class RequestPatternBuilder {

    private UrlPattern url;
    private RequestMethod method;
    private Map<String, MultiValuePattern> headers = newLinkedHashMap();
    private Map<String, MultiValuePattern> queryParams = newLinkedHashMap();
    private List<ContentPattern<?>> bodyPatterns = newArrayList();
    private Map<String, StringValuePattern> cookies = newLinkedHashMap();
    private BasicCredentials basicCredentials;
    private CustomMatcherDefinition customMatcherDefinition;

    private RequestPatternBuilder() {}

    public static RequestPatternBuilder like(RequestPattern requestPattern) {
        RequestPatternBuilder builder = new RequestPatternBuilder();
        builder.url = requestPattern.getUrlMatcher();
        builder.method = requestPattern.getMethod();
        builder.headers = requestPattern.getHeaders();
        builder.queryParams = requestPattern.getQueryParameters();
        builder.cookies = requestPattern.getCookies();
        builder.basicCredentials = requestPattern.getBasicAuthCredentials();
        builder.bodyPatterns = requestPattern.getBodyPatterns();
        builder.customMatcherDefinition = requestPattern.getCustomMatcher();
        return builder;
    }

    public RequestPatternBuilder but() {
        return this;
    }

    public RequestPatternBuilder withUrl(String url) {
        this.url = WireMock.urlEqualTo(url);
        return this;
    }

    public RequestPatternBuilder withUrlPattern(UrlPattern url) {
        this.url = url;
        return this;
    }

    public RequestPatternBuilder withHeader(String key, StringValuePattern valuePattern) {
        headers.put(key, MultiValuePattern.of(valuePattern));
        return this;
    }

    public RequestPatternBuilder withoutHeader(String key) {
        headers.put(key, MultiValuePattern.absent());
        return this;
    }

    public RequestPatternBuilder withQueryParam(String key, StringValuePattern valuePattern) {
        queryParams.put(key, MultiValuePattern.of(valuePattern));
        return this;
    }

    public RequestPatternBuilder withQueryParams(Map<String, MultiValuePattern> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public RequestPatternBuilder withCookie(String key, StringValuePattern valuePattern) {
        cookies.put(key, valuePattern);
        return this;
    }

    public RequestPatternBuilder withBasicAuth(BasicCredentials basicCredentials) {
        this.basicCredentials = basicCredentials;
        return this;
    }

    public RequestPatternBuilder withRequestBody(StringValuePattern valuePattern) {
        this.bodyPatterns.add(valuePattern);
        return this;
    }

    public RequestPattern build() {
        return customMatcherDefinition != null ?
                new RequestPattern(customMatcherDefinition) :
                new RequestPattern(
                    url,
                    method,
                    headers.isEmpty() ? null : headers,
                    queryParams.isEmpty() ? null : queryParams,
                    cookies.isEmpty() ? null : cookies,
                    basicCredentials,
                    bodyPatterns.isEmpty() ? null : bodyPatterns,
                    null
                );
    }
}
