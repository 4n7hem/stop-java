
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Server{

    private ServerSocket servidor;
    private boolean escutando = true;
    private List<PrintStream> clientes;
    private Map<Socket, PrintStream> connected;
    private ArrayList<ServerThread> servTreads;
    private InterfaceCli Cli;
    private Timer timeIn;
    private long timeInit = System.currentTimeMillis();
    private long timeEnd;

    public Server(){
        try{
            System.out.println("Iniciando o servidor....");
            this.servidor = new ServerSocket(8081);
            servidor.setReuseAddress(true);
            System.out.println("Servidor iniciado na porta 8081.");
            connected = new LinkedHashMap<Socket, PrintStream>();
            servTreads = new ArrayList<ServerThread>();
            timeIn = new Timer();
            Cli = new InterfaceCli();
        }
        catch(Exception e){
            System.out.println("ERRO");
            System.out.println(e.getMessage());
        }
    }

    private void countingTime(){
      for (ServerThread th : servTreads){
        timeIn.schedule(th, 5*1000);
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
      servTreads.add(clientSock);
      new Thread(clientSock).start();
      PrintStream ps = new PrintStream(client.getOutputStream());
      ps.println(Cli.openningGame());
      for (Socket sock : connected.keySet()){
        ps.println(Integer.toString(sock.getPort()));
      }
      distribuiMensagem(Integer.toString(client.getPort()), null);
      connected.put(client, ps);
    }
    public void run(){
      long wait = 0;
      try{
        while(escutando && connected.size() < 3){
          //if(connected.size() == 2) timeInit = System.currentTimeMillis();
          joinPlayers();
          // System.out.println(connected.size());
          // if(connected.size() >= 2 && connected.size() < 4){
          //   timeEnd = System.currentTimeMillis();
          //   wait = TimeUnit.MILLISECONDS.toSeconds(timeEnd - timeInit);
          //   System.out.println(wait);
          // }
          // if(wait > 15){
          //   distribuiMensagem("CABOUU", null);
          //   escutando = !escutando;
          //   break;
          // }

        }
        distribuiMensagem("Tem "+Integer.toString(connected.size())+" aqui.\n", null);
        
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
