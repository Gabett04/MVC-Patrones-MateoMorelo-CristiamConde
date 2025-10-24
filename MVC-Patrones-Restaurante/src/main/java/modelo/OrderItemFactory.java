package modelo;

public class OrderItemFactory {
    
    public static OrderItem createItem(String itemType, String name, double price, Object... additionalParams) {
        switch (itemType.toLowerCase()) {
            case "food":
                String category = (String) additionalParams[0];
                boolean isVegetarian = (Boolean) additionalParams[1];
                return new FoodItem(name, price, category, isVegetarian);
                
            case "beverage":
                String size = (String) additionalParams[0];
                boolean isAlcoholic = (Boolean) additionalParams[1];
                return new BeverageItem(name, price, size, isAlcoholic);
                
            default:
                throw new IllegalArgumentException("Tipo de item no soportado: " + itemType);
        }
    }
}