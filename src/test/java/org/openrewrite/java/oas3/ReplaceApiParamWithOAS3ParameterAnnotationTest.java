package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiParamWithOAS3ParameterAnnotationTest implements RewriteTest {

	@Override
	public void defaults(final RecipeSpec spec) {
		spec.recipe(new ReplaceApiParamWithOAS3ParameterAnnotation()).parser(
				JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
	}

	@Test
	void replaceAnnotation() {
		// language=java
		rewriteRun(java(// Before
				"""
						import io.swagger.annotations.ApiParam;

						public class PetApi {

						    public void someRequest(@ApiParam(name = "petId", value = "ID of pet that needs to be updated", defaultValue = "0", hidden = true, required = true) final Long petId) {

						    }
						}
						                """, // After
				"""
						import io.swagger.v3.oas.annotations.Parameter;

						public class PetApi {

						    public void someRequest(@Parameter(name = "petId", description = "ID of pet that needs to be updated", example = "0", hidden = true, required = true) final Long petId) {

						    }
						}
						"""));
	}

}
