package org.atxsm.kenken;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.atxsm.kenken.Operator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        final Solver solver = new Solver(new Puzzle.Builder(3)
                .addCage(2, RATIO, 0,0, 0,1)
                .addCage(3, RATIO, 1,0, 1,1)
                .addCage(11, SUM, 0,2, 1,2, 2,0, 2,1, 2,2)
                .build());
        assertTrue("limitRatios", solver.limitPairs());
        final Solution solution = solver.getSolution();
        final Set<Integer> oneTwo = new HashSet<Integer>(Arrays.asList(1,2));
        assertEquals(oneTwo, solution.findPossibilities(0, 0));
        assertEquals(oneTwo, solution.findPossibilities(0, 1));
        final Set<Integer> oneThree = new HashSet<Integer>(Arrays.asList(1,3));
        assertEquals(oneThree, solution.findPossibilities(1, 0));
        assertEquals(oneThree, solution.findPossibilities(1, 1));
    }
    
    @Test
    public void testLimitDifferences() throws Exception {
        final Solver solver = new Solver(new Puzzle.Builder(3)
                .addCage(1, DIFFERENCE, 0,0, 0,1)
                .addCage(1, SUM, 1, 0)
                .addCage(2, PRODUCT, 1, 1)
                .addCage(-2, DIFFERENCE, 0,2, 1,2)
                .addCage(2, DIFFERENCE, 2,0, 2,1)
                .addCage(2, SUM, 2,2)
                .build());
        assertTrue("limitDifferences", solver.limitPairs());
        final Solution solution = solver.getSolution();
        final Set<Integer> oneTwoThree = new HashSet<Integer>(Arrays.asList(1,2,3));
        assertEquals(oneTwoThree, solution.findPossibilities(0, 0));
        assertEquals(oneTwoThree, solution.findPossibilities(0, 1));
        final Set<Integer> oneThree = new HashSet<Integer>(Arrays.asList(1,3));
        assertEquals(oneThree, solution.findPossibilities(0, 2));
        assertEquals(oneThree, solution.findPossibilities(1, 2));
        assertEquals(oneThree, solution.findPossibilities(2, 0));
        assertEquals(oneThree, solution.findPossibilities(2, 1));

        solver.solve();
        assertEquals(new Solution(3).setAll(
                2,3,1,
                1,2,3,
                3,1,2),
                solver.getSolution());
    }

    @Test
    public void testShrinkSum() throws Exception {
        final Puzzle puzzle = new Puzzle.Builder(2)
                .addCage(6, SUM, 0,0, 0,1, 1,0, 1,1)
                .build();
        final Puzzle.Cage cage = puzzle.getCages().iterator().next();
        final Solver solver = new Solver(puzzle);
        final Puzzle.Cage expectedCage1 = new Puzzle.Cage(5, SUM, 0,1, 1,0, 1,1);
        final Puzzle.Cage smallerCage1 = solver.createSmallerCage(cage, 0, 1);
        assertEquals(expectedCage1, smallerCage1);
        final Puzzle.Cage expectedCage2 = new Puzzle.Cage(4, SUM, 0,0, 1,0, 1,1);
        final Puzzle.Cage smallerCage2 = solver.createSmallerCage(cage, 2, 2);
        assertEquals(expectedCage2, smallerCage2);
        final Puzzle.Cage expectedCage3 = new Puzzle.Cage(4, SUM, 0,0, 0,1, 1,1);
        final Puzzle.Cage smallerCage3 = solver.createSmallerCage(cage, 4, 2);
        assertEquals(expectedCage3, smallerCage3);
        final Puzzle.Cage expectedCage4 = new Puzzle.Cage(5, SUM, 0,0, 0,1, 1,0);
        final Puzzle.Cage smallerCage4 = solver.createSmallerCage(cage, 6, 1);
        assertEquals(expectedCage4, smallerCage4);
        
        assertFalse(solver.shrinkCages());
    }

    @Test
    public void testShrinkProduct() throws Exception {
        final Puzzle puzzle = new Puzzle.Builder(2)
                .addCage(4, PRODUCT, 0,0, 0,1, 1,0, 1,1)
                .build();
        final Puzzle.Cage cage = puzzle.getCages().iterator().next();
        final Solver solver = new Solver(puzzle);
        final Puzzle.Cage expectedCage1 = new Puzzle.Cage(4, PRODUCT, 0,1, 1,0, 1,1);
        final Puzzle.Cage smallerCage1 = solver.createSmallerCage(cage, 0, 1);
        assertEquals(expectedCage1, smallerCage1);
        final Puzzle.Cage expectedCage2 = new Puzzle.Cage(2, PRODUCT, 0,0, 1,0, 1,1);
        final Puzzle.Cage smallerCage2 = solver.createSmallerCage(cage, 2, 2);
        assertEquals(expectedCage2, smallerCage2);
        final Puzzle.Cage expectedCage3 = new Puzzle.Cage(2, PRODUCT, 0,0, 0,1, 1,1);
        final Puzzle.Cage smallerCage3 = solver.createSmallerCage(cage, 4, 2);
        assertEquals(expectedCage3, smallerCage3);
        final Puzzle.Cage expectedCage4 = new Puzzle.Cage(4, PRODUCT, 0,0, 0,1, 1,0);
        final Puzzle.Cage smallerCage4 = solver.createSmallerCage(cage, 6, 1);
        assertEquals(expectedCage4, smallerCage4);

        assertFalse(solver.shrinkCages());
    }

    @Test
    @Ignore("not smart enough yet")
    public void testSolveExample6() {
        final Solver solver = new Solver(new Puzzle.Builder(6)
                .addCage(11, SUM, 0,0, 1,0)
                .addCage(2, RATIO, 0,1, 0,2)
                .addCage(20, PRODUCT, 0,3, 1,3)
                .addCage(6, PRODUCT, 0,4, 0,5, 1,5, 2,5)
                .addCage(3, DIFFERENCE, 1,1, 1,2)
                .addCage(3, RATIO, 1,4, 2,4)
                .addCage(240, PRODUCT, 2,0, 2,1, 3,0, 3,1)
                .addCage(6, PRODUCT, 2,2, 2,3)
                .addCage(6, PRODUCT, 3,2, 4,2)
                .addCage(7, SUM, 3,3, 4,3, 4,4)
                .addCage(30, PRODUCT, 3,4, 3,5)
                .addCage(6, PRODUCT, 4,0, 4,1)
                .addCage(9, SUM, 4,5, 5,5)
                .addCage(8, SUM, 5,0, 5,1, 5,2)
                .addCage(2, RATIO, 5,3, 5,4)
                .build());
        solver.solve();

        assertEquals(new Solution(6).setAll(
                5, 6, 3, 4, 1, 2,
                6, 1, 4, 5, 2, 3,
                4, 5, 2, 3, 6, 1,
                3, 4, 1, 2, 5, 6,
                2, 3, 6, 1, 4, 5,
                1, 2, 5, 6, 3, 4),
                solver.getSolution());
    }
}
