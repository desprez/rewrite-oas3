package org.openrewrite.java.utils.annotation;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.openrewrite.java.AnnotationMatcher;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Space;
import org.openrewrite.java.tree.TypeUtils;
import org.openrewrite.java.utils.AnnotationUtils;
import org.openrewrite.java.utils.annotation.attribute.AttributePairMigration;
import org.openrewrite.java.utils.annotation.attribute.AttributePairMigrations;
import org.openrewrite.marker.Markers;

/**
 * AnnotationDescriptor for the new annotation to replace the old annotation
 * with.
 */
public class NewAnnotationDescriptor implements AnnotationDescriptor {

	private final String fullyQualifiedTypeName;
	private final String simpleName;
	private final AnnotationMatcher matcher;

	private final AttributePairMigrations attributePairMigrations;

	/**
	 * Constructor with one AttributePairMigration
	 * 
	 * @param packageName            a package name of the annotation
	 * @param simpleName             a name of the annotation
	 * @param attributePairMigration a attributePairMigration
	 */
	public NewAnnotationDescriptor(final String packageName, final String simpleName,
			final AttributePairMigration attributePairMigration) {

		Objects.requireNonNull(packageName);
		Objects.requireNonNull(simpleName);
		Objects.requireNonNull(attributePairMigration);

		this.simpleName = simpleName;
		this.fullyQualifiedTypeName = packageName + "." + simpleName;
		this.matcher = new AnnotationMatcher("@" + this.fullyQualifiedTypeName);
		this.attributePairMigrations = new AttributePairMigrations(attributePairMigration);
	}

	/**
	 * Constructor with list of AttributePairMigration
	 * 
	 * @param packageName             a package name of the annotation
	 * @param simpleName              a name of the annotation
	 * @param attributePairMigrations a attributePairMigration list
	 */
	public NewAnnotationDescriptor(final String packageName, final String simpleName,
			final AttributePairMigration... attributePairMigrations) {

		Objects.requireNonNull(packageName);
		Objects.requireNonNull(simpleName);
		Objects.requireNonNull(attributePairMigrations);

		this.simpleName = simpleName;
		this.fullyQualifiedTypeName = packageName + "." + simpleName;
		this.matcher = new AnnotationMatcher("@" + this.fullyQualifiedTypeName);
		this.attributePairMigrations = new AttributePairMigrations(attributePairMigrations);
	}

	@Override
	public String simpleName() {
		return simpleName;
	}

	@Override
	public String fullyQualifiedTypeName() {
		return fullyQualifiedTypeName;
	}

	/**
	 * Return a AnnotationMatcher for this annotation (with @ and
	 * fullyQualifiedTypeName)
	 * 
	 * @return a AnnotationMatcher for this annotation
	 */
	public AnnotationMatcher matcher() {
		return matcher;
	}

	/**
	 * Replace this Annotation with the given one.
	 * 
	 * @param a the new Annotation
	 * @return an Annotation
	 */
    public J.Annotation replace(J.Annotation a) {
        final J.Identifier oldIdentifier = (J.Identifier) a.getAnnotationType();
        final J.Identifier newIdentifier = oldIdentifier.withSimpleName(simpleName())
                .withType(JavaType.buildType(fullyQualifiedTypeName()));

        final List<Expression> newAttributes = AnnotationUtils.buildNewAttributeExpressions(a.getArguments(), attributePairMigrations);

        if (newAttributes.isEmpty()) {
            return a.withAnnotationType(newIdentifier);
        }

        return a.withAnnotationType(newIdentifier)
                .withArguments(newAttributes);
    }

	/**
	 * Create an Annotation with this descriptor
	 * 
	 * @return an Annotation
	 */
	public J.Annotation create() {
		final J.Identifier identifier = new J.Identifier(UUID.randomUUID(), Space.EMPTY, Markers.EMPTY, simpleName(),
				JavaType.buildType(fullyQualifiedTypeName()), null);
		return new J.Annotation(UUID.randomUUID(), Space.EMPTY, Markers.EMPTY, identifier, null);
	}
}