package com.cloudreach.connect.fromsalesforceprocessor.services;

import com.cloudreach.connect.api.context.PluginContext;
import com.cloudreach.connect.api.context.SynchronousContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.codejargon.feather.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
public class FeatherModule {

    private final PluginContext pluginContext;
    private final Connection connection;

    public FeatherModule() {
        this(null, null);
    }

    @Provides
    @Singleton
    public SynchronousContext synchronousContext(PluginContext pluginContext) {
        return (SynchronousContext) pluginContext;
    }

    @Provides
    @Singleton
    public PluginContext pluginContext() {
        return pluginContext;
    }

    @Provides
    @Singleton
    public Connection connection() {
        return connection;
    }

    @Provides
    @Singleton
    public Properties properties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = FeatherModule.class.getResourceAsStream("/from-salesforce-processor.properties")) {
            properties.load(inputStream);
        }
        return properties;
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    @Named("mapTypeReference")
    public TypeReference<Map<String, Object>> mapTypeReference() {
        return new TypeReference<Map<String, Object>>() {
        };
    }

}
