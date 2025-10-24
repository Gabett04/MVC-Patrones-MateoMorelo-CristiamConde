package vista;

import modelo.Order;
import modelo.OrderItem;
import java.util.List;

public interface OrderDisplay {
    void updateOrderDisplay(Order order);
    void updateMenuDisplay(List<OrderItem> menuItems);
    void updateOrdersList(List<Order> orders);
    void showMessage(String message);
    void showError(String error);
    void clearOrderDisplay();
    String getCurrentOrderId();
}