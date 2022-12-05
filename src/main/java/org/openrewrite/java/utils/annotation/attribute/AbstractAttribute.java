package org.openrewrite.java.utils.annotation.attribute;

/**
 * Super Class for Annotation's Attributes.
 */
public class AbstractAttribute {

	/**
	 * Attribute's name
	 */
	protected String name;

	/**
	 * Check if attribute's name is "Value"
	 * 
	 * @return true if name is equals to "value"
	 */
	public boolean isValueAttribute() {
		return "value".equals(name);
	}

	/**
	 * Getter
	 * 
	 * @return name of the attribute to replace
	 */
	public String name() {
		return name;
	}

}