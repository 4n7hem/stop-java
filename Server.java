
import java.io.IOException;
import java.net.ServerSocket;
import stopjava.ServerThread;

public class Server{
    
    private ServerSocket servidor;    
    private boolean escutando = true;

    public Server(){
        try{
            System.out.println("Iniciando o servidor....");
            this.servidor = new ServerSocket(8081);
            System.out.println("Servidor iniciado na porta 8081.");
        }
        catch(Exception e){
            System.out.println("ERRO");
            System.out.println(e.getMessage());
        }
    }

    public void run(){
        while(escutando){
            try{                
                new ServerThread(servidor.accept()).run();
            }
            catch(IOException e){
               System.out.println(e.getMessage());
               System.exit(-1);  
            }
        }
    }

    public static void main(String[] args){
        Server servidor = new Server();
        servidor.run();
    }

}