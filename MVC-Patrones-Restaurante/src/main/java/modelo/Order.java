package modelo;

import java.util.*;

public class Order {
    private String orderId;
    private List<OrderItem> items;
    private String status;
    private Date creationDate;
    private String customerName;
    private boolean paid;
    private double tip;
    private String paymentMethod;
    
    // Estados posibles del pedido
    public static final String PENDIENTE = "PENDIENTE";
    public static final String CONFIRMADO = "CONFIRMADO";
    public static final String EN_PREPARACION = "EN PREPARACIÓN";
    public static final String LISTO = "LISTO";
    public static final String ENTREGADO = "ENTREGADO";
    public static final String CANCELADO = "CANCELADO";
    
    public Order(String orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.status = PENDIENTE;
        this.creationDate = new Date();
        this.paid = false;
        this.tip = 0.0;
        this.paymentMethod = null;
    }
    
    public void addItem(OrderItem item) {
        items.add(item);
    }
    
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }
    
    public double calculateSubtotal() {
        return items.stream().mapToDouble(OrderItem::getPrice).sum();
    }
    
    public double calculateTax() {
        return calculateSubtotal() * 0.19; // 19% de IVA
    }
    
    public double calculateTotal() {
        return calculateSubtotal() + calculateTax() + tip;
    }
    
    public boolean canChangeStatus(String newStatus) {
        // Lógica de transición de estados
        switch (this.status) {
            case PENDIENTE:
                return newStatus.equals(CONFIRMADO) || newStatus.equals(CANCELADO);
            case CONFIRMADO:
                return newStatus.equals(EN_PREPARACION) || newStatus.equals(CANCELADO);
            case EN_PREPARACION:
                return newStatus.equals(LISTO) || newStatus.equals(CANCELADO);
            case LISTO:
                return newStatus.equals(ENTREGADO);
            case ENTREGADO:
                return false; // No se puede cambiar después de entregado
            case CANCELADO:
                return false; // No se puede cambiar después de cancelado
            default:
                return false;
        }
    }
    
    public boolean setStatus(String newStatus) {
        if (canChangeStatus(newStatus)) {
            this.status = newStatus;
            return true;
        }
        return false;
    }
    
    public boolean processPayment(String paymentMethod, double tip) {
        if (!paid && (status.equals(LISTO) || status.equals(ENTREGADO))) {
            this.paymentMethod = paymentMethod;
            this.tip = tip;
            this.paid = true;
            return true;
        }
        return false;
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public String getStatus() { return status; }
    public Date getCreationDate() { return creationDate; }
    public int getItemCount() { return items.size(); }
    public String getCustomerName() { return customerName; }
    public boolean isPaid() { return paid; }
    public double getTip() { return tip; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getSubtotal() { return calculateSubtotal(); }
    public double getTax() { return calculateTax(); }
}