package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.extension.Extension;

public interface JsExtendUserExtension extends Extension{
    String getJavascript();
}
