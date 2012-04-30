package org.atxsm.kenken;

import org.junit.Assert;
import org.junit.Test;

import static org.atxsm.kenken.Operator.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        new Puzzle.Builder(1)
                .addCage(1, SUM, 0, 0)
                .build();
    }

    @Test
    public void testMissingCage() {
        try {
            new Puzzle.Builder(1).build();
            fail("Expected IllegalStateException, none thrown");
        } catch (IllegalStateException expected) {
            assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("Some cells remain uncaged: "));
            assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("(0,0)"));
        }
    }
    
    @Test
    public void testTinyPuzzleSetup() {
        new Puzzle.Builder(2)
                .addCage(3, SUM, 0,0, 1,0)
                .addCage(2, RATIO, 0, 1, 1, 1)
                .build();
    }

    @Test
    public void testRecage() {
        try {
            new Puzzle.Builder(2)
                    .addCage(3, SUM, 0,0, 1,0)
                    .addCage(2, RATIO, 0,1, 1,1, 0,0)
                    .build();
            fail("Expected IllegalStateException, none thrown");
        } catch (IllegalStateException expected) {
            assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("0,0"));
            assertTrue("Unexpected exception message: " + expected.getMessage(),
                    expected.getMessage().contains("cannot be in both"));
        }
    }
    
    @Test
    public void testPuzzleSetup() {
        new Puzzle.Builder(6)
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
                .build();
    }
}
