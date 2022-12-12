package manager.memory;

import manager.HistoryManager;
import tasks.*;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected final static int MAX_LENGTH = 10;

    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() == MAX_LENGTH) {
                history.removeFirst();
            }
            history.addLast(task);
        }
    }
}
