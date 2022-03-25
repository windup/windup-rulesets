public class ThreadStop {

    public static void main(String[] args) {
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
