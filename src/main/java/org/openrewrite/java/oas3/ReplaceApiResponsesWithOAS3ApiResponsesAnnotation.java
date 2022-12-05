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
 * Replace @ApiResponse annotations with the @ApiResponse annotations.
 * <ul>
 * <li>@ApiResponse changes to @ApiResponse
 * </ul>
 */
public class ReplaceApiResponsesWithOAS3ApiResponsesAnnotation extends Recipe {

	private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor(
			"io.swagger.annotations", "ApiResponses");
	private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor(
			"io.swagger.v3.oas.annotations.responses", "ApiResponses", AttributePairMigration.of("value", "value"));

	@Override
	public String getDisplayName() {
		return "Change `@ApiResponse` Swagger2 annotation to `@ApiResponse` OAS3 annotation";
	}

	@Override
	public String getDescription() {
		return "Changes `@ApiResponse` annotation to `@ApiResponse` annotation " //
				+ " and change the according `@ApiResponse` attributes names if needed.";
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