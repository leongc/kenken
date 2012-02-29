package org.atxsm.kenken;

import org.junit.Assert;
import org.junit.Test;

/**
 * Basic problem example from http://en.wikipedia.org/wiki/KenKen
 *
 * User: Cheng Leong
 * Date: 2/29/12
 * Time: 6:01 AM
 */
public class PuzzleTest {
    @Test
    public void testTrivialPuzzleSetup() {
        new Puzzle(1,
                new Puzzle.Cage(1, Puzzle.Cage.Operator.SUM, 0,0));
    }

    @Test
    public void testMissingCage() {
        try {
            new Puzzle(1);
            Assert.fail("Expected IllegalArgumentException, none thrown");
        } catch (IllegalArgumentException expected) {
            Assert.assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("Some cells remain uncaged"));
            Assert.assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("row=0, column=0"));
        }
    }
    
    @Test
    public void testTinyPuzzleSetup() {
        new Puzzle(2,
                new Puzzle.Cage(3, Puzzle.Cage.Operator.SUM, 0,0, 1,0),
                new Puzzle.Cage(2, Puzzle.Cage.Operator.RATIO, 0,1, 1,1));
    }

    @Test
    public void testRecage() {
        try {
            new Puzzle(2,
                    new Puzzle.Cage(3, Puzzle.Cage.Operator.SUM, 0,0, 1,0),
                    new Puzzle.Cage(2, Puzzle.Cage.Operator.RATIO, 0,1, 1,1, 0,0));
            Assert.fail("Expected IllegalStateException, none thrown");
        } catch (IllegalStateException expected) {
            Assert.assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("Cell already belongs to a cage"));
            Assert.assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("row=0, column=0"));
        }
    }
    
    @Test
    public void testPuzzleSetup() {
        new Puzzle(6,
                new Puzzle.Cage(11, Puzzle.Cage.Operator.SUM, 0,0, 1,0),
                new Puzzle.Cage(2, Puzzle.Cage.Operator.RATIO, 0,1, 0,2),
                new Puzzle.Cage(20, Puzzle.Cage.Operator.PRODUCT, 0,3, 1,3),
                new Puzzle.Cage(6, Puzzle.Cage.Operator.PRODUCT, 0,4, 0,5, 1,5, 2,5),
                new Puzzle.Cage(3, Puzzle.Cage.Operator.DIFFERENCE, 1,1, 1,2),
                new Puzzle.Cage(3, Puzzle.Cage.Operator.RATIO, 1,4, 2,4),
                new Puzzle.Cage(240, Puzzle.Cage.Operator.PRODUCT, 2,0, 2,1, 3,0, 3,1),
                new Puzzle.Cage(6, Puzzle.Cage.Operator.PRODUCT, 2,2, 2,3),
                new Puzzle.Cage(6, Puzzle.Cage.Operator.PRODUCT, 3,2, 4,2),
                new Puzzle.Cage(7, Puzzle.Cage.Operator.SUM, 3,3, 4,3, 4,4),
                new Puzzle.Cage(30, Puzzle.Cage.Operator.PRODUCT, 3,4, 3,5),
                new Puzzle.Cage(6, Puzzle.Cage.Operator.PRODUCT, 4,0, 4,1),
                new Puzzle.Cage(9, Puzzle.Cage.Operator.SUM, 4,5, 5,5),
                new Puzzle.Cage(8, Puzzle.Cage.Operator.SUM, 5,0, 5,1, 5,2),
                new Puzzle.Cage(2, Puzzle.Cage.Operator.RATIO, 5,3, 5,4));
    }
}
