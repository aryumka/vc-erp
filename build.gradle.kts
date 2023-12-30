import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
	kotlin("plugin.jpa") version "1.9.21"
	id ("io.swagger.core.v3.swagger-gradle-plugin") version "2.2.20"
}

group = "aryumka"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_19
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	runtimeOnly("com.h2database:h2")
	testImplementation ("io.kotest:kotest-runner-junit5:5.8.0")
	testImplementation ("io.kotest.extensions:kotest-extensions-spring:1.1.3")
	testImplementation("io.kotest:kotest-framework-datatest:5.8.0")
	testImplementation("io.mockk:mockk:1.13.4")
	testImplementation("com.ninja-squad:springmockk:4.0.0")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "19"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
