package com.github.masonm.wiremock.extension;

import com.github.tomakehurst.wiremock.extension.Extension;

import java.util.Map;
import java.util.UUID;

public interface CompositeExtension<T> extends Extension {
    Map<UUID, T> getExtensions();
}
