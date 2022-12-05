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
 * Simplify `@AuthorizationScope` Swagger2 annotation
 * <ul>
 * <li>@AuthorizationScope(scope = "write:pets", description = "modify pets in your account") changes to @AuthorizationScope(value = "write:pets")
 * </ul>
 */
public class SymplifyAuthorizationScopeAnnotation extends Recipe {

	private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor(
			"io.swagger.annotations", "AuthorizationScope");
	private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor("io.swagger.v3.oas.annotations",
			"AuthorizationScope", AttributePairMigration.of("scope", "value"),
			AttributePairMigration.of("description", ""));

	@Override
	public String getDisplayName() {
		return "Simplify `@AuthorizationScope` Swagger2 annotation";
	}

	@Override
	public String getDescription() {
		return "Simplify `@AuthorizationScope` Swagger2 annotation.";
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
