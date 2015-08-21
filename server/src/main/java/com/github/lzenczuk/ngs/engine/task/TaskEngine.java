package com.github.lzenczuk.ngs.engine.task;

import com.github.lzenczuk.ngs.engine.Engine;
import com.github.lzenczuk.ngs.message.task.AddTask;
import com.github.lzenczuk.ngs.message.task.InTaskMessage;
import com.github.lzenczuk.ngs.message.task.OutTaskMessage;
import com.github.lzenczuk.ngs.message.task.TaskAdded;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lzenczuk 21/08/2015
 */
public class TaskEngine implements Engine<InTaskMessage, OutTaskMessage> {

    private Map<Long, String> tasksMap = new HashMap<>();
    private long taskId = 0;

    @Override
    public Optional<OutTaskMessage> process(InTaskMessage message) {
        if(message instanceof AddTask){
            return addTask((AddTask) message);
        }
        return Optional.empty();
    }

    private Optional<OutTaskMessage> addTask(AddTask addTask){
        long taskId = generateTaskId();
        tasksMap.put(taskId, addTask.getTaskTitle());

        return Optional.of(new TaskAdded(addTask.getEngineId(), taskId, addTask.getTaskTitle()));
    }

    private long generateTaskId(){
        return taskId++;
    }
}
