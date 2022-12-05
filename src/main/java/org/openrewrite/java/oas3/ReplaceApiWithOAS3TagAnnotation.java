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
 * <li>@Api changes to @Tag
 * </ul>
 */
public class ReplaceApiWithOAS3TagAnnotation extends Recipe {

	private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor(
			"io.swagger.annotations", "Api");
	private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor(
			"io.swagger.v3.oas.annotations.tags", "Tag", AttributePairMigration.of("value", "name"),
			AttributePairMigration.of("tags", "name"));

	@Override
	public String getDisplayName() {
		return "Replace `@Api` Swagger2 annotation with `@Tag` OAS3 annotation";
	}

	@Override
	public String getDescription() {
		return "Replace `@Api` annotation with `@Tag` annotation " + //
				" and change the according `@Tag` attributes names if needed.";
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
