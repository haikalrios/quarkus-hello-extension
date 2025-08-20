package com.haikal.hello.ext.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Configuration for the Hello extension.
 * <p>
 * Prefix: {@code quarkus.hello-ext.*}
 * <ul>
 *   <li>{@code quarkus.hello-ext.enabled}</li>
 *   <li>{@code quarkus.hello-ext.path}</li>
 *   <li>{@code quarkus.hello-ext.message}</li>
 *   <li>{@code quarkus.hello-ext.context}</li>
 * </ul>
 */
@StaticInitSafe
@ConfigMapping(prefix = "quarkus.hello-ext")
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
public interface HelloExtConfig {

  /** Enable/disable the hello extension. Default: {@code true}. */
  @WithDefault("true")
  boolean enabled();

  /** HTTP path where the servlet will be exposed. Default: {@code /hello-ext}. */
  @WithDefault("/hello-ext")
  String path();

  /** Message returned by the endpoint. Default: {@code Hello from Quarkus extension!}. */
  @WithDefault("Hello from Quarkus extension!")
  String message();


  /**
   * Optional context appended to the response. If absent or blank, it's ignored.
   */
  java.util.Optional<String> context();
}
