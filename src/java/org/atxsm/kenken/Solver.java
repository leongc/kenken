package org.atxsm.kenken;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.atxsm.kenken.Operator.*;

/**
 * Solution space
 * <ul>
 *     <li>Identity. A cell in a cage with only one cell has
 *         the value of the aggregate value.</li>
 *     <li>Uniqueness. None of the other cells in the same row 
 *         or column as a cell can have the same value as that 
 *         cell.</li>
 *     <li>Factors. If a cage aggregate operator is * then all 
 *         the unknown cell values in the cage must be factors 
 *         of the aggregate value divided by the known cell 
 *         values.</li>
 *     <li>Limited products. If the operator is / and both 
 *         values are unknown, then all the unknown cell values 
 *         must either be evenly divisible by the aggregate 
 *         value using 1..N or be multiplied by the aggregate 
 *         value to one of 1..N </li>
 *     <li>Inverse pairs. If a cage has two cells and one value 
 *         is known, the value of the other cell can be derived 
 *         algebraically using the aggregate value, the 
 *         aggregate operator's inverse, and the known cell 
 *         value.</li>
 * </ul>
 *
 * User: Cheng Leong
 * Date: 4/28/12
 * Time: 11:46 PM
 */
public class Solver {
    private final Solution solution;
    private final Set<Puzzle.Cage> cages;
    
    public Solver(Puzzle puzzle) {
        this.solution = new Solution(puzzle.getSize());
        this.cages = new HashSet<Puzzle.Cage>(puzzle.getCages());
    }

    public Solution getSolution() {
        return solution;
    }

    /**
     * Computes solution for puzzle
     * @return true if all values found; false otherwise
     * @see #getSolution()
     */
    public boolean solve() {
        boolean modified;
        do {
            modified = evaluateIdentity() | shrinkCages() | limitPairs();
        } while (modified);
        return solution.isComplete();
    }

    /**
     * Set the value for all single-celled Cages
     */
    boolean evaluateIdentity() {
        boolean modified = false;
        for (Iterator<Puzzle.Cage> iterator = cages.iterator(); iterator.hasNext(); ) {
            Puzzle.Cage cage = iterator.next();
            if (cage.rowsCols.length == 2) {
                solution.set(cage.rowsCols[0], cage.rowsCols[1], cage.target);
                iterator.remove(); // solved cage
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes cells with values from SUM or PRODUCT cages
     * @return true if cages were shrunk; false otherwise
     */
    boolean shrinkCages() {
        boolean modified = false;
        Set<Puzzle.Cage> shrunkCages = new HashSet<Puzzle.Cage>();
        for (Iterator<Puzzle.Cage> iterator = cages.iterator(); iterator.hasNext(); ) {
            Puzzle.Cage cage = iterator.next();
            if (cage.operator == SUM || cage.operator == PRODUCT) {
                for (int i = 0; i < cage.rowsCols.length; i += 2) {
                    final Integer value = solution.get(cage.rowsCols[i], cage.rowsCols[i + 1]);
                    if (value != null) {
                        shrunkCages.add(createSmallerCage(cage, i, value));
                        iterator.remove(); // old cage
                        modified = true;
                    }
                }
            }
        }
        if (modified) {
            cages.addAll(shrunkCages);
        }
        return modified;
    }

    Puzzle.Cage createSmallerCage(Puzzle.Cage cage, int rowColOffset, int value) {
        final int reducedTarget;
        switch (cage.operator) {
            case SUM: reducedTarget = cage.target - value; break;
            case PRODUCT: reducedTarget = cage.target / value; break;
            default: throw new IllegalArgumentException(
                    "Can only shrink SUM or PRODUCT cages, not " + cage);
        }
        int[] reducedRowsCols = new int[cage.rowsCols.length - 2];
        if (rowColOffset > 0) {
            System.arraycopy(cage.rowsCols, 0,
                    reducedRowsCols, 0, rowColOffset);
        }
        if (rowColOffset < reducedRowsCols.length) {
            System.arraycopy(cage.rowsCols, rowColOffset + 2,
                    reducedRowsCols, rowColOffset, reducedRowsCols.length - rowColOffset);
        }
        return new Puzzle.Cage(reducedTarget, cage.operator, reducedRowsCols);
    }

    /**
     * Mark impossible pairs of ratios or differences
     * @return true if impossibilities were added; false otherwise
     */
    boolean limitPairs() {
        boolean modified = false;
        for (Puzzle.Cage cage : cages) {
            if (cage.rowsCols.length == 4) {
                final int row1 = cage.rowsCols[0];
                final int col1 = cage.rowsCols[1];
                final Set<Integer> possibilities1 = solution.findPossibilities(row1, col1);
                final int row2 = cage.rowsCols[2];
                final int col2 = cage.rowsCols[3];
                final Set<Integer> possibilities2 = solution.findPossibilities(row2, col2);
                for (Iterator<Integer> iterator = possibilities1.iterator(); iterator.hasNext(); ) {
                    int i = iterator.next();
                    if (impossiblePairs(i, possibilities2, cage.target, cage.operator)) {
                        modified |= solution.markImpossible(row1, col1, i);
                        iterator.remove(); // remove i from possibilities1
                    }
                }
                for (int j : possibilities2) {
                    if (impossiblePairs(j, possibilities1, cage.target, cage.operator)) {
                        modified |= solution.markImpossible(row2, col2, j);
                    }
                }
            }
        }
        return modified;
    }

    private boolean impossiblePairs(int i, Set<Integer> js, int target, Operator operator) {
        for (int j : js) {
            if (((operator == DIFFERENCE && (i - j == target || j - i == target)))
                    || (operator == RATIO && (i * target == j || j * target == i))
                    || (operator == SUM && (i + j == target))
                    || (operator == PRODUCT && (i * j == target))) {
                return false;
            }
        }
        return true;
    }

}
