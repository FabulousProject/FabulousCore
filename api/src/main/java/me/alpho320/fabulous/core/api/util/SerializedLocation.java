package me.alpho320.fabulous.core.api.util;

import java.util.List;

public interface SerializedLocation<T> {

    String serializedLocation(T location);
    List<String> serializedLocation(List<T> location);

    T deserializedLocation(String location);
    List<T> deserializedLocation(List<String> location);

}