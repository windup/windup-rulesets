package com.jboss.windup.test;

public class Main extends Thread {

    public static void main(String[] args) {

        Main thread = new Main();
        thread.start();
        System.out.println("The thread inside the main method!!!");
    }

    public void run() {
        System.out.println("The thread outside the main method!!");
    }
}