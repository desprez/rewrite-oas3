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
 * Replace @ApiImplicitParam annotations with the @Parameter annotations.
 * <ul>
 * <li>@ApiImplicitParam changes to @Parameter
 * </ul>
 */
public class ReplaceApiImplicitParamWithOAS3ParameterAnnotation extends Recipe {

    private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor("io.swagger.annotations", "ApiImplicitParam");
    private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor("io.swagger.v3.oas.annotations", "Parameter",
            AttributePairMigration.of("value", "value"));

    @Override
    public String getDisplayName() {
        return "Replace `@ApiImplicitParam` Swagger2 annotation with `@Parameter` OAS3 annotation";
    }

    @Override
    public String getDescription() {
        return "Replace `@ApiImplicitParam` annotation with `@Parameter` annotation " //
                + " and change the according `@Parameter` attributes names if needed.";
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
