package org.tracker.excel;

import java.util.LinkedList;
import java.util.Queue;

public class ExcelQueue {

    private final Queue<String> queue;

    public ExcelQueue() {
        this.queue = new LinkedList<>();
    }

    public void enqueue(String filePath) {
        queue.add(filePath);
    }

    public String dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return !queue.isEmpty();
    }
}
