package org.cryptotrader.api.library.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {
    
    @Override
    public @NotNull PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        Resource resource = encodedResource.getResource();
        String sourceName = name != null ? name : resource.getFilename();
        if (sourceName == null) {
            sourceName = "application.yml";
        }
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> sources = loader.load(sourceName, resource);
        if (sources.isEmpty()) {
            return new PropertiesPropertySource(sourceName, new Properties());
        }
        return sources.getFirst();
    }
}
