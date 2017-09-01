package DBManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
public class EmployeeTable extends Table {
    public Table employeeFrame;
    public JTable employee;

    public EmployeeTable(JTable table, String name){
        super(table, name);
    }

    public EmployeeTable(String driver, String url){
        initEmployeeTable(driver, url);
    }

    private void initEmployeeTable(String driver, String url){
        employee = new JTable(){
            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  add custom rendering here
                if(employee.getEditingRow()!=employee.getRowCount()-1) {
                    if (employee.getValueAt(row, 0).equals(insert)) {
                        c.setForeground(Color.lightGray);
                    } else c.setForeground(Color.black);
                }
                if(row == employee.getSelectedRow()){
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

        
        MyDefaultTableModel dtm = new MyDefaultTableModel(0,0);

        String header[] = new String[]{"Employee ID", "Employee Name", "Gender", "Date Of Birth", "Position", "Salary", "Branch ID"};

        dtm.setColumnIdentifiers(header);
        employee.setModel(dtm);

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM EMPLOYEE");

            while(results.next()) {
                dtm.addRow(new Object[]{results.getString("emp_id"), results.getString("emp_name"),
                results.getString("gender"), results.getString("dob"), results.getString("position"),
                        results.getString("salary"), results.getString("branch_id")});
            }

            dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert, insert});

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        /***COMBO BOX AT SOME ATTRIBUTES***/
        JComboBox comboBox1 = super.comboBoxColumn(driver, url, "Branch", "branch_id");

        TableColumn column1 = employee.getColumnModel().getColumn(6);
        column1.setCellEditor(new DefaultCellEditor(comboBox1));

        employeeFrame = new Table(employee, "Employee");

        this.employeeFrame.add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                employee.setRowSelectionInterval(employee.getRowCount()-1, employee.getRowCount()-1);

                employeeFrame.vertical.setValue(employeeFrame.vertical.getMaximum());

                if(employee.getCellEditor()!= null) {
                    employee.getCellEditor().stopCellEditing();
                }
                if((dtm.getValueAt(employee.getRowCount()-1,0) != insert &&
                        dtm.getValueAt(employee.getRowCount()-1,1) != insert &&
                        dtm.getValueAt(employee.getRowCount()-1,2) != insert &&
                        dtm.getValueAt(employee.getRowCount()-1,3) != insert &&
                        dtm.getValueAt(employee.getRowCount()-1,4) != insert &&
                        dtm.getValueAt(employee.getRowCount()-1,5) != insert &&
                        dtm.getValueAt(employee.getRowCount()-1,6) != insert) &&
                        (dtm.getValueAt(employee.getRowCount()-1,0) != null &&
                                dtm.getValueAt(employee.getRowCount()-1,1) != null &&
                                dtm.getValueAt(employee.getRowCount()-1,2) != null &&
                                dtm.getValueAt(employee.getRowCount()-1,3) != null &&
                                dtm.getValueAt(employee.getRowCount()-1,4) != null &&
                                dtm.getValueAt(employee.getRowCount()-1,5) != null &&
                                dtm.getValueAt(employee.getRowCount()-1,6) != null) ){
                    String employee_id = (String)dtm.getValueAt(employee.getRowCount()-1, 0);
                    String employee_name = (String)dtm.getValueAt(employee.getRowCount()-1, 1);
                    String employee_gender = (String)dtm.getValueAt(employee.getRowCount()-1, 2);
                    String employee_dob = (String)dtm.getValueAt(employee.getRowCount()-1, 3);
                    String employee_position = (String)dtm.getValueAt(employee.getRowCount()-1, 4);
                    String employee_salary = (String)dtm.getValueAt(employee.getRowCount()-1, 5);
                    String branch_id = (String)dtm.getValueAt(employee.getRowCount()-1, 6);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "INSERT INTO Employee (emp_id,emp_name,gender, dob, position, salary, branch_id) VALUES ("
                                +"\'"+employee_id+"\'"+","+"\'"+employee_name+"\'"+","+"\'"+employee_gender+"\'" +
                                "," + "\'" + employee_dob + "\'" +
                                "," + "\'" + employee_position + "\'" +
                                "," + employee_salary +
                                "," + "\'" + branch_id + "\'" + ")";
                        statement.executeUpdate(query);
                        dtm.addRow(new String[]{insert, insert, insert, insert, insert, insert, insert});

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                }
                else{
                    dtm.setValueAt(null,employee.getRowCount()-1,0);
                    dtm.setValueAt(null,employee.getRowCount()-1,1);
                    dtm.setValueAt(null,employee.getRowCount()-1,2);
                    dtm.setValueAt(null,employee.getRowCount()-1,3);
                    dtm.setValueAt(null,employee.getRowCount()-1,4);
                    dtm.setValueAt(null,employee.getRowCount()-1,5);
                    dtm.setValueAt(null,employee.getRowCount()-1,6);
                    employee.editCellAt(employee.getRowCount()-1,0);
                }

            }
        });

        this.employee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if((employee.getValueAt(employee.getRowCount()-1,0) == null ||
                        employee.getValueAt(employee.getRowCount()-1,1) == null ||
                        employee.getValueAt(employee.getRowCount()-1,2) == null ||
                        employee.getValueAt(employee.getRowCount()-1,3) == null ||
                        employee.getValueAt(employee.getRowCount()-1,4) == null ||
                        employee.getValueAt(employee.getRowCount()-1,5) == null ||
                        employee.getValueAt(employee.getRowCount()-1,6) == null)
                        &&!employee.isRowSelected(employee.getRowCount()-1)){
                    dtm.setValueAt(insert, employee.getRowCount()-1, 0);
                    dtm.setValueAt(insert, employee.getRowCount()-1, 1);
                    dtm.setValueAt(insert, employee.getRowCount()-1, 2);
                    dtm.setValueAt(insert, employee.getRowCount()-1, 3);
                    dtm.setValueAt(insert, employee.getRowCount()-1, 4);
                    dtm.setValueAt(insert, employee.getRowCount()-1, 5);
                    dtm.setValueAt(insert, employee.getRowCount()-1, 6);
                }
            }
        });

        this.employeeFrame.delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (employee.getSelectedRow() != employee.getRowCount() - 1) {
                    int response = JOptionPane.showConfirmDialog (null, "Are you sure to delete this row?","Confirm", JOptionPane.YES_NO_OPTION);
                    if(response == JOptionPane.YES_OPTION) {
                        String employee_id = (String)dtm.getValueAt(employee.getSelectedRow(), 0);

                        try {
                            Class.forName(driver);
                            Connection connection = DriverManager.getConnection(url);

                            Statement statement = connection.createStatement();
                            String query = "DELETE FROM Employee WHERE emp_id = " + "\'" + employee_id + "\'";
                            statement.executeUpdate(query);

                        } catch (ClassNotFoundException | SQLException ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                        dtm.removeRow(employee.getSelectedRow());
                    }
                }
            }
        });

        this.employeeFrame.edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!employeeFrame.isEditing) {
                    employeeFrame.isEditing = true;
                    if (employee.getSelectedRow() != employee.getRowCount() - 1) {
                        for (int i = 1; i < employee.getColumnCount(); i++) {
                            dtm.setCellEditable(employee.getSelectedRow(), i, true);
                        }
                        employeeFrame.edit.setText("Done");
                        employee.editCellAt(employee.getSelectedRow(), 1);
                    }
                }
                else{
                    employeeFrame.edit.setText("Edit");

                    if(employee.getCellEditor()!= null) {
                        employee.getCellEditor().stopCellEditing();
                    }

                    String employee_id = (String)dtm.getValueAt(employee.getSelectedRow(), 0);
                    String employee_name = (String)dtm.getValueAt(employee.getSelectedRow(), 1);
                    String employee_gender = (String)dtm.getValueAt(employee.getSelectedRow(), 2);
                    String employee_dob = (String)dtm.getValueAt(employee.getSelectedRow(), 3);
                    String employee_position = (String)dtm.getValueAt(employee.getSelectedRow(), 4);
                    String employee_salary = (String)dtm.getValueAt(employee.getSelectedRow(), 5);
                    String branch_id = (String)dtm.getValueAt(employee.getSelectedRow(), 6);

                    try {
                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        String query = "Update Employee SET emp_name = "+"\'"+employee_name+"\'"+", gender = "+"\'"+employee_gender+"\'" +
                                ", dob = " + "\'" + employee_dob + "\'" +
                                ", position = " + "\'" + employee_position + "\'" +
                                ", salary = " + employee_salary +
                                ", branch_id = " + "\'" + branch_id + "\'"
                                +" WHERE emp_id = " + "\'" + employee_id + "\'";
                        statement.executeUpdate(query);

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                    for (int i = 1; i < employee.getColumnCount(); i++) {
                        dtm.setCellEditable(employee.getSelectedRow(), i, false);
                    }

                    employeeFrame.isEditing = false;
                }
            }
        });

        employeeFrame.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String search = employeeFrame.searchField.getText();
                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url);

                    Statement statement = connection.createStatement();
                    String query = "SElECT * FROM Employee WHERE emp_id LIKE \'%"
                            + search + "%\' OR emp_name LIKE \'%"
                            + search + "%\' OR gender LIKE \'%"
                            + search + "%\' OR dob LIKE \'%"
                            + search + "%\' OR position LIKE \'%"
                            + search + "%\' OR salary LIKE \'%"
                            + search + "%\' OR branch_id LIKE \'%"
                            + search + "%\'";
                    ResultSet results = statement.executeQuery(query);
                    if(results != null){
                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }
                    }
                    while(results.next()){
                        dtm.addRow(new Object[]{results.getString("emp_id"), results.getString("emp_name"),
                                results.getString("gender"), results.getString("dob"), results.getString("position"),
                                results.getString("salary"), results.getString("branch_id")});
                    }

                } catch (ClassNotFoundException | SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String search = employeeFrame.searchField.getText();
                if(search == null || search == "" || search.trim().length() == 0) {
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Employee");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("emp_id"), results.getString("emp_name"),
                                    results.getString("gender"), results.getString("dob"), results.getString("position"),
                                    results.getString("salary"), results.getString("branch_id")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert, insert, insert, insert, insert});

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

        employeeFrame.sort.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] attributes = new String[]{"emp_id", "emp_name", "gender", "dob", "position", "salary", "branch_id"};
                String option = employeeFrame.sort.getSelectedItem().toString();
                if(option == "Employee ID"){
                    employeeFrame.orderBy(driver,url,"Employee",dtm,"emp_id", attributes);
                }
                else if(option == "Employee Name"){
                    employeeFrame.orderBy(driver,url,"Employee",dtm,"emp_name", attributes);
                }
                else if(option == "Gender"){
                    employeeFrame.orderBy(driver,url,"Employee",dtm,"gender", attributes);
                }
                else if(option == "Date Of Birth"){
                    employeeFrame.orderBy(driver,url,"Employee",dtm,"dob", attributes);
                }
                else if(option == "Position"){
                    employeeFrame.orderBy(driver,url,"Employee",dtm,"position", attributes);
                }
                else if(option == "Salary"){
                    employeeFrame.orderBy(driver,url,"Employee",dtm,"salary", attributes);
                }
                else if(option == "Branch ID"){
                    employeeFrame.orderBy(driver,url,"Employee",dtm,"branch_id", attributes);
                }
                else{
                    try {

                        for(int i = dtm.getRowCount()-1; i >=0; i--){
                            dtm.removeRow(i);
                        }

                        Class.forName(driver);
                        Connection connection = DriverManager.getConnection(url);

                        Statement statement = connection.createStatement();
                        ResultSet results = statement.executeQuery("SELECT * FROM Employee");

                        while (results.next()) {
                            dtm.addRow(new Object[]{results.getString("emp_id"), results.getString("emp_name"),
                                    results.getString("gender"), results.getString("dob"), results.getString("position"),
                                    results.getString("salary"), results.getString("branch_id")});
                        }

                        dtm.addRow(new Object[]{insert, insert, insert, insert, insert, insert, insert});


                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });

    }

}
