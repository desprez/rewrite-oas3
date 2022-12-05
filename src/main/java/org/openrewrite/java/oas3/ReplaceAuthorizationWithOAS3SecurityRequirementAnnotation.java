package org.openrewrite.java.oas3;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.search.UsesType;
import org.openrewrite.java.utils.annotation.LegacyAnnotationDescriptor;
import org.openrewrite.java.utils.annotation.NewAnnotationDescriptor;
import org.openrewrite.java.utils.annotation.attribute.AttributePairMigration;

/**
 * Replace @Api annotations with the @Tag annotations.
 * <ul>
 * <li>@Authorization(value = "petstore_auth", scopes = {...}) changes to @SecurityRequirement(name = "petstore_auth", scopes = {...})
 * </ul>
 */
public class ReplaceAuthorizationWithOAS3SecurityRequirementAnnotation extends Recipe {

	private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor(
			"io.swagger.annotations", "Authorization");
	private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor(
			"io.swagger.v3.oas.annotations.security", "SecurityRequirement", AttributePairMigration.of("value", "name") );

	@Override
	public String getDisplayName() {
		return "Replace `@Authorization` Swagger2 annotation with `@SecurityRequirement` OAS3 annotation";
	}

	@Override
	public String getDescription() {
		return "Replace `@Authorization` annotation with `@SecurityRequirement` annotation " + //
				" and change the according `@SecurityRequirement` attributes names if needed.";
	}

	@Override
	protected @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
		return new UsesType<>(LEGACY_ANNOTATION.fullyQualifiedTypeName());
	}

	@Override
	protected JavaIsoVisitor<ExecutionContext> getVisitor() {
		return new DefaultAnnotationMigrationVisitor(LEGACY_ANNOTATION, NEW_ANNOTATION);
	}

}
