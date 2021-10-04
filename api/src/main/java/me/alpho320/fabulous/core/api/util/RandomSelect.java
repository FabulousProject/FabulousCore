package me.alpho320.fabulous.core.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSelect<T> {

    private final @NotNull List<T> list;

    public RandomSelect(@NotNull List<T> list) {
        this.list = list;
    }
    
    public T choose() {
        return list.get(random(list.size() >= 2 ? list.size() - 1 : list.size()));
    }

    public T choose(@NotNull List<T> list) {
        return list.get(random(list.size() >= 2 ? list.size() - 1 : list.size()));
    }
    
    public List<T> choose(int limit, boolean duplicate) {
        if (list.size() <= limit && !duplicate) return list;

        List<T> elements = new ArrayList<>();
        int limitClone = limit;

        while (limitClone > 0) {
            T element = choose(list);

            if (elements.contains(element) && !duplicate) continue;

            elements.add(element);
            --limitClone;
        }

        return elements;
    }

    private int random(int seed) {
        return new Random().nextInt(seed);
    }

}
