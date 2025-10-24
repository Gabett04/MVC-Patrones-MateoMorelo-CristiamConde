package modelo;

public class FoodItem implements OrderItem {
    private String name;
    private double price;
    private String category;
    private boolean isVegetarian;
    
    public FoodItem(String name, double price, String category, boolean isVegetarian) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.isVegetarian = isVegetarian;
    }
    
    @Override public String getName() { return name; }
    @Override public double getPrice() { return price; }
    
    @Override
    public String getDescription() {
        return String.format("%s (%s)", name, category);
    }
    
    @Override
    public String getType() { return "COMIDA"; }
    
    @Override
    public String getDetails() {
        return String.format("Categoría: %s | %s", category, isVegetarian ? "Vegetariano" : "No vegetariano");
    }
    
    public String getCategory() { return category; }
    public boolean isVegetarian() { return isVegetarian; }
}
