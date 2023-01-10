package manager.memory;

import manager.HistoryManager;
import tasks.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

        private final Map<Integer, Node> historyMap = new HashMap<>();
        private Node first;
        private Node last;

        public Node linkLast(Task task) {
            final Node newNode = new Node(last, task, null);
            if (first == null){
                first = newNode;
            } else {
                last.next = newNode;
            }
            last = newNode;
            return newNode;
        }

        public List<Task> getTask() {
            List<Task> allHistory = new LinkedList<>();
            Node node = first;
            while (node != null){
                allHistory.add(node.value);
                node = node.next;
            }
            return allHistory;
        }

        public void removeNode(int id) {
            final Node node = historyMap.remove(id);
            if (node == null) {
                return;
            }
            if (node.previous != null) {
                node.previous.next = node.next;
                if (node.next == null) {
                    last = node.previous;
                } else {
                    node.next.previous = node.previous;
                }
            } else {
                first = node.next;
                if (first == null) {
                    last = null;
                } else {
                    first.previous = null;
                }
            }
        }

    @Override
    public void remove(int id) {
            removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        removeNode(task.getId());
        historyMap.put(task.getId(), linkLast(task));
    }

    private static class Node {
        private Node previous;
        private final Task value;
        private Node next;

        public Node(Node previous, Task value, Node next) {
            this.previous = previous;
            this.value = value;
            this.next = next;
        }
    }
}
