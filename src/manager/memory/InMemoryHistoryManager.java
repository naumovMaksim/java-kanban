package manager.memory;

import manager.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected final static int MAX_LENGTH = 10;

    protected List<Task> list = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return list;
    }

    @Override
    public void add(Task task) {
        if(list.size() == MAX_LENGTH) {
            list.remove(0);
        }
        if (task != null){
            list.add(task);
        }
    }
}
