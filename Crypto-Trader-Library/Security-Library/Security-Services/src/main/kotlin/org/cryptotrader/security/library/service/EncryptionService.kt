package org.cryptotrader.security.library.service

import com.google.crypto.tink.Aead
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.config.TinkConfig
import org.slf4j.LoggerFactory
import java.io.File

class EncryptionService(
    private val keysetPath: String
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var aead: Aead
    
    init { 
        val shouldCreateKey: Boolean = System.getProperty("cryptotrader.load.aead")?.toBoolean() ?: false
        if (shouldCreateKey) {
            this.aead = this.createAeadFromKeyset() 
        }
    }

    fun encrypt(bytes: ByteArray, keyAlias: String? = null): ByteArray {
        return this.aead.encrypt(bytes, null)
    }

    fun decrypt(bytes: ByteArray): ByteArray = this.aead.decrypt(bytes, null)

    private fun createAeadFromKeyset(): Aead {
        TinkConfig.register()
        val keysetPath = this.keysetPath
        this.log.info("Initializing encryption (Google Tink AEAD). Keyset source configured as: {}", keysetPath)
        return when {
            keysetPath.startsWith("classpath:") -> {
                this.loadKeysetFromClasspath(keysetPath.removePrefix("classpath:"))
            }
            keysetPath.startsWith("file:") -> {
                this.loadKeysetFromFile(keysetPath.removePrefix("file:"), generateIfMissing = true)
            }
            else -> this.loadKeysetFromFile(keysetPath, generateIfMissing = true)
        }
    }

    private fun loadKeysetFromClasspath(resourcePath: String): Aead {
        val cleanPath = if (resourcePath.startsWith("/")) resourcePath else "/$resourcePath"
        val stream = this.javaClass.getResourceAsStream(cleanPath)
            ?: Thread.currentThread().contextClassLoader.getResourceAsStream(cleanPath.removePrefix("/"))
        return if (stream != null) {
            stream.use {
                val json = it.readBytes().toString(Charsets.UTF_8)
                val handle: KeysetHandle = TinkJsonProtoKeysetFormat.parseKeyset(json, InsecureSecretKeyAccess.get())
                handle.getPrimitive(Aead::class.java)
            }
        } else {
            this.log.warn("Tink keyset not found on classpath at {}. Generating ephemeral in-memory keyset.", cleanPath)
            val handle: KeysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES256_GCM"))
            handle.getPrimitive(Aead::class.java)
        }
    }

    private fun loadKeysetFromFile(filePath: String, generateIfMissing: Boolean): Aead {
        val file = File(filePath)
        if (!file.exists()) {
            if (generateIfMissing) {
                file.parentFile?.mkdirs()
                val handle: KeysetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES256_GCM"))
                val keysetJson: String = TinkJsonProtoKeysetFormat.serializeKeyset(handle, InsecureSecretKeyAccess.get())
                file.writeText(keysetJson, Charsets.UTF_8)
                this.log.info("Generated new Tink AEAD keyset at {}", file.absolutePath)
                return handle.getPrimitive(Aead::class.java)
            } else {
                throw IllegalStateException("Tink keyset file not found: $filePath")
            }
        }
        val json: String = file.readText(Charsets.UTF_8)
        val handle: KeysetHandle = TinkJsonProtoKeysetFormat.parseKeyset(json, InsecureSecretKeyAccess.get())
        return handle.getPrimitive(Aead::class.java)
    }
}