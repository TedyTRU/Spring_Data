package com.example.next_level_technologies.util;

public interface DataConverter {

    <T> T deserialize(String input, Class<T> type);

    String serialize(Object o);

}
