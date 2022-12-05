package org.openrewrite.java.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J.Assignment;
import org.openrewrite.java.tree.J.Identifier;
import org.openrewrite.java.tree.J.Literal;
import org.openrewrite.java.tree.J.NewArray;
import org.openrewrite.java.utils.annotation.attribute.AttributePairMigration;
import org.openrewrite.java.utils.annotation.attribute.AttributePairMigrations;
import org.openrewrite.java.utils.annotation.attribute.LegacyAttribute;
import org.openrewrite.java.utils.annotation.attribute.NewAttribute;

/**
 * Utility class used to build annotation expressions.
 */
public class AnnotationUtils {

	/**
	 * Utility method used to build an Expression from the legacy Expression with
	 * the new attribute.
	 * 
	 * @param argument                the legacy expression
	 * @param attributePairMigrations pairs of attributes
	 * @param annotation              the visited Annotation
	 * @return an optional Expression
	 */
	private static Optional<Expression> buildAttributeExpression(final Expression argument,
			final AttributePairMigrations attributePairMigrations) {

		if (argument instanceof Assignment) {
			Assignment assignment = (Assignment) argument;
			if (assignment.getVariable() instanceof Identifier) {
				Identifier identifier = (Identifier) assignment.getVariable();

				if (attributePairMigrations.hasLegacyAttribute(identifier.getSimpleName())) {
					final NewAttribute newAttribute = attributePairMigrations
							.getNewAttribute(identifier.getSimpleName());
					if (assignment.getAssignment() instanceof Literal) {
						return Optional.of(newAttribute.buildAttributeExpression(assignment, identifier,
								(Literal) assignment.getAssignment()));
					}
				}
			}
		}
		if (!(argument instanceof Literal)) {
			return Optional.empty();
		}
		Literal literal = (Literal) argument;
		if (attributePairMigrations.hasLegacyAttribute("value")) {
			final AttributePairMigration attributePairMigration = attributePairMigrations
					.getAttributePairMigration("value");
			final NewAttribute newAttribute = attributePairMigration.newAttribute();
			final LegacyAttribute legacyAttribute = attributePairMigration.legacyAttribute();

			if (legacyAttribute.isValueAttribute() && newAttribute.isValueAttribute()) {
				return Optional.of(literal);
			} else {
				return Optional.of(newAttribute.buildAttributeExpression(literal));
			}
		} else {
			final NewAttribute newAttribute = attributePairMigrations.getNewFirstAttribute();
			return Optional.of(newAttribute.buildAttributeExpression(literal));
		}
	}

	/**
	 * Utility method used to build an Expression list from the legacy Expression
	 * list with the new attribute.
	 * 
	 * @param arguments               the legacy expression list
	 * @param attributePairMigrations list of pairs of attributes
	 * @return a list of Expression
	 */
	public static List<Expression> buildNewAttributeExpressions(final List<Expression> arguments,
			final AttributePairMigrations attributePairMigrations) {
		if (arguments == null || arguments.isEmpty()) {
			return Collections.emptyList();
		}

		if (arguments.size() == 1) {
			return buildAttributeExpression(arguments.get(0), attributePairMigrations).map(Collections::singletonList)
					.orElseGet(Collections::emptyList);
		}

		return arguments.stream().map(e -> {
			if (e instanceof Assignment) {
				Assignment assignment = (Assignment) e;
				if (assignment.getVariable() instanceof Identifier) {
					Identifier identifier = (Identifier) assignment.getVariable();

					if (attributePairMigrations.hasLegacyAttribute(identifier.getSimpleName())) {
						final NewAttribute newAttribute = attributePairMigrations
								.getNewAttribute(identifier.getSimpleName());

						if (newAttribute.name().isEmpty()) {
							return null;
						}
						if (assignment.getAssignment() instanceof Literal) {
							return newAttribute.buildAttributeExpression(assignment, identifier,
									(Literal) assignment.getAssignment());
						}
						if (assignment.getAssignment() instanceof NewArray) {
							return newAttribute.buildAttributeExpression(assignment, identifier,
									(NewArray) assignment.getAssignment());
						}
					}
				}
			}

			return e;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}
}
