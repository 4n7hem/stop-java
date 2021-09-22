
import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socketCliente;
    private BufferedReader input;
    private PrintWriter output;
    private BufferedReader inputConfig;

    public Client(){
        try{
            inputConfig= new BufferedReader(new InputStreamReader(System.in));
            socketCliente = new Socket(host(), porta());
            output = new PrintWriter(socketCliente.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void write(){
        while(true){        
        try{            
            String servidorResposta; 
            String inputUsuario;      
            while(true){
                inputUsuario = inputConfig.readLine();
                if(inputUsuario != null){
                    output.print(inputUsuario);
                }
                if((servidorResposta = this.input.readLine()) != null){
                    System.out.println("Server: "+ servidorResposta);
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        }
        
    }
    
    private String host(){
        System.out.println("Diga o host: ");
        try{
            return inputConfig.readLine();
        }        
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return "";
        
    }
    
    private int porta(){

                System.out.println("Diga a porta: ");
        try{
            return Integer.parseInt(inputConfig.readLine());
        }        
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return 8080;
    }

    public static void main(String[] args){
        Client cliente = new Client();
        cliente.write();
    }
}
