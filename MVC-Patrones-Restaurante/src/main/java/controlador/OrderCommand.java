package controlador;

public interface OrderCommand {
    void execute();
    void undo();
    String getDescription();
}