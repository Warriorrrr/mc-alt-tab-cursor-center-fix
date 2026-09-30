plugins {
    id("net.fabricmc.fabric-loom") version "1.18.2"
    id("maven-publish")
}

version = project.property("mod_version").toString()
group = project.property("maven_group").toString()

base {
    archivesName = project.property("archives_base_name").toString()
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // Loom adds the essential Maven repositories automatically.
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version").toString(),
            "loader_version" to project.property("loader_version").toString()
        )
    }
}

val targetJavaVersion = 25

tasks.withType<JavaCompile>().configureEach {
    // Ensure that the encoding is set to UTF-8, regardless of the system default.
    options.encoding = "UTF-8"

    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)

    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }

    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("archives_base_name")}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name").toString()
            from(components["java"])
        }
    }

    repositories {
        // Add repositories to publish to here.
    }
}
