package com.alant7_.util.command;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.command.tabcomplete.TabCompleter;
import com.alant7_.util.reflections.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class CommandExecutor implements org.bukkit.command.CommandExecutor, org.bukkit.command.TabCompleter {

    private Map<String, TabCompleter> tabCompleterMap = new HashMap<>();

    private Map<String, Pair<Method, com.alant7_.util.command.Command>> methodsNoParams = new HashMap<>();

    private Map<String, Map<Method, com.alant7_.util.command.Command>> methods = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        String base = cmd.getName().toLowerCase();

        Map<Method, com.alant7_.util.command.Command> methods = this.methods.get(base);

        if (methods == null)
            return false;

        Method methodNoParams = methodsNoParams.get(base).getKey();

        com.alant7_.util.command.Command commandNoParams = methodsNoParams.get(base).getValue();

        if (args.length == 0) {

            if (methodNoParams != null) {
                try {
                    methodNoParams.invoke(this, new ExecutedCommand(sender, base, new String[0], false, false));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;

        }

        final Pair<Method, com.alant7_.util.command.Command> match = new Pair<>();

        AtomicInteger maxArgsMatch = new AtomicInteger(0);

        methods.forEach(((method, command) -> {

            /* if (completed.get())
                return; */

            String[] params = command.params();
/*
            if (params.length > args.length)
                return;
*/
            int min = Math.min(params.length, args.length);

            for (int i = 0; i < min; i++) {

                if (!params[i].equalsIgnoreCase(args[i]) && !params[i].equals(com.alant7_.util.command.Command.ANY)) {
                    return;
                }

            }

            if (match.getKey() == null || match.getValue() == null || min > maxArgsMatch.get()) {
                match.setKey(method);
                match.setValue(command);
                maxArgsMatch.set(min);
            } else {

                if (params.length == args.length) {
                    match.setKey(method);
                    match.setValue(command);
                    maxArgsMatch.set(min);
                }

            }

        }));

        try {

            com.alant7_.util.command.Command command;

            Method method;

            boolean tooFew;

            boolean tooMany;

            if (match.getKey() != null) {
                method = match.getKey();
                tooFew = false;
                tooMany = args.length > match.getValue().params().length;
                command = match.getValue();
            } else if (methodNoParams != null) {
                method = methodNoParams;
                tooFew = true;
                tooMany = false;
                command = commandNoParams;
            } else return false;

            if (command.requirePlayer()) {
                sender.sendMessage(command.requirePlayerMessage());
                return false;
            }

            method.invoke(this, new ExecutedCommand(sender, base, args, tooFew, tooMany));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }

    public TabCompleter tabCompleter(String base) {
        return tabCompleterMap.computeIfAbsent(base, k -> new TabCompleter());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {

        String base = cmd.getName().toLowerCase();

        TabCompleter tabCompleter = tabCompleterMap.get(base);

        if (tabCompleter == null) {
            return null;
        }

        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i];
            if (tabCompleter == null) {
                return null;
            }
            if (tabCompleter.isValid(arg)) {
                tabCompleter = tabCompleter.get(arg);
            }
        }

        return tabCompleter.process(args[args.length - 1]);

    }

    public void register(AlanJavaPlugin plugin) {

        Arrays.stream(getClass().getDeclaredMethods()).forEach((method -> {

            com.alant7_.util.command.Command a = method.getDeclaredAnnotation(com.alant7_.util.command.Command.class);
            if (a != null) {
                String base = a.base();
                if (a.params().length == 0) {
                    methodsNoParams.put(base, new Pair<>(method, a));
                } else {
                    Map<Method, com.alant7_.util.command.Command> map = methods.get(base);
                    if (map == null) {
                        map = new HashMap<>();
                        methods.put(base, map);

                        PluginCommand cmd = plugin.getCommand(base);
                        if (cmd != null) {
                            cmd.setExecutor(this);
                            cmd.setTabCompleter(this);
                        }
                    }

                    map.put(method, a);
                }
            }

        }));

    }

}
