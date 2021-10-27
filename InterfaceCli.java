
import java.io.*;
import java.net.*;
import java.util.*;

public class InterfaceCli{
  public String openningGame(){
    String text = "=================================================\n"+
                  "                     STOPEEE!                    \n"+
                  "=================================================\n";
    text+="Aguardando pelo menos mais 2 jogadores...\n";
    text+= "Jogadores disponiveis:\n";
    return text;
  }

  public String rankingGame(HashMap<Socket, Integer> rank){
    String ranking = "RANKING \n==================\n";
    for (Socket player : rank.keySet()){
      ranking += Integer.toString(player.getPort())+"..........."+
      Integer.toString(rank.get(player))+"\n";
    }
    return ranking;
  }

  public String aLetra(char letraAtual){
    return "A letra e: "+letraAtual+"\n";
  }

}
