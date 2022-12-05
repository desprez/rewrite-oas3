package org.openrewrite.java.utils.annotation.attribute;

import java.util.Objects;

/**
 * Annotation attribute to replace.
 */
public class LegacyAttribute extends AbstractAttribute {

	/**
	 * Constructor
	 * 
	 * @param name of the attribute to replace
	 */
	public LegacyAttribute(String name) {
		super();
		Objects.requireNonNull(name);
		this.name = name;
	}

}