package utils;

import models.Task;

public class Node {
    private Task data;
    private Node prev;
    private Node next;

    public Node(Task data, Node next, Node first) {
        this.data = data;
        this.prev = first;
        this.next = next;
    }

    public Task getData() {
        return data;
    }

    public void setData(Task data) {
        this.data = data;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
