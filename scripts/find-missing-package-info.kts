import java.io.File

/**
 * Kotlin script to find and report packages missing the package-info.java file.
 * It scans all src/main/java and src/main/kotlin directories in the project.
 */

fun main() {
    val projectRoot = File(".")
    val missingPackages = mutableListOf<String>()

    projectRoot.walk()
        .filter { it.isDirectory && (it.name == "java" || it.name == "kotlin")
                                 && it.parentFile?.name == "main"
                                 && it.parentFile?.parentFile?.name == "src" }
        .forEach { sourceRoot ->
            scanSourceRoot(sourceRoot, missingPackages)
        }

    if (missingPackages.isEmpty()) {
        println("All packages have a package-info.java file.")
    } else {
        println("Found ${missingPackages.size} packages missing package-info.java:")
        missingPackages.sorted().forEach { println(" - $it") }
    }
}

fun scanSourceRoot(sourceRoot: File, missingPackages: MutableList<String>) {
    sourceRoot.walk()
        .filter { it.isDirectory && it != sourceRoot }
        .forEach { dir ->
            val hasSourceFiles = dir.listFiles { _, name -> name.endsWith(".java") || name.endsWith(".kt") }?.isNotEmpty() == true
            if (hasSourceFiles) {
                // Check for package-info.java in the same directory (for Java)
                // or in the corresponding Java directory if it's a Kotlin directory
                val hasPackageInfo: Boolean = hasPackageInfo(dir, sourceRoot)
                if (!hasPackageInfo) {
                    val packageName: String = dir.relativeTo(sourceRoot).path.replace(File.separator, ".")
                    if (packageName.isNotEmpty() && !missingPackages.contains(packageName)) {
                        missingPackages.add(packageName)
                    }
                }
            }
        }
}

fun hasPackageInfo(dir: File, sourceRoot: File): Boolean {
    if (File(dir, "package-info.java").exists()) {
        return true
    }

    if (sourceRoot.name == "kotlin") {
        val relativePath: String = dir.relativeTo(sourceRoot).path
        val javaSourceRoot = File(sourceRoot.parentFile, "java")
        val correspondingJavaDir = File(javaSourceRoot, relativePath)
        if (File(correspondingJavaDir, "package-info.java").exists()) return true
    }

    return false
}

main()
