package org;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        List<String> files = Arrays.asList("file1.txt", "file2.txt"); // входные файлы
        int numWorkers = 3;
        int numReduce = 2;

        Coordinator coordinator = new Coordinator(files, numReduce);

        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            Thread t = new Thread(new Worker(coordinator));
            t.start();
            workers.add(t);
        }

        for (Thread t : workers) {
            t.join();
        }

        System.out.println("MapReduce завершён!");
    }
}