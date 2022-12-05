package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

@Disabled("Disabled until fixed!")
class RemoveApiIgnoreAnnotationTest implements RewriteTest {

	@Override
	public void defaults(final RecipeSpec spec) {
		spec.recipe(new RemoveApiIgnoreAnnotation()).parser(JavaParser.fromJavaVersion()
				.logCompilationWarningsAndErrors(true).classpath("springfox-core", "swagger-annotations"));
	}

	@Test
	void removeApiIgnoreMethodAnnotation() {

		rewriteRun(java( // before
				"""
						import io.swagger.annotations.ApiResponse;
						import springfox.documentation.annotations.ApiIgnore;

						public class PetApi {

						    @ApiIgnore @ApiResponse(code = 200, message = " ", response = Number.class)
						    public void someRequestWithResponse() {}
						}

						        """, // after
				"""
						import io.swagger.annotations.ApiResponse;

						public class PetApi {

						    @ApiResponse(code = 200, message = " ", response = Number.class)
						    public void someRequestWithResponse() {}
						}
						        """));
	}

	@Test
	void removeApiIgnoreParameterAnnotation() {

		rewriteRun(java( // before
				"""
						import springfox.documentation.annotations.ApiIgnore;

						public class PetApi {

						    public void someRequestWithResponse(@ApiIgnore Integer doNotShow) {}
						}
						""", // after
				"""
						public class PetApi {

						    public void someRequestWithResponse( Integer doNotShow) {}
						}

						"""));
	}
}
