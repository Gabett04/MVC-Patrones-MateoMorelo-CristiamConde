package controlador;

import java.util.*;

import java.util.*;

public class CommandInvoker {
    private Stack<OrderCommand> commandHistory;
    private Stack<OrderCommand> undoneCommands;
    
    public CommandInvoker() {
        this.commandHistory = new Stack<>();
        this.undoneCommands = new Stack<>();
    }
    
    public void executeCommand(OrderCommand command) {
        command.execute();
        commandHistory.push(command);
        undoneCommands.clear();
    }
    
    public void undo() {
        if (!commandHistory.isEmpty()) {
            OrderCommand command = commandHistory.pop();
            command.undo();
            undoneCommands.push(command);
        }
    }
    
    public boolean canUndo() {
        return !commandHistory.isEmpty();
    }
}