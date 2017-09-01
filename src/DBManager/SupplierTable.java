package DBManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.EventObject;

/**
 * Created by khang on 27/11/2015.
 */
public class SupplierTable extends Table {
    public Table supplierFrame;
    public JTable supplier;
    MyDefaultTableModel dtm;

    JButton edit;
    JButton add;
    JButton delete;
    JButton cancel;

    public SupplierTable(JTable table, String name){
        super(table, name);
    }

    public SupplierTable(String driver, String url){
        initSupplierTable(driver, url);
    }

    private void initSupplierTable(String driver, String url){

        supplier = new JTable(){
            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  add custom rendering here
                if(supplier.getEditingRow()!=supplier.getRowCount()-1) {
                    if (supplier.getValueAt(row, 0).equals(insert)) {
                        c.setForeground(Color.lightGray);
                    } else c.setForeground(Color.black);
                }

                if(row == supplier.getSelectedRow()){
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

        String header[] = new String[]{"SupplierID", "Supplier Name", "Supplier Address", "Supplier Phone"};

        dtm.setColumnIdentifiers(header);
        supplier.setModel(dtm);

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM Supplier");

            while(results.next()) {
                dtm.addRow(new Object[]{results.getString("sup_id"), results.getString("sup_name"), results.getString("sup_address"), results.getString("sup_phone")});
            }
            dtm.addRow(new String[]{insert, insert, insert, insert});

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        supplierFrame = new SupplierTable(supplier, "Supplier");

        this.supplierFrame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                supplier.setRowSelectionInterval(supplier.getRowCount()-1, supplier.getRowCount()-1);

                supplierFrame.vertical.setValue( supplierFrame.vertical.getMaximum() );

                if(supplier.getCellEditor()!= null) {
                    supplier.getCellEditor().stopCellEditing();
                }
                if((dtm.getValueAt(supplier.getRowCount()-1,0) != insert &&
                        dtm.getValueAt(supplier.getRowCount()-1,1) != insert &&
                        dtm.getValueAt(supplier.getRowCount()-1,2) != insert&&
                        dtm.getValueAt(supplier.getRowCount()-1,3) != insert)&&
                        (dtm.getValueAt(supplier.getRowCount()-1,0) != null &&
                                dtm.getValueAt(supplier.getRowCount()-1,1) != null &&
                                dtm.getValueAt(supplier.getRowCount()-1,2) != null &&
                                dtm.getValueAt(supplier.getRowCount()-1,3) != null)){
                    String sup_id = (String)dtm.getValueAt(supplier.getRowCount()-1, 0);
                    String sup_name = (String)dtm.getValueAt(supplier.getRowCount()-1, 1);
                    String sup_address = (String)dtm.getValueAt(supplier.getRowCount()-1, 2);
                    String sup_phone = (String)dtm.getValueAt(supplier.getRowCount()-1, 3);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "INSERT INTO Supplier (sup_id,sup_name,sup_address, sup_phone) VALUES ("+"\'"+sup_id+"\'"+","+"\'"+sup_name+"\'"+","+"\'"+sup_address+"\'"+
                        ","+"\'"+sup_phone+"\')";
                        statement.executeUpdate(query);
                        dtm.addRow(new String[]{insert, insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }


                }
                else{
                    dtm.setValueAt(null,supplier.getRowCount()-1,0);
                    dtm.setValueAt(null,supplier.getRowCount()-1,1);
                    dtm.setValueAt(null,supplier.getRowCount()-1,2);
                    dtm.setValueAt(null,supplier.getRowCount()-1,3);
                    supplier.editCellAt(supplier.getRowCount()-1,0);
                }

            }
        });

        this.supplierFrame.delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (supplier.getSelectedRow() != supplier.getRowCount() - 1) {
                    int response = JOptionPane.showConfirmDialog (null, "Are you sure to delete this row?","Confirm", JOptionPane.YES_NO_OPTION);
                    if(response == JOptionPane.YES_OPTION) {
                        String sup_id = (String)dtm.getValueAt(supplier.getSelectedRow(), 0);

                        try {
                            Class.forName(driver);
                            Connection connection = DriverManager.getConnection(url);

                            Statement statement = connection.createStatement();
                            String query = "DELETE FROM Supplier WHERE sup_id = " + "\'" + sup_id + "\'";
                            statement.executeUpdate(query);

                        } catch (ClassNotFoundException | SQLException ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        dtm.removeRow(supplier.getSelectedRow());
                    }
                }
            }
        });

        this.supplier.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if((supplier.getValueAt(supplier.getRowCount()-1,0) == null ||
                        supplier.getValueAt(supplier.getRowCount()-1,1) == null ||
                        supplier.getValueAt(supplier.getRowCount()-1,2) == null ||
                        supplier.getValueAt(supplier.getRowCount()-1,3) == null)
                        &&!supplier.isRowSelected(supplier.getRowCount()-1)){
                    dtm.setValueAt(insert, supplier.getRowCount()-1, 0);
                    dtm.setValueAt(insert, supplier.getRowCount()-1, 1);
                    dtm.setValueAt(insert, supplier.getRowCount()-1, 2);
                    dtm.setValueAt(insert, supplier.getRowCount()-1, 3);
                }
            }
        });

        this.supplierFrame.edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!supplierFrame.isEditing) {
                    supplierFrame.isEditing = true;
                    if (supplier.getSelectedRow() != supplier.getRowCount() - 1) {
                        for (int i = 1; i < supplier.getColumnCount(); i++) {
                            dtm.setCellEditable(supplier.getSelectedRow(), i, true);
                        }
                        supplierFrame.edit.setText("Done");
                        supplier.editCellAt(supplier.getSelectedRow(), 1);
                    }
                }
                else{
                    supplierFrame.edit.setText("Edit");

                    if(supplier.getCellEditor()!= null) {
                        supplier.getCellEditor().stopCellEditing();
                    }

                    String sup_id = (String)dtm.getValueAt(supplier.getSelectedRow(), 0);
                    String sup_name = (String)dtm.getValueAt(supplier.getSelectedRow(), 1);
                    String sup_address = (String)dtm.getValueAt(supplier.getSelectedRow(), 2);
                    String sup_phone = (String)dtm.getValueAt(supplier.getSelectedRow(), 3);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "Update Supplier SET sup_name = "+"\'"+sup_name+"\'"+", sup_address = "+"\'"+sup_address+"\'" +
                                ", sup_phone = " + "\'" + sup_phone + "\'"
                                + " WHERE sup_id = " + "\'" + sup_id + "\'";
                        statement.executeUpdate(query);

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                    for (int i = 1; i < supplier.getColumnCount(); i++) {
                        dtm.setCellEditable(supplier.getSelectedRow(), i, false);
                    }

                    supplierFrame.isEditing = false;
                }
            }
        });

        supplierFrame.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String search = supplierFrame.searchField.getText();
                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url);

                    Statement statement = connection.createStatement();
                    String query = "SElECT * FROM Supplier WHERE sup_id LIKE \'%"
                            + search + "%\' OR sup_name LIKE \'%"
                            + search + "%\' OR sup_address LIKE \'%"
                            + search + "%\' OR sup_phone LIKE \'%"
                            + search + "%\'";
                    ResultSet results = statement.executeQuery(query);
                    if(results != null){
                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }
                    }
                    while(results.next()){
                        dtm.addRow(new Object[]{results.getString("sup_id"), results.getString("sup_name"), results.getString("sup_address"), results.getString("sup_phone")});
                    }

                } catch (ClassNotFoundException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String search = supplierFrame.searchField.getText();
                if(search == null || search == "" || search.trim().length() == 0) {
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Supplier");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("sup_id"), results.getString("sup_name"), results.getString("sup_address"), results.getString("sup_phone")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert, insert});

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

        supplierFrame.sort.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] attributes = new String[]{"sup_id", "sup_name", "sup_address", "sup_phone"};
                String option = supplierFrame.sort.getSelectedItem().toString();
                if(option == "Supplier ID"){
                    supplierFrame.orderBy(driver,url,"Supplier",dtm,"sup_id", attributes);
                }
                else if(option == "Supplier Name"){
                    supplierFrame.orderBy(driver,url,"Supplier",dtm,"sup_name", attributes);
                }
                else if(option == "Supplier Address"){
                    supplierFrame.orderBy(driver,url,"Supplier",dtm,"sup_address", attributes);
                }
                else if(option == "Supplier Phone"){
                    supplierFrame.orderBy(driver,url,"Supplier",dtm,"sup_phone", attributes);
                }
                else{
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Supplier");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("sup_id"), results.getString("sup_name"), results.getString("sup_address"), results.getString("sup_phone")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }

}
