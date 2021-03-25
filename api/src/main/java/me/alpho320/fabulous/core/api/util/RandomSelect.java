package me.alpho320.fabulous.core.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomSelect<T> {

    private final T random;

    public RandomSelect(@NotNull final List<T> list) {
        this.random = chooseRandom(list);
    }
    
    private <A> A chooseRandom(@NotNull final List<A> list) {
        return list.get(random(list.size()));
    }
    
    private <A> List<A> chooseRandoms(@NotNull final List<A> list, final int limit, final boolean duplicate) {
        if (list.size() <= limit && !duplicate) {
            return list;
        }

        final ArrayList<A> things = new ArrayList<>();
        int limitClone = limit;

        while (limitClone > 0) {
            final A thing = chooseRandom(list);

            if (things.contains(thing) && !duplicate) {
                continue;
            }

            things.add(thing);
            --limitClone;
        }

        return things;
    }

    private int random(final int seed) {
        return new Random().nextInt(seed);
    }
    
    public Optional<T> value() {
        return Optional.of(this.random);
    }

}