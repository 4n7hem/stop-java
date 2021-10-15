package stopjava;
import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Map<Socket, ServerThread> connected = new HashMap<Socket, ServerThread>();

    public ServerThread(Socket socket){
        super("ServerThread");
        this.socket = socket;
        try{
            this.in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Conexao feita: " + socket.getPort());
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void clientMessage(Socket socket, ServerThread serverThread){
      out.println(socket);
      out.println("teste");
      connected.put(socket, serverThread);
    }
    boolean begin = true;
    public void run(){
        // while(true){
            try{
                String inputLine;
                while(!(inputLine = in.readLine()).equalsIgnoreCase("exit")){
                    if(inputLine.equals("ON") && begin) {
                      out.println("=======================================\n"+
                                  "                STOPEEE!               \n"+
                                  "=======================================\n");
                      begin = false;
                    }
                    else System.out.println("Mensagem recebida: "+inputLine);
                    //out.println(inputLine.toString().length());
                }

            }
            catch(IOException e){
                e.printStackTrace();
            }
        //}
        finally {
  				try {
  					if (out != null) {
              System.out.println("Conexao com "+socket.getPort()+" encerrada.");
  						out.close();
  					}
  					if (in != null) {
  						in.close();
  						socket.close();
  					}
  				}
  				catch (IOException e) {
  					e.printStackTrace();
  				}
  			}
    }
}
