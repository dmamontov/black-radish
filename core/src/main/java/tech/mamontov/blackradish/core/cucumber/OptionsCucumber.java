package tech.mamontov.blackradish.core.cucumber;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.testng.CucumberOptions;
import org.reflections.Reflections;
import tech.mamontov.blackradish.core.annotations.Glue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Override cucumber options
 *
 * @author Dmitry Mamontov
 */
@SuppressWarnings("unchecked")
public class OptionsCucumber implements CucumberOptions {
    private final CucumberOptions cucumberOptionsAnnotation;

    private final String className;

    private final Boolean isParallel;

    /**
     * Constructor
     *
     * @param clazz Class
     * @param isParallel Boolean
     */
    public OptionsCucumber(@SuppressWarnings("rawtypes") Class clazz, Boolean isParallel) {
        this.cucumberOptionsAnnotation = (CucumberOptions) clazz.getAnnotation(CucumberOptions.class);
        this.className = clazz.getName();
        this.isParallel = isParallel;
    }

    /**
     * Dry run
     *
     * @return Boolean
     */
    @Override
    public boolean dryRun() {
        return cucumberOptionsAnnotation.dryRun();
    }

    /**
     * Features list
     *
     * @return String[]
     */
    @Override
    public String[] features() {
        return cucumberOptionsAnnotation.features();
    }

    /**
     * Glue list
     *
     * @return String[]
     */
    @Override
    public String[] glue() {
        LinkedHashSet<String> glue = new LinkedHashSet<>(Arrays.asList(cucumberOptionsAnnotation.glue()));

        List<String> packages = this.getPackages(Glue.class);
        if (!packages.isEmpty()) {
            glue.addAll(packages);
        }

        return glue.toArray(new String[]{});
    }

    /**
     * Extra glue list
     *
     * @return String[]
     */
    @Override
    public String[] extraGlue() {
        return cucumberOptionsAnnotation.extraGlue();
    }

    /**
     * Tags
     *
     * @return String
     */
    @Override
    public String tags() {
        return cucumberOptionsAnnotation.tags();
    }

    /**
     * Plugin list
     *
     * @return String[]
     */
    @Override
    public String[] plugin() {
        LinkedHashSet<String> plugin = new LinkedHashSet<>(Arrays.asList(cucumberOptionsAnnotation.plugin()));

        String testSuffix = System.getProperty("TESTSUFFIX");
        String targetExecutionsPath = "target/executions/";

        if (testSuffix != null) {
            targetExecutionsPath = targetExecutionsPath + testSuffix + "/";
        }

        plugin.add("testng:" + targetExecutionsPath + this.className + ".xml");
        plugin.add("tech.mamontov.blackradish.core.plugins.Loader");
        plugin.add("tech.mamontov.blackradish.core.plugins.Formatter");
        plugin.add("io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm");

        if (System.getenv("ENABLE_SPHINX") != null && !this.isParallel) {
            plugin.add("tech.mamontov.blackradish.core.plugins.SphinxFormatter");
        }

        return plugin.toArray(new String[]{});

    }

    /**
     * Is publish
     *
     * @return Boolean
     */
    @Override
    public boolean publish() {
        return false;
    }

    /**
     * Is monochrome
     *
     * @return Boolean
     */
    @Override
    public boolean monochrome() {
        return cucumberOptionsAnnotation.monochrome();
    }

    /**
     * Get name array
     *
     * @return String[]
     */
    @Override
    public String[] name() {
        return cucumberOptionsAnnotation.name();
    }

    /**
     * Get snippets
     * @return SnippetType
     */
    @Override
    public SnippetType snippets() {
        return cucumberOptionsAnnotation.snippets();
    }

    /**
     * Get object factory
     *
     * @return Class
     */
    @Override
    public Class<? extends ObjectFactory> objectFactory() {
        return null;
    }

    /**
     * Get annotation type
     *
     * @return Class
     */
    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    /**
     * Get packages list
     *
     * @param clazz Class
     * @return List<String>
     */
    private List<String> getPackages(Class<? extends Annotation> clazz) {
        Reflections reflections = new Reflections("tech.mamontov.blackradish");
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(clazz);

        return set.stream()
                .map(Class::getPackageName)
                .distinct()
                .toList();
    }
}
