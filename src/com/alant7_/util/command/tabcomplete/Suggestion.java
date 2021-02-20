package com.alant7_.util.command.tabcomplete;

public class Suggestion<T> {

    private T t;

    Suggestion(T t) {
        this.t = t;
    }

    public T getValue() {
        return t;
    }

    public static final Suggestion<?> ANY = new Suggestion<>(null);

    public static Suggestion<String> STRING(String str) {
        return create(str);
    }

    public static Suggestion<Integer> INT(int a) {
        return create(a);
    }

    public static Suggestion<String[]> STRING_ARRAY(String... arr) {
        return create(arr);
    }

    public static Suggestion<int[]> INT_ARRAY(int... arr) {
        return create(arr);
    }

    public static Suggestion<Double> DOUBLE(double d) {
        return create(d);
    }

    public static Suggestion<double[]> DOUBLE_ARRAY(double... arr) {
        return create(arr);
    }

    private static <T> Suggestion<T> create(T t) {
        return new Suggestion<>(t);
    }

}