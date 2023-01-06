package manager.memory;

import manager.HistoryManager;
import tasks.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

        private final Map<Integer, Node> map = new HashMap<>();
        private Node fist;
        private Node last;

        public Node linkLast(Task task){
            final Node newLast = last;
            final Node newNode = new Node(newLast, task, null);
            last = newNode;
            if (newLast != null){
                newLast.next = newNode;
            } else {
                fist = newNode;
            }
            return newNode;
        }

        public List<Task> getTask(){
            LinkedList<Task> allHistory = new LinkedList<>();
            Node node = fist;
            while (node != null){
                allHistory.add(node.value);
                node = node.next;
            }
            return allHistory;
        }

        public void removeNode(Node n){
            if (n == fist){
                if (fist.next != null){
                    fist = fist.next;
                } else fist = null;
            }

            if (n == last){
                if(last.previous != null){
                    last = last.previous;
                }else last = null;
            }

            if (n.previous != null){
                n.previous.next = n.next;
                if (n.next != null){
                    n.next.previous = n.previous;
                }
            }
        }
    @Override
    public void remove(int id){
        if (map.containsKey(id)){
            removeNode(map.get(id));
            map.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (map.containsKey(task.getId())) {
                removeNode(map.get(task.getId()));
                map.remove(task.getId());
            }
            map.put(task.getId(), linkLast(task));
        }
    }

    private static class Node{
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
