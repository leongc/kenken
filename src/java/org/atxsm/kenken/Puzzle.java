package org.atxsm.kenken;

import java.util.*;

/**
 * Problem space
 * <ul>
 *     <li>A KenKen puzzle consists of an NxN grid of cells.</li>
 *     <li>Each cell has an integer value between 1..N.</li>
 *     <li>Each row and each column contains exactly one cell value for each integer 1..N.</li>
 *     <li>Each cell belongs to one cage.</li>
 *     <li>A cage is a set of 1 or more cells.</li>
 *     <li>A cage has an aggregate value and an aggregate operator.</li>
 *     <li>the operator + has an aggregate value equal to the sum of all cell values</li>
 *     <li>the operator * has an aggregate value equal to the product of all cell values</li>
 *     <li>the operator - has an aggregate value equal to the difference between the cell values</li>
 *     <li>the operator / has an aggregate value equal to the ratio between the cell values</li>
 * </ul>
 *
 * Generator constraints 
 * <ul>
 *     <li>Adjacent cells may belong to the same cage.</li>
 *     <li>Non-adjacent (eg: diagonal) cells may not belong to the same cage.</li>
 *     <li>The operators - and / are only valid for cages with two cells.</li>
 * </ul>
 * 
 *
 * User: Cheng Leong
 * Date: 2/29/12
 * Time: 3:28 AM
 */
public class Puzzle {
    private final int size;
    private final Set<Cage> cages;

    // must use Builder
    private Puzzle(final int size, Set<Cage> cages) {
        this.size = size;
        this.cages = Collections.unmodifiableSet(new HashSet<Cage>(cages));
    }

    public int getSize() {
        return size;
    }

    public Set<Cage> getCages() {
        return cages;
    }

    public static class Builder {
        private int size;
        private Set<Cage> cages = new HashSet<Cage>();

        public Builder(int size) {
            this.size = size;
        }

        public Builder addCage(int val, Operator op, int... rowsCols) {
            cages.add(new Cage(val, op, rowsCols));
            return this;
        }

        public Puzzle build() {
            checkAllCellsInCages();
            return new Puzzle(size, cages);
        }

        /**
         * check to ensure all cells are in a cage
         * @throws IllegalStateException when cells remain uncaged
         */
        private void checkAllCellsInCages() {
            Map<String, Cage> cellCageMap = new HashMap<String, Cage>();
            // put all cells in a Map keyed by "row,col"
            for (Cage cage : cages) {
                for (int i = 0; i < cage.rowsCols.length; i += 2) {
                    final int row = cage.rowsCols[i];
                    final int col = cage.rowsCols[i+1];
                    final String key = makeKey(row, col);
                    if (cellCageMap.containsKey(key)) {
                        throw new IllegalStateException("Cell at ("
                                + key + ") cannot be in both "
                                + cellCageMap.get(key) + " and " + cage);
                    }
                    cellCageMap.put(key, cage);
                }
            }
            // verify all cells in grid are in a cage
            final StringBuilder sb = new StringBuilder();
            for (int row = 0 ; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    final String key = makeKey(row, col);
                    if (!cellCageMap.containsKey(key)) {
                        if (sb.length() > 0) { sb.append(','); }
                        sb.append('(').append(key).append(')');
                    }
                }
            }
            if (sb.length() > 0) {
                sb.insert(0, "Some cells remain uncaged: ");
                throw new IllegalStateException(sb.toString());
            }
        }

        private String makeKey(int r, int c) {
            return String.format("%d,%d", r, c);
        }

    }

    static class Cage {
        final int target;
        final Operator operator;
        final int[] rowsCols;

        Cage(int val, Operator op, int... rowsCols) {
            if (rowsCols.length % 2 != 0 || rowsCols.length < 1) {
                throw new IllegalArgumentException("rows and columns of cell locations must be in pairs");
            }
            if ((op == Operator.RATIO || op == Operator.DIFFERENCE)
                && rowsCols.length != 4) {
                throw new IllegalArgumentException(op + " must have exactly two cells");
            }
            this.target = val;
            this.operator = op;
            this.rowsCols = rowsCols;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cage cage = (Cage) o;

            if (target != cage.target) return false;
            if (operator != cage.operator) return false;
            if (!Arrays.equals(rowsCols, cage.rowsCols)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = target;
            result = 31 * result + (operator != null ? operator.hashCode() : 0);
            result = 31 * result + (rowsCols != null ? Arrays.hashCode(rowsCols) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Cage{" +
                    Integer.toString(hashCode(), 36) +
                    ", " + target +
                    " " + operator +
                    '}';
        }
    }
}
