package org.openrewrite.java.oas3;

import static org.junit.jupiter.api.Assertions.*;
import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

class SymplifyAuthorizationScopeAnnotationTest implements RewriteTest {

	@Override
	public void defaults(final RecipeSpec spec) {
		spec.recipe(new SymplifyAuthorizationScopeAnnotation()).parser(
				JavaParser.fromJavaVersion().logCompilationWarningsAndErrors(true).classpath("swagger-annotations"));
	}

	@Test
	void simplifyAnnotation() {
		// language=java
		rewriteRun(java(// Before
				"""
						import io.swagger.annotations.ApiOperation;
						import io.swagger.annotations.Authorization;
						import io.swagger.annotations.AuthorizationScope;

						public class PetApi {

							@ApiOperation(value = "Add a new pet to the store", notes = "", response = Void.class, authorizations = {
									@Authorization(value = "petstore_auth", scopes = {
											@AuthorizationScope(scope = "write:pets", description = "modify pets in your account") }) }, tags = { "pet" })
							public void someRequest() {

							}
						}
						""", // After
				"""
						import io.swagger.annotations.ApiOperation;
						import io.swagger.annotations.Authorization;
						import io.swagger.v3.oas.annotations.AuthorizationScope;

						public class PetApi {

							@ApiOperation(value = "Add a new pet to the store", notes = "", response = Void.class, authorizations = {
									@Authorization(value = "petstore_auth", scopes = {
											@AuthorizationScope(value = "write:pets") }) }, tags = { "pet" })
							public void someRequest() {

							}
						}
						"""));
	}


}
