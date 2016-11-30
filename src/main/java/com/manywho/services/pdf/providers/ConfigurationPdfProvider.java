package com.manywho.services.pdf.providers;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.classpath.ClasspathConfigurationSource;
import org.cfg4j.source.compose.MergeConfigurationSource;
import org.cfg4j.source.context.environment.DefaultEnvironment;
import org.cfg4j.source.context.environment.Environment;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.system.EnvironmentVariablesConfigurationSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.common.io.Resources;
import java.util.List;

public class ConfigurationPdfProvider {

    public ConfigurationProvider configurationProvider() {
        ConfigFilesProvider configFilesProvider = createConfigFilesProvider(
                new ArrayList<>(Arrays.asList("service.properties.yaml", "service.properties")));

        ConfigurationSource source;

        if (configFilesProvider != null) {
            source= new MergeConfigurationSource(new ClasspathConfigurationSource(configFilesProvider), new EnvironmentVariablesConfigurationSource());
        } else {
            source = new EnvironmentVariablesConfigurationSource();
        }

        return new ConfigurationProviderBuilder()
                .withConfigurationSource(source)
                .withEnvironment( new DefaultEnvironment())
                .build();
    }

    private ConfigFilesProvider createConfigFilesProvider(List<String> paths) {
        ArrayList<Path> existingFilePaths = new ArrayList<>();

        for (String path: paths) {
            try {
                Resources.getResource(path);
                existingFilePaths.add(Paths.get(path));
            } catch (IllegalArgumentException e) {
                e.getMessage();
            }
        }

        if(existingFilePaths.isEmpty()) {
            return null;
        }

        ConfigFilesProvider configFilesProvider = () -> existingFilePaths;

        return configFilesProvider;
    }
}
