package org.openrewrite.java.utils.annotation.attribute;

/**
 * Class used to associate tuple of annotation's attributes, the old one and the
 * new one.
 */
public class AttributePairMigration {

	private LegacyAttribute legacyAttribute;

	private NewAttribute newAttribute;

	/**
	 * Constructor
	 * 
	 * @param legacyAttribute the Attribute to replace
	 * @param newAttribute    New Attribute to replace the old Attribute with.
	 */
	public AttributePairMigration(LegacyAttribute legacyAttribute, NewAttribute newAttribute) {
		this.legacyAttribute = legacyAttribute;
		this.newAttribute = newAttribute;
	}

	/**
	 * Factory method to get a new AttributePairMigration.
	 * 
	 * @param legacyName the Attribute name to replace.
	 * @param newName    the Attribute to replace the old Attribute with.
	 * @return an AttributePairMigration
	 */
	public static AttributePairMigration of(String legacyName, String newName) {
		return new AttributePairMigration(new LegacyAttribute(legacyName), new NewAttribute(newName));
	}

	/**
	 * Factory method to get a new AttributePairMigration with new type parameter.
	 * 
	 * @param legacyName the Attribute name to replace.
	 * @param newName    the Attribute to replace the old Attribute with.
	 * @param newType    the new Attribute type
	 * @return an AttributePairMigration
	 */
	public static AttributePairMigration of(final String legacyName, final String newName, String newType) {
		return new AttributePairMigration(new LegacyAttribute(legacyName), new NewAttribute(newName, newType));
	}

	/**
	 * New Attribute to replace the old Attribute with.
	 * 
	 * @return the NewAttribute
	 */
	public NewAttribute newAttribute() {
		return newAttribute;
	}

	/**
	 * the Attribute to replace.
	 * 
	 * @return the LegacyAttribute
	 */
	public LegacyAttribute legacyAttribute() {
		return legacyAttribute;
	}

}