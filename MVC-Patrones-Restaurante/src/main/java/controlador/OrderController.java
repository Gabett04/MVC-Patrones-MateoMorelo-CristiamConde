package controlador;

import modelo.OrderModel;
import vista.MainFrame;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class OrderController {
    private OrderModel model;
    private MainFrame view;
    private CommandInvoker invoker;
    private String currentOrderId;
    
    public OrderController(OrderModel model, MainFrame view) {
        this.model = model;
        this.view = view;
        this.invoker = new CommandInvoker();
        this.view.setController(this);
        initializeView();
    }
    
    private void initializeView() {
        refreshMenu();
        refreshOrders();
        view.clearOrderDisplay();
    }
    
    public void createNewOrder(String customerName) {
        CreateOrderCommand command = new CreateOrderCommand(model, customerName);
        invoker.executeCommand(command);
        currentOrderId = command.getOrderId();
        view.setCurrentOrderId(currentOrderId);
        view.showMessage("Pedido creado exitosamente: " + currentOrderId);
        refreshOrders();
        view.updateOrderDisplay(model.getOrder(currentOrderId));
    }
    
    public void addItemToOrder(int itemIndex) {
        if (currentOrderId == null) {
            view.showError("Primero debe crear un pedido");
            return;
        }
        
        AddItemCommand command = new AddItemCommand(model, currentOrderId, itemIndex);
        invoker.executeCommand(command);
        view.showMessage("Item agregado al pedido");
        view.updateOrderDisplay(model.getOrder(currentOrderId));
    }
    
    public void removeLastItem() {
        if (currentOrderId == null) {
            view.showError("No hay pedido activo");
            return;
        }
        view.showMessage("Funcionalidad de eliminar item en desarrollo");
    }
    
    public void updateOrderStatus(String newStatus) {
        if (currentOrderId == null) {
            view.showError("No hay pedido activo");
            return;
        }
        
        if (model.updateOrderStatus(currentOrderId, newStatus)) {
            view.showMessage("Estado actualizado a: " + newStatus);
            view.updateOrderDisplay(model.getOrder(currentOrderId));
            refreshOrders();
        } else {
            view.showError("Error al actualizar el estado - Transición no válida");
        }
    }
    
    public void undoLastOperation() {
        if (invoker.canUndo()) {
            invoker.undo();
            view.showMessage("Última operación deshecha");
            if (currentOrderId != null) {
                view.updateOrderDisplay(model.getOrder(currentOrderId));
            }
        } else {
            view.showError("No hay operaciones para deshacer");
        }
    }
    
    public void refreshMenu() {
        List<modelo.OrderItem> menu = model.getMenuItems();
        view.updateMenuDisplay(menu);
    }
    
    public void refreshOrders() {
        List<modelo.Order> orders = model.getAllOrders();
        view.updateOrdersList(orders);
    }
    
    public void viewOrder(String orderId) {
        modelo.Order order = model.getOrder(orderId);
        if (order != null) {
            currentOrderId = orderId;
            view.setCurrentOrderId(orderId);
            view.updateOrderDisplay(order);
        } else {
            view.showError("Pedido no encontrado");
        }
    }
    
    public void processPayment(String orderId, String paymentMethod, double tip) {
        ProcessPaymentCommand paymentCommand = new ProcessPaymentCommand(model, orderId, paymentMethod, tip);
        invoker.executeCommand(paymentCommand);
        
        if (paymentCommand.wasExecuted()) {
            view.showMessage("Pago procesado exitosamente: " + paymentMethod);
            view.updateOrderDisplay(model.getOrder(orderId));
        } else {
            view.showError("No se pudo procesar el pago. Verifique que el pedido esté LISTO o ENTREGADO");
        }
    }
    
    public void showInvoice() {
        if (currentOrderId != null) {
            modelo.Order order = model.getOrder(currentOrderId);
            if (order != null && order.isPaid()) {
                StringBuilder invoice = new StringBuilder();
                invoice.append("════════════ FACTURA ════════════\n");
                invoice.append("Pedido: ").append(order.getOrderId()).append("\n");
                invoice.append("Cliente: ").append(order.getCustomerName()).append("\n");
                invoice.append("Fecha: ").append(order.getCreationDate()).append("\n");
                invoice.append("Método de Pago: ").append(order.getPaymentMethod()).append("\n\n");
                
                invoice.append("ITEMS:\n");
                for (modelo.OrderItem item : order.getItems()) {
                    invoice.append(String.format("  %s - $%.2f\n", item.getDescription(), item.getPrice()));
                }
                
                invoice.append("\nDESGLOSE:\n");
                invoice.append(String.format("  Subtotal: $%.2f\n", order.getSubtotal()));
                invoice.append(String.format("  IVA (19%%): $%.2f\n", order.getTax()));
                invoice.append(String.format("  Propina: $%.2f\n", order.getTip()));
                invoice.append(String.format("  TOTAL: $%.2f\n", order.calculateTotal()));
                invoice.append("══════════════════════════════════\n");
                
                JTextArea textArea = new JTextArea(invoice.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                
                JOptionPane.showMessageDialog(null, scrollPane, "Factura - " + order.getOrderId(), JOptionPane.INFORMATION_MESSAGE);
            } else {
                view.showError("El pedido no ha sido pagado aún");
            }
        } else {
            view.showError("No hay pedido seleccionado");
        }
    }
    
    public List<String> getAvailableStatusTransitions(String orderId) {
        return model.getAvailableStatusTransitions(orderId);
    }
    
    public String getCurrentOrderId() {
        return currentOrderId;
    }
}