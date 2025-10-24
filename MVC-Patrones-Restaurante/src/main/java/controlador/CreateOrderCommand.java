package controlador;

import modelo.OrderModel;

public class CreateOrderCommand implements OrderCommand {
    private OrderModel model;
    private String customerName;
    private String orderId;
    private boolean executed;
    
    public CreateOrderCommand(OrderModel model, String customerName) {
        this.model = model;
        this.customerName = customerName;
        this.executed = false;
    }
    
    @Override
    public void execute() {
        this.orderId = model.createNewOrder(customerName);
        executed = true;
    }
    
    @Override
    public void undo() {
        if (executed && orderId != null) {
            executed = false;
        }
    }
    
    @Override
    public String getDescription() {
        return "Crear pedido para: " + customerName;
    }
    
    public String getOrderId() {
        return orderId;
    }
}