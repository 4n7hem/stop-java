import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Timer;
import java.util.TimerTask;


public class ThreadGame extends Thread {

    private InterfaceCli Cli = new InterfaceCli();
    private InputStream cliente;
    private Server servidor;
    private Socket socket;
    private PrintStream mensagem;
    private String user;
    public boolean wait = true;
    private long init;
    public boolean bateu;


    public ThreadGame(InputStream cliente, Server servidor, Socket socket, PrintStream printaCli) {
        this.cliente = cliente;
        this.servidor = servidor;
        this.socket = socket;
        this.mensagem = printaCli;
    }


    public void run() {
      try{
        init = System.currentTimeMillis();
        user = Integer.toString(socket.getPort());
        //Cli.contagemRegr(servidor);
        //Thread.sleep(6000);
        this.cliente.skip(this.cliente.available());
        Scanner s = new Scanner(this.cliente);
        String command = "";

        //s.hasNextLine()
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
            mensagem.println(servidor.jogo.categorias[i.intValue()]+":\n");
            command = s.nextLine();
            if(command.equals("SAIR")) break;
            servidor.jogo.addResposta(user, command,
              servidor.jogo.categorias[i.intValue()]);
            //System.out.println(s.nextLine());//coisas que estão vindo dos clientes
          }
          mensagem.println("ACABOU");
          if(!servidor.jogo.getBateu()) servidor.jogo.bateu(user);

          //wait = !wait;
          //mensagem.println("YESS agora e so aguardar...\n");
          //System.exit(0);
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
