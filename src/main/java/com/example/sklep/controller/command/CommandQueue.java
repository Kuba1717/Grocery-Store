package com.example.sklep.controller.command;

import java.util.LinkedList;
import java.util.Queue;

public class CommandQueue {
    private final Queue<Command> queue;

    public CommandQueue() {
        this.queue = new LinkedList<>();
    }

    public void addCommand(Command command) {
        queue.add(command);
    }

    public void processCommands() {
        while (!queue.isEmpty()) {
            Command command = queue.poll();
            command.execute();
        }
    }

    public void cancelCommand(Command command) {
        command.cancel();
        queue.remove(command);
    }
}
