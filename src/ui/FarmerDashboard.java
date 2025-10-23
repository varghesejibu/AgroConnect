package ui;

import model.*;
import dao.ProduceDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FarmerDashboard extends JFrame {
    private Farmer farmer;
    private DefaultListModel<String> produceListModel;
    private ProduceDAO produceDAO;
    private JList<String> list; // Make list a class field

    public FarmerDashboard(Farmer farmer) {
        this.farmer = farmer;
        this.produceDAO = new ProduceDAO(); 
        
        setTitle("Farmer Dashboard - " + farmer.getUsername());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        produceListModel = new DefaultListModel<>();
        list = new JList<>(produceListModel); // Initialize class field
        JScrollPane scroll = new JScrollPane(list);

        // --- Top Panel for Adding ---
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton addBtn = new JButton("Add Produce");

        addBtn.addActionListener(e -> {
            try {
                farmer.addProduce(nameField.getText(), Double.parseDouble(priceField.getText()));
                refreshList(); // Refreshes UI from the farmer object's list
                nameField.setText(""); 
                priceField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        JPanel top = new JPanel(new GridLayout(3, 2, 5, 5));
        top.add(new JLabel("Produce Name:"));
        top.add(nameField);
        top.add(new JLabel("Price:"));
        top.add(priceField);
        top.add(new JLabel());
        top.add(addBtn);

        // --- Bottom Panel for Deleting ---
        JButton deleteBtn = new JButton("Delete Selected");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(deleteBtn);

        // --- Add Action Listener for Delete Button ---
        deleteBtn.addActionListener(e -> handleDeleteProduce());

        // --- Add components to frame ---
        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH); // Add new panel

        // --- Load initial data ---
        loadFarmerProduce();
    }

    /**
     * Handles the logic for the "Delete Selected" button.
     */
    private void handleDeleteProduce() {
        int selectedIndex = list.getSelectedIndex();
        
        // Check if an item is actually selected
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Nothing Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the produce object from the local list
        Produce selectedProduce = farmer.getProduceList().get(selectedIndex);

        // Show confirmation dialog
        int choice = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete '" + selectedProduce.getName() + "'?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        // If they click YES
        if (choice == JOptionPane.YES_OPTION) {
            try {
                // 1. Delete from database
                produceDAO.deleteProduceById(selectedProduce.getId());
                
                // 2. Remove from local farmer list
                farmer.removeProduce(selectedProduce);
                
                // 3. Update the UI
                refreshList();
                
                JOptionPane.showMessageDialog(this, "Produce deleted successfully.");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting produce: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Loads this specific farmer's items from the database.
     */
    private void loadFarmerProduce() {
        try {
            // 1. Get produce from DB *only for this farmer*
            List<Produce> existingProduce = produceDAO.getProduceByFarmerId(farmer.getId());
            
            // 2. Load that list into the farmer object
            farmer.setProduceList(existingProduce);

            // 3. Refresh the UI to show the items
            refreshList();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading existing produce: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refreshes the UI list (JList)
     * using the data inside the farmer object's produceList.
     */
    private void refreshList() {
        produceListModel.clear();
        // This reads from farmer.getProduceList()
        for (Produce p : farmer.getProduceList()) {
            produceListModel.addElement(p.getDetails());
        }
    }
}