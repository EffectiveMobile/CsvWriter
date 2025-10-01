package org;

import org.task.MapTask;
import org.task.ReduceTask;
import org.task.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker implements Runnable {
    private Coordinator coordinator;

    public Worker(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void run() {
        while (true) {
            Task task = coordinator.getTask();
            if (task == null) break;

            if (task instanceof MapTask) {
                processMapTask((MapTask) task);
            } else if (task instanceof ReduceTask) {
                processReduceTask((ReduceTask) task);
            }
        }
    }

    private void processMapTask(MapTask task) {

        try {
            String content = new String(Files.readAllBytes(Paths.get(task.fileName)));
            List<KeyValue> kvList = MapReduceFunctions.map(task.fileName, content);

            // создаем промежуточные файлы для reduce задач
            List<String> files = new ArrayList<>();
            for (int i = 0; i < task.numReduce; i++) {
                String fileName = "intermediate_map" + task.id + "_reduce" + i + ".txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                for (KeyValue kv : kvList) {
                    if (Math.abs(kv.key.hashCode()) % task.numReduce == i) {
                        writer.write(kv.key + "\t" + kv.value + "\n");
                    }
                }
                writer.close();
                files.add(fileName);
            }

            coordinator.reportIntermediateFiles(files);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processReduceTask(ReduceTask task) {
        try {
            Map<String, List<String>> grouped = new HashMap<>();

            // читаем все промежуточные файлы
            for (String file : task.files) {
                List<String> lines = Files.readAllLines(Paths.get(file));
                for (String line : lines) {
                    String[] parts = line.split("\t");
                    grouped.computeIfAbsent(parts[0], k -> new ArrayList<>()).add(parts[1]);
                }
            }

            // сортируем по ключу
            List<String> keys = new ArrayList<>(grouped.keySet());
            Collections.sort(keys);

            // пишем финальный файл
            String outFile = "reduce_result_" + task.id + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            for (String key : keys) {
                String result = MapReduceFunctions.reduce(key, grouped.get(key));
                writer.write(key + " " + result + "\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
