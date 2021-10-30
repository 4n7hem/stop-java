
import java.io.*;
import java.net.*;
import java.util.*;

public class InterfaceCli{
  private static Timer timer;
  private static int interval;
  public String openningGame(){
    String text = "=================================================\n"+
                  "                     STOPEEE!                    \n"+
                  "=================================================\n";
    text+="Digite [SAIR] em qualquer momento do jogo para sair.\n";
    text+="Aguardando pelo menos mais 2 jogadores...\n";
    text+= "Jogadores disponiveis:\n";
    return text;
  }

  public String rankingGame(Map<String, Integer> rank){
    String ranking = " RANKING DA RODADA \n====================\n";
    for (String player : rank.keySet()){
      ranking += player+"..........."+
      Integer.toString(rank.get(player))+"\n";
    }
    return ranking;
  }

  public String finalRank(HashMap<Socket, Integer> rank){
    return "";
  }

  public String aLetra(char letraAtual, int n){
    String text = "=== RODADA "+n+" ===\n";
    return text+="A letra e: "+Character.toUpperCase(letraAtual);
  }

  private static final int setInterval() {
    if (interval == 2)
        timer.cancel();
    return --interval;
  }

  public void contagemRegr(Server serv)throws InterruptedException{
    timer = new Timer();
    interval = 4;
    timer.schedule(new TimerTask() {
        public void run() {
            serv.distribuiMensagem("    - "+Integer.toString(setInterval())+" -");
        }
    }, 1000, 1000);
    Thread.sleep(4000);
    serv.distribuiMensagem("  == JA! ==");
  }

  public String fimRodada(String user){
    String text = "\n"+user+" disse STOPEEE!\n";
    text += "\n=== FIIIIM DE RODADA ===\n\n";
    return text;
  }

  public String rankingFinal(Map<String, Integer> rank, String user){
    String ranking = "\n=== FIIIM DE JOOOGO! ===\n";
    ranking += "O vencedor e: "+user+"!!!\n";
    ranking += "      RANKING FINAL \n====================\n";
    for (String player : rank.keySet()){
      ranking += player+"..........."+
      Integer.toString(rank.get(player))+"\n";
    }
    return ranking;
  }

}
