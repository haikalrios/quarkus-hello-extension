package com.haikal.hello.ext.deployment;

import com.haikal.hello.ext.runtime.HelloExtConfig;
import com.haikal.hello.ext.runtime.HelloServlet;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ConfigurationBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.undertow.deployment.ServletBuildItem;

class HelloExtProcessor {
    private static final String FEATURE = "hello-ext";

    @BuildStep
    FeatureBuildItem feature() {
        // FeatureBuildItem é usado para registrar a extensão no Quarkus
        // Pode se usar o enum Feature com parametro no FeatureBuildItem.
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    ServletBuildItem servlet(ConfigurationBuildItem cfgItem) {
        HelloExtConfig cfg = (HelloExtConfig) cfgItem.getReadResult()
                .requireObjectForClass(HelloExtConfig.class);

        if (!cfg.enabled()) return null;

        ServletBuildItem.Builder b = ServletBuildItem
                .builder(FEATURE, HelloServlet.class.getName())
                .addMapping(cfg.path());

        b.addInitParam("message", cfg.message());
        cfg.context().filter(ctx -> !ctx.isBlank())
                .ifPresent(ctx -> b.addInitParam("context", ctx));
        return b.build();
    }
}
