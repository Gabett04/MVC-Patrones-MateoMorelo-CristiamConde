package modelo;

public class BeverageItem implements OrderItem {
    private String name;
    private double price;
    private String size;
    private boolean isAlcoholic;
    
    public BeverageItem(String name, double price, String size, boolean isAlcoholic) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.isAlcoholic = isAlcoholic;
    }
    
    @Override public String getName() { return name; }
    @Override public double getPrice() { return price; }
    
    @Override
    public String getDescription() {
        return String.format("%s (%s)", name, size);
    }
    
    @Override
    public String getType() { return "BEBIDA"; }
    
    @Override
    public String getDetails() {
        return String.format("Tamaño: %s | %s", size, isAlcoholic ? "Alcohólica" : "No alcohólica");
    }
    
    public String getSize() { return size; }
    public boolean isAlcoholic() { return isAlcoholic; }
}

