package DBManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.EventObject;

/**
 * Created by khang on 27/11/2015.
 */
public class WarehouseTable extends Table {
    public Table warehouseFrame;
    public JTable warehouse;
    MyDefaultTableModel dtm;

    public WarehouseTable(JTable table, String name){
        super(table, name);
    }

    public WarehouseTable(String driver, String url){
        initWarehouseTable(driver, url);
    }

    private void initWarehouseTable(String driver, String url){
        warehouse = new JTable(){
            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  add custom rendering here
                if(warehouse.getEditingRow()!=warehouse.getRowCount()-1) {
                    if (warehouse.getValueAt(row, 0).equals(insert)) {
                        c.setForeground(Color.lightGray);
                    } else c.setForeground(Color.black);
                }
                if(row == warehouse.getSelectedRow()){
                    c.setBackground(new Color(219,247,255));
                }

                else{
                    c.setBackground(Color.white);
                }

                return c;
            }

            @Override // Always selectAll()
            public boolean editCellAt(int row, int column, EventObject e) {
                boolean result = super.editCellAt(row, column, e);
                final Component editor = getEditorComponent();
                if (editor == null || !(editor instanceof JTextComponent)) {
                    return result;
                }
                if (e instanceof MouseEvent) {
                    EventQueue.invokeLater(() -> {
                        ((JTextComponent) editor).selectAll();
                    });
                } else {
                    ((JTextComponent) editor).selectAll();
                }
                return result;
            }
        };
        dtm = new MyDefaultTableModel(0,0);

        String header[] = new String[]{"Item ID", "Barcode", "Supplier ID", "Quantity", "Cost", "Manufactured Date", "Expired Date", "Imported Date", "Note"};

        dtm.setColumnIdentifiers(header);
        warehouse.setModel(dtm);

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM Warehouse");

            while(results.next()) {
                dtm.addRow(new Object[]{results.getString("item_id"), results.getString("barcode"), results.getString("sup_id"), results.getString("quantity"),
                        results.getString("cost"), results.getString("manu_date"), results.getString("exp_date"), results.getString("import_date"),
                        results.getString("note")});
            }

            dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert, insert, insert, insert});

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        /***COMBO BOX AT SOME ATTRIBUTES***/
        JComboBox comboBox1 = super.comboBoxColumn(driver, url, "Warehouse", "barcode");

        TableColumn column1 = warehouse.getColumnModel().getColumn(1);
        column1.setCellEditor(new DefaultCellEditor(comboBox1));

        JComboBox comboBox2 = super.comboBoxColumn(driver, url, "Supplier", "sup_id");

        TableColumn column2 = warehouse.getColumnModel().getColumn(2);
        column2.setCellEditor(new DefaultCellEditor(comboBox2));


        /***END SET UP COMBOBOX***/

        warehouseFrame = new Table(warehouse, "Warehouse");

        this.warehouseFrame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                warehouse.setRowSelectionInterval(warehouse.getRowCount()-1, warehouse.getRowCount()-1);

                warehouseFrame.vertical.setValue(warehouseFrame.vertical.getMaximum());

                if(warehouse.getCellEditor()!= null) {
                    warehouse.getCellEditor().stopCellEditing();
                }
                if((dtm.getValueAt(warehouse.getRowCount()-1,0) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,1) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,2) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,3) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,4) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,5) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,6) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,7) != insert &&
                        dtm.getValueAt(warehouse.getRowCount()-1,8) != insert) &&
                        (dtm.getValueAt(warehouse.getRowCount()-1,0) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,1) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,2) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,3) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,4) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,5) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,6) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,7) != null &&
                                dtm.getValueAt(warehouse.getRowCount()-1,8) != null ) ){
                    String item_id = (String)dtm.getValueAt(warehouse.getRowCount()-1, 0);
                    String barcode = (String)dtm.getValueAt(warehouse.getRowCount()-1, 1);
                    String sup_id = (String)dtm.getValueAt(warehouse.getRowCount()-1, 2);
                    String quantity = (String)dtm.getValueAt(warehouse.getRowCount()-1, 3);
                    String cost = (String)dtm.getValueAt(warehouse.getRowCount()-1, 4);
                    String manu_date = (String)dtm.getValueAt(warehouse.getRowCount()-1, 5);
                    String exp_date = (String)dtm.getValueAt(warehouse.getRowCount()-1, 6);
                    String import_date = (String)dtm.getValueAt(warehouse.getRowCount()-1, 7);
                    String note = (String)dtm.getValueAt(warehouse.getRowCount()-1, 8);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "INSERT INTO Warehouse (item_id,barcode,sup_id, quantity, cost, manu_date, exp_date, import_date, note) VALUES ("
                                +"\'"+item_id+"\'"+","+"\'"+barcode+"\'"+","+"\'"+sup_id+"\'" +
                                "," + quantity +
                                "," + cost +
                                "," + "\'" + manu_date + "\'" +
                                "," + "\'" + exp_date + "\'" +
                                "," + "\'" + import_date + "\'" +
                                "," + "\'" + note + "\'" + ")";
                        statement.executeUpdate(query);
                        dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert, insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                }
                else{
                    dtm.setValueAt(null,warehouse.getRowCount()-1,0);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,1);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,2);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,3);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,4);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,5);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,6);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,7);
                    dtm.setValueAt(null,warehouse.getRowCount()-1,8);
                    warehouse.editCellAt(warehouse.getRowCount()-1,0);
                }

            }
        });

        this.warehouse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if((warehouse.getValueAt(warehouse.getRowCount()-1,0) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,1) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,2) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,3) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,4) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,5) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,6) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,7) == null ||
                        warehouse.getValueAt(warehouse.getRowCount()-1,8) == null)
                        &&!warehouse.isRowSelected(warehouse.getRowCount()-1)){
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 0);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 1);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 2);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 3);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 4);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 5);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 6);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 7);
                    dtm.setValueAt(insert, warehouse.getRowCount()-1, 8);
                }
            }
        });

        this.warehouseFrame.delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (warehouse.getSelectedRow() != warehouse.getRowCount() - 1) {
                    int response = JOptionPane.showConfirmDialog (null, "Are you sure to delete this row?","Confirm", JOptionPane.YES_NO_OPTION);
                    if(response == JOptionPane.YES_OPTION) {
                        String item_id = (String)dtm.getValueAt(warehouse.getSelectedRow(), 0);

                        try {
                            Class.forName(driver);
                            Connection connection = DriverManager.getConnection(url);

                            Statement statement = connection.createStatement();
                            String query = "DELETE FROM Warehouse WHERE item_id = " + "\'" + item_id + "\'";
                            statement.executeUpdate(query);

                        } catch (ClassNotFoundException | SQLException ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        dtm.removeRow(warehouse.getSelectedRow());
                    }
                }
            }
        });

        this.warehouseFrame.edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!warehouseFrame.isEditing) {
                    warehouseFrame.isEditing = true;
                    if (warehouse.getSelectedRow() != warehouse.getRowCount() - 1) {
                        for (int i = 1; i < warehouse.getColumnCount(); i++) {
                            dtm.setCellEditable(warehouse.getSelectedRow(), i, true);
                        }
                        warehouseFrame.edit.setText("Done");
                        warehouse.editCellAt(warehouse.getSelectedRow(), 1);
                    }
                }
                else{
                    warehouseFrame.edit.setText("Edit");

                    if(warehouse.getCellEditor()!= null) {
                        warehouse.getCellEditor().stopCellEditing();
                    }

                    String item_id = (String)dtm.getValueAt(warehouse.getSelectedRow(), 0);
                    String barcode = (String)dtm.getValueAt(warehouse.getSelectedRow(), 1);
                    String sup_id = (String)dtm.getValueAt(warehouse.getSelectedRow(), 2);
                    String quantity = (String)dtm.getValueAt(warehouse.getSelectedRow(), 3);
                    String cost = (String)dtm.getValueAt(warehouse.getSelectedRow(), 4);
                    String manu_date = (String)dtm.getValueAt(warehouse.getSelectedRow(), 5);
                    String exp_date = (String)dtm.getValueAt(warehouse.getSelectedRow(), 6);
                    String import_date = (String)dtm.getValueAt(warehouse.getSelectedRow(), 7);
                    String note = (String)dtm.getValueAt(warehouse.getSelectedRow(), 8);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "Update Warehouse SET barcode = "+"\'"+barcode+"\'"+", sup_id = "+"\'"+sup_id+"\'" +
                                ", quantity = " + quantity +
                                ", cost = " + cost +
                                ", manu_date = " + "\'" + manu_date + "\'" +
                                ", exp_date = " + "\'" + exp_date + "\'" +
                                ", import_date = " + "\'" + import_date + "\'" +
                                ", note = " + "\'" + note + "\'"
                                +" WHERE item_id = " + "\'" + item_id + "\'";
                        statement.executeUpdate(query);

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                    for (int i = 1; i < warehouse.getColumnCount(); i++) {
                        dtm.setCellEditable(warehouse.getSelectedRow(), i, false);
                    }

                    warehouseFrame.isEditing = false;
                }
            }
        });

        warehouseFrame.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String search = warehouseFrame.searchField.getText();
                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url);

                    Statement statement = connection.createStatement();
                    String query = "SElECT * FROM Warehouse WHERE item_id LIKE \'%"
                            + search + "%\' OR barcode LIKE \'%"
                            + search + "%\' OR sup_id LIKE \'%"
                            + search + "%\' OR quantity LIKE \'%"
                            + search + "%\' OR cost LIKE \'%"
                            + search + "%\' OR manu_date LIKE \'%"
                            + search + "%\' OR exp_date LIKE \'%"
                            + search + "%\' OR import_date LIKE \'%"
                            + search + "%\' OR note LIKE \'%"
                            + search + "%\'";
                    ResultSet results = statement.executeQuery(query);
                    if(results != null){
                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }
                    }
                    while(results.next()){
                        dtm.addRow(new Object[]{results.getString("item_id"), results.getString("barcode"), results.getString("sup_id"), results.getString("quantity"),
                                results.getString("cost"), results.getString("manu_date"), results.getString("exp_date"), results.getString("import_date"),
                                results.getString("note")});
                    }

                } catch (ClassNotFoundException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String search = warehouseFrame.searchField.getText();
                if(search == null || search == "" || search.trim().length() == 0) {
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Warehouse");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("item_id"), results.getString("barcode"), results.getString("sup_id"), results.getString("quantity"),
                                    results.getString("cost"), results.getString("manu_date"), results.getString("exp_date"), results.getString("import_date"),
                                    results.getString("note")});
                        }

                        dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert, insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                else{
                    insertUpdate(e);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        warehouseFrame.sort.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] attributes = new String[]{"item_id", "barcode", "sup_id", "quantity", "cost", "manu_date", "exp_date", "import_date", "note"};
                String option = warehouseFrame.sort.getSelectedItem().toString();
                if(option == "Item ID"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"item_id", attributes);
                }
                else if(option == "Barcode"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"barcode", attributes);
                }
                else if(option == "Supplier ID"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"sup_id", attributes);
                }
                else if(option == "Quantity"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"quantity", attributes);
                }
                else if(option == "Cost"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"cost", attributes);
                }
                else if(option == "Manufactured Date"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"manu_date", attributes);
                }
                else if(option == "Expired Date"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"exp_date", attributes);
                }
                else if(option == "Imported Date"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"import_date", attributes);
                }
                else if(option == "Note"){
                    warehouseFrame.orderBy(driver,url,"Warehouse",dtm,"note", attributes);
                }
                else{
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Warehouse");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("item_id"), results.getString("barcode"), results.getString("sup_id"), results.getString("quantity"),
                                    results.getString("cost"), results.getString("manu_date"), results.getString("exp_date"), results.getString("import_date"),
                                    results.getString("note")});
                        }

                        dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert, insert, insert, insert});


                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }
}
