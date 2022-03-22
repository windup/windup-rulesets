package com.test.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.jar.Pack200;

public class PackerRemovals {
    public static void main(String[] args) {
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(evt.getPropertyName());
            }
        };

        Pack200.Packer packer = Pack200.newPacker();
        packer.addPropertyChangeListener(propertyChangeListener);
        packer.removePropertyChangeListener(propertyChangeListener);

        Pack200.Unpacker unpacker = Pack200.newUnpacker();
        unpacker.addPropertyChangeListener(propertyChangeListener);
        unpacker.removePropertyChangeListener(propertyChangeListener);
    }
}