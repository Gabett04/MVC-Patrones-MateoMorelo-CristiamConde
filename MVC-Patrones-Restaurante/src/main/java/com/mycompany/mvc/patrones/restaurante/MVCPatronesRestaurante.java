package com.mycompany.mvc.patrones.restaurante;

import modelo.OrderModel;
import vista.MainFrame;
import controlador.OrderController;
import javax.swing.*;

public class MVCPatronesRestaurante {
    public static void main(String[] args) {
        // Ejecutar en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Crear componentes MVC
                OrderModel model = new OrderModel();
                MainFrame view = new MainFrame();
                OrderController controller = new OrderController(model, view);
                
                // Mostrar la ventana principal
                view.setVisible(true);
                
                System.out.println("Aplicación iniciada correctamente");
                System.out.println("Patrones implementados:");
                System.out.println("- Factory Method (Modelo)");
                System.out.println("- Decorator (Vista)"); 
                System.out.println("- Command (Controlador)");
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error crítico al iniciar la aplicación: " + e.getMessage(), 
                    "Error de Inicialización", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}