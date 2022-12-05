package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiModelWithOAS3SchemaAnnotationTest implements RewriteTest {

    @Override
    public void defaults(final RecipeSpec spec) {
        spec.recipe(new ReplaceApiModelWithOAS3SchemaAnnotation())
                .parser(JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
    }

    @Test
    void replaceApiModelAnnotation() {
        // language=java
        rewriteRun(java( // Before
                """
                           import io.swagger.annotations.ApiModel;
                           import io.swagger.annotations.ApiModelProperty;

                           @ApiModel(value = "Pet", description = "Pet data")
                           public class Pet {

                               private String name;

                               @ApiModelProperty(example = "doggie", required = true, value = "name")
                               public String getName() {
                                   return name;
                               }

                               public void setName(final String name) {
                                   this.name = name;
                               }

                           }
                        """, // After
                """
                           import io.swagger.annotations.ApiModelProperty;
                           import io.swagger.v3.oas.annotations.media.Schema;

                           @Schema(name = "Pet", description = "Pet data")
                           public class Pet {

                               private String name;

                               @ApiModelProperty(example = "doggie", required = true, value = "name")
                               public String getName() {
                                   return name;
                               }

                               public void setName(final String name) {
                                   this.name = name;
                               }

                           }
                        """));
    }

}
