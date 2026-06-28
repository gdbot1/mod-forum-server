package com.modsProject.modsProject.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static <T> List<T> cutList(List<T> list, int from, int to) {
        List<T> res_list = new ArrayList<>();

        for (int i = from; i < to; i++) {
            if (i < 0 || i >= list.size()) {
                return res_list;
            }

            res_list.add(list.get(i));
        }

        return res_list;
    }
}