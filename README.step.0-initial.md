## Parte 1 — gerar o esqueleto da extensão

Passo basico para criar uma extensão Quarkus. A extensão é um “plugin” que adiciona funcionalidades à aplicação Quarkus, como novos endpoints, integrações com serviços, etc.

A extensão tem **dois módulos**: `runtime` (o que roda na app) e `deployment` (build steps que “plugam” sua feature no Quarkus durante o build). O plugin do Quarkus gera isso pra gente. ([quarkus.io][1])

> Use Java 21

```bash
# em um diretório vazio
mvn io.quarkus.platform:quarkus-maven-plugin:3.25.3:create-extension -N \
  -DgroupId=com.haikal \
  -DextensionId=hello-ext \
  -DwithoutTests
```

*Por quê:* esse goal já cria parent + `runtime` + `deployment` e um `Processor` com um `FeatureBuildItem`, que é o “cartão de visita” da sua extensão no log. ([quarkus.io][1])

---

## Parte 2 — implementar o “Hello” no **runtime**

Vamos expor um endpoint simples via **Servlet** (Undertow). É didático e o guia oficial usa esse caminho para o “hello”. ([quarkus.io][1])

1. Adicione a dependência no `runtime/pom.xml`:

```xml
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-undertow</artifactId>
</dependency>
```

2. Crie a classe `com.haikal.helloext.HelloServlet` em `runtime/src/main/java/...`:

```java
package com.haikal.helloext;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet
public class HelloServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.getWriter().write("Hello from Quarkus extension!");
  }
}
```

*Por quê:* o **runtime** é onde fica a “feature” em si (o Servlet). Usar Undertow aqui facilita, e o Quarkus já tem os ganchos prontos para registrar esse Servlet no build. ([quarkus.io][1])

---

## Parte 3 — registrar o endpoint no **deployment**

Agora dizemos ao Quarkus (no build) para mapear o Servlet em uma rota.

1. Em `deployment/pom.xml`, adicione:

```xml
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-undertow-deployment</artifactId>
</dependency>
```

2. Edite o `...deployment.HelloExtProcessor` gerado para algo assim:

```java
package com.haikal.helloext.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.undertow.deployment.ServletBuildItem;
import com.haikal.helloext.HelloServlet;

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
```

*Por quê:* **build steps** produzem `BuildItem`s. O `ServletBuildItem` manda o Quarkus gerar o bytecode de registro do Servlet no caminho `/hello-ext` durante o build (sem refletir em runtime). ([quarkus.io][1])

---

## Parte 4 — build & instalar localmente

```bash
mvn -q -DskipTests install
```

---

## Parte 5 — usar a extensão numa app de teste

1. Crie uma app Quarkus vazia:

```bash
mvn io.quarkus.platform:quarkus-maven-plugin:3.25.3:create \
  -DprojectGroupId=com.haikal \
  -DprojectArtifactId=demo-app
cd demo-app
```

2. No `demo-app/pom.xml`, adicione a dependência da sua extensão:

```xml
<dependency>
  <groupId>com.haikal</groupId>
  <artifactId>hello-ext</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

3. Rode:

```bash
./mvnw quarkus:dev
curl http://localhost:8080/hello-ext
# -> Hello from Quarkus extension!
```

---

Refs oficiais para consulta rápida:

* “Building my first extension” (o guia que seguimos). ([quarkus.io][1])
* “Writing Your Own Extension” (conceitos de build vs runtime/augmentation). ([quarkus.io][3])
* “Extension Capabilities” e “Extension Metadata” (boas práticas e como a extensão se descreve). ([quarkus.io][2])

[1]: https://quarkus.io/guides/building-my-first-extension "Building my first extension - Quarkus"
[2]: https://quarkus.io/guides/capabilities?utm_source=chatgpt.com "Extension Capabilities"
[3]: https://quarkus.io/guides/writing-extensions?utm_source=chatgpt.com "Writing Your Own Extension"
