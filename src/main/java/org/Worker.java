package org;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(Worker.class);
    private Coordinator coordinator;

    public Worker(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void run() {
        try {

            Task task = coordinator.getTask();

            if ((task = coordinator.getTask()) != null) {

                if (task instanceof MapTask) {
                    processMapTask((MapTask) task);
                } else if (task instanceof ReduceTask) {
                    processReduceTask((ReduceTask) task);
                }
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("Ошибка при получении или обработке задачи: {}", e.getMessage(), e);
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
            log.error("Ошибка ввода/вывода при обработке MapTask id={}: {}", task.id, e.getMessage(), e);
            returnTaskForRetry(task, e);
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обработке MapTask id={}: {}", task.id, e.getMessage(), e);
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

        }catch (IOException e) {

            log.error("Ошибка ввода/вывода при обработке ReduceTask id={}: {}", task.id, e.getMessage(), e);
            returnTaskForRetry(task, e);
        } catch (Exception e) {

            log.error("Неожиданная ошибка при обработке ReduceTask id={}: {}", task.id, e.getMessage(), e);
        }
    }
    private void returnTaskForRetry(Task task, Exception e) {

        synchronized (coordinator) {
            if (task instanceof MapTask) {
                coordinator.getMapTasks().offer((MapTask) task);
            } else if (task instanceof ReduceTask) {
                ReduceTask reduceTask = (ReduceTask) task;
                reduceTask.setFiles(new ArrayList<>(coordinator.getIntermediateFiles()));
                coordinator.getReduceTasks().offer(reduceTask);
            } else {
                log.error("Неизвестный тип задачи: {}", task.getClass().getSimpleName());
            }
        }
    }

}
