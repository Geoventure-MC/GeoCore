// The in-game visual test harness: a CLI that launches GeoCore mod dev clients from captured launch specs,
// drives them over the GeoCore harness socket, and runs single-file Java scenarios against them.
// No Minecraft dependency: everything game-side lives in GeoCore (fr.geoventure.geocore.harness).
plugins {
    java
    application
}

group = "fr.geoventure"
version = "0.1.0"

repositories {
    mavenCentral()
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
}

application {
    mainClass.set("fr.geoventure.harness.cli.Main")
}

// One self-contained jar so the `harness` wrappers need nothing but a JDK: `java -jar build/libs/geocore-harness.jar`.
tasks.jar {
    archiveFileName.set("geocore-harness.jar")
    manifest { attributes("Main-Class" to "fr.geoventure.harness.cli.Main") }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/versions/**/module-info.class")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}
