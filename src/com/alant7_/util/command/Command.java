package com.alant7_.util.command;

import com.alant7_.util.AlanJavaPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String base();

    String[] params();

    String permission() default "";

    String permissionMessage() default "§cSorry, you do not have permission.";

    boolean requirePlayer() default false;

    String requirePlayerMessage() default "§cThis command is for players only.";

    String ANY = "§";

}
