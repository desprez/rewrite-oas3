package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiModelPropertyWithOAS3SchemaAnnotationTest implements RewriteTest {

    @Override
    public void defaults(final RecipeSpec spec) {
        spec.recipe(new ReplaceApiModelPropertyWithOAS3SchemaAnnotation())
                .parser(JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
    }

    @Test
    void replaceAnnotationWithExplicitAttributes() {
        // language=java
        rewriteRun(java("""
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
                """, """
                   import io.swagger.annotations.ApiModel;
                   import io.swagger.v3.oas.annotations.media.Schema;

                   @ApiModel(value = "Pet", description = "Pet data")
                   public class Pet {

                       private String name;

                       @Schema(example = "doggie", required = true, description = "name")
                       public String getName() {
                           return name;
                       }

                       public void setName(final String name) {
                           this.name = name;
                       }

                   }
                """));
    }

    @Test
    void replaceAnnotationWithImplicitAttribute() {
        // language=java
        rewriteRun(java("""
                   import io.swagger.annotations.ApiModel;
                   import io.swagger.annotations.ApiModelProperty;

                   @ApiModel(value = "Pet", description = "Pet data")
                   public class Pet {

                       private String name;

                       @ApiModelProperty(  "name")
                       public String getName() {
                           return name;
                       }

                       public void setName(final String name) {
                           this.name = name;
                       }

                   }
                """, """
                   import io.swagger.annotations.ApiModel;
                   import io.swagger.v3.oas.annotations.media.Schema;

                   @ApiModel(value = "Pet", description = "Pet data")
                   public class Pet {

                       private String name;

                       @Schema(description = "name")
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
