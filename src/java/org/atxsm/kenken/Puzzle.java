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
 * Solution space
 * <ul>
 *     <li>Identity. A cell in a cage with only one cell has the value of the aggregate value.</li>
 *     <li>Uniqueness. None of the other cells in the same row or column as a cell can have the same value as that cell.</li>
 *     <li>Factors. If a cage aggregate operator is * then all the unknown cell values in the cage must be factors of the aggregate value divided by the known cell values.</li>
 *     <li>Limited products. If the operator is / and both values are unknown, then all the unknown cell values must either be evenly divisible by the aggregate value using 1..N or be multiplied by the aggregate value to one of 1..N </li>
 *     <li>Inverse pairs. If a cage has two cells and one value is known, the value of the other cell can be derived algebraically using the aggregate value, the aggregate operator's inverse, and the known cell value.</li>
 * </ul>
 *
 *
 * User: Cheng Leong
 * Date: 2/29/12
 * Time: 3:28 AM
 */
public class Puzzle {
    protected final int size;
    protected final Unique[] rows;
    protected final Unique[] columns;
    protected final Cage[] cages;
    public Puzzle(final int size, Cage... cages) {
        this.size = size;
        rows = new Unique[size];
        columns = new Unique[size];
        for (int k = 0; k < size; k++) {
            rows[k] = new Unique(k);
            columns[k] = new Unique(k);
        }
        List<Cell> allCells = new ArrayList<Cell>(size * size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                allCells.add(new Cell(rows[i], columns[j]));
            }
        }
        this.cages = cages;
        for (Cage cage : cages) {
            cage.populate(rows);
        }
        
        // sanity check; are all cells in a cage?
        List<Cell> uncagedCells = new LinkedList<Cell>();
        for (Cell cell : allCells) {
            if (cell.cage == null) {
                uncagedCells.add(cell);
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
        Integer value;
        boolean[] impossible;
        Unique row;
        Unique column;
        Cage cage;
        Cell(Unique row, Unique column) {
            this.impossible = new boolean[size];
            this.row = row;
            this.row.add(this);
            this.column = column;
            this.column.add(this);
        }
        public void setCage(Cage cage) {
            if (this.cage != null) {
                throw new IllegalStateException("Cell already belongs to a cage: " + toString());
            }
            this.cage = cage;
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "row=" + row +
                    ", column=" + column +
                    (value == null ? ", impossible=" + Arrays.toString(impossible) : ", value=" + value) +
                    ", cage=" + cage +
                    '}';
        }
    }
    
    class Unique {
        List<Cell> cells = new ArrayList<Cell>(size);
        int i;
        public Unique(int i) {
            this.i = i;
        }
        public void add(Cell cell) {
            cells.add(cell);
        }
        public Cell get(int index) {
            return cells.get(index);
        }

        @Override
        public String toString() {
            return String.valueOf(i);
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
        public void populate(Unique[] rows) {
            for (int i = 0; i < rowsCols.length; i += 2) {
                int r = rowsCols[i];
                int c = rowsCols[i+1];
                rows[r].get(c).setCage(this);
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
