package com.alant7_.util.command.tabcomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabCompleter {

    private Map<Object, TabCompleter> completerMap = new HashMap<>();

    public TabCompleter add(String key) {

        TabCompleter tabCompleter = new TabCompleter();
        completerMap.put(key, tabCompleter);

        return this;
    }

    public TabCompleter get(String key) {
        return completerMap.getOrDefault(key, null);
    }

    public TabCompleter get(Suggestion<?> key) {

        if (key == Suggestion.ANY) {
            return completerMap.getOrDefault(key, null);
        }

        return completerMap.getOrDefault(String.valueOf(key.getValue()), null);

    }

    public TabCompleter add(Suggestion<?> key) {

        TabCompleter tabCompleter = new TabCompleter();

        if (key != Suggestion.ANY) {
            completerMap.put(key, tabCompleter);
        } else {
            completerMap.put(String.valueOf(key.getValue()), tabCompleter);
        }

        return this;

    }

    public boolean isValid(String key) {
        return completerMap.containsKey(key) || completerMap.containsKey(Suggestion.ANY);
    }

    public List<String> process(String string) {
        List<String> r = new ArrayList<>();

        completerMap.keySet().forEach((key) -> {
            if (key == Suggestion.ANY)
                return;

            if (shouldGetSuggested(string, key.toString())) {
                r.add(key.toString());
            }
        });

        return r;
    }

    private boolean shouldGetSuggested(String arg, String targetArg) {

        arg = arg.toLowerCase();

        targetArg = targetArg.toLowerCase();

        if (arg.length() == 0)
            return true;

        if (arg.charAt(0) != arg.charAt(0))
            return false;

        if (arg.length() > targetArg.length())
            return false;

        for (int i = 0; i < arg.length(); i++) {
            if (arg.charAt(i) != targetArg.charAt(i)) {
                return false;
            }
        }

        return true;

    }

    // Suggest argument that match argument that sender began writing
    private void suggest(List<String> arr, String arg, String... args) {
        for (String arg1 : args)
            if (shouldGetSuggested(arg, arg1))
                arr.add(arg1);
    }

}
