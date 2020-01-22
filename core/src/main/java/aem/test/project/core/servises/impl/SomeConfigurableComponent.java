package aem.test.project.core.servises.impl;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

import java.util.Arrays;
import java.util.List;

@Component(
        property = {
                "my.osgi.property=MY-VALUE",
                "another.osgi.property=" + SomeConfigurableComponent.ANOTHER_VALUE
        },
        configurationPolicy = ConfigurationPolicy.REQUIRE
)
@Designate(ocd = SomeConfigurableComponent.Config.class)
public class SomeConfigurableComponent {

    public static final String ANOTHER_VALUE = "10";
    public static final List<String> STRINGS = Arrays.asList("a", "b", "c");

    private Config config;

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
        int i = 10;
        int maxSize = config.max_size();
        System.out.println("aaaa" + maxSize);
    }

    @ObjectClassDefinition
    @interface Config {


        @AttributeDefinition(
                name = "Max size",
                description = "The Max size ",
                min = "10",
                max = "100",
                required = true
        )
        int max_size() default 10;

        @AttributeDefinition(
                name = "Items"
        )
        String[] items();

        @AttributeDefinition(
                options = {
                        @Option(label = "Option For", value = "foo")

                }
        )
        String foo_bar() default "bar";

    }
}
