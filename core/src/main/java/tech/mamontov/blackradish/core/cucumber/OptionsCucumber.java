package tech.mamontov.blackradish.core.cucumber;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.testng.CucumberOptions;
import org.reflections.Reflections;
import tech.mamontov.blackradish.specs.Spec;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class OptionsCucumber implements CucumberOptions {
    private final CucumberOptions cucumberOptionsAnnotation;

    private final String className;

    public OptionsCucumber(@SuppressWarnings("rawtypes") Class clazz) {
        this.cucumberOptionsAnnotation = (CucumberOptions) clazz.getAnnotation(CucumberOptions.class);
        this.className = clazz.getName();
    }

    @Override
    public boolean dryRun() {
        return cucumberOptionsAnnotation.dryRun();
    }

    @Override
    public String[] features() {
        return cucumberOptionsAnnotation.features();
    }

    @Override
    public String[] glue() {
        LinkedHashSet<String> glue = new LinkedHashSet<>(Arrays.asList(cucumberOptionsAnnotation.glue()));

        Reflections reflections = new Reflections("tech.mamontov.blackradish");
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Spec.class);

        List<String> packages = set.stream()
                .map(Class::getPackageName)
                .distinct()
                .toList();

        if (!packages.isEmpty()) {
            glue.addAll(packages);
        }

        return glue.toArray(new String[]{});
    }

    @Override
    public String[] extraGlue() {
        return cucumberOptionsAnnotation.extraGlue();
    }

    @Override
    public String tags() {
        return cucumberOptionsAnnotation.tags();
    }

    @Override
    public String[] plugin() {
        LinkedHashSet<String> plugin = new LinkedHashSet<>(Arrays.asList(cucumberOptionsAnnotation.plugin()));

        String testSuffix = System.getProperty("TESTSUFFIX");
        String targetExecutionsPath = "target/executions/";

        if (testSuffix != null) {
            targetExecutionsPath = targetExecutionsPath + testSuffix + "/";
        }

        plugin.add("testng:" + targetExecutionsPath + this.className + ".xml");
        plugin.add("tech.mamontov.blackradish.core.plugins.ConfigurationLoader");
        plugin.add("tech.mamontov.blackradish.core.plugins.Formatter");
        plugin.add("io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm");

        return plugin.toArray(new String[]{});

    }

    @Override
    public boolean publish() {
        return false;
    }

    @Override
    public boolean monochrome() {
        return cucumberOptionsAnnotation.monochrome();
    }

    @Override
    public String[] name() {
        return cucumberOptionsAnnotation.name();
    }

    @Override
    public SnippetType snippets() {
        return cucumberOptionsAnnotation.snippets();
    }

    @Override
    public Class<? extends ObjectFactory> objectFactory() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
