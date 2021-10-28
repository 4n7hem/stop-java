import java.io.*;
import java.net.*;
import java.util.*;


public class Client {

    private Socket socketCliente;
    private BufferedReader input;
    private PrintWriter output;
    private BufferedReader inputConfig;
    private InterfaceCli Cli;
    private Scanner sc = new Scanner(System.in);
    private String line = null;
    private boolean resp = false;

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

    public void viewPlayers() throws IOException{
      while(true){
        String answerServer = input.readLine();
        if(answerServer == null)
          System.out.println("Conexão com o server encerrada.");
        else {
          if(answerServer.equals("resp")){
            //System.out.println(answerServer);
            line = sc.nextLine();
            output.println(line);
          }
          System.out.println(answerServer);
        }
      }
    }


    public void write(){
      try{


        //
         while(!"exit".equalsIgnoreCase(line)){
          //output.println("ON");
  				//output.flush();

          String answerServer = input.readLine();
          if(answerServer == null)
            System.out.println("Conexão com o server encerrada.");
          else {
            if(answerServer.equals("resp")){
              //System.out.println(answerServer);
              line = sc.nextLine();
              output.println(line);

            }
            System.out.println(answerServer);
          }
         }
          sc.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
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
        while(input.readLine() != null) System.out.println("A");
        cliente.write();
    }
}
