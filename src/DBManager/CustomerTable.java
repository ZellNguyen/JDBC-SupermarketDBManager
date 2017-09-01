package DBManager;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.EventObject;

/**
 * Created by khang on 26/11/2015.
 */
public class CustomerTable extends Table{
    public Table customerFrame;
    public JTable customer;

    MyDefaultTableModel dtm;

    public CustomerTable(JTable table, String name){
        super(table, name);
    }

    public CustomerTable(String driver, String url){
        initCustomerTable(driver, url);
    }

    private void initCustomerTable(String driver, String url){
        customer = new JTable(){
            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  add custom rendering here
                if(customer.getEditingRow()!=customer.getRowCount()-1) {
                    if (customer.getValueAt(row, 0).equals(insert)) {
                        c.setForeground(Color.lightGray);
                    } else c.setForeground(Color.black);
                }
                if(row == customer.getSelectedRow()){
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

        String header[] = new String[]{"Customer ID", "Customer Name", "Customer Address", "Customer Phone", "Customer Point", "Customer Level"};

        dtm.setColumnIdentifiers(header);
        customer.setModel(dtm);

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM CUSTOMER");

            while(results.next()) {
                dtm.addRow(new Object[]{results.getString("cus_id"), results.getString("cus_name"), results.getString("cus_address"), results.getString("cus_phone"),
                results.getString("cus_point"), results.getString("cus_level")});
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert});

        customerFrame = new Table(customer, "Customer");

        this.customerFrame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customer.setRowSelectionInterval(customer.getRowCount()-1, customer.getRowCount()-1);

                customerFrame.vertical.setValue(customerFrame.vertical.getMaximum());

                if(customer.getCellEditor()!= null) {
                    customer.getCellEditor().stopCellEditing();
                }
                if((dtm.getValueAt(customer.getRowCount()-1,0) != insert &&
                        dtm.getValueAt(customer.getRowCount()-1,1) != insert &&
                        dtm.getValueAt(customer.getRowCount()-1,2) != insert &&
                        dtm.getValueAt(customer.getRowCount()-1,3) != insert &&
                        dtm.getValueAt(customer.getRowCount()-1,4) != insert &&
                        dtm.getValueAt(customer.getRowCount()-1,5) != insert)&&
                (dtm.getValueAt(customer.getRowCount()-1,0) != null &&
                        dtm.getValueAt(customer.getRowCount()-1,1) != null &&
                        dtm.getValueAt(customer.getRowCount()-1,2) != null &&
                        dtm.getValueAt(customer.getRowCount()-1,3) != null &&
                        dtm.getValueAt(customer.getRowCount()-1,4) != null &&
                        dtm.getValueAt(customer.getRowCount()-1,5) != null)){
                    String customer_id = (String)dtm.getValueAt(customer.getRowCount()-1, 0);
                    String customer_name = (String)dtm.getValueAt(customer.getRowCount()-1, 1);
                    String customer_address = (String)dtm.getValueAt(customer.getRowCount()-1, 2);
                    String customer_phone = (String)dtm.getValueAt(customer.getRowCount()-1, 3);
                    String customer_point = (String)dtm.getValueAt(customer.getRowCount()-1, 4);
                    String customer_level = (String)dtm.getValueAt(customer.getRowCount()-1, 5);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "INSERT INTO Customer (cus_id,cus_name,cus_address, cus_phone, cus_point, cus_level) VALUES ("
                                +"\'"+customer_id+"\'"+","+"\'"+customer_name+"\'"+","+"\'"+customer_address+"\'" +
                                "," + "\'" + customer_phone + "\'" +
                                ","  + customer_point +
                                "," + "\'" + customer_level + "\'" +")";
                        statement.executeUpdate(query);

                        dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }


                }
                else{
                    dtm.setValueAt(null,customer.getRowCount()-1,0);
                    dtm.setValueAt(null,customer.getRowCount()-1,1);
                    dtm.setValueAt(null,customer.getRowCount()-1,2);
                    dtm.setValueAt(null,customer.getRowCount()-1,3);
                    dtm.setValueAt(null,customer.getRowCount()-1,4);
                    dtm.setValueAt(null,customer.getRowCount()-1,5);
                    customer.editCellAt(customer.getRowCount()-1,0);
                }

            }
        });

        this.customer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if((customer.getValueAt(customer.getRowCount()-1,0) == null ||
                        customer.getValueAt(customer.getRowCount()-1,1) == null ||
                        customer.getValueAt(customer.getRowCount()-1,2) == null ||
                        customer.getValueAt(customer.getRowCount()-1,3) == null ||
                        customer.getValueAt(customer.getRowCount()-1,4) == null ||
                        customer.getValueAt(customer.getRowCount()-1,5) == null)
                        &&!customer.isRowSelected(customer.getRowCount()-1)){
                    dtm.setValueAt(insert, customer.getRowCount()-1, 0);
                    dtm.setValueAt(insert, customer.getRowCount()-1, 1);
                    dtm.setValueAt(insert, customer.getRowCount()-1, 2);
                    dtm.setValueAt(insert, customer.getRowCount()-1, 3);
                    dtm.setValueAt(insert, customer.getRowCount()-1, 4);
                    dtm.setValueAt(insert, customer.getRowCount()-1, 5);
                }
            }
        });

        this.customerFrame.delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (customer.getSelectedRow() != customer.getRowCount() - 1) {
                    int response = JOptionPane.showConfirmDialog (null, "Are you sure to delete this row?","Confirm", JOptionPane.YES_NO_OPTION);
                    if(response == JOptionPane.YES_OPTION) {
                        String customer_id = (String)dtm.getValueAt(customer.getSelectedRow(), 0);

                        try {
                            Class.forName(driver);
                            Connection connection = DriverManager.getConnection(url);

                            Statement statement = connection.createStatement();
                            String query = "DELETE FROM Customer WHERE cus_id = " + "\'" + customer_id + "\'";
                            statement.executeUpdate(query);

                        } catch (ClassNotFoundException | SQLException ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        dtm.removeRow(customer.getSelectedRow());
                    }
                }
            }
        });

        this.customerFrame.edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!customerFrame.isEditing) {
                    customerFrame.isEditing = true;
                    if (customer.getSelectedRow() != customer.getRowCount() - 1) {
                        for (int i = 1; i < customer.getColumnCount(); i++) {
                            dtm.setCellEditable(customer.getSelectedRow(), i, true);
                        }
                        customerFrame.edit.setText("Done");
                        customer.editCellAt(customer.getSelectedRow(), 1);
                    }
                }
                else{
                    customerFrame.edit.setText("Edit");

                    if(customer.getCellEditor()!= null) {
                        customer.getCellEditor().stopCellEditing();
                    }

                    String customer_id = (String)dtm.getValueAt(customer.getSelectedRow(), 0);
                    String customer_name = (String)dtm.getValueAt(customer.getSelectedRow(), 1);
                    String customer_address = (String)dtm.getValueAt(customer.getSelectedRow(), 2);
                    String customer_phone = (String)dtm.getValueAt(customer.getSelectedRow(), 3);
                    String customer_point = (String)dtm.getValueAt(customer.getSelectedRow(), 4);
                    String customer_level = (String)dtm.getValueAt(customer.getSelectedRow(), 5);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "Update Customer SET cus_name = "+"\'"+customer_name+"\'"+", cus_address = "+"\'"+customer_address+"\'" +
                                ", cus_phone = " + "\'" + customer_phone + "\'" +
                                ", cus_point = " + customer_point +
                                ", cus_level = " + "\'" + customer_level + "\'"
                                +" WHERE cus_id = " + "\'" + customer_id + "\'";
                        statement.executeUpdate(query);

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                    for (int i = 1; i < customer.getColumnCount(); i++) {
                        dtm.setCellEditable(customer.getSelectedRow(), i, false);
                    }

                    customerFrame.isEditing = false;
                }
            }
        });

        customerFrame.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String search = customerFrame.searchField.getText();
                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url);

                    Statement statement = connection.createStatement();
                    String query = "SElECT * FROM Customer WHERE cus_id LIKE \'%"
                            + search + "%\' OR cus_name LIKE \'%"
                            + search + "%\' OR cus_address LIKE \'%"
                            + search + "%\' OR cus_phone LIKE \'%"
                            + search + "%\' OR cus_point LIKE \'%"
                            + search + "%\' OR cus_level LIKE \'%"
                            + search + "%\'";
                    ResultSet results = statement.executeQuery(query);
                    if(results != null){
                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }
                    }
                    while(results.next()){
                        dtm.addRow(new Object[]{results.getString("cus_id"), results.getString("cus_name"), results.getString("cus_address"), results.getString("cus_phone"),
                                results.getString("cus_point"), results.getString("cus_level")});
                    }

                } catch (ClassNotFoundException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String search = customerFrame.searchField.getText();
                if(search == null || search == "" || search.trim().length() == 0) {
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Customer");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("cus_id"), results.getString("cus_name"), results.getString("cus_address"), results.getString("cus_phone"),
                                    results.getString("cus_point"), results.getString("cus_level")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert, insert, insert, insert});

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

        customerFrame.sort.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] attributes = new String[]{"cus_id", "cus_name", "cus_address", "cus_phone", "cus_point", "cus_level"};
                String option = customerFrame.sort.getSelectedItem().toString();
                if(option == "Customer ID"){
                    customerFrame.orderBy(driver,url,"Customer",dtm,"cus_id", attributes);
                }
                else if(option == "Customer Name"){
                    customerFrame.orderBy(driver,url,"Customer",dtm,"cus_name", attributes);
                }
                else if(option == "Customer Address"){
                    customerFrame.orderBy(driver,url,"Customer",dtm,"cus_address", attributes);
                }
                else if(option == "Customer Phone"){
                    customerFrame.orderBy(driver,url,"Customer",dtm,"cus_phone", attributes);
                }
                else if(option == "Customer Point"){
                    customerFrame.orderBy(driver,url,"Customer",dtm,"cus_point", attributes);
                }
                else if(option == "Customer Level"){
                    customerFrame.orderBy(driver,url,"Customer",dtm,"cus_level", attributes);
                }
                else{
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Customer");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("cus_id"), results.getString("cus_name"), results.getString("cus_address"), results.getString("cus_phone"),
                                    results.getString("cus_point"), results.getString("cus_level")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }

}
