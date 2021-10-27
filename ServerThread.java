import java.net.*;
import java.io.*;
import java.util.*;


public class ServerThread extends TimerTask implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private InterfaceCli Cli;
    private Socket newCli;
    private boolean once = false;
    private Server servidor;

    public ServerThread(Socket socket, Map conectList, Server servidor){
        this.servidor = servidor;
        this.Cli = new InterfaceCli();
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

    //MINIMO 2 E MAXIMO 4
    //System.currentTimeMillis(); div por 60000 = minutos

    public void run(){
        try{
          //while(true){
            String inputLine = "";
          //  while(!inputLine.equalsIgnoreCase("exit")){
            out.println("resp");

              if((inputLine = in.readLine()) == null){
                inputLine = 
                //begin = true;
              }
               //aqui vai ter que ter uma segurada na thread para liberar o scanner do cliente.
            //}
          //}
        }
        catch(IOException e){
            e.printStackTrace();
        }

        finally {
  				try {
  					if (out != null) {
              System.out.println("Conexao com "+socket.getPort()+" encerrada.");
              servidor.distribuiMensagem(Integer.toString(socket.getPort())+" saiu.", null);
              servidor.getConnected().remove(this.socket);
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
