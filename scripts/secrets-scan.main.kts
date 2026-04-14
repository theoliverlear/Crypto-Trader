#!/usr/bin/env kotlin

import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

/**
 * Pre-commit script to detect potential secrets in staged files.
 * Returns exit code 1 if any secrets are found, blocking the commit.
 */

val SECRET_PATTERNS: Map<String, Regex> = mapOf(
    "AWS Access Key" to Regex("AKIA[0-9A-Z]{16}"),
    "AWS Secret Key" to Regex("(?i)aws_secret_access_key\\s*[:=]\\s*['\"][A-Za-z0-9/+=]{40}['\"]"),
    "Private Key" to Regex("-----BEGIN (RSA|EC|DSA|OPENSSH) PRIVATE KEY-----"),
    "Google API Key" to Regex("AIza[0-9A-Za-z-_]{35}"),
    "Generic Secret" to Regex("(?i)(api_key|secret|password|token|mnemonic)\\s*[:=]\\s*['\"][A-Za-z0-9/+=]{16,}['\"]"),
    "Ethereum/Crypto Address Pattern" to Regex("0x[a-fA-F0-9]{40}") // Warning: may have false positives
)

fun main() {
    println("Scanning staged files for secrets...")

    val stagedFiles: List<String> = getStagedFiles()
    if (stagedFiles.isEmpty()) {
        println("No staged files to scan.")
        return
    }

    val findings: MutableList<String> = mutableListOf()

    for (filePath: String in stagedFiles) {
        val file = File(filePath)
        if (!file.exists() || file.isDirectory) {
            continue
        }

        file.useLines { lines ->
            lines.forEachIndexed { index, line ->
                SECRET_PATTERNS.forEach { (name, regex) ->
                    if (regex.containsMatchIn(line)) {
                        findings.add("$name found in $filePath at line ${index + 1}")
                    }
                }
            }
        }
    }

    if (findings.isNotEmpty()) {
        println("\nSecrets detected! Commit aborted:")
        findings.forEach { println(it) }
        println("\nRun 'git reset' to unstage these files or remove the secrets before committing.")
        exitProcess(1)
    } else {
        println("No secrets detected. Proceeding with commit.")
    }
}

fun getStagedFiles(): List<String> {
    return try {
        val process: Process = ProcessBuilder("git", "diff", "--cached", "--name-only", "--diff-filter=ACM")
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        process.inputStream.bufferedReader().readLines()
    } catch (_: Exception) {
        emptyList()
    }
}

main()
