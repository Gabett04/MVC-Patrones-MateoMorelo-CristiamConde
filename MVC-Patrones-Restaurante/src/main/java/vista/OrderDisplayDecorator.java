package vista;

import modelo.Order;
import modelo.OrderItem;
import java.util.List;

public abstract class OrderDisplayDecorator implements OrderDisplay {
    protected OrderDisplay decoratedDisplay;
    
    public OrderDisplayDecorator(OrderDisplay decoratedDisplay) {
        this.decoratedDisplay = decoratedDisplay;
    }
    
    @Override
    public void updateOrderDisplay(Order order) {
        decoratedDisplay.updateOrderDisplay(order);
    }
    
    @Override
    public void updateMenuDisplay(List<OrderItem> menuItems) {
        decoratedDisplay.updateMenuDisplay(menuItems);
    }
    
    @Override
    public void updateOrdersList(List<Order> orders) {
        decoratedDisplay.updateOrdersList(orders);
    }
    
    @Override
    public void showMessage(String message) {
        decoratedDisplay.showMessage(message);
    }
    
    @Override
    public void showError(String error) {
        decoratedDisplay.showError(error);
    }
    
    @Override
    public void clearOrderDisplay() {
        decoratedDisplay.clearOrderDisplay();
    }
    
    @Override
    public String getCurrentOrderId() {
        return decoratedDisplay.getCurrentOrderId();
    }
}