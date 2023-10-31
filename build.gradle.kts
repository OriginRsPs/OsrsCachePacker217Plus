group = "com.mark"
version = 1.0

plugins {
    kotlin("jvm")
    id("maven-publish")
}

repositories {
    mavenCentral()
    maven(url = "https://repo.runelite.net/")
    maven(url = "https://repo.openrs2.org/repository/openrs2-snapshots/")
    maven(url = "https://jitpack.io")
}



dependencies {
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    val slf4jVersion = "2.0.9"
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("me.tongfei:progressbar:0.9.2")
    implementation("com.beust:klaxon:5.6")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.lingala.zip4j:zip4j:2.11.4")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.displee:disio:2.2")
    implementation("com.github.jponge:lzma-java:1.3")
    implementation("org.apache.ant:ant:1.10.13")

    for (module in listOf(
        "handler",
        "buffer",
        "transport-native-epoll",
        "transport-native-kqueue",
    )) implementation("io.netty:netty-$module:4.1.99.Final")

    implementation("io.netty.incubator:netty-incubator-transport-native-io_uring:0.0.22.Final")

    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("org.jctools:jctools-core:4.0.1")

    for (module in listOf("buffer", "cache"))
        implementation("org.openrs2:openrs2-$module:0.1.0-SNAPSHOT")


}

kotlin {
    jvmToolchain(17)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    register<JavaExec>("Update Cache") {
        group = "Service"
        description = "Update Cache to the defined revision"
        classpath = project.sourceSets.main.get().runtimeClasspath
        mainClass.set("com.mark.UpdateCacheKt")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/mark7625/osrs-data-packer")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            groupId = "com.mark"
            artifactId = "packer"
            version = "1.0"

            from(components["java"])

        }
    }
}
