package stopjava;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

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

    public void clientMessage(String text){
      out.println(text);
    }

    public void run(){
        // while(true){
            try{
                String inputLine;
                while(!(inputLine = in.readLine()).equalsIgnoreCase("exit")){
                    System.out.println("Mensagem recebida: "+inputLine);
                    out.println(inputLine.toString().length());
                    //break;
                }
            }
            catch(IOException e){
                e.printStackTrace();
                //break;
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
