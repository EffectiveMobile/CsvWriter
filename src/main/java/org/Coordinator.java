package org;

import lombok.Getter;
import lombok.Setter;
import org.task.MapTask;
import org.task.ReduceTask;
import org.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
@Setter
public class Coordinator {

    private Queue<MapTask> mapTasks;
    private Queue<ReduceTask> reduceTasks;
    private List<String> intermediateFiles;
    private int numReduce;

    public Coordinator(List<String> fileNames, int numReduce) {
        this.numReduce = numReduce;
        mapTasks = new LinkedList<>();
        reduceTasks = new LinkedList<>();
        intermediateFiles = Collections.synchronizedList(new ArrayList<>());

        int taskId = 0;
        for (String file : fileNames) {
            mapTasks.add(new MapTask(taskId++, file, numReduce));
        }

        for (int i = 0; i < numReduce; i++) {
            reduceTasks.add(new ReduceTask(i));
        }
    }

    // Получить задачу для воркера
    public synchronized Task getTask() {
        if (!mapTasks.isEmpty()) {
            return mapTasks.poll();
        }
        if (mapTasks.isEmpty() && !reduceTasks.isEmpty()) {
            ReduceTask t = reduceTasks.poll();
            t.setFiles(new ArrayList<>(intermediateFiles));
            return t;
        }
        return null; // задачи закончились
    }

    public synchronized void reportIntermediateFiles(List<String> files) {
        intermediateFiles.addAll(files);
    }
}
