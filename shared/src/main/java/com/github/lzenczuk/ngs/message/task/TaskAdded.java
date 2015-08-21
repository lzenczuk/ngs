package com.github.lzenczuk.ngs.message.task;

/**
 * @author lzenczuk 21/08/2015
 */
public class TaskAdded extends OutTaskMessage {

    private final long taskId;
    private final String taskTitle;

    public TaskAdded(long engineId, long taskId, String taskTitle) {
        super(engineId);
        this.taskId = taskId;
        this.taskTitle = taskTitle;
    }

    public long getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }
}
