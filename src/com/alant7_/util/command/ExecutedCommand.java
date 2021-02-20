package com.alant7_.util.command;

import org.bukkit.command.CommandSender;

public class ExecutedCommand {

    public ExecutedCommand(CommandSender sender, String base, String[] args, boolean fewArguments, boolean extraArguments) {
        this.sender = sender;
        this.base = base;
        this.args = args;
        this.fewArguments = fewArguments;
        this.extraArguments = extraArguments;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getBase() {
        return base;
    }

    public String[] getArgs() {
        return args;
    }

    public boolean hasFewArguments() {
        return fewArguments;
    }

    public boolean hasExtraArguments() {
        return extraArguments;
    }

    private CommandSender sender;

    private String base;

    private String[] args;

    private boolean fewArguments;

    private boolean extraArguments;

}
