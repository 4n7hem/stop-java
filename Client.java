import java.util.*;
import java.net.*;
import java.io.*;



public class Client {
    private InterfaceCli Cli = new InterfaceCli();
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
        System.out.println(Cli.openningGame());
      }catch(Exception e){
        e.printStackTrace();
      }
    }

    public void executa(){
      try{
        // thread para receber mensagens do servidor
        PrintCli r = new PrintCli(cliente.getInputStream());
        new Thread(r).start();

        // lê msgs do teclado e manda pro servidor
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
            System.out.println(e.getMessage());
      }
    }
}
