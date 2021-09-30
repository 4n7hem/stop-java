package stopjava;

import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket socket){
        super("ServerThread");
        this.socket = socket;
        try{ 
            this.in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Conexao feita: " + socket.toString());
        }
        catch(IOException e){
            e.printStackTrace();
        }        
    }
    
    public void run(){
        while(true){
            try{
                String inputLine;
                while((inputLine = in.readLine()) != null){
                    System.out.println("Mensagem recebida.");
                    out.println("Pong");
                    break;
                }
                
            }
            catch(IOException e){
                System.out.println(e.getMessage() + ": " + socket.getPort());
                break;           
            }
        }
        try{
            socket.close();
        } 
        catch(IOException e){
            System.out.println(e.getMessage());
        }        
    }
}
