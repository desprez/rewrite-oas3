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
 * Replace method declaration @ApiOperation annotations with the @Operation
 * annotations and change the according attributes names.
 * <ul>
 * <li>@ApiOperation(value = "foo", notes = "bar") changes to @Operation(summary
 * = "foo", description = "bar")
 * </ul>
 */
public class ReplaceApiOperationWithOAS3OperationAnnotation extends Recipe {

	private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor(
			"io.swagger.annotations", "ApiOperation");
	private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor(
			"io.swagger.v3.oas.annotations", "Operation", AttributePairMigration.of("value", "summary"),
			AttributePairMigration.of("notes", "description"), 
			AttributePairMigration.of("response", ""), 
			AttributePairMigration.of("authorizations", "security"));

	@Override
	public String getDisplayName() {
		return "Replace `@ApiOperation` with `@Operation`";
	}

	@Override
	public String getDescription() {
		return "Replace method declaration `@ApiOperation` annotations with the `@Operation` annotations"
				+ " and change the according `@Operation` attributes names if needed.";
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
