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
    public void testEquality() throws Exception {
        assertEquals("Empty solutions", new Solution(1), new Solution(1));
        assertEquals("Populated solutions", TINY_SOLUTION, new Solution(2).setAll(1, 2, 2, 1));
        assertFalse("Different size empty solutions", new Solution(1).equals(new Solution(2)));
    }
    
    @Test
    public void testComplete() throws Exception {
        assertFalse(new Solution(1).isComplete());
        assertTrue(TINY_SOLUTION.isComplete());
    }

    @Test
    public void testGet() throws Exception {
        assertEquals(Integer.valueOf(1), TINY_SOLUTION.get(0, 0));
        assertEquals(Integer.valueOf(2), TINY_SOLUTION.get(0, 1));
        assertEquals(Integer.valueOf(2), TINY_SOLUTION.get(1, 0));
        assertEquals(Integer.valueOf(1), TINY_SOLUTION.get(1, 1));
    }

    @Test
    public void testElimination() throws Exception {
        final Solution solution = new Solution(2); // no values yet
        assertTrue(solution.markImpossible(0, 0, 2));
        assertEquals(TINY_SOLUTION, solution);
    }

    @Test
    public void testMarkImpossible() throws Exception {
        final Solution solution = new Solution(3); // no values yet
        assertTrue(solution.set(0, 0, 1));
        assertImpossible(solution, 0, 0, 2);
        assertImpossible(solution, 0, 0, 3);
        assertImpossible(solution, 0, 1, 1);
        assertImpossible(solution, 0, 2, 1);
        assertImpossible(solution, 1, 0, 1);
        assertImpossible(solution, 2, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetBadValue() throws Exception {
        new Solution(1).set(0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetBadNull() throws Exception {
        new Solution(1).set(0, 0, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetBadAddress() throws Exception {
        new Solution(1).set(1, 0, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetBadTwice() throws Exception {
        final Solution solution = new Solution(2);
        assertTrue(solution.set(0, 0, 2));
        solution.set(0, 0, 1);
    }

    private static void assertImpossible(Solution solution, int row, int col, int value) {
        // markImpossible returns false if cell was already marked
        assertFalse(row + "," + col + " could be " + value,
                solution.markImpossible(row, col, value));
    }

}
