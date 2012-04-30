package org.atxsm.kenken;

import java.util.Set;

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
    private final Puzzle puzzle;
    private final Solution solution;
    
    public Solver(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.solution = new Solution(puzzle.getSize());
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
        evaluateIdentity();
        boolean modified;
        do {
            modified = limitRatios();
        } while (modified);
        return solution.isComplete();
    }

    /**
     * Set the value for all single-celled Cages
     */
    void evaluateIdentity() {
        for (Puzzle.Cage cage : puzzle.getCages()) {
            if (cage.rowsCols.length == 2) {
                solution.set(cage.rowsCols[0], cage.rowsCols[1], cage.target);
            }
        }
    }

    /**
     * Mark impossible ratios
     * @return true if impossibilities were added; false otherwise
     */
    boolean limitRatios() {
        boolean modified = false;
        for (Puzzle.Cage cage : puzzle.getCages()) {
            if (cage.operator == Operator.RATIO) {
                final int row1 = cage.rowsCols[0];
                final int col1 = cage.rowsCols[1];
                final Set<Integer> possibilities1 = solution.findPossibilities(row1, col1);
                final int row2 = cage.rowsCols[2];
                final int col2 = cage.rowsCols[3];
                final Set<Integer> possibilities2 = solution.findPossibilities(row2, col2);
                for (int i : possibilities1) {
                    if (impossibleRatio(i, possibilities2, cage.target)) {
                        modified |= solution.markImpossible(row1, col1, i);
                    }
                }
                for (int j : possibilities2) {
                    if (impossibleRatio(j, possibilities1, cage.target)) {
                        modified |= solution.markImpossible(row2, col2, j);
                    }
                }
            }
        }
        return modified;
    }

    private boolean impossibleRatio(int i, Set<Integer> js, int target) {
        for (int j : js) {
            if (i * target == j || j * target == i) {
                return false;
            }
        }
        return true;
    }

}
