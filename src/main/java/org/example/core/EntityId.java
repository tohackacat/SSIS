package org.example.core;

import java.util.UUID;

public record EntityId(UUID value) {

    //random id generation
    public static EntityId random() {
        return new EntityId(UUID.randomUUID());
    }

    //specify an id
    public static EntityId fromString(String text){
        return new EntityId(UUID.fromString(text));
    }
    
    //override the built-in toString record method
    //only get the value as a string
    @Override
    public String toString(){
        return value.toString();
    }
}
