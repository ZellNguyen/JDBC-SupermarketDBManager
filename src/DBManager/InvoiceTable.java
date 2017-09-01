package DBManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

/**
 * Created by khang on 27/11/2015.
 */
public class InvoiceTable extends JFrame{
    public JFrame invoiceFrame;

    public InvoiceTable(String driver, String url){
        initInvoiceTable(driver, url);
    }

    private void initInvoiceTable(String driver, String url){
        invoiceFrame = new JFrame();
        JTable invoice = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        DefaultTableModel dtm = new DefaultTableModel(0,0);

        String header[] = new String[]{"Product", "Barcode", "Item ID", "Quantity", "Price"};

        dtm.setColumnIdentifiers(header);
        invoice.setModel(dtm);

        Container container = new Container();
        GroupLayout gl = new GroupLayout(container);
        container.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        JComboBox dateBox = new JComboBox();
        DefaultComboBoxModel dcbm1 = new DefaultComboBoxModel();
        dcbm1.addElement("Choose a day");
        dateBox.setModel(dcbm1);
        dateBox.setMaximumSize(dateBox.getPreferredSize());
        dateBox.setPrototypeDisplayValue("XXXXXXXXXX");

        JComboBox invoiceBox = new JComboBox();
        DefaultComboBoxModel dcbm2 = new DefaultComboBoxModel();
        dcbm2.addElement("Choose an ID");
        invoiceBox.setModel(dcbm2);
        invoiceBox.setMaximumSize(invoiceBox.getPreferredSize());
        invoiceBox.setPrototypeDisplayValue("XXXXXXXXXX");

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM Invoice");

            while(results.next()) {
                dcbm1.addElement(results.getString("issued_date"));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        dateBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dcbm2.removeAllElements();
                dcbm2.addElement("Choose an id");
                if(dateBox.getSelectedIndex() > 0) {
                    String date = dateBox.getSelectedItem().toString();
                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Invoice WHERE issued_date = " + "\'" + date + "\'");

                        while (results.next()) {
                            dcbm2.addElement(results.getString("invoice_id"));
                        }

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                invoiceBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        dtm.getDataVector().removeAllElements();
                        if(invoiceBox.getSelectedIndex() != -1) {
                            String id = invoiceBox.getSelectedItem().toString();
                            try {
                                Class.forName(driver);
                                Connection connection = DriverManager.getConnection(url);

                                Statement statement = connection.createStatement();
                                ResultSet results = statement.executeQuery("SELECT Product.product_name, Product.barcode, " +
                                        "InvoiceDetail.item_id, InvoiceDetail.quantity,  (Warehouse.cost*1.2*InvoiceDetail.quantity) AS price " +
                                        "FROM InvoiceDetail " +
                                        "INNER JOIN Warehouse " +
                                        "ON InvoiceDetail.item_id=Warehouse.item_id " +
                                        "INNER JOIN Product " +
                                        "ON Warehouse.barcode = Product.barcode " +
                                        "WHERE InvoiceDetail.invoice_id = " + "\'" + id + "\'");

                                while (results.next()) {
                                    dtm.addRow(new Object[]{results.getString("product_name"), results.getString("barcode"), results.getString("item_id"),
                                            results.getString("quantity"), results.getString("price")});
                                }

                            } catch (ClassNotFoundException | SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                });
            }
        });

        JScrollPane pane = new JScrollPane(invoice);

        gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(dateBox).addComponent(invoiceBox))
                .addComponent(pane));

        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(dateBox).addComponent(invoiceBox))
                .addComponent(pane));

        invoiceFrame.add(container);

        invoiceFrame.setTitle("Invoice's Information");
        invoiceFrame.setSize(800, 300);
        invoiceFrame.setLocationRelativeTo(null);
        invoiceFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        invoiceFrame.setVisible(true);
    }
}
