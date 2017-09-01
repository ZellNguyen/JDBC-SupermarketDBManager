package DBManager;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

/**
 * Created by Zell on 11/28/15.
 */
public class MyDefaultTableModel extends DefaultTableModel{
    public boolean[][] editable_cells; // 2d array to represent rows and columns
    public int rowLengths;

    public MyDefaultTableModel(int rows, int cols) { // constructor
        super(rows, cols);
        this.editable_cells = new boolean[rows][cols];
        this.rowLengths = this.editable_cells.length;
    }

    @Override
    public void addRow(Object[] rowData) {
        super.addRow(rowData);
        this.rowLengths = this.rowLengths + 1;
        this.editable_cells = new boolean[this.rowLengths][rowData.length];
        if(rowLengths > 1) Arrays.fill(this.editable_cells[rowLengths-2],false);
        Arrays.fill(this.editable_cells[rowLengths-1],true);
    }

    @Override
    public boolean isCellEditable(int row, int column) { // custom isCellEditable function
        return this.editable_cells[row][column];
    }

    public void setCellEditable(int row, int col, boolean value) {
        this.editable_cells[row][col] = value; // set cell true/false
        this.fireTableCellUpdated(row, col);
    }
}
