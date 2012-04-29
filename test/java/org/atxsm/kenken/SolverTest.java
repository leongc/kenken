package org.atxsm.kenken;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Cheng Leong
 * Date: 4/29/12
 * Time: 12:22 AM
 */
public class SolverTest {
    private static Puzzle TRIVIAL_PUZZLE;

    public static Puzzle getTrivialPuzzle() {
        if (TRIVIAL_PUZZLE == null) {
            TRIVIAL_PUZZLE = new Puzzle(1, new Puzzle.Cage(1, Puzzle.Cage.Operator.SUM, 0,0));
        }
        return TRIVIAL_PUZZLE;
    }

    @Test
    public void testSolution() throws Exception {
        final Solver solver = new Solver(getTrivialPuzzle());
        Assert.assertEquals(new Solution((Integer) null), solver.getSolution());
    }

    @Test
    public void testIdentity() throws Exception {
        final Solver solver = new Solver(getTrivialPuzzle());
        Assert.assertTrue(solver.solve());
        Assert.assertEquals(new Solution(1), solver.getSolution());
    }
}
