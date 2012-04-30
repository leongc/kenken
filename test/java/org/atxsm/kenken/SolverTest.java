package org.atxsm.kenken;

import org.junit.Assert;
import org.junit.Test;

import static org.atxsm.kenken.Operator.*;
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

}
