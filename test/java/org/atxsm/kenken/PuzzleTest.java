package org.atxsm.kenken;

import org.junit.Test;

import static org.atxsm.kenken.Operator.*;
import static org.junit.Assert.*;

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
                    .addCage(2, PRODUCT, 0,1, 1,1, 0,0)
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
    public void testBadRatio() {
        try {
            new Puzzle.Builder(2)
                    .addCage(1, RATIO, 0, 1, 1, 1, 0, 0)
                    .build();
            fail("Expected IllegalArgumentException, none thrown");
        } catch (IllegalArgumentException expected) {
            assertEquals("RATIO must have exactly two cells", expected.getMessage());
        }
    }
    
}
