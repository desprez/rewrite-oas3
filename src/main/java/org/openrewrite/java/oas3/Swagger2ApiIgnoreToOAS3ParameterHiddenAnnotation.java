package org.openrewrite.java.oas3;

import org.openrewrite.Recipe;

/**
 * Replace parameter declaration @ApiIgnore annotations with the @Parameter(hidden = true) annotations.
 * <ul>
 * <li>@ApiIgnore changes to @Parameter(hidden = true)
 * </ul>
 */
public class Swagger2ApiIgnoreToOAS3ParameterHiddenAnnotation extends Recipe {

    @Override
    public String getDisplayName() {
        return "`@ApiIgnore` changes to @Parameter(hidden = true)";
    }

    @Override
    public String getDescription() {
        return "Replace parameter declaration `@ApiIgnore` annotations with the `@Parameter(hidden = true)` annotations.";
    }

}
