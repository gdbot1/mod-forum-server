package com.modsProject.modsProject.utils.pages;

public class PageUtils {
    /***
     * Функция возвращает список доступных страниц клиенту.
     * @param page - текущая страница.
     * @param buttons_count - максимальное количество кнопок.
     * @param pages_count - общее количество страниц.
     * @return - возвращает список чисел, где число - это 1 доступная страница.
     */
    public static int[] getPages(int page, int buttons_count, int pages_count) {
        int[] pages = new int[buttons_count];

        int min = page - (buttons_count / 2);
        int max = (buttons_count - 1) + page - (buttons_count / 2);

        int bias = 0;

        if (min < 0) {
            bias = -min;
        }

        if (max >= pages_count) {
            bias = pages_count - max - 1;
        }

        for (int i = 0; i < buttons_count; i++) {
            pages[i] = i + page - (buttons_count / 2) + bias;
        }

        return pages;
    }
}
