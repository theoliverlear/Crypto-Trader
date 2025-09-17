package org.cryptotrader.security.library.config

import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.CompositePropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory

/**
 * Allows using @PropertySource with YAML files.
 */
class YamlPropertySourceFactory : PropertySourceFactory {
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val loader = YamlPropertySourceLoader()
        val filename = resource.resource.filename ?: "application-secure.yml"
        val sources = loader.load(filename, resource.resource)
        if (sources.size == 1) return sources[0]

        val compositeName = name ?: filename
        val composite = CompositePropertySource(compositeName)
        sources.forEach { composite.addPropertySource(it) }
        return composite
    }
}