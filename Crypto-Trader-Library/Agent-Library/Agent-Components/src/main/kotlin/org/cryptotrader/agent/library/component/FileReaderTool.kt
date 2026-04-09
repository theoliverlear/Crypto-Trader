package org.cryptotrader.agent.library.component

import info.debatty.java.stringsimilarity.Levenshtein
import org.cryptotrader.agent.library.communication.response.DirectoryListingResponse
import org.cryptotrader.agent.library.communication.response.FileContentResponse
import org.cryptotrader.agent.library.config.AgentConstraintsProperties
import org.cryptotrader.agent.library.model.FileMetadata
import org.springaicommunity.mcp.annotation.McpToolParam
import org.springframework.ai.tool.annotation.Tool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.*

// TODO: Make file searching/reading for sensitive files only accessible for
//       super-admin users. Perhaps annotations like @Admin, @SuperAdmin,
//       @User, etc would easily manage this across tools.
@Component
class FileReaderTool @Autowired constructor(
    private val properties: AgentConstraintsProperties
) {

    private val allowedRoot: Path by lazy {
        Paths.get(properties.allowedRoot).toAbsolutePath().normalize()
    }

    @Tool(description = "Read a UTF-8 text file within the project directory. Supports line-based reading and safety limits.")
    fun readFile(
        @McpToolParam(description = "The relative path to the file from the project root")
        relativePath: String,
        @McpToolParam(description = "The starting line number (1-indexed)")
        startLine: Int = 1,
        @McpToolParam(description = "The ending line number (inclusive)")
        endLine: Int? = null,
        @McpToolParam(description = "Maximum bytes to read to prevent memory issues")
        maxBytes: Int = 256 * 1024 * 1024 // 256MB
    ): FileContentResponse {
        val path: Path = this.resolveAndVerify(relativePath)
        require(path.isRegularFile()) { "File not found or not a regular file: $relativePath" }

        require(!this.looksLikeBinaryFile(path)) { "File appears to be binary. Binary files are not supported." }

        val lines: List<String> = path.useLines { it.toList() }
        val totalLines: Int = lines.size
        val effectiveEndLine: Int = endLine ?: totalLines

        val startingIndex: Int = (startLine - 1).coerceAtLeast(0)
        val endingIndex: Int = (effectiveEndLine - startLine + 1).coerceAtLeast(0)
        val selectedLines: List<String> = lines.drop(startingIndex).take(endingIndex)

        val content: String = selectedLines.joinToString("\n")
        val bytes: ByteArray = content.toByteArray(StandardCharsets.UTF_8)

        val isTruncated: Boolean = bytes.size > maxBytes
        val finalContent: String = if (isTruncated) {
            this.getStringFromBytes(bytes, maxBytes)
        } else {
            content
        }

        return FileContentResponse(
            path = relativePath,
            totalLines = totalLines,
            content = finalContent,
            isTruncated = isTruncated
        )
    }

    @Tool(description = "List directory contents.")
    fun listDirectories(
        @McpToolParam(description = "The relative path of the directory to list")
        relativePath: String = ".",
        @McpToolParam(description = "Whether to list contents recursively")
        recursive: Boolean = false
    ): DirectoryListingResponse {
        val root: Path = this.resolveAndVerify(relativePath)
        require(root.isDirectory()) { "Not a directory: $relativePath" }

        val entries: MutableList<FileMetadata> = mutableListOf()
        val ignoredNames: Set<String> = properties.ignoredDirectories

        if (recursive) {
            Files.walkFileTree(root, object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (dir != root && ignoredNames.contains(dir.fileName.toString())) {
                        return FileVisitResult.SKIP_SUBTREE
                    }
                    return FileVisitResult.CONTINUE
                }

                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    entries.add(createMetadata(file))
                    return FileVisitResult.CONTINUE
                }

                override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                    if (dir != root) {
                        entries.add(createMetadata(dir))
                    }
                    return FileVisitResult.CONTINUE
                }
            })
        } else {
            Files.newDirectoryStream(root).use { stream ->
                for (path in stream) {
                    if (!ignoredNames.contains(path.fileName.toString())) {
                        entries.add(createMetadata(path))
                    }
                }
            }
        }

        return DirectoryListingResponse(relativePath, entries)
    }

    @Tool(description = "Search for files by name or glob pattern within the project. Example: searchFiles(\"Toolkit\", \"*.kt\")")
    fun searchFiles(
        @McpToolParam(description = "The text to search for in the file name")
        query: String,
        @McpToolParam(description = "The glob pattern to filter files (e.g., \"**/*.kt\")")
        glob: String = "**/*"
    ): List<FileMetadata> {
        val results: MutableList<FileMetadata> = mutableListOf()
        val ignoredNames: Set<String> = properties.ignoredDirectories

        val pathMatcher: PathMatcher = FileSystems.getDefault().getPathMatcher("glob:$glob")

        Files.walkFileTree(this.allowedRoot, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (dir != allowedRoot && ignoredNames.contains(dir.fileName.toString())) {
                    return FileVisitResult.SKIP_SUBTREE
                }
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                val relPath: Path = allowedRoot.relativize(file)
                if (pathMatcher.matches(relPath) && file.fileName.toString().contains(query, ignoreCase = true)) {
                    results.add(createMetadata(file))
                }
                return FileVisitResult.CONTINUE
            }
        })

        return results
    }

    @Tool(description = "Read multiple files at once.")
    fun readManyFiles(
        @McpToolParam(description = "A list of relative paths to the files")
        paths: List<String>
    ): Map<String, FileContentResponse> {
        return paths.associateWith { path ->
            try {
                this.readFile(path)
            } catch (_: Exception) {
                throw IllegalArgumentException("Error reading file: $path")
            }
        }
    }

    @Tool(description = "Resolve a path hint using fuzzy matching if it doesn't exist.")
    fun resolvePath(
        @McpToolParam(description = "The path hint or partial filename to resolve")
        hint: String
    ): List<String> {
        val resolved: Path = this.allowedRoot.resolve(hint).normalize()
        val ignoredNames: Set<String> = this.properties.ignoredDirectories

        if (resolved.exists() && resolved.startsWith(this.allowedRoot)) {
            val relPath: Path = this.allowedRoot.relativize(resolved)
            if (!relPath.any { ignoredNames.contains(it.toString()) }) {
                return listOf(relPath.toString())
            }
        }

        val distanceCalculator = Levenshtein()
        val results: MutableList<Pair<String, Double>> = mutableListOf()

        Files.walkFileTree(this.allowedRoot, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (dir != allowedRoot && ignoredNames.contains(dir.fileName.toString())) {
                    return FileVisitResult.SKIP_SUBTREE
                }
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                this.processPath(file)
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                if (dir != allowedRoot) {
                    this.processPath(dir)
                }
                return FileVisitResult.CONTINUE
            }

            private fun processPath(path: Path) {
                val relPath: String = allowedRoot.relativize(path).toString().replace("\\", "/")
                val fileName: String = path.fileName.toString()
                val score: Double = minOf(
                    distanceCalculator.distance(hint.lowercase(), relPath.lowercase()),
                    distanceCalculator.distance(hint.lowercase(), fileName.lowercase())
                )
                if (score < maxOf(hint.length, relPath.length) * 0.7) {
                    results.add(relPath to score)
                }
            }
        })

        return results
            .sortedBy { it.second }
            .take(5)
            .map { it.first }
    }

    internal fun resolveAndVerify(relativePath: String): Path {
        val resolvedPath: Path = allowedRoot.resolve(relativePath).normalize()
        require(resolvedPath.startsWith(allowedRoot)) { "Path is outside allowed root." }
        return resolvedPath
    }

    internal fun createMetadata(path: Path): FileMetadata {
        val attrs: BasicFileAttributes = path.readAttributes<BasicFileAttributes>()
        val lineCount: Int? = if (path.isRegularFile()) {
            try {
                path.useLines { it.count() }
            } catch (_: Exception) {
                null
            }
        } else null

        return FileMetadata(
            path = allowedRoot.relativize(path).toString(),
            size = attrs.size(),
            lastModified = attrs.lastModifiedTime().toString(),
            isDirectory = attrs.isDirectory,
            lineCount = lineCount?.toLong()
        )
    }
    internal fun looksLikeBinaryFile(path: Path): Boolean {
        val sample: ByteArray = path.inputStream().use { it.readNBytes(1024) }
        return sample.any { it == 0.toByte() }
    }

    internal fun getStringFromBytes(
        bytes: ByteArray,
        maxBytes: Int
    ): String {
        return String(
            bytes.take(maxBytes).toByteArray(),
            StandardCharsets.UTF_8
        )
    }
}
