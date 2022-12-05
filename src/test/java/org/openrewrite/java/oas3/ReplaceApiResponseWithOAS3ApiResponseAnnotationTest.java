package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiResponseWithOAS3ApiResponseAnnotationTest implements RewriteTest {

	@Override
	public void defaults(final RecipeSpec spec) {
		spec.recipe(new ReplaceApiResponseWithOAS3ApiResponseAnnotation()).parser(
				JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
	}

	@Test
	void replaceAnnotation() {
		// language=java
		rewriteRun(java(// Before
				"""
						import io.swagger.annotations.ApiResponse;
						import io.swagger.annotations.ApiResponses;

						public class PetApi {

						    @ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid ID supplied", response = Void.class),
								@ApiResponse(code = 404, message = "Pet not found", response = Void.class),
								@ApiResponse(code = 405, message = "Validation exception", response = Void.class) })
						    void updatePet() {

						    }

						}
						""", // After
				"""
						import io.swagger.annotations.ApiResponses;
						import io.swagger.v3.oas.annotations.responses.ApiResponse;

						public class PetApi {

						    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Invalid ID supplied", response = Void.class),
								@ApiResponse(responseCode = "404", description = "Pet not found", response = Void.class),
								@ApiResponse(responseCode = "405", description = "Validation exception", response = Void.class) })
						    void updatePet() {

						    }

						}
						"""));
	}

}
