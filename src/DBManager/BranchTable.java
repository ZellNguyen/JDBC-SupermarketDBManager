package DBManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.EventObject;

/**
 * Created by khang on 26/11/2015.
 */
public class BranchTable extends Table {

    public Table branchFrame;
    public JTable branch;
    MyDefaultTableModel dtm;


    public BranchTable(JTable table, String name){
        super(table, name);
    }

    public BranchTable(String driver, String url){
        initBranchTable(driver, url);
    }

    private void initBranchTable(String driver, String url){

        branch = new JTable(){

            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  add custom rendering here
                if(branch.getEditingRow()!=branch.getRowCount()-1) {
                    if (branch.getValueAt(row, 0).equals(insert)) {
                        c.setForeground(Color.lightGray);
                    } else c.setForeground(Color.black);
                }
                if(row == branch.getSelectedRow()){
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

        String header[] = new String[]{"Branch ID", "Branch Name", "Branch Address"};

        dtm.setColumnIdentifiers(header);
        branch.setModel(dtm);

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM BRANCH");

            while(results.next()) {
                dtm.addRow(new Object[]{results.getString("branch_id"), results.getString("branch_name"), results.getString("branch_address")});
            }

            dtm.addRow(new Object[]{insert, insert, insert});

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        branchFrame = new BranchTable(branch, "Branch");


        this.branchFrame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                branch.setRowSelectionInterval(branch.getRowCount()-1, branch.getRowCount()-1);

                branchFrame.vertical.setValue( branchFrame.vertical.getMaximum() );

                if(branch.getCellEditor()!= null) {
                    branch.getCellEditor().stopCellEditing();
                }
                if((dtm.getValueAt(branch.getRowCount()-1,0) != insert &&
                        dtm.getValueAt(branch.getRowCount()-1,1) != insert &&
                        dtm.getValueAt(branch.getRowCount()-1,2) != insert)&&
                        (dtm.getValueAt(branch.getRowCount()-1,0) != null &&
                                dtm.getValueAt(branch.getRowCount()-1,1) != null &&
                                dtm.getValueAt(branch.getRowCount()-1,2) != null)){
                    String branch_id = (String)dtm.getValueAt(branch.getRowCount()-1, 0);
                    String branch_name = (String)dtm.getValueAt(branch.getRowCount()-1, 1);
                    String branch_address = (String)dtm.getValueAt(branch.getRowCount()-1, 2);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "INSERT INTO Branch (branch_id,branch_name,branch_address) VALUES ("+"\'"+branch_id+"\'"+","+"\'"+branch_name+"\'"+","+"\'"+branch_address+"\')";
                        statement.executeUpdate(query);
                        dtm.addRow(new String[]{insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }


                }
                else{
                    dtm.setValueAt(null,branch.getRowCount()-1,0);
                    dtm.setValueAt(null,branch.getRowCount()-1,1);
                    dtm.setValueAt(null,branch.getRowCount()-1,2);
                    branch.editCellAt(branch.getRowCount()-1,0);
                }

            }
        });

        this.branchFrame.delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (branch.getSelectedRow() != branch.getRowCount() - 1) {
                    int response = JOptionPane.showConfirmDialog (null, "Are you sure to delete this row?","Confirm", JOptionPane.YES_NO_OPTION);
                    if(response == JOptionPane.YES_OPTION) {
                        String branch_id = (String)dtm.getValueAt(branch.getSelectedRow(), 0);

                        try {
                            Class.forName(driver);
                            Connection connection = DriverManager.getConnection(url);

                            Statement statement = connection.createStatement();
                            String query = "DELETE FROM Branch WHERE branch_id = " + "\'" + branch_id + "\'";
                            statement.executeUpdate(query);

                        } catch (ClassNotFoundException | SQLException ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        dtm.removeRow(branch.getSelectedRow());
                    }
                }
            }
        });

        this.branch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if((branch.getValueAt(branch.getRowCount()-1,0) == null ||
                        branch.getValueAt(branch.getRowCount()-1,1) == null ||
                        branch.getValueAt(branch.getRowCount()-1,2) == null )
                &&!branch.isRowSelected(branch.getRowCount()-1)){
                    dtm.setValueAt(insert, branch.getRowCount()-1, 0);
                    dtm.setValueAt(insert, branch.getRowCount()-1, 1);
                    dtm.setValueAt(insert, branch.getRowCount()-1, 2);
                }
            }
        });

        this.branchFrame.edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!branchFrame.isEditing) {
                    branchFrame.isEditing = true;
                    if (branch.getSelectedRow() != branch.getRowCount() - 1) {
                        for (int i = 1; i < branch.getColumnCount(); i++) {
                            dtm.setCellEditable(branch.getSelectedRow(), i, true);
                        }
                        branchFrame.edit.setText("Done");
                        branch.editCellAt(branch.getSelectedRow(), 1);
                    }
                }
                else{
                    branchFrame.edit.setText("Edit");

                    if(branch.getCellEditor()!= null) {
                        branch.getCellEditor().stopCellEditing();
                    }

                    String branch_id = (String)dtm.getValueAt(branch.getSelectedRow(), 0);
                    String branch_name = (String)dtm.getValueAt(branch.getSelectedRow(), 1);
                    String branch_address = (String)dtm.getValueAt(branch.getSelectedRow(), 2);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "Update Branch SET branch_name = "+"\'"+branch_name+"\'"+", branch_address = "+"\'"+branch_address+"\' WHERE branch_id = " + "\'" + branch_id + "\'";
                        statement.executeUpdate(query);

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                    for (int i = 1; i < branch.getColumnCount(); i++) {
                        dtm.setCellEditable(branch.getSelectedRow(), i, false);
                    }

                    branchFrame.isEditing = false;
                }
            }
        });


        branchFrame.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String search = branchFrame.searchField.getText();
                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url);

                    Statement statement = connection.createStatement();
                    String query = "SElECT * FROM Branch WHERE branch_id LIKE \'%"
                            + search + "%\' OR branch_name LIKE \'%"
                            + search + "%\' OR branch_address LIKE \'%"
                            + search + "%\'";
                    ResultSet results = statement.executeQuery(query);
                    if(results != null){
                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }
                    }
                    while(results.next()){
                        dtm.addRow(new Object[]{results.getString("branch_id"), results.getString("branch_name"), results.getString("branch_address")});
                    }

                } catch (ClassNotFoundException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String search = branchFrame.searchField.getText();
                if(search == null || search == "" || search.trim().length() == 0) {
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM BRANCH");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("branch_id"), results.getString("branch_name"), results.getString("branch_address")});
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

        branchFrame.sort.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String option = branchFrame.sort.getSelectedItem().toString();
                if(option == "Branch ID"){
                    branchFrame.orderBy(driver,url,"Branch",dtm,"branch_id", new String[]{"branch_id", "branch_name", "branch_address"});
                }
                else if(option == "Branch Name"){
                    branchFrame.orderBy(driver,url,"Branch",dtm,"branch_name", new String[]{"branch_id", "branch_name", "branch_address"});
                }
                else if(option == "Branch Address"){
                    branchFrame.orderBy(driver,url,"Branch",dtm,"branch_address", new String[]{"branch_id", "branch_name", "branch_address"});
                }
                else{
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM BRANCH");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("branch_id"), results.getString("branch_name"), results.getString("branch_address")});
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
