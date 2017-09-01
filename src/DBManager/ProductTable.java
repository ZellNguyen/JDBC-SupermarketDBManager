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
public class ProductTable extends Table {
    public Table productFrame;
    public JTable product;
    MyDefaultTableModel dtm;

    public ProductTable(String driver, String url){
        initProductTable(driver, url);
    }

    private void initProductTable(String driver, String url){

        product = new JTable(){
            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  add custom rendering here
                if(product.getEditingRow()!=product.getRowCount()-1) {
                    if (product.getValueAt(row, 0).equals(insert)) {
                        c.setForeground(Color.lightGray);
                    } else c.setForeground(Color.black);
                }
                if(row == product.getSelectedRow()){
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

        String header[] = new String[]{"Barcode", "Product Name", "Manufacture"};

        dtm.setColumnIdentifiers(header);
        product.setModel(dtm);

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM Product");

            while(results.next()) {
                dtm.addRow(new Object[]{results.getString("barcode"), results.getString("product_name"), results.getString("manufacturer")});
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        dtm.addRow(new String[]{insert, insert, insert});

        productFrame = new Table(product,"Product");

        this.productFrame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                product.setRowSelectionInterval(product.getRowCount()-1, product.getRowCount()-1);

                productFrame.vertical.setValue( productFrame.vertical.getMaximum() );

                if(product.getCellEditor()!= null) {
                    product.getCellEditor().stopCellEditing();
                }
                if((dtm.getValueAt(product.getRowCount()-1,0) != insert &&
                        dtm.getValueAt(product.getRowCount()-1,1) != insert &&
                        dtm.getValueAt(product.getRowCount()-1,2) != insert)&&
                        (dtm.getValueAt(product.getRowCount()-1,0) != null &&
                                dtm.getValueAt(product.getRowCount()-1,1) != null &&
                                dtm.getValueAt(product.getRowCount()-1,2) != null)){
                    String barcode = (String)dtm.getValueAt(product.getRowCount()-1, 0);
                    String product_name = (String)dtm.getValueAt(product.getRowCount()-1, 1);
                    String manufacturer = (String)dtm.getValueAt(product.getRowCount()-1, 2);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "INSERT INTO Product (barcode,product_name,manufacturer) VALUES ("+"\'"+barcode+"\'"+","+"\'"+product_name+"\'"+","+"\'"+manufacturer+"\')";
                        statement.executeUpdate(query);
                        dtm.addRow(new String[]{insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }


                }
                else{
                    dtm.setValueAt(null,product.getRowCount()-1,0);
                    dtm.setValueAt(null,product.getRowCount()-1,1);
                    dtm.setValueAt(null,product.getRowCount()-1,2);
                    product.editCellAt(product.getRowCount()-1,0);
                }

            }
        });

        this.productFrame.delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (product.getSelectedRow() != product.getRowCount() - 1) {
                    int response = JOptionPane.showConfirmDialog (null, "Are you sure to delete this row?","Confirm", JOptionPane.YES_NO_OPTION);
                    if(response == JOptionPane.YES_OPTION) {
                        String barcode = (String)dtm.getValueAt(product.getSelectedRow(), 0);

                        try {
                            Class.forName(driver);
                            Connection connection = DriverManager.getConnection(url);

                            Statement statement = connection.createStatement();
                            String query = "DELETE FROM Product WHERE barcode = " + "\'" + barcode + "\'";
                            statement.executeUpdate(query);

                        } catch (ClassNotFoundException | SQLException ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        dtm.removeRow(product.getSelectedRow());
                    }
                }
            }
        });

        this.product.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if((product.getValueAt(product.getRowCount()-1,0) == null ||
                        product.getValueAt(product.getRowCount()-1,1) == null ||
                        product.getValueAt(product.getRowCount()-1,2) == null )
                        &&!product.isRowSelected(product.getRowCount()-1)){
                    dtm.setValueAt(insert, product.getRowCount()-1, 0);
                    dtm.setValueAt(insert, product.getRowCount()-1, 1);
                    dtm.setValueAt(insert, product.getRowCount()-1, 2);
                }
            }
        });

        this.productFrame.edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!productFrame.isEditing) {
                    productFrame.isEditing = true;
                    if (product.getSelectedRow() != product.getRowCount() - 1) {
                        for (int i = 1; i < product.getColumnCount(); i++) {
                            dtm.setCellEditable(product.getSelectedRow(), i, true);
                        }
                        productFrame.edit.setText("Done");
                        product.editCellAt(product.getSelectedRow(), 1);
                    }
                }
                else{
                    productFrame.edit.setText("Edit");

                    if(product.getCellEditor()!= null) {
                        product.getCellEditor().stopCellEditing();
                    }

                    String barcode = (String)dtm.getValueAt(product.getSelectedRow(), 0);
                    String product_name = (String)dtm.getValueAt(product.getSelectedRow(), 1);
                    String manufacturer = (String)dtm.getValueAt(product.getSelectedRow(), 2);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "Update Product SET product_name = "+"\'"+product_name+"\'"+", manufacturer = "+"\'"+manufacturer+"\' WHERE barcode = " + "\'" + barcode + "\'";
                        statement.executeUpdate(query);

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                    for (int i = 1; i < product.getColumnCount(); i++) {
                        dtm.setCellEditable(product.getSelectedRow(), i, false);
                    }

                    productFrame.isEditing = false;
                }
            }
        });

        productFrame.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String search = productFrame.searchField.getText();
                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url);

                    Statement statement = connection.createStatement();
                    String query = "SElECT * FROM Product WHERE barcode LIKE \'%"
                            + search + "%\' OR product_name LIKE \'%"
                            + search + "%\' OR manufacturer LIKE \'%"
                            + search + "%\'";
                    ResultSet results = statement.executeQuery(query);
                    if(results != null){
                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }
                    }
                    while(results.next()){
                        dtm.addRow(new Object[]{results.getString("barcode"), results.getString("product_name"), results.getString("manufacturer")});
                    }

                } catch (ClassNotFoundException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String search = productFrame.searchField.getText();
                if(search == null || search == "" || search.trim().length() == 0) {
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Product");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("barcode"), results.getString("product_name"), results.getString("manufacturer")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert});

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

        productFrame.sort.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] attributes = new String[]{"barcode", "product_name", "manufacturer"};
                String option = productFrame.sort.getSelectedItem().toString();
                if(option == "Barcode"){
                    productFrame.orderBy(driver,url,"Product",dtm,"barcode", attributes);
                }
                else if(option == "Product Name"){
                    productFrame.orderBy(driver,url,"Product",dtm,"product_name", attributes);
                }
                else if(option == "Manufacturer"){
                    productFrame.orderBy(driver,url,"Product",dtm,"manufacturer_address", attributes);
                }
                else{
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Product");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("barcode"), results.getString("product_name"), results.getString("manufacturer")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }

}
