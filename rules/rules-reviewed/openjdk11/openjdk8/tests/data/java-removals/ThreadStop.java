public class ThreadStop {

    public static void main(String[] args) {
        System.runFinalizersOnExit(true);
        Runtime.runFinalizersOnExit(true);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("Test thread running.");
                }
            }
        }, "Test Thread");
        t1.start();
        t1.stop(new ArrayIndexOutOfBoundsException());
        t1.destroy();
    }
}
