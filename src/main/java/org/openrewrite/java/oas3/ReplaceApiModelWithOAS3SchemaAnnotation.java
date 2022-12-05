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
 *
 */
public class ReplaceApiModelWithOAS3SchemaAnnotation extends Recipe {

    private static final LegacyAnnotationDescriptor LEGACY_ANNOTATION = new LegacyAnnotationDescriptor("io.swagger.annotations", "ApiModel");
    private static final NewAnnotationDescriptor NEW_ANNOTATION = new NewAnnotationDescriptor("io.swagger.v3.oas.annotations.media", "Schema",
    		 AttributePairMigration.of("value", "name"));

    @Override
    public String getDisplayName() {
        return "Replace `@ApiModel` annotation with `@Schema` annotation";
    }

    @Override
    public String getDescription() {
        return "Replace `@ApiModel annotation with `@Schema` annotation " //
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
