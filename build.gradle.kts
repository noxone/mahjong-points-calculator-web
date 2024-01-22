plugins {
    kotlin("js") version "1.9.22"
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

group = "org.olafneumann.mahjong.points"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.11.0")
}

kotlin {
    js {
        useCommonJs()
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    //enabled.set(true)
                }
            }

            webpackTask {
                cssSupport {
                    enabled.set(true)
                }
            }

            runTask {
                cssSupport {
                    enabled.set(true)
                }
            }

            testTask {
                useKarma {
                    useFirefoxHeadless()
                    webpackConfig.cssSupport {
                        enabled.set(true)
                    }
                }
            }
        }
    }
}

plugins.withType<io.gitlab.arturbosch.detekt.DetektPlugin> {
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt> detekt@{
        reports.html.required.set(true)
        reports.xml.required.set(true)
        reports.txt.required.set(false)
        reports.sarif.required.set(true)
    }
}

detekt {
    // Define the detekt configuration(s) you want to use.
    config = files("$projectDir/.config/detekt.yml")

    // Applies the config files on top of detekt's default config file. `false` by default.
    buildUponDefaultConfig = true

    // Turns on all the rules. `false` by default.
    allRules = false

    ignoreFailures = true
}
