package org.atxsm.kenken;

import org.junit.Assert;
import org.junit.Test;

import static org.atxsm.kenken.Puzzle.Cage;
import static org.atxsm.kenken.Puzzle.Cage.Operator.*;

/**
 * User: Cheng Leong
 * Date: 4/29/12
 * Time: 12:22 AM
 */
public class SolverTest {
    private static Puzzle TRIVIAL_PUZZLE;
    private static Puzzle TINY_PUZZLE;

    public static Puzzle getTrivialPuzzle() {
        if (TRIVIAL_PUZZLE == null) {
            TRIVIAL_PUZZLE = new Puzzle(1, new Cage(1, SUM, 0,0));
        }
        return TRIVIAL_PUZZLE;
    }

    public static Puzzle getTinyPuzzle() {
        if (TINY_PUZZLE == null) {
            TINY_PUZZLE = new Puzzle(2, 
                    new Cage(1, SUM, 0,0), 
                    new Cage(2, SUM, 0,1),
                    new Cage(2, PRODUCT, 1,0, 1,1)
            );
        }
        return TINY_PUZZLE;
    }

    @Test
    public void testSolution() throws Exception {
        final Solver solver = new Solver(getTrivialPuzzle());
        Assert.assertEquals(new Solution((Integer) null), solver.getSolution());
    }

    @Test
    public void testIdentity() throws Exception {
        final Solver solver = new Solver(getTrivialPuzzle());
        solver.solve();
        Assert.assertEquals(new Solution(1), solver.getSolution());
        Assert.assertTrue(solver.getSolution().isComplete());
    }

    @Test
    public void testTinyElimination() throws Exception {
        final Solver solver = new Solver(getTinyPuzzle());
        solver.solve();
        Assert.assertEquals(new Solution(1, 2, 2, 1), solver.getSolution());
        Assert.assertTrue(solver.getSolution().isComplete());
    }

}
