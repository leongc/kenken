package org.atxsm.kenken;

import java.util.Arrays;

/**
 * A KenKen solution consists of an NxN grid of cells.
 * Each cell has an integer value between 1..N.
 * User: Cheng Leong
 * Date: 4/29/12
 * Time: 12:05 AM
 */
public class Solution {
    final private int size;
    final private Integer values[][];
    final private boolean impossibles[][][];

    private Solution(int size) {
        this.size = size;
        values = new Integer[size][size];
        impossibles = new boolean[size][size][size];
    }

    public Solution(Puzzle puzzle) {
        this(puzzle.size); 
    }

    // for test assertions
    Solution(Integer... vals) {
        this(calculateSqrtLength(vals)); 
        
        int row = 0;
        int col = 0;
        for (Integer val : vals) {
            values[row][col++] = val;
            if (col >= size) {
                row++;
                col = 0;
            }
        }
    }

    private static int calculateSqrtLength(Integer[] vals) {
        if (vals.length < 1) {
            throw new IllegalArgumentException("values are required");
        }
        int sqrt = 1;
        long square;
        while ((square = sqrt * sqrt) < vals.length) {
            sqrt++;
        }
        if (vals.length < square) {
            throw new IllegalArgumentException("a square number of values is required");
        }
        return sqrt;
    }

    /**
     * @return true if value modified; false if unchanged
     */
    public boolean set(int row, int col, Integer value) {
        if ((values[row][col] == null && value == null)
                || value.equals(values[row][col])) {
            return false;
        }
        values[row][col] = value;
        for (int i = 0; i < size; i++) {
            if (i != row) {
                markImpossible(i, col, value);
            }
        }
        for (int j = 0; j < size; j++) {
            if (j != row) {
                markImpossible(row, j, value);
            }
        }
        return true;
    }
    
    /**
     * @return true if some value was modified; false if unchanged
     */
    private boolean markImpossible(int row, int col, int value) {
        impossibles[row][col][value - 1] = true;
        Integer possibleValue = findPossible(row, col);
        return possibleValue != null && set(row, col, possibleValue);
    }

    /**
     * @return only possible value or null if multiple values are possible
     */
    private Integer findPossible(int row, int col) {
        Integer possible = null;
        for (int k = 0; k < size; k++) {
            if (!impossibles[row][col][k]) {
                if (possible != null) {
                    return -1; // multiple possibilities
                }
                possible = k + 1;
            }
        }
        return possible;
    }

    public Integer get(int row, int col) {
        return values[row][col];
    }

    public boolean isComplete() {
        for (Integer[] row : values) {
            for (Integer value : row) {
                if (value == null) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        return this == o
                || !(o == null || getClass() != o.getClass())
                && Arrays.deepEquals(this.values, ((Solution) o).values);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Solution{\n");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                sb.append(get(row, col)).append('\t');
            }
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
}
