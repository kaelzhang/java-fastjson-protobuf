[![Build Status](https://travis-ci.org/kaelzhang/java-fastjson-protobuf.svg?branch=master)](https://travis-ci.org/kaelzhang/java-fastjson-protobuf)
[![Coverage](https://codecov.io/gh/kaelzhang/java-fastjson-protobuf/branch/master/graph/badge.svg)](https://codecov.io/gh/kaelzhang/java-fastjson-protobuf)
<!-- optional appveyor tst
[![Windows Build Status](https://ci.appveyor.com/api/projects/status/github/kaelzhang/java-fastjson-protobuf?branch=master&svg=true)](https://ci.appveyor.com/project/kaelzhang/java-fastjson-protobuf)
-->
<!-- optional npm version
[![NPM version](https://badge.fury.io/js/java-fastjson-protobuf.svg)](http://badge.fury.io/js/java-fastjson-protobuf)
-->
<!-- optional npm downloads
[![npm module downloads per month](http://img.shields.io/npm/dm/java-fastjson-protobuf.svg)](https://www.npmjs.org/package/java-fastjson-protobuf)
-->
<!-- optional dependency status
[![Dependency Status](https://david-dm.org/kaelzhang/java-fastjson-protobuf.svg)](https://david-dm.org/kaelzhang/java-fastjson-protobuf)
-->

# fastjson-protobuf

Spring `HttpMessageConverter` implementation with Alibaba FastJson and serializer/deserializer of Protobuf Messages.

With `fastjson-protobuf`, we can use protocol buffers to define both request and response entities.

## Install

### Gradle

```gradle
compile "ai.ost:fastjson-protobuf:${VERSION}"
```

### Maven

```xml
<dependency>
  <groupId>ai.ost</groupId>
  <artifactId>fastjson-protobuf</artifactId>
  <version>${VERSION}</version>
</dependency>
```

## Usage

First, define our own `HttpMessageConverter`

```java
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void configureMessageConverters(
    List<HttpMessageConverter<?>> converters
  ) {
    
    FastJsonProtobufHttpMessageConverter converter = 
      new FastJsonProtobufHttpMessageConverter();
    
    converter.setSupportedMediaTypes(
      Arrays.asList(
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_JSON_UTF8
      )
    );

    converters.add(converter);
  }
}
```

Then define Protocol Buffer messages:

```protobuf
message HelloRequest {
  string name = 1;
}

message HelloResponse {
  string msg = 1;
}
```

In order to use protobuf messages as entities, we can use gradle plugin `com.google.protobuf` together with [protoc](https://search.maven.org/artifact/com.google.protobuf/protoc) and protoc plugin [protoc-gen-grpc-java](https://search.maven.org/artifact/io.grpc/protoc-gen-grpc-java/).

```java
@RestController
public class APIController {
  @RequestMapping(
    value = "/hello",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @RequestBody
  public HelloResponse hello(@RequestBody HelloRequest req) {
    // We can use `HelloResponse` as the return value
    return HelloResponse.newBuilder()
      .setMsg("Hello " + req.getName())
      .build();
  }
}
```

```sh
$ curl -X POST -d '{"name": "World"}' http://localhost:8080/hello
{"msg": "Hello World"}
```

## License

MIT
