package me.alpho320.fabulous.core.api.util;

import java.util.List;

public interface SerializedLocation {

    String serializedLocation(Object location);
    List<String> serializedLocation(List<Object> location);

    Object deserializedLocation(String location);
    List<Object> deserializedLocation(List<String> location);

}