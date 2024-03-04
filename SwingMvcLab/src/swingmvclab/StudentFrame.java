package swingmvclab;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;

/*
 * A megjelenítendő ablakunk osztálya.
 */
public class StudentFrame extends JFrame {
    
    /*
     * Ebben az objektumban vannak a hallgatói adatok.
     * A program indulás után betölti az adatokat fájlból, bezáráskor pedig kimenti oda.
     * 
     * NE MÓDOSÍTSD!
     */
    private StudentData data;
    JTextField nameField = new JTextField(20);
    JTextField neptunField = new JTextField(6);
    
    /*
     * Itt hozzuk létre és adjuk hozzá az ablakunkhoz a különböző komponenseket
     * (táblázat, beviteli mező, gomb).
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());


        JTable table = new JTable(data);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        add(scrollPane, BorderLayout.CENTER);

        JPanel addStud = new JPanel();

        JButton button = new JButton("Felvesz");
        button.addActionListener(new AddButtonListener());
        addStud.add(new JLabel("Név:"));
        addStud.add(nameField);
        addStud.add(new JLabel("Neptun:"));
        addStud.add(neptunField);
        addStud.add(button);
        add(addStud, BorderLayout.SOUTH);

        table.setRowSorter(new TableRowSorter<>(table.getModel()));

        table.setDefaultRenderer(String.class, new StudentTableCellRenderer(table.getDefaultRenderer(String.class)));
        table.setDefaultRenderer(Boolean.class, new StudentTableCellRenderer(table.getDefaultRenderer(Boolean.class)));
        table.setDefaultRenderer(Integer.class, new StudentTableCellRenderer(table.getDefaultRenderer(Integer.class)));
        
    }
    final class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            data.addStudent(nameField.getText(), neptunField.getText());
        }
    }

    class StudentTableCellRenderer implements TableCellRenderer {

        private TableCellRenderer renderer;

        public StudentTableCellRenderer(TableCellRenderer defRenderer) {
            this.renderer = defRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component component = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            if(((Integer)(data.getValueAt(table.getRowSorter().convertRowIndexToModel(row), 3)))< 2) {
                component.setBackground(RED);
            }else{
                component.setBackground(GREEN);
            }

            return component;
        }
    }

    /*
     * Az ablak konstruktora.
     * 
     * NE MÓDOSÍTSD!
     */
    @SuppressWarnings("unchecked")
    public StudentFrame() {
        super("Hallgatói nyilvántartás");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Induláskor betöltjük az adatokat
        try {
            data = new StudentData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"));
            data.students = (List<Student>)ois.readObject();
            ois.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Bezáráskor mentjük az adatokat
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"));
                    oos.writeObject(data.students);
                    oos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Felépítjük az ablakot
        setMinimumSize(new Dimension(525, 250));
        initComponents();
    }

    /*
     * A program belépési pontja.
     * 
     * NE MÓDOSÍTSD!
     */
    public static void main(String[] args) {
        // Megjelenítjük az ablakot
        StudentFrame sf = new StudentFrame();
        sf.setVisible(true);
    }
}