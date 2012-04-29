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
    protected final int size;
    protected final Cage[] cages;
    public Puzzle(final int size, Cage... cages) {
        this.size = size;
        Cell[][] allCells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                allCells[i][j] = new Cell(i, j);
            }
        }
        this.cages = cages;
        for (Cage cage : cages) {
            cage.populate(allCells);
        }
        
        // sanity check; are all cells in a cage?
        List<Cell> uncagedCells = new LinkedList<Cell>();
        for (Cell[] cellArray : allCells) {
            for (Cell cell : cellArray) {
                if (cell.cage == null) {
                    uncagedCells.add(cell);
                }
            }
        }
        if (!uncagedCells.isEmpty()) {
            final StringBuilder sb = new StringBuilder("Some cells remain uncaged:\n");
            for (Cell cell : uncagedCells) {
                sb.append(cell.toString()).append('\n');
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }


    class Cell {
        int row;
        int column;
        Cage cage;
        Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }
        public void setCage(Cage cage) {
            if (this.cage != null) {
                throw new IllegalStateException("Cell already belongs to a cage: " + toString());
            }
            this.cage = cage;
        }

        @Override
        public String toString() {
            return "Cell{" + row + ", " + column +
                    ", " + cage +
                    '}';
        }
    }

    static class Cage {
        static enum Operator {
           SUM, PRODUCT, DIFFERENCE, RATIO 
        }
        final int aggregateValue;
        final Operator operator;
        final int[] rowsCols;
        public Cage(int val, Operator op, int... rowsCols) {
            if (rowsCols.length % 2 != 0 || rowsCols.length < 1) {
                throw new IllegalArgumentException("rows and columns of cell locations must be in pairs");
            }
            this.aggregateValue = val;
            this.operator = op;
            this.rowsCols = rowsCols;
        }
        public void populate(Cell[][] cells) {
            for (int i = 0; i < rowsCols.length; i += 2) {
                int r = rowsCols[i];
                int c = rowsCols[i+1];
                cells[r][c].setCage(this);
            }
        }

        @Override
        public String toString() {
            return "Cage{" + 
                    Integer.toString(hashCode(), 36) +
                    ", aggregateValue=" + aggregateValue +
                    ", operator=" + operator +
                    '}';
        }
    }
}
