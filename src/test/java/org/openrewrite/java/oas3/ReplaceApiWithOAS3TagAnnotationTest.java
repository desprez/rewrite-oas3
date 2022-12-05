package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiWithOAS3TagAnnotationTest implements RewriteTest {

    @Override
    public void defaults(final RecipeSpec spec) {
        spec.recipe(new ReplaceApiWithOAS3TagAnnotation())
                .parser(JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
    }

    @Test
    void replaceApiWithTagAnnotation() {

        rewriteRun(java( // before
                """
                            import io.swagger.annotations.Api;

                            @Api("pet")
                            class PetApi {

                            }
                        """, // after
                """
                            import io.swagger.v3.oas.annotations.tags.Tag;

                            @Tag(name = "pet")
                            class PetApi {

                            }
                        """));
    }

    @Test
    void replaceApiWithTagAnnotationWith2argsOrMore() {

        rewriteRun(java( // before
                """
                            import io.swagger.annotations.Api;

                            @Api(value = "pet", description = "the pet API")
                            class PetApi {

                            }
                        """, // after
                """
                            import io.swagger.v3.oas.annotations.tags.Tag;

                            @Tag(name = "pet", description = "the pet API")
                            class PetApi {

                            }
                        """));
    }

}
