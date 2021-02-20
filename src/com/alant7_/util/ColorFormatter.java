package com.alant7_.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFormatter {

    static Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static String format(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);

        String r = text;
        Matcher matcher = pattern.matcher(r);
        while (matcher.find()) {
            String hex = text.substring(matcher.start(), matcher.end());
            r = r.replace(hex, ChatColor.of(hex).toString());
        }

        return r;
    }

}
