import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class JavaNativeProcess {

    public static void main(String[] args) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("notepad.exe");
        ProcessBuilder pb = new ProcessBuilder("javac hello.java");
        ProcessHandle ph = p.toHandle();
        System.out.println("Process is Running: " + ph.isAlive());

        ProcessHandle.Info info = ph.info();

        System.out.println("Process Command:" + info.command());
        System.out.println("Process Argument:" + info.arguments());
        CompletableFuture<ProcessHandle> cf = ph.onExit();

        cf.thenRunAsync(() -> System.out.println("Process Exit"));

    }

}