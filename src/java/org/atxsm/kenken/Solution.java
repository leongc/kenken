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
    final Integer values[][];
    
    public Solution(Puzzle puzzle) {
        values = new Integer[puzzle.size][puzzle.size]; 
    }
    
    Solution(Integer... vals) {
        final int size = getSize(vals);
        values = new Integer[size][size]; 
        
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

    private int getSize(Integer[] vals) {
        if (vals.length < 1) {
            throw new IllegalArgumentException("values are required");
        }
        int size = 1;
        long square;
        while ((square = size * size) < vals.length) {
            size++;
        }
        if (vals.length < square) {
            throw new IllegalArgumentException("a square number of values is required");
        }
        return size;
    }

    public void set(int row, int col, Integer value) {
        values[row][col] = value;
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
        final int size = values.length;
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
