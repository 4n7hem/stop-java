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

        while (this.connected.size() <2) {
          System.out.println(this.connected.size());
          Socket client = servidor.accept();
          servidor.setReuseAddress(true);
          System.out.println("Nova conexao com " +
              Integer.toString(client.getPort()));
          distribuiMensagem(Integer.toString(client.getPort())+"\n");

          PrintStream ps = new PrintStream(client.getOutputStream());
          for (Socket sock : connected.keySet()){
            ps.println(Integer.toString(sock.getPort())+"\n");
          }
          ThreadGame tc = new ThreadGame(client.getInputStream(), this, client, ps);
          Thread T = new Thread(tc);
          this.TCli.put(tc, T);
          //TCli.add(T);
          tc.start();
          this.connected.put(client, ps);
        }
      //for(Thread t : TCli) t.interrupt();
    }

    public Map<Socket, PrintStream> getConnected(){
      return this.connected;
    }

    public void run() throws IOException {
      try{
        entraJogadores();
        jogo = new OJogo();
        for(int n=0; n<this.connected.size(); n++){
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

          }
          distribuiMensagem(jogo.getUserBateu()+" BATEU!\n");
          //Thread.sleep(40000);

          System.out.println(jogo.getPontuacao());
          for(Row r : jogo.palavrasAtuais){
            System.out.println("CAT: "+ r.nomeDaCategoria);
            for(String j : r.jogadorResposta.keySet())
              System.out.println(r.jogadorResposta.get(j));
          }
          jogo.calcularFrequencia();
          distribuiMensagem("Aqui vem o ranking");
          distribuiMensagem(Cli.rankingGame(jogo.getPontuacao()));
        }


//     COMO FAZER O STOP?
//    - botar no ThreadGame um while enquanto tiver respostas passar pelas categorias
//    - printar na tela pelo distribuirMensagem e pegar pelo sc
//    - jogar no objeto jogo do objeto servidor la dentro
//    - aqui no serve vai ser o congelamento das threads depois do tempo

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
