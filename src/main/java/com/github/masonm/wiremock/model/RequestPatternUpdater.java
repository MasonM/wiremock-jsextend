package com.github.masonm.wiremock.model;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.*;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class RequestPatternUpdater {

    private UrlPattern url;
    private RequestMethod method;
    private Map<String, MultiValuePattern> headers = newLinkedHashMap();
    private Map<String, MultiValuePattern> queryParams = newLinkedHashMap();
    private List<ContentPattern<?>> bodyPatterns = newArrayList();
    private Map<String, StringValuePattern> cookies = newLinkedHashMap();
    private BasicCredentials basicCredentials;
    private CustomMatcherDefinition customMatcherDefinition;

    private RequestPatternUpdater() {}

    public static RequestPatternUpdater like(RequestPattern requestPattern) {
        RequestPatternUpdater builder = new RequestPatternUpdater();
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

    public RequestPatternUpdater but() {
        return this;
    }

    public RequestPatternUpdater withUrl(String url) {
        this.url = WireMock.urlEqualTo(url);
        return this;
    }

    public RequestPatternUpdater withUrlPattern(UrlPattern url) {
        this.url = url;
        return this;
    }

    public RequestPatternUpdater withHeader(String key, StringValuePattern valuePattern) {
        headers.put(key, MultiValuePattern.of(valuePattern));
        return this;
    }

    public RequestPatternUpdater withoutHeader(String key) {
        headers.put(key, MultiValuePattern.absent());
        return this;
    }

    public RequestPatternUpdater withQueryParam(String key, StringValuePattern valuePattern) {
        queryParams.put(key, MultiValuePattern.of(valuePattern));
        return this;
    }

    public RequestPatternUpdater withQueryParams(Map<String, MultiValuePattern> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    public RequestPatternUpdater withCookie(String key, StringValuePattern valuePattern) {
        cookies.put(key, valuePattern);
        return this;
    }

    public RequestPatternUpdater withBasicAuth(BasicCredentials basicCredentials) {
        this.basicCredentials = basicCredentials;
        return this;
    }

    public RequestPatternUpdater withRequestBody(StringValuePattern valuePattern) {
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
