import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.5.6" apply false
    id("io.spring.dependency-management") version "1.1.7"
    id("java-library")
    id("maven-publish")
}

group = "com.kio.core"
//version = "0.0.1-SNAPSHOT"    // JitPack은 Git 태그를 버전으로 사용하므로 별도 버전 설정 불필요
description = "q-it-core"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.jar {
    enabled = true
    archiveClassifier.set("") // 중복 방지
    archiveVersion.set("${project.version}")
    archiveBaseName.set("q-it-core")
}

repositories {
    mavenCentral()
}

// Spring Boot Plugin 없이도 스프링 프레임워크의 의존성 관리를 사용할 수 있도록 설정
dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(kotlin("stdlib"))    // Kotlin 표준 라이브러리
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    // Kotlin Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "${project.group}"
            artifactId = "q-it-core"
//            version = "${project.version}"
            from(components["java"])
        }
    }
}