package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiImplicitParamWithOAS3ParameterAnnotationTest implements RewriteTest {

    @Override
    public void defaults(final RecipeSpec spec) {
        spec.recipe(new ReplaceApiImplicitParamWithOAS3ParameterAnnotation())
                .parser(JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
    }

    @Test
    void replaceApiImplicitParamAnnotation() {

        rewriteRun(java( // before
                """
                        import io.swagger.annotations.ApiImplicitParam;
                        import io.swagger.annotations.ApiImplicitParams;

                        public class PetApi {

                            @ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "User's name", required = true, dataType = "string", paramType = "query"),
                                    @ApiImplicitParam(name = "email", value = "User's email", required = false, dataType = "string", paramType = "query"),
                                    @ApiImplicitParam(name = "id", value = "User ID", required = true, dataType = "long", paramType = "query") })
                            public void doPost(final String request, final String response) throws Exception {

                            }
                        }
                        """, // after
                """
                        import io.swagger.annotations.ApiImplicitParams;
                        import io.swagger.v3.oas.annotations.Parameter;

                        public class PetApi {

                            @ApiImplicitParams({ @Parameter(name = "name", value = "User's name", required = true, dataType = "string", paramType = "query"),
                                    @Parameter(name = "email", value = "User's email", required = false, dataType = "string", paramType = "query"),
                                    @Parameter(name = "id", value = "User ID", required = true, dataType = "long", paramType = "query") })
                            public void doPost(final String request, final String response) throws Exception {

                            }
                        }
                        """));
    }

}
