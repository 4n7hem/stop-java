import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;


public class Server {

    private Map<Socket, PrintStream> connected;
    private InterfaceCli Cli = new InterfaceCli();
    private Map<ThreadGame, Thread> TCli;
    private ServerSocket servidor;
    private int porta;
    public OJogo jogo;

    public static void main(String[] args) throws IOException {
        // inicia o servidor
        new Server(8081).run();
    }


    public Server (int porta) {
        this.porta = porta;
        this.connected = new LinkedHashMap<Socket, PrintStream>();
        this.TCli = new LinkedHashMap<ThreadGame, Thread>();
    }

      public void entraJogadores()throws Exception{
        servidor = new ServerSocket(this.porta);
        System.out.println("Server ON");

        //aceitando n jogadores para começar
        while (this.connected.size() < 2) {
          System.out.println(this.connected.size());
          Socket client = servidor.accept();
          servidor.setReuseAddress(true);
          System.out.println("Nova conexao com " +
              Integer.toString(client.getPort()));

          //mostra pros clientes quem está ON
          distribuiMensagem(Integer.toString(client.getPort())+"\n");

          PrintStream ps = new PrintStream(client.getOutputStream());
          for (Socket sock : connected.keySet()){
            ps.println(Integer.toString(sock.getPort())+"\n");
          }
          ThreadGame tc = new ThreadGame(client.getInputStream(), this, client, ps);
          Thread T = new Thread(tc);
          this.TCli.put(tc, T);
          tc.start();
          this.connected.put(client, ps);
        }
    }

    public Map<Socket, PrintStream> getConnected(){
      return this.connected;
    }

    public void rodada(int n) throws InterruptedException{
      jogo.rerollLetra();
      distribuiMensagem(Cli.aLetra(jogo.getLetra(), n+1));
      Cli.contagemRegr(this);
      for(ThreadGame t : TCli.keySet()){
        synchronized (t) {
          t.wait = !t.wait; //false
          t.notify();
        }
      }
      while(!jogo.getBateu()){
        Thread.sleep(500);
      }

      distribuiMensagem(Cli.fimRodada(jogo.getUserBateu()));
      Thread.sleep(2000);
      jogo.calcularFrequencia();

      distribuiMensagem(Cli.rankingGame(jogo.getPontuacao()));
      Thread.sleep(3000);
    }

    public void run() throws IOException {
      try{
        entraJogadores();
        jogo = new OJogo(this.connected);
        //qtd rodadas = qtd jogadores para teste
        for(int n = 0; n<this.connected.size(); n++){
          rodada(n);
        }

        //qtd rodadas = qtd jogadores para teste
        for(int n = 0; n<this.connected.size(); n++) rodada(n);

        //fim de jogo



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
        // envia msg para todo mundo
        for (PrintStream cliente : this.connected.values()) {
            cliente.println(msg);
        }
    }
}
