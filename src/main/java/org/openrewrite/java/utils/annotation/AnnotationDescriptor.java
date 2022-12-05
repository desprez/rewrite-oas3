package org.openrewrite.java.utils.annotation;

/**
 * Descriptor of an annotation.
 */
public interface AnnotationDescriptor {

	/**
	 * Simple name of an annotation (without package)
	 * 
	 * @return name of an annotation
	 */
	String simpleName();

	/**
	 * Fully qualified name of an annotation (with package)
	 * 
	 * @return Fully qualified name of an annotation
	 */
	String fullyQualifiedTypeName();

}