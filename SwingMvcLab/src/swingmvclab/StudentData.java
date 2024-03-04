package swingmvclab;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/*
 * A hallgatók adatait tároló osztály.
 */
public class StudentData extends AbstractTableModel {

    /*
     * Ez a tagváltozó tárolja a hallgatói adatokat.
     * NE MÓDOSÍTSD!
     */
    List<Student> students = new ArrayList<Student>();


    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch(columnIndex){
            case 0:
                return student.getName();
            case 1:
                return student.getNeptun();
            case 2:
                return student.hasSignature();
            default:
                return student.getGrade();
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0:
                return "Név";
            case 1:
                return "Neptun";
            case 2:
                return "Aláírás";
            default:
                return "Jegy";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 0:
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            default:
                return Integer.class;

        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if( columnIndex == 2 || columnIndex == 3){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        if(columnIndex == 2){
            student.setSignature((Boolean) aValue);
        }else if(columnIndex == 3){
            student.setGrade((Integer) aValue);
        }
    }

    public void addStudent(String name, String neptun) {
        students.add(new Student(name, neptun, false, 0));
        fireTableRowsInserted(0, students.size() - 1);
    }
}