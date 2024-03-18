package org.theoliverlear.model.thread;

import java.util.ArrayList;

public class ThreadManager {
    ArrayList<Thread> threads;
    public ThreadManager(ArrayList<Thread> threads) {
        this.threads = threads;
    }
    public void startThreads() {
        for (Thread thread : this.threads) {
            if (!thread.isAlive()) {
                thread.start();
            }
        }
    }
    public void stopThreads() {
        for (Thread thread : this.threads) {
            thread.interrupt();
        }
    }
    public void addThread(Thread thread) {
        this.threads.add(thread);
    }
    public void removeThread(Thread thread) {
        this.threads.remove(thread);
    }
}
