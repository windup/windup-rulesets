package com.test.project;

import org.jboss.windup.util.FurnaceCompositeClassLoader;
public class ConstructClassLoader {


    public static void main(String[] args) {

        ClassLoader loader = new FurnaceCompositeClassLoader(null);
        String name = loader.getName();
    }

}