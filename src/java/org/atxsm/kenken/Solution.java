package org.atxsm.kenken;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public Solution(int size) {
        this.size = size;
        values = new Integer[size][size];
        impossibles = new boolean[size][size][size];
    }

    // for tests
    Solution setAll(Integer... vals) {
        int row = 0;
        int col = 0;
        for (Integer val : vals) {
            values[row][col++] = val;
            if (col >= size) {
                row++;
                col = 0;
            }
        }
        return this;
    }

    /**
     * @return true if value modified; false if unchanged
     */
    public boolean set(int row, int col, Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("cannot unset value");
        }
        if (value < 1 || value > size) {
            throw new IllegalArgumentException("value must be between 1 and " + size);
        }
        if (value.equals(values[row][col])) {
            return false;
        }
        if (values[row][col] != null) {
            throw new IllegalStateException("value can only be set once");
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
        for (int k = 0; k < size; k++) {
            if (k+1 != value) {
                markImpossible(row, col, k+1);
            }
        }
        return true;
    }
    
    /**
     * @return true if this is a new fact; false if unchanged
     */
    boolean markImpossible(int row, int col, int value) {
        if (impossibles[row][col][value - 1]) {
            return false;
        }
        impossibles[row][col][value - 1] = true;
        Integer possibleValue = findSinglePossibility(row, col);
        if (possibleValue != null) {
            set(row, col, possibleValue);
        }
        return true;
    }

    /**
     * @return only possible value or null if multiple values are possible
     */
    private Integer findSinglePossibility(int row, int col) {
        Integer possible = null;
        for (int k = 0; k < size; k++) {
            if (!impossibles[row][col][k]) {
                if (possible != null) {
                    return null; // multiple possibilities
                }
                possible = k + 1;
            }
        }
        return possible;
    }

    Set<Integer> findPossibilities(int row, int col) {
        Set<Integer> possible = new HashSet<Integer>();
        for (int k = 0; k < size; k++) {
            if (!impossibles[row][col][k]) {
                possible.add(k + 1);
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
                Object value = get(row, col);
                if (value == null) {
                    value = findPossibilities(row, col);
                }
                sb.append(value).append('\t');
            }
            sb.append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
}
