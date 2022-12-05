package org.openrewrite.java.oas3;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class ReplaceApiOperationWithOAS3OperationAnnotationTest implements RewriteTest {

	@Override
	public void defaults(final RecipeSpec spec) {
		spec.recipe(new ReplaceApiOperationWithOAS3OperationAnnotation()).parser(
				JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
	}

	@Test
	void replaceAnnotation() {
		// language=java
		rewriteRun(java(// Before
				"""
						import io.swagger.annotations.ApiOperation;

						public class PetApi {

						    @ApiOperation(value = "foo", notes = "bar")
						    public void someRequest() {

						    }
						}
						                """, // After
				"""
						import io.swagger.v3.oas.annotations.Operation;

						public class PetApi {

						    @Operation(summary = "foo", description = "bar")
						    public void someRequest() {

						    }
						}
						"""));
	}

	@Test
	void replaceAnnotationWithSecurity() {
		// language=java
		rewriteRun(java(// Before
				"""
						import io.swagger.annotations.ApiOperation;
						import io.swagger.annotations.Authorization;
						import io.swagger.annotations.AuthorizationScope;
						
						public class PetApi {

							@ApiOperation(value = "Add a new pet to the store", notes = "Add a new pet to the store", response = Void.class, authorizations = {
									@Authorization(value = "petstore_auth", scopes = {
											@AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
											@AuthorizationScope(scope = "read:pets", description = "read your pets") }) }, tags = { "pet" })
							public void someRequest() {

							}
						}
						""", // After
				"""
						import io.swagger.annotations.Authorization;
						import io.swagger.annotations.AuthorizationScope;
						import io.swagger.v3.oas.annotations.Operation;
						
						public class PetApi {

							@Operation(summary = "Add a new pet to the store", description = "Add a new pet to the store", security = {
									@Authorization(value = "petstore_auth", scopes = {
											@AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
											@AuthorizationScope(scope = "read:pets", description = "read your pets") }) }, tags = { "pet" })
							public void someRequest() {

							}
						}
						"""));
	}

}
