package org.openrewrite.java.oas3;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.search.UsesType;
import org.openrewrite.java.utils.annotation.LegacyAnnotationDescriptor;
import org.openrewrite.java.utils.annotation.NewAnnotationDescriptor;
import org.openrewrite.java.utils.annotation.attribute.AttributePairMigration;

/**
 * Replace @ApiModelProperty annotations with the @Schema annotations.
 * <ul>
 * <li>@ApiModelProperty(example = "doggie", required = true, value = "name") changes to @Schema(example = "doggie", required = true, description = "name")
 * <li>@ApiModelProperty("name") changes to @Schema(description = "name")
 * </ul>
 */
public class ReplaceApiModelPropertyWithOAS3SchemaAnnotation extends Recipe {

    private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor("io.swagger.annotations", "ApiModelProperty");
    private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor("io.swagger.v3.oas.annotations.media", "Schema",
            AttributePairMigration.of("value", "description"));

    @Override
    public String getDisplayName() {
        return "Change `@ApiModelProperty` annotation to `@Schema` annotation";
    }

    @Override
    public String getDescription() {
        return "Changes @ApiModelProperty annotation to `@Schema` annotation " //
                + " and change the according `@Schema` attributes names if needed.";
    }

    @Override
    protected @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
        return new UsesType<>(LEGACY_ANNOTATION.fullyQualifiedTypeName());
    }

    @Override
    protected JavaIsoVisitor<ExecutionContext> getVisitor() {
        return new DefaultAnnotationMigrationVisitor(LEGACY_ANNOTATION, NEW_ANNOTATION);
    }
}
