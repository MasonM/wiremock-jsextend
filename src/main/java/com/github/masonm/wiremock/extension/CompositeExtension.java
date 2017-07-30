package com.github.masonm.wiremock.extension;

import com.github.tomakehurst.wiremock.extension.Extension;

import java.util.Map;

public interface CompositeExtension<T> extends Extension {
    Map<String, T> getExtensions();
}
