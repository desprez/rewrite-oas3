package org.openrewrite.java.oas3;

import org.openrewrite.Recipe;

/**
 * Replace method declaration @ApiIgnore annotations with the @Operation(hidden = true) annotations.
 * <ul>
 * <li>@ApiIgnore changes to @Operation(hidden = true)
 * </ul>
 */
public class Swagger2ApiIgnoreToOAS3OperationHiddenAnnotation extends Recipe {

    @Override
    public String getDisplayName() {
        return "Replace `@ApiIgnore` with `@Operation(hidden = true)`";
    }

    @Override
    public String getDescription() {
        return "Replace method declaration `@ApiIgnore` annotations with the `@Operation(hidden = true)` annotations.";
    }

}
