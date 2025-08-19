package com.haikal.hello.ext.deployment;

import com.haikal.hello.ext.runtime.HelloServlet;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.undertow.deployment.ServletBuildItem;

class HelloExtProcessor {

    private static final String FEATURE = "hello-ext";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    ServletBuildItem servlet() {
        return ServletBuildItem.builder("hello-ext", HelloServlet.class.getName())
                .addMapping("/hello-ext")
                .build();
    }
}
