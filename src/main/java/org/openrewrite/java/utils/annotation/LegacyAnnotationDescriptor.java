package org.openrewrite.java.utils.annotation;

import java.util.Objects;

import org.openrewrite.java.AnnotationMatcher;

/**
 * AnnotationDescriptor for the annotation to replace.
 */
public class LegacyAnnotationDescriptor implements AnnotationDescriptor {

	private final String fullyQualifiedTypeName;
	private final String simpleName;
	private final AnnotationMatcher matcher;

	/**
	 * Constructor
	 * 
	 * @param packageName            a package name of the annotation
	 * @param simpleName             a name of the annotation
	 */
	public LegacyAnnotationDescriptor(final String packageName, final String simpleName) {
		Objects.requireNonNull(packageName);
		Objects.requireNonNull(simpleName);

		this.simpleName = simpleName;
		this.fullyQualifiedTypeName = packageName + "." + simpleName;
		this.matcher = new AnnotationMatcher("@" + this.fullyQualifiedTypeName);
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
}