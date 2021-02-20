package com.alant7_.util.sorting;

import java.util.ArrayList;
import java.util.Collection;

public class QuickSort {

    public static Collection<Sortable> sort(Collection<? extends Sortable> list) {

        ArrayList<Sortable> items = new ArrayList<>(list);
        quickSort(items, 0, items.size() - 1);

        return items;

    }

    public static <T> Collection<T> sort(Collection<? extends Sortable> list, Class<T> clazz) {

        ArrayList<Sortable> items = new ArrayList<>(list);
        quickSort(items, 0, items.size() - 1);

        ArrayList<T> itemsCasted = new ArrayList<>();
        for (Sortable sortable : items) {
            itemsCasted.add(clazz.cast(sortable));
        }

        return itemsCasted;

    }

    static int partition(ArrayList<Sortable> array, int begin, int end) {
        int pivot = end;

        int counter = begin;
        for (int i = begin; i < end; i++) {
            if (array.get(i).greaterThan(array.get(pivot))) {
                Sortable temp = array.get(counter);
                array.set(counter, array.get(i));
                array.set(i, temp);
                counter++;
            }
        }

        Sortable temp = array.get(pivot);
        array.set(pivot, array.get(counter));
        array.set(counter, temp);

        return counter;
    }

    private static void quickSort(ArrayList<Sortable> array, int begin, int end) {
        if (end <= begin) return;
        int pivot = partition(array, begin, end);

        quickSort(array, begin, pivot-1);
        quickSort(array, pivot+1, end);
    }

}
