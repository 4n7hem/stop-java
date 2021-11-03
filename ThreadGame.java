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
    private String command = "";


    public ThreadGame(InputStream cliente, Server servidor, Socket socket, PrintStream printaCli) {
        this.cliente = cliente;
        this.servidor = servidor;
        this.socket = socket;
        this.mensagem = printaCli;
    }

    public void run() {
      try{
        user = Integer.toString(socket.getPort());
        Scanner s = new Scanner(this.cliente);

        //enquanto a pessoa não pedir para sair durante uma rodada o codigo segue fluido
        while(!command.equals("SAIR")){
          //limpa quaisquer entradas anteriores ao incio da rodada
          this.cliente.skip(this.cliente.available());
          
          Object[] suporte = servidor.jogo.ordemAl.toArray();
          for  (int i = 0; i < suporte.length; i++){
            Integer n = (Integer) suporte[i];
            mensagem.println("\n"+servidor.jogo.categorias[n.intValue()]+":");
            command = s.nextLine();
            if (servidor.jogo.getBateu() || command.equals("SAIR")) break;
            servidor.jogo.addResposta(user, command,
              servidor.jogo.categorias[n.intValue()]);
          }
          //se a pessoa terminou o loop for, indica que passou por todos as categorias, então ela bateu
          if(!servidor.jogo.getBateu() && !command.equals("SAIR")) servidor.jogo.bateu(user);
          if(servidor.jogo.getBateu()) break;
        }
        
        if(servidor == null){
          socket.close();
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
      finally {
				try {
          if(command.equals("SAIR")){
            System.out.println("Conexao com "+user+" encerrada.");
            servidor.distribuiMensagem(user+" saiu.");
            servidor.getConnected().remove(this.socket);
            socket.close();
          } 
          
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
    }
}
