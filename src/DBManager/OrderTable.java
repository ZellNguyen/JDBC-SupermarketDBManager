package DBManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

/**
 * Created by khang on 27/11/2015.
 */
public class OrderTable extends Table{
    public Table orderFrame;


    public OrderTable(String driver, String url){
        initOrderTable(driver, url);
    }

    private void initOrderTable(String driver, String url){
        orderFrame = new Table();
        JTable order = new JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        DefaultTableModel dtm = new DefaultTableModel(0,0);

        String header[] = new String[]{"Product", "Barcode", "Quantity", "Price"};

        dtm.setColumnIdentifiers(header);
        order.setModel(dtm);

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

        JComboBox orderBox = new JComboBox();
        DefaultComboBoxModel dcbm2 = new DefaultComboBoxModel();
        dcbm2.addElement("Choose an ID");
        orderBox.setModel(dcbm2);
        orderBox.setMaximumSize(orderBox.getPreferredSize());
        orderBox.setPrototypeDisplayValue("XXXXXXXXXX");

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM OrderRequest");

            while(results.next()) {
                dcbm1.addElement(results.getString("order_date"));
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
                        ResultSet results = statement.executeQuery("SELECT * FROM OrderRequest WHERE order_date = " + "\'" + date + "\'");

                        while (results.next()) {
                            dcbm2.addElement(results.getString("order_id"));
                        }

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                orderBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        dtm.getDataVector().removeAllElements();
                        if(orderBox.getSelectedIndex() != -1) {
                            String id = orderBox.getSelectedItem().toString();
                            try {
                                Class.forName(driver);
                                Connection connection = DriverManager.getConnection(url);

                                Statement statement = connection.createStatement();
                                ResultSet results = statement.executeQuery("SELECT Product.product_name, Product.barcode, " +
                                        "OrderDetail.quantity, Warehouse.cost "+
                                        "FROM OrderDetail " +
                                        "INNER JOIN Warehouse " +
                                        "ON OrderDetail.barcode=Warehouse.barcode " +
                                        "INNER JOIN Product " +
                                        "ON Warehouse.barcode = Product.barcode " +
                                        "WHERE OrderDetail.order_id = " + "\'" + id + "\'");

                                while (results.next()) {
                                    dtm.addRow(new Object[]{results.getString("Product.product_name"), results.getString("Product.barcode"),
                                            results.getString("OrderDetail.quantity"), results.getString("Warehouse.cost")});
                                }

                            } catch (ClassNotFoundException | SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                });
            }
        });

        JScrollPane pane = new JScrollPane(order);

        gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(dateBox).addComponent(orderBox))
                .addComponent(pane));

        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(dateBox).addComponent(orderBox))
                .addComponent(pane));

        orderFrame.add(container);

        orderFrame.setTitle("Order's Information");
        orderFrame.setSize(800, 300);
        orderFrame.setLocationRelativeTo(null);
        orderFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        orderFrame.setVisible(true);
    }

}
