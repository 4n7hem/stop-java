import java.util.*;
import java.io.*;

public class PrintCli implements Runnable {

    private InputStream servidor;

    public PrintCli(InputStream servidor) {
        this.servidor = servidor;
    }

    public void run() {
        // recebe msgs do servidor e imprime na tela
        Scanner s = new Scanner(this.servidor);
        while (s.hasNextLine()) {
            System.out.println(s.nextLine());
        }
    }
}
