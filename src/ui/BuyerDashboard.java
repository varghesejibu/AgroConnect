package ui;

import model.*;
import dao.ProduceDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class BuyerDashboard extends JFrame {
    private Buyer buyer;
    private List<Produce> market = new ArrayList<>();
    private ProduceDAO produceDAO;
    private DefaultListModel<String> model;
    private JList<String> list;
    private JTextField qtyField;

    public BuyerDashboard(Buyer buyer) {
        this.buyer = buyer;
        this.produceDAO = new ProduceDAO();
        setTitle("Buyer Dashboard - " + buyer.getUsername());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        model = new DefaultListModel<>();
        list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        
        // --- 1. "Add to Cart" Panel (at the top of the bottom) ---
        JPanel addPanel = new JPanel(new FlowLayout());
        qtyField = new JTextField(5); // Set a preferred size
        JButton addBtn = new JButton("Add to Cart");

        addPanel.add(new JLabel("Quantity:"));
        addPanel.add(qtyField);
        addPanel.add(addBtn);

        // --- 2. "Place Order" Button (at the bottom of the bottom) ---
        JButton placeOrderBtn = new JButton("View Cart & Place Order");

        // --- Add sub-panels to the main bottom panel ---
        bottomPanel.add(addPanel, BorderLayout.CENTER);
        bottomPanel.add(placeOrderBtn, BorderLayout.SOUTH);
        
        // --- 3. "Add to Cart" Button Logic ---
        addBtn.addActionListener(e -> handleAddToCart());
        
        // --- 4. "Place Order" Button Logic ---
        placeOrderBtn.addActionListener(e -> handlePlaceOrder());

        // --- Add components to frame ---
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadProduceItems();
    }

    /**
     * Logic for the "Add to Cart" button
     */
    private void handleAddToCart() {
        int index = list.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item to add.");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyField.getText());
            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive.");
                return;
            }

            // Get the selected produce
            Produce selectedProduce = market.get(index);
            
            // Add to the buyer's cart
            buyer.getCart().addItem(selectedProduce, qty);
            
            JOptionPane.showMessageDialog(this, "Added to cart!");
            qtyField.setText(""); // Clear quantity field
            list.clearSelection(); // Deselect item

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
        }
    }

    /**
     * Logic for the "View Cart & Place Order" button
     */
    private void handlePlaceOrder() {
        Cart cart = buyer.getCart();

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        // 1. Build the summary string
        StringBuilder summary = new StringBuilder("--- Your Order ---\n\n");
        for (CartItem item : cart.getItems()) {
            Produce p = item.getProduce();
            summary.append(String.format("%s (%d x ₹%.2f) = ₹%.2f\n",
                p.getName(),
                item.getQuantity(),
                p.getPrice(),
                item.getItemTotal()
            ));
        }
        summary.append("\n-------------------\n");
        summary.append(String.format("GRAND TOTAL: ₹%.2f\n\n", cart.calculateGrandTotal()));
        summary.append("Confirm order?");

        // 2. Show confirmation dialog
        int choice = JOptionPane.showConfirmDialog(
            this,
            summary.toString(),
            "Confirm Your Order",
            JOptionPane.YES_NO_OPTION
        );

        // 3. If "YES", place the order
        if (choice == JOptionPane.YES_OPTION) {
            
            // (This is where you would add code to save the order to a new 'orders' table in your DB)
            // For now, we'll just clear the cart.

            cart.clearCart();
            JOptionPane.showMessageDialog(this, "Order placed successfully!");
        }
    }

    /**
     * Fetches produce from the database
     */
    private void loadProduceItems() {
        model.clear();
        market.clear();

        try {
            List<Produce> itemsFromDB = produceDAO.getAllProduce();
            for (Produce p : itemsFromDB) {
                market.add(p); // Add to the backing list
                model.addElement(p.getDetails()); // Add to the UI JList
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading produce from database: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}