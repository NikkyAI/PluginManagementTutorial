import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.kotlin
import java.net.URI

fun RepositoryHandler.eap() {
   maven { url = URI.create("http://dl.bintray.com/kotlin/kotlin-eap") }
}
