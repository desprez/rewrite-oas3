package org.openrewrite.java.utils.annotation.attribute;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.Assignment;
import org.openrewrite.java.tree.J.Identifier;
import org.openrewrite.java.tree.J.NewArray;
import org.openrewrite.java.tree.JLeftPadded;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

/**
 * New Annotation attribute.
 */
public class NewAttribute extends AbstractAttribute {

	/**
	 * Forced type of the new attribute
	 */
	protected String type;

	/**
	 * Constructor
	 * 
	 * @param name the new name of the annotation attribute
	 */
	public NewAttribute(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}

	/**
	 * Constructor
	 * 
	 * @param name    the new name of the annotation attribute
	 * @param newType the new type of the annotation attribute
	 */
	public NewAttribute(String name, String newType) {
		this(name);
		this.type = newType;
	}

	/**
	 * Build Expression according to the given Assignment, Identifier and Literal.
	 * 
	 * @param assignment the Assignment
	 * @param identifier the Identifier
	 * @param literal    the Literal
	 * @return an new Expression
	 */
	public Expression buildAttributeExpression(final J.Assignment assignment, final J.Identifier identifier,
			final J.Literal literal) {

		final String newValueSource = maybeQuoteStringArgument(literal.getValueSource());

		return assignment.withVariable(identifier.withSimpleName(name()))
				.withAssignment(literal.withValueSource(newValueSource));
	}

	/**
	 * Build Expression according to the given Assignment, Identifier and NewArray.
	 * 
	 * @param assignment the Assignment
	 * @param identifier the Identifier
	 * @param newArray   the NewArray
	 * @return an new Expression
	 */
	public Expression buildAttributeExpression(Assignment assignment, Identifier identifier, NewArray newArray) {
		final List<Expression> newValueSource = newArray.getInitializer();

		return assignment.withVariable(identifier.withSimpleName(name()))
				.withAssignment(newArray.withInitializer(newValueSource));
	}

	/**
	 * Build Expression according to the given Literal.
	 * 
	 * @param literal the Literal
	 * @return an new Expression
	 */
	public Expression buildAttributeExpression(final J.Literal literal) {
		final String newValueSource = literal.getValueSource();
		final String newSimpleName = name();

		final J.Identifier identifier = new J.Identifier(UUID.randomUUID(), Space.EMPTY, Markers.EMPTY, newSimpleName,
				literal.getType(), null);

		final JLeftPadded<Expression> innerAssignment = new JLeftPadded<>(Space.build(" ", Collections.emptyList()),
				literal.withValueSource(newValueSource).withPrefix(Space.build(" ", Collections.emptyList())),
				Markers.EMPTY);

		return new J.Assignment(UUID.randomUUID(), Space.EMPTY, Markers.EMPTY, identifier, innerAssignment,
				literal.getType());
	}

	private String maybeQuoteStringArgument(String attributeValue) {
		if (attributeIsString()) {
			return "\"" + attributeValue + "\"";
		}
		return attributeValue;
	}

	private boolean attributeIsString() {
		return "java.lang.String".equals(type);
	}

}