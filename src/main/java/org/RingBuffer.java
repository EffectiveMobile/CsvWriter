package org;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RingBuffer <T>{

    private final T[] buffer;
    private int head = 0;
    private int tail = 0;
    private int count = 0;
    private int size = 0;
    private boolean closed = false;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public RingBuffer(int capacity) {


        buffer = (T[]) new Object[capacity];
    }


    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                notFull.await();
            }
            buffer[tail] = item;
            tail = (tail + 1) % buffer.length;
            count++;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public synchronized boolean isFull() {
        return size == buffer.length;
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    public synchronized boolean isClosed() {
        return closed;
    }

    // Извлечение элемента из буфера
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            T item = buffer[head];
            head = (head + 1) % buffer.length;
            count--;
            notFull.signalAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
