package stopjava;
import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Map<Socket, ServerThread> connected = new HashMap<Socket, ServerThread>();
    private Socket newCli;
    private boolean begin = false;
    private boolean theFirst = false;
    private boolean once = false;

    public ServerThread(Socket socket, Map conectList){
        super("ServerThread");
        this.connected = conectList;
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

    public void clientMessage(Socket socket){
      this.newCli = socket;
    }


    public void run(){
            try{
              while(true){
                String inputLine;
                while(!(inputLine = in.readLine()).equalsIgnoreCase("exit")){
                    if(inputLine.equals("ON") && !begin){
                      if(connected.size() == 1 && !theFirst){
                        out.println("Digite [S] para iniciar ou digite [E] para sair.");
                        theFirst = true;
                      }
                      if(!once){
                        out.println("Jogadores disponiveis:");
                        for (Socket port : connected.keySet())
                          if(port.getPort() == socket.getPort()) out.println("Voce");
                          else out.println(port.getPort());
                        once = !once;
                      }
                      System.out.println(newCli.getPort());
                      //out.println(newCli.getPort());
                      connected.put(newCli, this);


                      //
                      //begin = false;
                    }
                    else System.out.println("Mensagem recebida: "+inputLine);
                    //out.println(inputLine.toString().length());
                }
              }

            }
            catch(IOException e){
                e.printStackTrace();
            }

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
