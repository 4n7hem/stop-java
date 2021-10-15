
import java.io.*;
import java.net.*;
import stopjava.ServerThread;
import java.util.*;

public class Server{

    private ServerSocket servidor;
    private boolean escutando = true;
    // private InterfaceCli Cli = new InterfaceCli();
    private Map<Socket, ServerThread> connected = new HashMap<Socket, ServerThread>();

    public Server(){
        try{
            System.out.println("Iniciando o servidor....");
            this.servidor = new ServerSocket(8081);
            servidor.setReuseAddress(true);
            System.out.println("Servidor iniciado na porta 8081.");
        }
        catch(Exception e){
            System.out.println("ERRO");
            System.out.println(e.getMessage());
        }
    }

    // public void sendingListToClients(Socket connectedSocket, ServerThread connectedThread){
    //   for (Socket port : connected.keySet())
    //     System.out.println("aaaaaaaaaa");
    //     connected.get(port).clientMessage(connectedSocket, connectedThread);
    // }

    public void run(){

      try{
        while(escutando){
            Socket client = servidor.accept();
            System.out.println("Novo cliente "+ client.getInetAddress().getHostAddress());
            ServerThread clientSock = new ServerThread(client);
            new Thread(clientSock).start();
            // System.out.println(Cli.openningGame(""));
            // if(connected.isEmpty()) clientSock.clientMessage(Cli.openningGame("First"));
            // else clientSock.clientMessage(Cli.openningGame(""));
            //sendingListToClients(client, clientSock);
            connected.put(client, clientSock);
            //for (Socket porta : connected.keySet())  System.out.print(porta.getPort() + " ");
        }
      }
      catch(IOException e){
        e.printStackTrace();
         // System.out.println(e.getMessage());
         // System.exit(-1);
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
