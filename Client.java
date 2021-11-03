import java.util.*;
import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args)throws UnknownHostException, IOException {
        // dispara cliente
        new Client("localhost", 8081).executa();
    }

    private String host;
    private int porta;
    Socket cliente;

    public Client (String host, int porta) {
      try {
        this.host = host;
        this.porta = porta;
        cliente = new Socket(this.host, this.porta);
      }catch(Exception e){
        System.out.println("=== OPA! ===");
        System.out.println("Desculpe, nao temos jogos por agora. :(");
      }
    }

    public void executa(){
      try{
        // thread para receber mensagens do servidor
        PrintCli r = new PrintCli(cliente.getInputStream());
        new Thread(r).start();

        // recebendo entradas do cliente
        Scanner sc = new Scanner(System.in);
        PrintStream saida = new PrintStream(cliente.getOutputStream());
        while (sc.hasNextLine()) {
            saida.println(sc.nextLine());
            saida.flush();
        }
        saida.close();
        sc.close();
        cliente.close();
      }catch(Exception e){
      }
    }
}
