# Hello Ext - Quarkus Extension

A minimal Quarkus extension that registers a custom **Hello World Servlet**, fully configurable via `application.properties` and listed automatically in the **Dev UI Config Editor**.

---

## Features

* ✅ Configurable properties (`enabled`, `path`, `message`, `context`)
* ✅ Automatic listing in **Dev UI → Config Editor**
* ✅ `enabled` flag to completely disable the feature
* ✅ Support for environment variables

---

## Configuration

All properties live under the prefix:

```
quarkus.hello-ext.*
```

Available keys:

| Property                    | Default                         | Description                                 |
| --------------------------- | ------------------------------- | ------------------------------------------- |
| `quarkus.hello-ext.enabled` | `true`                          | Enable/disable the extension completely     |
| `quarkus.hello-ext.path`    | `/hello-ext`                    | HTTP path where the servlet will be exposed |
| `quarkus.hello-ext.message` | `Hello from Quarkus extension!` | Base message returned by the endpoint       |
| `quarkus.hello-ext.context` | *(none)*                        | Optional context appended to the message    |

---

## Usage

1. Add configuration in `application.properties`:

```properties
quarkus.hello-ext.enabled=true
quarkus.hello-ext.path=/hello-ext
quarkus.hello-ext.message=Hello, Haikal!
quarkus.hello-ext.context=local-dev
```

2. Run the app in dev mode:

```bash
./mvnw quarkus:dev
```

3. Access endpoints:

* `http://localhost:8080/hello-ext` → `Hello, Haikal! | context=local-dev`

---

## Dev UI Integration

Open the Dev UI:

```
http://localhost:8080/q/dev/
```

* Go to **Config Editor**
* Search for `hello-ext`
* All properties appear with default values and descriptions
* You can edit them live while running in dev mode

---

## Environment Variables

Quarkus automatically maps properties to env vars:

```bash
export QUARKUS_HELLO_EXT_MESSAGE="Hello from ENV!"
export QUARKUS_HELLO_EXT_CONTEXT="production"
```

This allows seamless configuration across environments without touching `application.properties`.

---

## Conventions

* The `enabled` flag is the canonical way to activate/deactivate a Quarkus extension.
* Properties are scoped with `quarkus.hello-ext.*` prefix to avoid conflicts.
* Values can come from config files, profiles, or environment variables.

---
