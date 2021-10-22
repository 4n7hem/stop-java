
import java.io.*;
import java.net.*;
import java.util.*;

public class Server{

    private ServerSocket servidor;
    private boolean escutando = true;
    private List<PrintStream> clientes;
    private Map<Socket, PrintStream> connected;
    private InterfaceCli Cli;

    public Server(){
        try{
            System.out.println("Iniciando o servidor....");
            this.servidor = new ServerSocket(8081);
            servidor.setReuseAddress(true);
            System.out.println("Servidor iniciado na porta 8081.");
            connected = new LinkedHashMap<Socket, PrintStream>();
            Cli = new InterfaceCli();
        }
        catch(Exception e){
            System.out.println("ERRO");
            System.out.println(e.getMessage());
        }
    }

    public Map<Socket, PrintStream> getConnected(){
      return connected;
    }

    public void distribuiMensagem(String msg, Socket actual) {
      for (PrintStream cliente : connected.values()) {
        if(actual != null){
          if(cliente != this.connected.get(actual)) cliente.println(msg);
        } else cliente.println(msg);
      }
    }

    private void joinPlayers() throws IOException{
      Socket client = servidor.accept();
      System.out.println("Novo cliente "+ client.getPort());
      ServerThread clientSock = new ServerThread(client, connected, this);
      new Thread(clientSock).start();
      PrintStream ps = new PrintStream(client.getOutputStream());
      ps.println(Cli.openningGame());
      for (Socket sock : connected.keySet()){
        ps.println(Integer.toString(sock.getPort()));
      }
      distribuiMensagem(Integer.toString(client.getPort()), null);
      connected.put(client, ps);
    }
    long start = System.currentTimeMillis();;
    public void run(){


      try{
        while(escutando){
          joinPlayers();
          long end;
          if(connected.size() == 2) start = System.currentTimeMillis();
          end = System.currentTimeMillis();
          if((end - start)/60000 > 1 || connected.size() > 3){
            distribuiMensagem("COMECOUU", null);
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();

      }
      finally {
  			if (servidor != null) {
  				try {
  					servidor.close();
  				}
  				catch (IOException e) {
  					e.printStackTrace();
  				}
  			}
  		}

  }

    public static void main(String[] args){
        Server servidor = new Server();
        servidor.run();
    }
  }
