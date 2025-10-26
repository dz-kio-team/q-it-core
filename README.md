# q-it-core

Q-IT MSA 프로젝트에서 공통적으로 사용하는 설정, 유틸리티, 예외 처리,  
비동기 구성 등을 포함한 **Core Library**입니다.

이 모듈은 `JitPack`을 통해 배포되며,  
다른 서비스에서 별도의 빌드 없이 `Gradle` 또는 `Maven` 의존성으로 바로 사용할 수 있습니다.

---

## 주요 기능

- `@AutoConfiguration` 기반 공통 Bean 등록
- `@RestControllerAdvice` 기반 전역 예외 처리
- 공통 `BaseEntity` 상속 구조 제공
- `ObjectMapper` 공통 설정
- `Async`, `Auditing`, `Security`, `CORS` 등 통합 설정

---

## 설치 방법

### 1. JitPack 저장소 추가

`settings.gradle.kts` 또는 `settings.gradle` 파일에 다음 추가:

```groovy
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. 의존성 추가

`build.gradle.kts` (또는 `build.gradle`)에 다음 코드 추가:

```groovy
dependencies {
    implementation("com.github.dz-kio-team:q-it-core:<VERSION>")
}
```

`<VERSION>` 부분에 [JitPack 페이지](https://jitpack.io/#dz-kio-team/q-it-core)에서 최신 릴리즈 버전을 확인 후 아래처럼
지정합니다.

예시:

```groovy
implementation("com.github.dz-kio-team:q-it-core:v1.0.0")
```

### 3. (선택) Snapshot 버전 사용

아직 Release Tag가 없거나 최신 main 브랜치를 바로 사용하고 싶다면:

```groovy
implementation("com.github.dz-kio-team:q-it-core:main-SNAPSHOT")
```
> `-SNAPSHOT` 은 JitPack이 최신 커밋을 빌드할 때 사용합니다.

---
## 사용 방법

### 1. @EnableCoreLibrary 어노테이션 추가
라이브러리의 스프링 빈은 다른 서비스에서 Component Scan 대상이 아니기 때문에,  
이를 활성화하기 위해서는 명시적으로 어노테이션을 추가해야 합니다.

Application 클래스에 `@EnableCoreLibrary` 어노테이션 추가:

```kotlin
@SpringBootApplication
@EnableCoreLibrary  // q-it-core 활성화
class MyApplication

fun main(args: Array<String>) {
    runApplication<MyApplication>(*args)
}
```

### 2. BaseEntity 상속
엔티티 클래스에 공통 필드(`createdAt`, `updatedAt`)를 추가하려면, 엔티티 클래스가 `BaseEntity`를 상속받도록 수정:
```kotlin
@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
) : BaseEntity()
```

### 3. 예외 처리
`@RestControllerAdvice` 기반의 `GlobalExceptionHandler`가 자동으로 등록됩니다.  
웹 애플리케이션에서만 활성화되며, Batch 서비스 등에서는 비활성화됩니다.  
`GlobalExceptionHandler`는 일반적인 예외를 처리하며, 필요에 따라 커스텀 ExceptionHandler를 추가할 수 있습니다.

`@Order(Ordered.HIGHEST_PRECEDENCE)`를 사용하여 `GlobalExceptionHandler`보다 우선순위가 높게 구현할 수 있습니다:
```kotlin
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class CustomExceptionHandler {
    // 커스텀 예외 처리 로직
    ...
}
```

### 4. Async 설정
Core 라이브러리의 AsyncConfig를 활성화하려면 `application.yml` 또는 `application.properties`에 다음 설정 추가합니다:
```yaml
qit:
  async:
    enabled: true   # 비동기 처리 활성화 여부 (기본값: true)
```

### 5. ObjectMapper
Core 라이브러리는 `ObjectMapper`에 Kotlin Module과 공통 설정을 자동으로 적용합니다.  
별도 ObjectMapper를 설정하지 않으면, 자동으로 core 라이브러리의 ObjectMapper가 사용됩니다.


## Core Library 개발 가이드
q-it-core는 [Spring Boot Auto-Configuration](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html) 기반으로 구성되어 있으며,  
공통 Configuration과 Bean을 자동으로 주입해주는 형태로 설계되어 있습니다.  
core 모듈에 새로운 기능이나 공통 빈을 추가할 때는 아래 원칙을 참고하세요.

### 1. SharedAutoConfig 구성
모든 자동 설정은 `SharedAutoConfig`를 통해 서비스 애플리케이션에 전파됩니다.  
새 공통 기능을 추가할 때는 `SharedAutoConfig`의 `@Import` 목록에 해당 설정 클래스를 추가하세요.

```kotlin
@AutoConfiguration
@Import(
    value = [
        // 여기에 라이브러리의 스프링 빈 구성 요소들을 추가합니다.
        JacksonConfig::class,
        AsyncConfig::class,
        GlobalExceptionHandler::class
    ]
)
class SharedAutoConfig {}
```
- `@AutoConfiguration`  
  클래스가 자동 구성 대상으로 인식되고, 서비스 측에서는 별도 import 없이 적용됩니다.
- `@Import`  
  core 내에서 서비스로 전파하고 싶은 설정 클래스 목록을 명시합니다.  
  여기에 추가된 Bean들은 서비스 애플리케이션의 컨텍스트에 자동으로 포함됩니다.

### 2. 조건부 빈 등록
라이브러리에서는 서비스 환경에 맞춰 유연하게 Bean을 등록해야 합니다.  
이때 `@Conditional` 계열 어노테이션을 사용해 조건부 Bean 주입을 수행합니다.

| 어노테이션 | 설명 | 예시                                      |
|-------------|------|-----------------------------------------|
| `@ConditionalOnMissingBean` | 동일 타입의 빈이 이미 등록되어 있지 않을 때만 빈을 생성합니다. | 공통 `ObjectMapper`, `ExceptionHandler` 등 |
| `@ConditionalOnProperty` | 특정 설정 프로퍼티가 활성화(`application.yml`)일 경우에만 Bean을 등록합니다. | `AysncConfig` |
| `@ConditionalOnWebApplication` | Web 환경일 때에만 활성화됩니다. | REST ExceptionHandler, WebSecurity 등    |

### 3. Bean Conflict 방지 — @ConditionalOnMissingBean
라이브러리에서 제공하는 Bean이 서비스 애플리케이션에서 이미 정의된 Bean과 충돌하지 않도록  
`@ConditionalOnMissingBean` 어노테이션을 적극 활용하세요.

```kotlin
@Bean
@ConditionalOnMissingBean(ObjectMapper::class)
fun objectMapper(): ObjectMapper = ObjectMapper().apply {
    registerModule(KotlinModule.Builder().build())
}
```

이렇게 구성하면, 서비스 프로젝트에서 커스텀 ObjectMapper를 등록하면  
core의 기본 ObjectMapper는 빈 충돌 없이 자동으로 비활성화됩니다.

### 4. 프로퍼티 기반 설정 — @ConditionalOnProperty
특정 기능을 프로퍼티로 활성화/비활성화할 수 있도록  
`@ConditionalOnProperty` 어노테이션을 사용하여 설정 클래스를 구성하세요.

```kotlin
@Configuration
@ConditionalOnProperty(
  prefix = "qit.async", 
  name = ["enabled"], 
  havingValue = "true"
)
class AsyncConfig {
  
    @Bean
    fun taskExecutor(): Executor { ... }
}
```
이렇게 하면, 서비스 애플리케이션에서 `qit.async.enabled` 프로퍼티를 통해 비동기 기능을 제어할 수 있습니다.
```yaml
qit:
  async:
    enabled: false   # 비동기 처리 비활성화
```

### 5. Web 전용 설정 — @ConditionalOnWebApplication
웹 애플리케이션에만 적용되어야 하는 설정은 `@ConditionalOnWebApplication` 어노테이션을 사용하여 구성하세요.

```kotlin
@ConditionalOnWebApplication
@RestControllerAdvice
class GlobalExceptionHandler { ... }
```

### 참고 자료
- [Spring Boot - Custom Auto Configuration (docs)](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html)
- [AutoConfiguration 원리와 직접 구현 가이드 (Tori's Devlog)](https://godekdls.github.io/Spring%20Boot/creating-your-own-auto-configuration/)
- [Custom Auto-Configuration Example (Baeldung)](https://www.baeldung.com/spring-boot-custom-auto-configuration)
- [Spring Boot 공통 라이브러리 구축 가이드 (공통 모듈)](https://devloo.io/spring-boot-%ea%b3%b5%ed%86%b5-%eb%9d%bc%ec%9d%b4%eb%b8%8c%eb%9f%ac%eb%a6%ac-%ea%b5%ac%ec%b6%95-%ea%b0%80%ec%9d%b4%eb%93%9c-%ea%b3%b5%ed%86%b5-%eb%aa%a8%eb%93%88/)
- [스프링부트 공유라이브러리 만들고 jitpack으로 배포하기](https://ssdragon.tistory.com/167)