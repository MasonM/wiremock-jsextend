package com.github.masonm.wiremock.extension;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.StubMappingTransformer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeStubMappingTransformer
    extends StubMappingTransformer
    implements CompositeExtension<StubMappingTransformer> {

    private static final Map<UUID, StubMappingTransformer> extensions = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "composite-stub-mapping-transformer";
    }

    @Override
    public Map<UUID, StubMappingTransformer> getExtensions() {
        return extensions;
    }

    @Override
    public StubMapping transform(StubMapping stubMapping, FileSource files, Parameters parameters) {
        for (StubMappingTransformer extension : extensions.values()) {
            stubMapping = extension.transform(stubMapping, files, parameters);
        }
        return stubMapping;
    }
}
