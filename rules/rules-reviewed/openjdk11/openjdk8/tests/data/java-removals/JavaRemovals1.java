package com.test.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.LogManager;

public class JavaRemovals1 {
    public static void main(String[] args) {
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(evt.getPropertyName());
            }
        };

        LogManager logManager = LogManager.getLogManager();
        logManager.addPropertyChangeListener(propertyChangeListener);
        logManager.removePropertyChangeListener(propertyChangeListener);
    }
}
