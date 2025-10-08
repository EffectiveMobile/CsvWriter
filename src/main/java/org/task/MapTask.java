package org.task;

public class MapTask extends Task {

    public int id;
    public String fileName;
    public int numReduce;

    public MapTask(int id, String fileName, int numReduce) {
        this.id = id;
        this.fileName = fileName;
        this.numReduce = numReduce;
    }
}
