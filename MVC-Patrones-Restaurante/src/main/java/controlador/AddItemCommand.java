package controlador;

import modelo.OrderModel;
import modelo.OrderItem;
import java.util.List;

public class AddItemCommand implements OrderCommand {
    private OrderModel model;
    private String orderId;
    private OrderItem item;
    private boolean executed;
    
    public AddItemCommand(OrderModel model, String orderId, int itemIndex) {
        this.model = model;
        this.orderId = orderId;
        List<OrderItem> menu = model.getMenuItems();
        this.item = menu.get(itemIndex);
        this.executed = false;
    }
    
    @Override
    public void execute() {
        if (model.addItemToOrder(orderId, item)) {
            executed = true;
        }
    }
    
    @Override
    public void undo() {
        if (executed) {
            // En una implementación real, llevaríamos registro del índice
            executed = false;
        }
    }
    
    @Override
    public String getDescription() {
        return "Agregar item: " + item.getName() + " al pedido " + orderId;
    }
}
