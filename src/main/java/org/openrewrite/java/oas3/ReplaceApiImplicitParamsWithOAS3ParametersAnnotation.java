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
 * Replace @ApiImplicitParams annotations with the @Parameters annotations.
 * <ul>
 * <li>@ApiImplicitParam changes to @Parameter
 * </ul>
 */
public class ReplaceApiImplicitParamsWithOAS3ParametersAnnotation extends Recipe {

    private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor("io.swagger.annotations", "ApiImplicitParams");
    private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor("io.swagger.v3.oas.annotations", "Parameters",
            AttributePairMigration.of("value", "value"));

    @Override
    public String getDisplayName() {
        return "Replace `@ApiImplicitParams` Swagger2 annotation with `@Parameters` OAS3 annotation";
    }

    @Override
    public String getDescription() {
        return "Replace `@ApiImplicitParams` annotation with `@Parameters` annotation " //
                + " and change the according `@Parameters` attributes names if needed.";
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
