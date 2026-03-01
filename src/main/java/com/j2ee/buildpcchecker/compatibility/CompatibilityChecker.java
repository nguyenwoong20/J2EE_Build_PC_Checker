package com.j2ee.buildpcchecker.compatibility;

import com.j2ee.buildpcchecker.dto.response.CompatibilityResult;

/**
 * Base interface for all compatibility checkers
 */
public interface CompatibilityChecker {

    /**
     * Perform compatibility check and add errors/warnings to result
     *
     * @param result The result object to populate with errors and warnings
     */
    void check(CompatibilityResult result);
}

