package com.github.masonm.wiremock.model;

import com.github.masonm.wiremock.extension.JsExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeExtensionSingleton {
    public final Map<String, List<JsExtension>> extensions = new ConcurrentHashMap<>();
}
