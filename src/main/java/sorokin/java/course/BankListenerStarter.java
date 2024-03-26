package sorokin.java.course;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BankListenerStarter {

    private final OperationsConsoleListener consoleListener;

    public BankListenerStarter(OperationsConsoleListener consoleListener) {
        this.consoleListener = consoleListener;
    }

    @PostConstruct
    public void init() {
        var consoleListenerThread = new Thread(consoleListener);
        consoleListenerThread.start();
    }
}
