import java.util.*;
import java.net.*;
import java.io.*;


public class Server {

    private Map<Socket, PrintStream> connected;
    private InterfaceCli Cli = new InterfaceCli();
    private Map<ThreadGame, Thread> TCli;
    private ServerSocket servidor;
    private int porta;
    private int qtdjogadores;
    private int qtdrodadas;
    public OJogo jogo;

    public static void main(String[] args) throws IOException {
        // inicia o servidor com quantidade de jogadores e de rodadas
        try{
          new Server(8081, 2, 3).run();
        }catch(Exception e){
          e.printStackTrace();
        }
        
    }

    public Server (int porta, int nplayers, int nrounds) {
        this.qtdjogadores = nplayers;
        this.qtdrodadas = nrounds;
        this.porta = porta;
        this.connected = new LinkedHashMap<Socket, PrintStream>();
        this.TCli = new HashMap<ThreadGame, Thread>();
    }

    public void entraJogadores()throws Exception{
      servidor = new ServerSocket(this.porta);
      System.out.println("Server ON");

      //aceitando n jogadores para começar
      while (this.connected.size() < qtdjogadores) {
        Socket client = servidor.accept();
        servidor.setReuseAddress(true);

        System.out.println("Conexao com " +
            Integer.toString(client.getPort()));

        //mostra pros clientes quem está ON
        distribuiMensagem(Integer.toString(client.getPort())+"\n");

        //armazenando streams e sockets
        PrintStream ps = new PrintStream(client.getOutputStream());
        ps.println(Cli.openningGame(qtdjogadores));
        for (Socket sock : connected.keySet())
          ps.println(Integer.toString(sock.getPort())+"\n");
        ThreadGame tc = new ThreadGame(client.getInputStream(), this, client, ps);
        this.TCli.put(tc, null);
        this.connected.put(client, ps);
      }
    }

    //thread para checar constantemente se alguém saiu do jogo
    public void checkConnect() throws IOException{
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
          public void run() {
            for(Socket sk : connected.keySet()){
              if(connected.get(sk).checkError()){
                try{
                  System.out.println("Conexao com "+Integer.toString(sk.getPort())+" encerrada.");
                  distribuiMensagem(Integer.toString(sk.getPort())+" saiu.");
                  connected.remove(sk);
                  sk.close();
                }catch(IOException e){
                  e.printStackTrace();
                } 
              }
            }
          }
      }, 1000, 1000);
    }

    public Map<Socket, PrintStream> getConnected(){
      return this.connected;
    }

    public void rodada(int n) throws InterruptedException, IOException{
      //sorteia letra da rodada, rearranja as categorias para exibir e anuncia a letra 
      jogo.rerollLetra();
      jogo.rearranjo();
      distribuiMensagem(Cli.aLetra(jogo.getLetra(), n+1));
      Cli.contagemRegr(this);

      //starta as threads que preenchem a rodada
      for(ThreadGame t : TCli.keySet()){      
        this.TCli.put(t, new Thread(t));
        TCli.get(t).start();
      }
      //enquanto ninguem bateu STOP nas threads, segura o server
      while(!jogo.getBateu()) Thread.sleep(500);

      try {
        //interrupção das threads após alguem bater STOP
        for(ThreadGame t : TCli.keySet()) TCli.get(t).interrupt();
      } catch (Exception ex) {
          ex.printStackTrace();
      } 

      //mostrando ranking da rodada
      distribuiMensagem(Cli.fimRodada(jogo.getUserBateu()));
      Thread.sleep(2000);
      jogo.calcularFrequencia();

      distribuiMensagem(Cli.rankingGame(jogo.getPontuacao()));
      Thread.sleep(3000);
    }

    public void run() throws Exception {
      //aguarda players ate quantidade estipulada
      entraJogadores();
      //checa se alguém caiu ou abandonou a partida
      checkConnect();

      try{
      jogo = new OJogo(this.connected.keySet());

      //rodadas de  jogo
      for(int n = 0; n<qtdrodadas; n++) rodada(n);

      //fim de jogo
      distribuiMensagem(Cli.Final(jogo.terminarOJogo()));
      Cli.clapClap(this);

      return;

      }catch(Exception e){
        e.printStackTrace();
      }
      finally{
        if (servidor != null){
  				try {
  					servidor.close();
  				}
  				catch (IOException e){
  					e.printStackTrace();
  				}
  			}
      }

    }

    public void distribuiMensagem(String msg) {
        // envia msg para todos os sockets
        for (PrintStream cliente : this.connected.values()) {
            cliente.println(msg);
        }
    }
}
