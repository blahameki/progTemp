package viseletRaktar;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class AdminMenu extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7326932609755718832L;

	// Segédfüggvény a ResultSet átalakításához TableModel formába
	public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
	    ResultSetMetaData metaData = resultSet.getMetaData();

	    // Táblázat oszlopneveinek megszerzése
	    int columnCount = metaData.getColumnCount();
	    Vector<String> columnNames = new Vector<>();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // Táblázat adatainak átalakítása
	    Vector<Vector<Object>> data = new Vector<>();
	    while (resultSet.next()) {
	        Vector<Object> row = new Vector<>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            row.add(resultSet.getObject(columnIndex));
	        }
	        data.add(row);
	    }

	    return new DefaultTableModel(data, columnNames);
	}

    public AdminMenu() {
        setTitle("Admin Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
     // Két gomb létrehozása: "raktár" és "felhasználók"
        JButton raktarButton = new JButton("Raktár");
        JButton felhasznalokButton = new JButton("Felhasználók");

        // A menü elrendezése GridLayout segítségével
        JPanel menuPanel = new JPanel(new GridLayout(2, 1));
        menuPanel.add(raktarButton);
        menuPanel.add(felhasznalokButton);

        add(menuPanel, BorderLayout.CENTER);

        // Raktár gomb eseménykezelője
        raktarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Felugró ablak létrehozása a raktárszerkesztési funkciókkal
            	AdminMenu.this.dispose();
            	JFrame raktarFrame = new JFrame("Raktár");
                raktarFrame.setSize(400, 300);
                raktarFrame.setLayout(new GridLayout(3, 1));

                JButton addNewItemButton = new JButton("Új viselet hozzáadása");
                JButton deleteItemButton = new JButton("Viselet törlése");
                JButton editQuantityButton = new JButton("Darabszám változtatás");
                JButton listItemsButton = new JButton("Viseletek listázása");
                JButton visszaButton = new JButton("Vissza");
                

                JPanel raktarMenuPanel = new JPanel(new GridLayout(3, 1));
                raktarMenuPanel.add(addNewItemButton);
                raktarMenuPanel.add(deleteItemButton);
                raktarMenuPanel.add(editQuantityButton);
                raktarMenuPanel.add(listItemsButton);
                raktarMenuPanel.add(visszaButton);

                raktarFrame.add(raktarMenuPanel);
                raktarFrame.setVisible(true);
	
	        // Gombok eseménykezelői
	        addNewItemButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // Felugró ablak létrehozása a viselet nevének bekérésére
	                String viseletNev = JOptionPane.showInputDialog(null, "Írd be a viselet nevét:");
	
	                // Adatbáziskapcsolat inicializálása
	                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
	                    // Ellenőrizze, hogy a viselet már létezik-e az adatbázisban
	                    String checkQuery = "SELECT * FROM raktar WHERE nev = ?";
	                    try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
	                        checkStatement.setString(1, viseletNev);
	                        ResultSet resultSet = checkStatement.executeQuery();
	                        if (resultSet.next()) {
	                            JOptionPane.showMessageDialog(null, "Ez a viselet már létezik a rendszerben!");
	                        } else {
	                            // Ha nem létezik, kérje be a darabszámot
	                            int darab = Integer.parseInt(JOptionPane.showInputDialog(null, "Írd be a darabszámot:"));
	                            // Majd hozzáadja az új viseletet az adatbázishoz
	                            String insertQuery = "INSERT INTO raktar (nev, darab, bent) VALUES (?, ?, ?)";
	                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
	                                preparedStatement.setString(1, viseletNev);
	                                preparedStatement.setInt(2, darab);
	                                preparedStatement.setInt(3, darab); // Bent lévő viseletek száma az új elem hozzáadásakor
	
	                                preparedStatement.executeUpdate();
	                                JOptionPane.showMessageDialog(null, "Új viselet hozzáadva!");
	                            }
	                        }
	                    }
	                } catch (SQLException | NumberFormatException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(null, "Hiba történt az új viselet hozzáadása közben.");
	                }
	            }
	        });
	
	        deleteItemButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // Felugró ablak létrehozása a törölni kívánt viselet nevének bekérésére
	                String viseletNev = JOptionPane.showInputDialog(null, "Írd be a törlendő viselet nevét:");
	
	                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
	                    // Ellenőrizze, hogy létezik-e a viselet az adatbázisban
	                    String checkQuery = "SELECT * FROM raktar WHERE nev = ?";
	                    try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
	                        checkStatement.setString(1, viseletNev);
	                        ResultSet resultSet = checkStatement.executeQuery();
	                        if (!resultSet.next()) {
	                            JOptionPane.showMessageDialog(null, "A viselet nem létezik a raktárban!");
	                        } else {
	                            // Ellenőrizze, hogy a darabszám megegyezik-e a bent lévő viseletek számával
	                            int darab = resultSet.getInt("darab");
	                            int bent = resultSet.getInt("bent");
	                            if (darab != bent) {
	                                JOptionPane.showMessageDialog(null, "Csak akkor lehet viseletet törölni, ha a raktárban van az összes darab!");
	                            } else {
	                                // Viselet törlése az adatbázisból
	                                String deleteQuery = "DELETE FROM raktar WHERE nev = ?";
	                                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
	                                    deleteStatement.setString(1, viseletNev);
	                                    deleteStatement.executeUpdate();
	                                    JOptionPane.showMessageDialog(null, "Viselet törölve!");
	                                }
	                            }
	                        }
	                    }
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(null, "Hiba történt a viselet törlése közben.");
	                }
	            }
	        });
	
	        editQuantityButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // Felugró ablak létrehozása a módosítani kívánt viselet nevének bekérésére
	                String viseletNev = JOptionPane.showInputDialog(null, "Írd be a viselet nevét:");
	                if (viseletNev == null || viseletNev.isEmpty()) {
	                    JOptionPane.showMessageDialog(null, "Érvénytelen viseletnév!");
	                    return;
	                }
	
	                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
	                    // Ellenőrizzük, hogy létezik-e a megadott viselet
	                    String checkQuery = "SELECT * FROM raktar WHERE nev = ?";
	                    try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
	                        checkStatement.setString(1, viseletNev);
	                        ResultSet resultSet = checkStatement.executeQuery();
	                        if (!resultSet.next()) {
	                            JOptionPane.showMessageDialog(null, "Nem létezik ilyen nevű viselet!");
	                            return;
	                        }
	                    }
	
	                    // Felugró ablak létrehozása a darabszám módosításának típusának bekérésére (+ vagy - gombbal)
	                    Object[] options = {"+", "-"};
	                    int operationChoice = JOptionPane.showOptionDialog(null, "Darabszám módosítása:", "Művelet kiválasztása",
	                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	                    if (operationChoice == -1) {
	                        return;
	                    }
	
	                    String operation = (operationChoice == 0) ? "+" : "-";
	
	                    // Felugró ablak létrehozása a módosítandó darabszám bekérésére
	                    int change;
	                    try {
	                        String input = JOptionPane.showInputDialog(null, "Írd be a módosítandó darabszámot:");
	                        change = Integer.parseInt(input);
	                    } catch (NumberFormatException ex) {
	                        JOptionPane.showMessageDialog(null, "Érvénytelen szám!");
	                        return;
	                    }
	
	                    // Ellenőrizzük, hogy a módosítás nem viszi-e a bent lévő viseletek számát 0 alá
	                    String checkBentQuery = "SELECT bent FROM raktar WHERE nev = ?";
	                    try (PreparedStatement checkBentStatement = connection.prepareStatement(checkBentQuery)) {
	                        checkBentStatement.setString(1, viseletNev);
	                        ResultSet bentResultSet = checkBentStatement.executeQuery();
	                        if (bentResultSet.next()) {
	                            int bent = bentResultSet.getInt("bent");
	                            if (operation.equals("-") && bent - change < 0) {
	                                JOptionPane.showMessageDialog(null, "A módosítás nem lehetséges, mert a bent lévő viseletek száma nem engedi ezt!");
	                                return;
	                            }
	                        }
	                    }
	
	                    // Módosítás végrehajtása az adatbázisban
	                    String updateQuery = (operation.equals("+")) ? "UPDATE raktar SET darab = darab + ?, bent = bent + ? WHERE nev = ?" : "UPDATE raktar SET darab = darab - ?, bent = bent - ? WHERE nev = ?";
	                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
	                        updateStatement.setInt(1, change);
	                        updateStatement.setInt(2, change);
	                        updateStatement.setString(3, viseletNev);
	                        updateStatement.executeUpdate();
	                        JOptionPane.showMessageDialog(null, "Darabszám módosítva!");
	                    }
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(null, "Hiba történt a darabszám módosítása közben.");
	                }
	            }
	        });
	        listItemsButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                JFrame listFrame = new JFrame("Viseletek listája");

	                // Adatok lekérdezése adatbázisból
	                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
	                    String query = "SELECT * FROM raktar";
	                    try (PreparedStatement statement = connection.prepareStatement(query)) {
	                        ResultSet resultSet = statement.executeQuery();

	                        // Táblázat létrehozása az adatokkal
	                        JTable table = new JTable(buildTableModel(resultSet));
	                        JScrollPane scrollPane = new JScrollPane(table);

	                        listFrame.add(scrollPane);
	                        listFrame.setSize(600, 400);
	                        listFrame.setLocationRelativeTo(null);
	                        listFrame.setVisible(true);
	                    }
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(null, "Hiba történt a viseletek lekérdezése közben.");
	                }
	            }
	        });
	        
	        visszaButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	raktarFrame.dispose(); // Raktár ablak bezárása

	                // Új példány létrehozása az AdminMenu-ból és megjelenítése
	                AdminMenu adminMenu = new AdminMenu();
	                adminMenu.setVisible(true);
	            }
	        });
            }
        });
        
     // Felhasználók gomb eseménykezelője
        felhasznalokButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Felhasználók listázása
                JFrame felhasznaloFrame = new JFrame("Felhasználók listája");

                // Adatok lekérdezése adatbázisból
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
                    String query = "SELECT * FROM users";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        ResultSet resultSet = statement.executeQuery();

                        // Táblázat létrehozása az adatokkal
                        JTable table = new JTable(buildTableModel(resultSet));
                        JScrollPane scrollPane = new JScrollPane(table);

                        felhasznaloFrame.add(scrollPane);
                        felhasznaloFrame.setSize(600, 400);
                        felhasznaloFrame.setLocationRelativeTo(null);
                        felhasznaloFrame.setVisible(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Hiba történt a felhasználók lekérdezése közben.");
                }
            }
        });
    }
    
}