package vista;

import modelo.Order;
import modelo.OrderItem;
import java.util.List;
import javax.swing.*;

public class BaseOrderDisplay implements OrderDisplay {
    private JTextArea orderTextArea;
    private JList<String> menuList;
    private JList<String> ordersList;
    private DefaultListModel<String> menuListModel;
    private DefaultListModel<String> ordersListModel;
    private JLabel totalLabel;
    private JLabel statusLabel;
    
    public BaseOrderDisplay(JTextArea orderTextArea, JList<String> menuList, JList<String> ordersList,
                          DefaultListModel<String> menuListModel, DefaultListModel<String> ordersListModel,
                          JLabel totalLabel, JLabel statusLabel) {
        this.orderTextArea = orderTextArea;
        this.menuList = menuList;
        this.ordersList = ordersList;
        this.menuListModel = menuListModel;
        this.ordersListModel = ordersListModel;
        this.totalLabel = totalLabel;
        this.statusLabel = statusLabel;
    }
    
    @Override
    public void updateOrderDisplay(Order order) {
        if (order != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("PEDIDO: ").append(order.getOrderId()).append("\n");
            sb.append("Cliente: ").append(order.getCustomerName()).append("\n");
            sb.append("Estado: ").append(order.getStatus()).append("\n");
            sb.append("Pago: ").append(order.isPaid() ? "PAGADO (" + order.getPaymentMethod() + ")" : "PENDIENTE").append("\n");
            sb.append("Fecha: ").append(order.getCreationDate()).append("\n\n");
            sb.append("ITEMS DEL PEDIDO:\n");
            sb.append("================\n");
            
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItem item = order.getItems().get(i);
                sb.append(String.format("%d. %s - $%.2f\n", i + 1, item.getDescription(), item.getPrice()));
                sb.append("   ").append(item.getDetails()).append("\n\n");
            }
            
            // Mostrar desglose de precios
            sb.append("DESGLOSE DE PAGO:\n");
            sb.append("────────────────\n");
            sb.append(String.format("Subtotal: $%.2f\n", order.getSubtotal()));
            sb.append(String.format("IVA (19%%): $%.2f\n", order.getTax()));
            sb.append(String.format("Propina: $%.2f\n", order.getTip()));
            sb.append(String.format("TOTAL: $%.2f\n", order.calculateTotal()));
            
            orderTextArea.setText(sb.toString());
            totalLabel.setText(String.format("$%.2f", order.calculateTotal()));
            statusLabel.setText(order.getStatus());
        } else {
            clearOrderDisplay();
        }
    }
    
    @Override
    public void updateMenuDisplay(List<OrderItem> menuItems) {
        menuListModel.clear();
        for (OrderItem item : menuItems) {
            String displayText = String.format("%s - $%.2f [%s]", 
                item.getDescription(), item.getPrice(), item.getType());
            menuListModel.addElement(displayText);
        }
    }
    
    @Override
    public void updateOrdersList(List<Order> orders) {
        ordersListModel.clear();
        for (Order order : orders) {
            String displayText = String.format("%s - %s - %s - $%.2f", 
                order.getOrderId(), order.getCustomerName(), order.getStatus(), order.calculateTotal());
            ordersListModel.addElement(displayText);
        }
    }
    
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void showError(String error) {
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    @Override
    public void clearOrderDisplay() {
        orderTextArea.setText("No hay pedido seleccionado\n\nSeleccione un pedido de la lista o cree uno nuevo.");
        totalLabel.setText("$0.00");
        statusLabel.setText("N/A");
    }
    
    @Override
    public String getCurrentOrderId() {
        return ""; // En esta implementación base, no manejamos el orderId
    }
}