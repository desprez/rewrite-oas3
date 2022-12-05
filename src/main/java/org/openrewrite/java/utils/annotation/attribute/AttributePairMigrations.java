package org.openrewrite.java.utils.annotation.attribute;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class used to handle list of tuples of annotation's attributes, the old ones
 * and the new ones.
 */
public class AttributePairMigrations {

	private final NavigableMap<LegacyAttribute, AttributePairMigration> pairs;

	/**
	 * Constructor
	 * 
	 * @param attributePairMigrations list of AttributePairMigration
	 */
	public AttributePairMigrations(final AttributePairMigration... attributePairMigrations) {
		Objects.requireNonNull(attributePairMigrations);
		this.pairs = Arrays.stream(attributePairMigrations)
				.collect(Collectors.toMap(AttributePairMigration::legacyAttribute, Function.identity(), (m1, m2) -> m1,
						() -> new TreeMap<>(Comparator.comparing(LegacyAttribute::name))));
	}

	/**
	 * Return true if a LegacyAttribute exist with the given name.
	 * 
	 * @param legacyName the legacy attribute name to search
	 * @return true if a LegacyAttribute exist with the given name.
	 */
	public boolean hasLegacyAttribute(final String legacyName) {
		return legacyName != null && pairs.containsKey(new LegacyAttribute(legacyName));
	}

	/**
	 * Get new attribute according to the given legacy attribute name.
	 * 
	 * @param legacyName the given legacy attribute name
	 * @return the new attribute name
	 */
	public NewAttribute getNewAttribute(final String legacyName) {
		return pairs.get(new LegacyAttribute(legacyName)).newAttribute();
	}

	/**
	 * Get the first NewAttribute of the Attribute Pair list.
	 * 
	 * @return the first attribute of the Attribute Pair list
	 */
	public NewAttribute getNewFirstAttribute() {
		return pairs.firstEntry().getValue().newAttribute();
	}

	/**
	 * Get AttributePairMigration according to the given legacy attribute name.
	 * 
	 * @param legacyName legacyName the given legacy attribute name
	 * @return an AttributePairMigration
	 */
	public AttributePairMigration getAttributePairMigration(final String legacyName) {
		return pairs.get(new LegacyAttribute(legacyName));
	}
}