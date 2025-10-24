package modelo;


import java.util.*;

public class OrderModel {
    private Map<String, Order> orders;
    private List<OrderItem> menuItems;
    private int orderCounter;
    
    public OrderModel() {
        this.orders = new HashMap<>();
        this.menuItems = new ArrayList<>();
        this.orderCounter = 1;
        initializeMenu();
    }
    
    private void initializeMenu() {
        // Comidas
        menuItems.add(OrderItemFactory.createItem("food", "Hamburguesa Clásica", 12.99, "Principal", false));
        menuItems.add(OrderItemFactory.createItem("food", "Ensalada César", 8.99, "Entrada", true));
        menuItems.add(OrderItemFactory.createItem("food", "Pizza Margarita", 14.99, "Principal", true));
        menuItems.add(OrderItemFactory.createItem("food", "Sopa del Día", 6.99, "Entrada", true));
        menuItems.add(OrderItemFactory.createItem("food", "Pasta Alfredo", 11.99, "Principal", false));
        
        // Bebidas
        menuItems.add(OrderItemFactory.createItem("beverage", "Coca-Cola", 2.99, "Mediano", false));
        menuItems.add(OrderItemFactory.createItem("beverage", "Cerveza Artesanal", 5.99, "Pinta", true));
        menuItems.add(OrderItemFactory.createItem("beverage", "Jugo Natural", 3.99, "Grande", false));
        menuItems.add(OrderItemFactory.createItem("beverage", "Café Americano", 2.49, "Regular", false));
        menuItems.add(OrderItemFactory.createItem("beverage", "Agua Mineral", 1.99, "500ml", false));
    }
    
    public String createNewOrder(String customerName) {
        String orderId = "ORD-" + orderCounter++;
        orders.put(orderId, new Order(orderId, customerName));
        return orderId;
    }
    
    public boolean addItemToOrder(String orderId, OrderItem item) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.addItem(item);
            return true;
        }
        return false;
    }
    
    public boolean removeItemFromOrder(String orderId, int itemIndex) {
        Order order = orders.get(orderId);
        if (order != null && itemIndex >= 0 && itemIndex < order.getItems().size()) {
            order.removeItem(itemIndex);
            return true;
        }
        return false;
    }
    
    public boolean updateOrderStatus(String orderId, String newStatus) {
        Order order = orders.get(orderId);
        if (order != null) {
            return order.setStatus(newStatus);
        }
        return false;
    }
    
    public boolean processPayment(String orderId, String paymentMethod, double tip) {
        Order order = orders.get(orderId);
        if (order != null) {
            return order.processPayment(paymentMethod, tip);
        }
        return false;
    }
    
    public List<String> getAvailableStatusTransitions(String orderId) {
        Order order = orders.get(orderId);
        List<String> availableTransitions = new ArrayList<>();
        
        if (order != null) {
            String currentStatus = order.getStatus();
            
            switch (currentStatus) {
                case Order.PENDIENTE:
                    availableTransitions.add(Order.CONFIRMADO);
                    availableTransitions.add(Order.CANCELADO);
                    break;
                case Order.CONFIRMADO:
                    availableTransitions.add(Order.EN_PREPARACION);
                    availableTransitions.add(Order.CANCELADO);
                    break;
                case Order.EN_PREPARACION:
                    availableTransitions.add(Order.LISTO);
                    availableTransitions.add(Order.CANCELADO);
                    break;
                case Order.LISTO:
                    availableTransitions.add(Order.ENTREGADO);
                    break;
            }
        }
        
        return availableTransitions;
    }
    
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }
    
    public List<OrderItem> getMenuItems() {
        return new ArrayList<>(menuItems);
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
    
    public boolean orderExists(String orderId) {
        return orders.containsKey(orderId);
    }
}