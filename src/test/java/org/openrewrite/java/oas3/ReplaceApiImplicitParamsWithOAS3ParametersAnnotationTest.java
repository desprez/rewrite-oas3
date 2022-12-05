package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiImplicitParamsWithOAS3ParametersAnnotationTest implements RewriteTest {

    @Override
    public void defaults(final RecipeSpec spec) {
        spec.recipe(new ReplaceApiImplicitParamsWithOAS3ParametersAnnotation())
                .parser(JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
    }

    @Test
    void replaceApiImplicitParamsAnnotation() {

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
                        import io.swagger.annotations.ApiImplicitParam;
                        import io.swagger.v3.oas.annotations.Parameters;

                        public class PetApi {

                            @Parameters({ @ApiImplicitParam(name = "name", value = "User's name", required = true, dataType = "string", paramType = "query"),
                                    @ApiImplicitParam(name = "email", value = "User's email", required = false, dataType = "string", paramType = "query"),
                                    @ApiImplicitParam(name = "id", value = "User ID", required = true, dataType = "long", paramType = "query") })
                            public void doPost(final String request, final String response) throws Exception {

                            }
                        }
                        """));
    }

}
