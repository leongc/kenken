package org.atxsm.kenken;

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
        this.solution = new Solution(puzzle);
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
        return solution.isComplete();
    }

    /**
     * Set the value for all single-celled Cages
     */
    private void evaluateIdentity() {
        for (Puzzle.Cage cage : puzzle.cages) {
            if (cage.rowsCols.length == 2) {
                solution.set(cage.rowsCols[0], cage.rowsCols[1], cage.aggregateValue);
            }
        }
    }

}
