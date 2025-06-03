plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("plugin.noarg") version "1.9.25"
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

noArg {
	annotation("jakarta.persistence.Entity")
}


group = "com.hyun"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

extra["springAiVersion"] = "1.0.0-M6"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.ai:spring-ai-ollama-spring-boot-starter")
//	implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA 사용
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.h2database:h2") // H2 데이터베이스 사용
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("com.google.cloud:google-cloud-speech:4.57.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
