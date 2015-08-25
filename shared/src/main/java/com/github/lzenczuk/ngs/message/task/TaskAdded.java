package com.github.lzenczuk.ngs.message.task;

/**
 * @author lzenczuk 21/08/2015
 */
public class TaskAdded extends OutTaskMessage {

    private long taskId;
    private String taskTitle;

    public TaskAdded() {}

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

    @Override
    public String toString() {
        return "TaskAdded{" +
                "taskId=" + taskId +
                ", taskTitle='" + taskTitle + '\'' +
                '}';
    }
}
