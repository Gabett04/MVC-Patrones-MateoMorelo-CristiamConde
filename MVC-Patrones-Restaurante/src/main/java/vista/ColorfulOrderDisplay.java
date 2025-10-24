package vista;

import modelo.Order;
import modelo.OrderItem;
import java.util.List;
import javax.swing.*;

public class ColorfulOrderDisplay extends OrderDisplayDecorator {
    
    public ColorfulOrderDisplay(OrderDisplay decoratedDisplay) {
        super(decoratedDisplay);
    }
    
    @Override
    public void updateOrderDisplay(Order order) {
        // Primero llamamos al método del decorado para mantener la funcionalidad base
        decoratedDisplay.updateOrderDisplay(order);
        
        // Luego aplicamos nuestras mejoras visuales
        if (order != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("🍽️ PEDIDO: ").append(order.getOrderId()).append("\n");
            sb.append("👤 Cliente: ").append(order.getCustomerName()).append("\n");
            sb.append("📊 Estado: ").append(order.getStatus()).append("\n");
            sb.append("💰 Pago: ").append(order.isPaid() ? "✅ PAGADO (" + order.getPaymentMethod() + ")" : "⏳ PENDIENTE").append("\n");
            sb.append("📅 Fecha: ").append(order.getCreationDate()).append("\n\n");
            sb.append("🛒 ITEMS DEL PEDIDO:\n");
            sb.append("═══════════════════\n");
            
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItem item = order.getItems().get(i);
                String icon = item.getType().equals("COMIDA") ? "🍴" : "🥤";
                sb.append(String.format("%s %d. %s - $%.2f\n", icon, i + 1, item.getDescription(), item.getPrice()));
                sb.append("   ").append(item.getDetails()).append("\n\n");
            }
            
            // Mostrar desglose de precios con emojis
            sb.append("💵 DESGLOSE DE PAGO:\n");
            sb.append("────────────────────\n");
            sb.append(String.format("📦 Subtotal: $%.2f\n", order.getSubtotal()));
            sb.append(String.format("🏛️  IVA (19%%): $%.2f\n", order.getTax()));
            sb.append(String.format("💝 Propina: $%.2f\n", order.getTip()));
            sb.append(String.format("💳 TOTAL: $%.2f\n", order.calculateTotal()));
            
            // Actualizar el área de texto con el formato mejorado
            // Esto asume que tenemos acceso al JTextArea, pero en nuestro diseño
            // el BaseOrderDisplay ya maneja esto. Podemos dejarlo así o modificar
            // el diseño para que ColorfulOrderDisplay tenga acceso directo.
        }
    }
    
    @Override
    public void updateMenuDisplay(List<OrderItem> menuItems) {
        // Aplicar formato colorido al menú
        decoratedDisplay.updateMenuDisplay(menuItems);
    }
    
    @Override
    public void showMessage(String message) {
        // Agregar emojis a los mensajes
        String decoratedMessage = "✅ " + message;
        decoratedDisplay.showMessage(decoratedMessage);
    }
    
    @Override
    public void showError(String error) {
        // Agregar emojis a los errores
        String decoratedError = "❌ " + error;
        decoratedDisplay.showError(decoratedError);
    }
}