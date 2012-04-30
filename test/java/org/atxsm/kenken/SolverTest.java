package org.atxsm.kenken;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.atxsm.kenken.Operator.RATIO;
import static org.atxsm.kenken.Operator.SUM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: Cheng Leong
 * Date: 4/29/12
 * Time: 12:22 AM
 */
public class SolverTest {
    private static final Puzzle.Builder TRIVIAL_PUZZLE_BUILDER =
            new Puzzle.Builder(1)
                    .addCage(1, SUM, 0,0);
    private static final Solution TRIVIAL_SOLUTION =
            new Solution(1).setAll(1);

    @Test
    public void testIdentity() throws Exception {
        final Solver solver = new Solver(TRIVIAL_PUZZLE_BUILDER.build());
        final Solution empty1 = new Solution(1); // no values yet
        assertEquals(empty1, solver.getSolution());
        
        solver.solve();
        assertEquals(TRIVIAL_SOLUTION, solver.getSolution());
        assertTrue(solver.getSolution().isComplete());
    }

    @Test
    public void testLimitRatios() throws Exception {
        final Solver solver = new Solver(
                new Puzzle.Builder(3)
                .addCage(2, RATIO, 0,0, 0,1)
                .addCage(3, RATIO, 1,0, 1,1)
                .addCage(11, SUM, 0,2, 1,2, 2,0, 2,1, 2,2)
                .build());
        assertTrue("limitRatios", solver.limitRatios());
        final Solution solution = solver.getSolution();
        final Set<Integer> oneTwo = new HashSet<Integer>(Arrays.asList(1,2));
        assertEquals(oneTwo, solution.findPossibilities(0, 0));
        assertEquals(oneTwo, solution.findPossibilities(0, 1));
        final Set<Integer> oneThree = new HashSet<Integer>(Arrays.asList(1,3));
        assertEquals(oneThree, solution.findPossibilities(1, 0));
        assertEquals(oneThree, solution.findPossibilities(1, 1));
    }
}
