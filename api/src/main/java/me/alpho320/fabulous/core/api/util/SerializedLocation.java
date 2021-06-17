package me.alpho320.fabulous.core.api.util;

import java.util.List;

public interface SerializedLocation<T> {

    String serialize(T location);
    List<String> serialize(List<T> list);

    T deserialize(String location);
    List<T> deserialize(List<String> list);

}