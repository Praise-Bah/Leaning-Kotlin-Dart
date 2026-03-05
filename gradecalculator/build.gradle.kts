plugins {
    kotlin("jvm") version "1.9.22"
    application
}

application {
    mainClass.set("StudentGradeCalculatorKt")
}

dependencies {
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "StudentGradeCalculatorKt"
    }
    
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
