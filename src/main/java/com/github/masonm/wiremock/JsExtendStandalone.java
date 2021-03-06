package com.github.masonm.wiremock;

import com.github.masonm.wiremock.extension.*;
import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class JsExtendStandalone {
    private JsExtendStandalone() {}

    // When WireMock is run in standalone mode, WireMockServerRunner.run() is the entry point,
    // so we just delegate to that, passing along a CSV string with each extension class to load
    public static void main(String... args) {
        args = ArrayUtils.add(args,
            "--extensions=" + StringUtils.join(extensionClasses(), ",")
        );
        new WireMockServerRunner().run(args);
    }

    private static String[] extensionClasses() {
        return new String[] {
            JsExtendApiExtension.class.getName(),
            CompositeRequestMatcherExtension.class.getName(),
            CompositeResponseDefinitionTransformer.class.getName(),
            CompositeResponseTransformer.class.getName(),
            CompositeStubMappingTransformer.class.getName()
        };
    }
}
