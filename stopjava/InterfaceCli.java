package stopjava;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;

public class InterfaceCli{
  public String openningGame(String OrderPlayer){
    String text = "=======================================\n"+
                  "                STOPEEE!               \n"+
                  "=======================================\n";
    if(OrderPlayer.equals("First")) text+="Digite [S] para iniciar ou digite [E] para sair.\n";
    text+= "Jogadores disponiveis:\n";
    return text;
    // System.out.println("==================================================================================================================");
    // System.out.println("                                                   STOPEEE!                                                       ");
    // System.out.println("==================================================================================================================");
    // if(OrderPlayer.equals("First")) System.out.println("Digite [S] para iniciar o jogo ou  digite [E] para sair.");
    // System.out.println("Jogadores disponíveis:");
  }

  public void waitingPlayers(ArrayList Players){
    //Iterator<Client> CliIt = Players.iterator();

    // while(CliIt.hasNext()){
    //
    // }
  }


  public void rankingGame(HashMap rank){

  }

}
