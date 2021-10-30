import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Timer;
import java.util.TimerTask;


public class ThreadGame extends Thread {

    private InputStream cliente;
    private Server servidor;
    private Socket socket;
    private PrintStream mensagem;
    private String user;
    public boolean wait = true;
    public boolean bateu;


    public ThreadGame(InputStream cliente, Server servidor, Socket socket, PrintStream printaCli) {
        this.cliente = cliente;
        this.servidor = servidor;
        this.socket = socket;
        this.mensagem = printaCli;
    }

    public void run() {
      try{
        user = Integer.toString(socket.getPort());
        this.cliente.skip(this.cliente.available());
        Scanner s = new Scanner(this.cliente);
        String command = "";

        while(!command.equals("SAIR")){
          wait = true;
          bateu = false;
          synchronized (this) {
            while (wait) {
              try {
                  wait();
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
          for (Integer i : servidor.jogo.ordemAl){
            if (servidor.jogo.getBateu()) break;
            mensagem.println("\n"+servidor.jogo.categorias[i.intValue()]+":");
            command = s.nextLine();
            if(command.equals("SAIR")) break;
            servidor.jogo.addResposta(user, command,
              servidor.jogo.categorias[i.intValue()]);
          }
          if(!servidor.jogo.getBateu()) servidor.jogo.bateu(user);
        }
        s.close();
        if(servidor == null){
          socket.close();
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
      finally {
				try {
          System.out.println("Conexao com "+user+" encerrada.");
          servidor.distribuiMensagem(user+" saiu.");
          servidor.getConnected().remove(this.socket);
					socket.close();

				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
    }
}
