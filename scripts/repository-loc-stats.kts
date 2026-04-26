import java.io.File

/**
 * Script to count the lines of code (LOC) in the Crypto-Trader source code.
 * Excludes generated files, documentation, and dependencies.
 */

// TODO: Clean up code.

fun main() {
    val projectRoot = File(".")
    var totalLines = 0L
    val stats = mutableMapOf<String, Long>()
    val moduleStats = mutableMapOf<String, Long>()
    val libraryStats = mutableMapOf<String, Long>()

    val includedExtensions = setOf("java", "kt", "ts", "py", "html", "scss", "xml", "yml", "yaml", "sql")
    val excludedDirs = setOf(
        "target", "node_modules", "build", ".angular", ".idea", ".git",
        "Crypto-Trader-Docs", "docs", "site", "playwright-report",
        "dist", "out", "coverage", "bin", "obj", "tink", "logs", "infra"
    )

    println("Counting lines of code in ${projectRoot.absolutePath}...")

    projectRoot.walkTopDown()
        .onEnter { dir ->
            // Skip excluded directories early
            val name = dir.name.lowercase()
            !excludedDirs.any { it.equals(name, ignoreCase = true) }
        }
        .filter { it.isFile && includedExtensions.contains(it.extension) }
        .filter { file ->
            // Additional check for generated Angular JS files or other common generated patterns
            !file.name.endsWith(".component.js") &&
            !file.name.endsWith(".service.js") &&
            !file.name.endsWith(".map")
        }
        .forEach { file ->
            try {
                val lines = file.readLines().size.toLong()
                totalLines += lines

                // Language stats
                val ext = file.extension.lowercase()
                stats[ext] = stats.getOrDefault(ext, 0L) + lines

                // Module stats
                val relativePath = file.relativeTo(projectRoot).path
                val pathParts = relativePath.split(File.separator)
                val moduleName = if (pathParts.size > 1) {
                    val topDir = pathParts[0]
                    if (topDir.startsWith("Crypto-Trader-")) topDir else "Other"
                } else {
                    "Root"
                }
                moduleStats[moduleName] = moduleStats.getOrDefault(moduleName, 0L) + lines

                // Library stats
                if (moduleName == "Crypto-Trader-Library" && pathParts.size > 2) {
                    val libDir = pathParts[1]
                    if (libDir.endsWith("-Library")) {
                        libraryStats[libDir] = libraryStats.getOrDefault(libDir, 0L) + lines
                    }
                }
            } catch (e: Exception) {
                System.err.println("Error reading file ${file.path}: ${e.message}")
            }
        }

    println("\n--- Source Lines of Code Report ---")
    println("Total Source Lines of Code: $totalLines")
    println("------------------------------------")
    stats.entries
        .sortedByDescending { it.value }
        .forEach { (ext, count) ->
            val percentage = if (totalLines > 0) (count.toDouble() / totalLines * 100) else 0.0
            println("${ext.uppercase().padEnd(5)}: ${count.toString().padStart(8)} lines (${String.format("%.2f", percentage)}%)")
        }

    println("\n--- Module Breakdown ---")
    moduleStats.entries
        .sortedByDescending { it.value }
        .forEach { (module, count) ->
            val percentage = if (totalLines > 0) (count.toDouble() / totalLines * 100) else 0.0
            println("${module.padEnd(25)}: ${count.toString().padStart(8)} lines (${String.format("%.2f", percentage)}%)")
        }

    if (libraryStats.isNotEmpty()) {
        println("\n--- Library Breakdown (within Crypto-Trader-Library) ---")
        val totalLibLines = libraryStats.values.sum()
        libraryStats.entries
            .sortedByDescending { it.value }
            .forEach { (lib, count) ->
                val percentage = if (totalLines > 0) (count.toDouble() / totalLines * 100) else 0.0
                val libPercentage = if (totalLibLines > 0) (count.toDouble() / totalLibLines * 100) else 0.0
                println("${lib.padEnd(25)}: ${count.toString().padStart(8)} lines (${String.format("%.2f", percentage)}% of total, ${String.format("%.2f", libPercentage)}% of libs)")
            }
    }
    println("------------------------------------")
}

main()
