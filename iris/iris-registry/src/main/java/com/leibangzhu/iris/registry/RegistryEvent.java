package com.leibangzhu.iris.registry;

import com.leibangzhu.iris.core.KeyValue;

public class RegistryEvent {

    public KeyValue<String, String> getPreKeyValue() {
        return preKeyValue;
    }

    public KeyValue<String, String> getKeyValue() {
        return keyValue;
    }

    private KeyValue<String,String> preKeyValue = new KeyValue<>();
    private KeyValue<String,String> keyValue = new KeyValue<>();
    private EventType eventType = EventType.UNRECOGNIZED;

    public static RegistryEventBuilder newBuilder(){
        return new RegistryEventBuilder();
    }

    public EventType getEventType() {
        return eventType;
    }

    public static class RegistryEventBuilder{

        private RegistryEvent registryEvent = new RegistryEvent();

        public RegistryEventBuilder preKey(String key){
            registryEvent.preKeyValue.setKey(key);
            return this;
        }

        public RegistryEventBuilder preValue(String value){
            registryEvent.preKeyValue.setValue(value);
            return this;
        }

        public RegistryEventBuilder key(String key){
            registryEvent.keyValue.setKey(key);
            return this;
        }

        public RegistryEventBuilder value(String value){
            registryEvent.keyValue.setValue(value);
            return this;
        }

        public RegistryEvent build(){
            return registryEvent;
        }

        public RegistryEventBuilder eventType(EventType eventType){
            registryEvent.eventType = eventType;
            return this;
        }
    }

    public static enum EventType {
        PUT,
        DELETE,
        UNRECOGNIZED;

        private EventType() {
        }
    }
}
