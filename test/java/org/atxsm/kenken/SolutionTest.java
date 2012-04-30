package org.atxsm.kenken;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: Cheng Leong
 * Date: 4/29/12
 * Time: 10:21 PM
 */
public class SolutionTest {
    private static final Solution TINY_SOLUTION =
            new Solution(2).setAll(1, 2, 2, 1);

    @Test
    public void testElimination() throws Exception {
        final Solution solution = new Solution(2); // no values yet
        solution.set(0, 0, 1);
        assertImpossible(solution, 0, 1, 1);
        assertImpossible(solution, 1, 0, 1);
        assertEquals(TINY_SOLUTION, solution);
        assertTrue(solution.isComplete());
    }

    private static void assertImpossible(Solution solution, int row, int col, int value) {
        // markImpossible returns false if cell was already marked
        assertFalse(row + "," + col + " could be " + value,
                solution.markImpossible(row, col, value));
    }


}
