package org.task;

import java.util.List;

public class ReduceTask extends Task {

    public int id;
    public List<String> files;

    public ReduceTask(int id) {
        this.id = id;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
