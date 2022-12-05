package org.openrewrite.java.oas3;

import org.openrewrite.ExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.utils.annotation.LegacyAnnotationDescriptor;
import org.openrewrite.java.utils.annotation.NewAnnotationDescriptor;

/**
 * Generic Visitor used to migrate annotation's attributes.
 */
class DefaultAnnotationMigrationVisitor extends JavaIsoVisitor<ExecutionContext> {

	private final LegacyAnnotationDescriptor legacyAnnotation;
	private final NewAnnotationDescriptor newAnnotation;

	/**
	 * Constructor
	 * 
	 * @param legacyAnnotation the annotation to replace
	 * @param newAnnotation    the new annotation
	 */
	public DefaultAnnotationMigrationVisitor(final LegacyAnnotationDescriptor legacyAnnotation,
			final NewAnnotationDescriptor newAnnotation) {
		this.legacyAnnotation = legacyAnnotation;
		this.newAnnotation = newAnnotation;
	}

	@Override
	public J.Annotation visitAnnotation(final J.Annotation annotation, final ExecutionContext executionContext) {
		final J.Annotation a = super.visitAnnotation(annotation, executionContext);

		if (legacyAnnotation.matcher().matches(annotation)) {

			maybeAddImport(newAnnotation.fullyQualifiedTypeName());
			maybeRemoveImport(legacyAnnotation.fullyQualifiedTypeName());

			return newAnnotation.replace(a);
		}

		return a;
	}
}