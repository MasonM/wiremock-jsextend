package com.github.masonm.wiremock.extension;

import com.github.masonm.wiremock.model.JsExtensionFactory;
import com.github.masonm.wiremock.tasks.*;
import com.github.tomakehurst.wiremock.admin.Router;
import com.github.tomakehurst.wiremock.extension.AdminApiExtension;

import static com.github.tomakehurst.wiremock.http.RequestMethod.*;

public class JsExtendApiExtension implements AdminApiExtension {

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public void contributeAdminApiRoutes(Router router) {
        JsExtensionFactory extensionFactory = new JsExtensionFactory();

        router.add(GET, "/extensions/{type}", JsExtendGetAllExtensionsTask.class);
        router.add(POST, "/extensions/{type}", new JsExtendCreateExtensionTask(extensionFactory));
        router.add(DELETE, "/extensions/{type}", JsExtendResetExtensionsTask.class);

        router.add(GET, "/extensions/{type}/{id}", JsExtendGetExtensionTask.class);
        router.add(PUT, "/extensions/{type}/{id}", new JsExtendPutExtensionTask(extensionFactory));
        router.add(DELETE, "/extensions/{type}/{id}", JsExtendRemoveExtensionTask.class);
    }
}
