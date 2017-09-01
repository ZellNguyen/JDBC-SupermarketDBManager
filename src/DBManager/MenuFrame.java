/*
 * Created by JFormDesigner on Thu Nov 26 18:14:44 ICT 2015
 */

package DBManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Khang Gia Nguyen
 */
public class MenuFrame extends JFrame {
    public String choice;
    public MenuFrame(String driver, String url) {
        initComponents(driver, url);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void button1ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Branch";
        BranchTable branch = new BranchTable(driver, url);
    }

     private void button2ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Customer";
        CustomerTable customer = new CustomerTable(driver, url);
    }

    private void button3ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Employee";
        EmployeeTable employeeTable = new EmployeeTable(driver, url);
    }

    private void button4ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Product";
        ProductTable productTable = new ProductTable(driver, url);
    }

    private void button6ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Warehouse";
        WarehouseTable warehouseTable = new WarehouseTable(driver, url);
    }

    private void button7ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Supplier";
        SupplierTable supplierTable = new SupplierTable(driver, url);
    }

    private void button8ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Invoice";
        InvoiceTable invoiceTable = new InvoiceTable(driver, url);
    }

    private void button9ActionPerformed(ActionEvent e, String driver, String url) {
        this.choice = "Order";
        OrderTable orderTable = new OrderTable(driver, url);
    }

    private void initComponents(String driver, String url) {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Khang Gia Nguyen
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        button1 = new JButton();
        button6 = new JButton();
        button2 = new JButton();
        button7 = new JButton();
        button3 = new JButton();
        button8 = new JButton();
        button4 = new JButton();
        button9 = new JButton();
        buttonBar = new JPanel();
        cancelButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            // JFormDesigner evaluation mark
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "Zell-Kane", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());

                //---- label1 ----
                label1.setText("CHOOSE THE TABLE");
                contentPanel.add(label1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- button1 ----
                button1.setText("Branch");
                button1.addActionListener(e -> button1ActionPerformed(e, driver, url));
                contentPanel.add(button1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 10), 0, 0));

                //---- button6 ----
                button6.setText("Warehouse");
                button6.addActionListener(e -> button6ActionPerformed(e, driver, url));
                contentPanel.add(button6, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- button2 ----
                button2.setText("Customer");
                button2.addActionListener(e -> button2ActionPerformed(e, driver, url));
                contentPanel.add(button2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 10), 0, 0));

                //---- button7 ----
                button7.setText("Supplier");
                button7.addActionListener(e -> button7ActionPerformed(e, driver, url));
                contentPanel.add(button7, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- button3 ----
                button3.setText("Employee");
                button3.addActionListener(e -> button3ActionPerformed(e, driver, url));
                contentPanel.add(button3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 10), 0, 0));

                //---- button8 ----
                button8.setText("Invoice");
                button8.addActionListener(e -> button8ActionPerformed(e, driver, url));
                contentPanel.add(button8, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- button4 ----
                button4.setText("Product");
                button4.addActionListener(e -> button4ActionPerformed(e, driver, url));
                contentPanel.add(button4, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 10), 0, 0));

                //---- button9 ----
                button9.setText("Order");
                button9.addActionListener(e -> button9ActionPerformed(e, driver, url));
                contentPanel.add(button9, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Khang Gia Nguyen
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JButton button1;
    private JButton button6;
    private JButton button2;
    private JButton button7;
    private JButton button3;
    private JButton button8;
    private JButton button4;
    private JButton button9;
    private JPanel buttonBar;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
