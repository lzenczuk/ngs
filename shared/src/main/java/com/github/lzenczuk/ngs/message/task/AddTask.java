package com.github.lzenczuk.ngs.message.task;

/**
 * @author lzenczuk 21/08/2015
 */
public class AddTask extends InTaskMessage {

    private String taskTitle;

    public AddTask() {}

    public AddTask(long engineId, String taskTitle) {
        super(engineId);
        this.taskTitle = taskTitle;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    @Override
    public String toString() {
        return "AddTask{" +
                "taskTitle='" + taskTitle + '\'' +
                '}';
    }
}
