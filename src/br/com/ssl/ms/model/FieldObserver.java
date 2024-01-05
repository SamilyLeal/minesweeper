package br.com.ssl.ms.model;

public interface FieldObserver {
    public void registerEvent(Field field, FieldEvent event);
}
