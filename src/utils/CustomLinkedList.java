package utils;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> tasksMap = new HashMap<>();

    private int size = 0;

    public void linkLast(Task element) {
        Node lastNode = this.tail;
        Node newNode = new Node(element, null, lastNode);

        this.tail = newNode;
        if (lastNode == null) {
            head = newNode;
        } else {
            lastNode.setNext(newNode);
        }

        tasksMap.put(newNode.getData().getId(), newNode);
        size++;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node head = this.head;

        while (head != null) {
            tasks.add(head.getData());
            head = head.getNext();
        }

        return tasks;
    }

    public void remove(int id) {
        if (tasksMap.containsKey(id)) {
            removeNode(tasksMap.get(id));
        }
    }

    private void removeNode(Node node) {
        if (node == head & node == tail) {
            tail = null;
            head = null;
            node.setNext(null);
            node.setPrev(null);
        } else if (node == head) {
            head = node.getNext();
            node.getNext().setPrev(null);
            node.setNext(null);
            node.setNext(null);
        } else if (node == tail) {
            tail = node.getPrev();
            node.getPrev().setNext(null);
            node.setPrev(null);
            node.setNext(null);
        } else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
            node.setNext(null);
            node.setPrev(null);
        }

        tasksMap.remove(node.getData().getId());
        size--;
    }
}
