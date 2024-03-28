package sorokin.java.course;

import org.springframework.stereotype.Component;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class OperationsConsoleListener implements Runnable {

    private final Map<ConsoleOperationType, OperationCommand> commandMap;
    private final Scanner scanner;

    public OperationsConsoleListener(
            List<OperationCommand> operationCommandList,
            Scanner scanner
    ) {
        this.commandMap = operationCommandList.stream()
                .collect(Collectors.toMap(OperationCommand::getOperationType, it -> it));
        this.scanner = scanner;
    }

    @Override
    public void run() {
        init();
        process();
        destroy();
    }

    private void process() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Please enter one of operation type:");
            printAllAvailableOperations();
            var nextOperation = listenNextOperationType();
            if (nextOperation == null) {
                return;
            }
            processNextCommand(nextOperation);
        }
    }

    private ConsoleOperationType listenNextOperationType() {
        while (!Thread.currentThread().isInterrupted()) {
            var operation = scanner.nextLine();
            if (operation.isBlank()) {
                continue;
            }
            try {
                return ConsoleOperationType.valueOf(operation);
            } catch (IllegalArgumentException e) {
                System.out.println("No such command found");
            }
        }
        return null;
    }

    private void processNextCommand(ConsoleOperationType operationType) {
        try {
            OperationCommand command = commandMap.get(operationType);
            command.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf(
                    "Error executing command %s: error=%s%n", operationType,
                    e.getMessage()
            );
        }
    }

    private void printAllAvailableOperations() {
        StringBuilder stringBuilder = new StringBuilder();
        commandMap.keySet()
                .forEach(consoleOperationType -> stringBuilder
                        .append("-")
                        .append(consoleOperationType)
                        .append("\n")
                );
        System.out.println(stringBuilder);
    }

    private void init() {
        System.out.println("Start listening console operations");
    }

    private void destroy() {
        System.out.println("End listening console operations");
    }

}

