package controlador;

import modelo.OrderModel;

public class ProcessPaymentCommand implements OrderCommand {
    private OrderModel model;
    private String orderId;
    private String paymentMethod;
    private double tip;
    private boolean executed;
    
    public ProcessPaymentCommand(OrderModel model, String orderId, String paymentMethod, double tip) {
        this.model = model;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.tip = tip;
        this.executed = false;
    }
    
    @Override
    public void execute() {
        if (model.processPayment(orderId, paymentMethod, tip)) {
            executed = true;
        }
    }
    
    @Override
    public void undo() {
        if (executed) {
            // En una implementación real, revertiríamos el pago
            executed = false;
        }
    }
    
    @Override
    public String getDescription() {
        return "Procesar pago para orden " + orderId + " - Método: " + paymentMethod;
    }
    
    public boolean wasExecuted() {
        return executed;
    }
}