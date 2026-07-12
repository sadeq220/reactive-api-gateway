package sadad.fusion.gateway.routing;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YamlRegisterer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private final List<String> baseYamlFileNames = List.of("mock-route.yaml", "mock-route2.yaml");
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        String[] activeProfiles = environment.getActiveProfiles();
        try {
            List<PropertySource<?>> loadedPropertySources = this.loadYamlRoutes(activeProfiles);
            for (PropertySource<?> propertySourceLoader: loadedPropertySources)
                propertySources.addLast(propertySourceLoader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load custom YAML",e);
        }

    }

    private List<PropertySource<?>> loadYamlRoutes(String[] activeProfiles) throws IOException {
        ArrayList<PropertySource<?>> propertySources = new ArrayList<>();
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        List<String> profileNamedYamlFiles = baseYamlFileNames.stream().flatMap(yf -> createProfileNamedYamlFiles(yf, activeProfiles).stream()).toList();
        for (String profileNamedYamlFile: profileNamedYamlFiles) {
            ClassPathResource yamlFileResource = new ClassPathResource("routes/" + profileNamedYamlFile);
            if (!yamlFileResource.exists())//guard clause
                continue;
            List<PropertySource<?>> loadedProperty = yamlPropertySourceLoader.load(profileNamedYamlFile, yamlFileResource);
            propertySources.addAll(loadedProperty);
        }
        return propertySources;
    }
    private List<String> createProfileNamedYamlFiles(String baseYamlFile, String[] activeProfiles){
        ArrayList<String> profileNamedYamlFiles = new ArrayList<>();
        for (String activeProfile: activeProfiles){
            String profileNamedYamlFile = this.getProfileFileName(baseYamlFile, activeProfile);
            profileNamedYamlFiles.add(profileNamedYamlFile);
        }
        profileNamedYamlFiles.add(baseYamlFile);
        return profileNamedYamlFiles;
    }
    private String getProfileFileName(String baseFile, String profile) {
        int lastDotIndex = baseFile.lastIndexOf('.');
        if (lastDotIndex == -1) {
            // No extension, just append profile with dash
            return baseFile + "-" + profile;
        }

        String baseName = baseFile.substring(0, lastDotIndex);
        String extension = baseFile.substring(lastDotIndex);
        return baseName + "-" + profile + extension;
    }
}
