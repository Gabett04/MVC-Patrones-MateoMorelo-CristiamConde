package vista;

import controlador.OrderController;
import modelo.Order;
import modelo.OrderItem;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainFrame extends JFrame implements OrderDisplay {
    private OrderController controller;
    private OrderDisplay display; // ✅ Variable para el patrón Decorator
    
    // Componentes de la interfaz
    private JTextArea orderTextArea;
    private JList<String> menuList;
    private JList<String> ordersList;
    private DefaultListModel<String> menuListModel;
    private DefaultListModel<String> ordersListModel;
    private JLabel totalLabel;
    private JLabel statusLabel;
    private JTextField customerField;
    private JTextField orderIdField;
    private JComboBox<String> statusComboBox;
    
    public MainFrame() {
        initializeComponents();
        setupDisplayDecorator(); // ✅ Configurar el patrón Decorator
        setupLayout();
        setupEventHandlers();
    }
    
    public void setController(OrderController controller) {
        this.controller = controller;
    }
    
    private void initializeComponents() {
        setTitle("Sistema de Gestión de Restaurante - MVC + Patrones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Inicializar modelos de lista
        menuListModel = new DefaultListModel<>();
        ordersListModel = new DefaultListModel<>();
        
        // Crear componentes
        orderTextArea = new JTextArea(15, 30);
        orderTextArea.setEditable(false);
        orderTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        menuList = new JList<>(menuListModel);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        ordersList = new JList<>(ordersListModel);
        ordersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        totalLabel = new JLabel("$0.00", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(Color.BLUE);
        
        statusLabel = new JLabel("N/A", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.LIGHT_GRAY);
        
        customerField = new JTextField(15);
        orderIdField = new JTextField(10);
        orderIdField.setEditable(false);
        
        statusComboBox = new JComboBox<>();
    }
    
    // ✅ MÉTODO NUEVO: Configurar el patrón Decorator
    private void setupDisplayDecorator() {
        // Crear el display base
        OrderDisplay baseDisplay = new BaseOrderDisplay(
            orderTextArea, menuList, ordersList,
            menuListModel, ordersListModel, totalLabel, statusLabel
        );
        
        // Aplicar el decorator colorido
        this.display = new ColorfulOrderDisplay(baseDisplay);
    }
    
    private void setupLayout() {
        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña 1: Gestión de Pedidos
        tabbedPane.addTab("Gestión de Pedidos", createOrderManagementPanel());
        
        // Pestaña 2: Menú y Órdenes
        tabbedPane.addTab("Menú y Órdenes", createMenuAndOrdersPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createOrderManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel superior - Crear pedido
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new TitledBorder("Crear Nuevo Pedido"));
        topPanel.add(new JLabel("Nombre del Cliente:"));
        topPanel.add(customerField);
        
        JButton createOrderBtn = new JButton("Crear Pedido");
        createOrderBtn.addActionListener(e -> createNewOrder());
        topPanel.add(createOrderBtn);
        
        // Panel central - Información del pedido
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // Panel de items del pedido
        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(new TitledBorder("Detalles del Pedido"));
        orderPanel.add(new JScrollPane(orderTextArea), BorderLayout.CENTER);
        
        // Panel de información resumida
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        infoPanel.setBorder(new TitledBorder("Información del Pedido"));
        infoPanel.add(new JLabel("Total:"));
        infoPanel.add(totalLabel);
        infoPanel.add(new JLabel("Estado:"));
        infoPanel.add(statusLabel);
        
        orderPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Panel de controles
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBorder(new TitledBorder("Acciones"));
        
        JButton addItemBtn = new JButton("Agregar Item Seleccionado");
        JButton removeItemBtn = new JButton("Eliminar Último Item");
        JButton updateStatusBtn = new JButton("Actualizar Estado");
        JButton undoBtn = new JButton("Deshacer");
        
        addItemBtn.addActionListener(e -> addSelectedItemToOrder());
        removeItemBtn.addActionListener(e -> removeLastItem());
        updateStatusBtn.addActionListener(e -> updateOrderStatus());
        undoBtn.addActionListener(e -> undoLastOperation());
        
        controlPanel.add(addItemBtn);
        controlPanel.add(removeItemBtn);
        controlPanel.add(new JLabel("Nuevo Estado:"));
        controlPanel.add(statusComboBox);
        controlPanel.add(updateStatusBtn);
        controlPanel.add(undoBtn);
        
        // Botones de pago
        controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
        JButton processPaymentBtn = new JButton("Procesar Pago");
        JButton showInvoiceBtn = new JButton("Ver Factura");
        processPaymentBtn.addActionListener(e -> processPayment());
        showInvoiceBtn.addActionListener(e -> showInvoice());
        controlPanel.add(processPaymentBtn);
        controlPanel.add(showInvoiceBtn);
        
        centerPanel.add(orderPanel, BorderLayout.CENTER);
        centerPanel.add(controlPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMenuAndOrdersPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel del menú
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBorder(new TitledBorder("Menú del Restaurante"));
        menuPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);
        
        JPanel menuInfoPanel = new JPanel(new FlowLayout());
        JButton refreshMenuBtn = new JButton("Actualizar Menú");
        refreshMenuBtn.addActionListener(e -> refreshMenu());
        menuInfoPanel.add(refreshMenuBtn);
        menuPanel.add(menuInfoPanel, BorderLayout.SOUTH);
        
        // Panel de órdenes existentes
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBorder(new TitledBorder("Órdenes Existentes"));
        ordersPanel.add(new JScrollPane(ordersList), BorderLayout.CENTER);
        
        JPanel ordersControlPanel = new JPanel(new FlowLayout());
        JButton refreshOrdersBtn = new JButton("Actualizar Lista");
        JButton viewOrderBtn = new JButton("Ver Pedido Seleccionado");
        
        refreshOrdersBtn.addActionListener(e -> refreshOrders());
        viewOrderBtn.addActionListener(e -> viewSelectedOrder());
        
        ordersControlPanel.add(refreshOrdersBtn);
        ordersControlPanel.add(viewOrderBtn);
        ordersPanel.add(ordersControlPanel, BorderLayout.SOUTH);
        
        panel.add(menuPanel);
        panel.add(ordersPanel);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Doble click en la lista de menú para agregar item
        menuList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    addSelectedItemToOrder();
                }
            }
        });
        
        // Doble click en la lista de órdenes para ver
        ordersList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewSelectedOrder();
                }
            }
        });
    }
    
    // ✅ MÉTODOS DE LA INTERFAZ OrderDisplay - DELEGADOS AL DISPLAY DECORADO
    @Override
    public void updateOrderDisplay(Order order) {
        display.updateOrderDisplay(order);
        
        // Mantener la lógica específica de colores del estado y actualización del ComboBox
        if (order != null) {
            // Cambiar color según estado
            switch (order.getStatus()) {
                case "PENDIENTE":
                    statusLabel.setBackground(Color.YELLOW);
                    statusLabel.setForeground(Color.BLACK);
                    break;
                case "CONFIRMADO":
                    statusLabel.setBackground(Color.ORANGE);
                    statusLabel.setForeground(Color.BLACK);
                    break;
                case "EN PREPARACIÓN":
                    statusLabel.setBackground(Color.BLUE);
                    statusLabel.setForeground(Color.WHITE);
                    break;
                case "LISTO":
                    statusLabel.setBackground(Color.GREEN);
                    statusLabel.setForeground(Color.BLACK);
                    break;
                case "ENTREGADO":
                    statusLabel.setBackground(Color.GRAY);
                    statusLabel.setForeground(Color.WHITE);
                    break;
                case "CANCELADO":
                    statusLabel.setBackground(Color.RED);
                    statusLabel.setForeground(Color.WHITE);
                    break;
                default:
                    statusLabel.setBackground(Color.LIGHT_GRAY);
                    statusLabel.setForeground(Color.BLACK);
            }
            
            // Actualizar el ComboBox de estados
            updateStatusComboBox(order.getOrderId());
            orderIdField.setText(order.getOrderId());
        }
    }
    
    @Override
    public void updateMenuDisplay(List<OrderItem> menuItems) {
        display.updateMenuDisplay(menuItems);
    }
    
    @Override
    public void updateOrdersList(List<Order> orders) {
        display.updateOrdersList(orders);
    }
    
    @Override
    public void showMessage(String message) {
        display.showMessage(message);
    }
    
    @Override
    public void showError(String error) {
        display.showError(error);
    }
    
    @Override
    public void clearOrderDisplay() {
        display.clearOrderDisplay();
        statusComboBox.removeAllItems();
        orderIdField.setText("");
    }
    
    @Override
    public String getCurrentOrderId() {
        return orderIdField.getText();
    }
    
    // Métodos de manejo de eventos
    private void createNewOrder() {
        String customerName = customerField.getText().trim();
        if (customerName.isEmpty()) {
            showError("Por favor ingrese el nombre del cliente");
            return;
        }
        if (controller != null) {
            controller.createNewOrder(customerName);
            customerField.setText("");
        }
    }
    
    private void addSelectedItemToOrder() {
        int selectedIndex = menuList.getSelectedIndex();
        if (selectedIndex != -1 && controller != null) {
            controller.addItemToOrder(selectedIndex);
        } else {
            showError("Seleccione un item del menú primero");
        }
    }
    
    private void removeLastItem() {
        if (controller != null) {
            controller.removeLastItem();
        }
    }
    
    private void updateOrderStatus() {
        String newStatus = (String) statusComboBox.getSelectedItem();
        if (newStatus != null && !newStatus.equals("NO HAY TRANSICIONES DISPONIBLES") && controller != null) {
            controller.updateOrderStatus(newStatus);
        }
    }
    
    private void undoLastOperation() {
        if (controller != null) {
            controller.undoLastOperation();
        }
    }
    
    private void refreshMenu() {
        if (controller != null) {
            controller.refreshMenu();
        }
    }
    
    private void refreshOrders() {
        if (controller != null) {
            controller.refreshOrders();
        }
    }
    
    private void viewSelectedOrder() {
        int selectedIndex = ordersList.getSelectedIndex();
        if (selectedIndex != -1 && controller != null) {
            String selectedValue = ordersListModel.getElementAt(selectedIndex);
            String orderId = selectedValue.split(" - ")[0];
            controller.viewOrder(orderId);
        }
    }
    
    private void processPayment() {
        if (controller != null && controller.getCurrentOrderId() != null) {
            String orderId = controller.getCurrentOrderId();
            
            // Diálogo para seleccionar método de pago
            String[] paymentMethods = {"EFECTIVO", "TARJETA CRÉDITO", "TARJETA DÉBITO", "TRANSFERENCIA"};
            String paymentMethod = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione método de pago:",
                "Procesar Pago",
                JOptionPane.QUESTION_MESSAGE,
                null,
                paymentMethods,
                paymentMethods[0]
            );
            
            if (paymentMethod != null) {
                // Diálogo para propina
                String tipInput = JOptionPane.showInputDialog(
                    this,
                    "Ingrese propina:",
                    "0.0"
                );
                
                try {
                    double tip = tipInput != null ? Double.parseDouble(tipInput) : 0.0;
                    controller.processPayment(orderId, paymentMethod, tip);
                } catch (NumberFormatException e) {
                    showError("Propina debe ser un número válido");
                }
            }
        } else {
            showError("No hay pedido seleccionado para procesar pago");
        }
    }
    
    private void showInvoice() {
        if (controller != null) {
            controller.showInvoice();
        }
    }
    
    private void updateStatusComboBox(String orderId) {
        if (controller != null && orderId != null) {
            java.util.List<String> availableStatuses = controller.getAvailableStatusTransitions(orderId);
            statusComboBox.removeAllItems();
            
            for (String status : availableStatuses) {
                statusComboBox.addItem(status);
            }
            
            if (availableStatuses.isEmpty()) {
                statusComboBox.addItem("NO HAY TRANSICIONES DISPONIBLES");
            }
        }
    }
    
    public void setCurrentOrderId(String orderId) {
        orderIdField.setText(orderId);
    }
}