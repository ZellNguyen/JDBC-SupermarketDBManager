package DBManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.*;
import java.util.Arrays;

/**
 * Created by khang on 27/11/2015.
 */
public class Table extends JFrame {
    JButton edit;
    JButton add;
    JButton delete;
    JButton cancel;

    JFormattedTextField searchField;
    JLabel searchButton;

    JComboBox sort;
    DefaultComboBoxModel sortDcbm;
    JLabel sortLabel;

    JScrollPane pane;

    JScrollBar vertical;

    public JFrame frame;

    JTable table;

    public static String insert = "Insert new value here...";

    public static boolean isEditing = false;

    public Table(JTable table, String name){
        this.table = table;
        tableInit(name);
    }

    public Table() {
        super();
    }

    private void tableInit(String name) {
        frame = new JFrame();
        edit = new JButton("Edit");

        add = new JButton("Add");

        delete = new JButton("Delete");

        cancel = new JButton("Cancel");

        searchField = new JFormattedTextField();
        searchField.setMaximumSize(new Dimension(180,10));

        searchButton = new JLabel("Search");
        Border border1 = searchButton.getBorder();
        Border margin1 = new EmptyBorder(10,10,10,10);
        searchButton.setBorder(new CompoundBorder(border1, margin1));

        sort = new JComboBox();
        sort.setMaximumSize(new Dimension(200,10));
        sortLabel = new JLabel("Sort by:");

        Border border2 = sortLabel.getBorder();
        Border margin2 = new EmptyBorder(10,10,10,10);
        sortLabel.setBorder(new CompoundBorder(border2, margin2));
        /***SET UP SORT OPTIONS***/
        sortDcbm = new DefaultComboBoxModel();
        sortDcbm.addElement("Choose...");
        for(int i = 0; i < table.getColumnCount(); i++){
            String option = table.getModel().getColumnName(i);
            sortDcbm.addElement(option);
        }
        sort.setModel(sortDcbm);
        sort.setPreferredSize(new Dimension(200, 10));
        
        sort.addItemListener(e->sortItemListener(e));


        cancel.addActionListener(e -> cancelActionPerformed(e));
        add.addActionListener(e->addActionPerformed(e));
        edit.addActionListener(e->editActionPerformed(e));
        delete.addActionListener(e->deleteActionPerformed(e));

        Container container = new Container();
        GroupLayout gl = new GroupLayout(container);
        container.setLayout(gl);

        pane = new JScrollPane(this.table);
        //pane.setMaximumSize(new Dimension(600,350));
        vertical = pane.getVerticalScrollBar();

        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(pane).
                addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(searchButton).addComponent(searchField)
                        .addGap(20).addComponent(sortLabel).addComponent(sort))).addGroup(gl.createSequentialGroup().addComponent(add)
                .addComponent(edit).addComponent(delete).addComponent(cancel)));

        gl.setVerticalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup().addComponent(pane)
                .addGroup(gl.createSequentialGroup().addComponent(searchButton).addComponent(searchField)
                        .addGap(20).addComponent(sortLabel).addComponent(sort)))
                .addGroup(gl.createParallelGroup().addComponent(add).addComponent(edit).addComponent(delete).addComponent(cancel)));


        frame.add(container);
        frame.setTitle(name+"'s Information");
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public void sortItemListener(ItemEvent e) {
    }

    public void deleteActionPerformed(ActionEvent e) {
    }

    public void editActionPerformed(ActionEvent e) {
    }

    public void addActionPerformed(ActionEvent e) {
    }

    public void cancelActionPerformed(ActionEvent e) {
        frame.dispose();
    }

    public void reloadTable(JTable table){
        Container container = new Container();
        GroupLayout gl = new GroupLayout(container);
        container.setLayout(gl);

        JScrollPane pane = new JScrollPane(table);

        gl.setHorizontalGroup(gl.createParallelGroup().addComponent(pane).addGroup(gl.createSequentialGroup().addComponent(add)
                .addComponent(edit).addComponent(delete).addComponent(cancel)));

        gl.setVerticalGroup(gl.createSequentialGroup().addComponent(pane)
                .addGroup(gl.createParallelGroup().addComponent(add).addComponent(edit).addComponent(delete).addComponent(cancel)));

    }

    public JComboBox comboBoxColumn(String driver, String url, String relation, String attribute){
        JComboBox comboBox = new JComboBox();
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        comboBox.setModel(dcbm);
        dcbm.addElement(null);
        comboBox.setMaximumSize(comboBox.getPreferredSize());
        comboBox.setPrototypeDisplayValue("XXXXXXXXXX");

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + relation);

            while(results.next()) {
                dcbm.addElement(results.getString(attribute));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return comboBox;
    }

    public void orderBy(String driver, String url, String table, MyDefaultTableModel dtm, String attribute, String[] attributes){
        dtm.getDataVector().removeAllElements();
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM " + table + " ORDER BY " + attribute);

            while (results.next()) {
                String[] rs = new String[attributes.length];
                for(int i = 0; i < rs.length; i++){
                    rs[i] = results.getString(attributes[i]);
                }
                dtm.addRow(rs);
            }

            String insertRow[] = new String[attributes.length];
            Arrays.fill(insertRow, insert);
            dtm.addRow(insertRow);

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
