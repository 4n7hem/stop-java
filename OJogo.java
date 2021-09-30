import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class OJogo {

    private char letraAtual;
    private Random r = new Random();
    private HashMap<String, Integer> pontuacao = new HashMap<String,Integer>();
    private HashMap<String, String> palavrasAtuais = new HashMap<String,String>();

    public OJogo(){
        /* Eu sou imune a essa piada, mas que vocês sofram aí. */
        System.out.println("Que os jogos comecem.");

    }


    /* rola o d20 */

    public void rerollLetra(){
        this.letraAtual = (char) (r.nextInt(26) + 'a');
    }

    /* O char da letra atual da rodada, para o servidor mandar aos clientes */

    public char getLetra(){
        return letraAtual;
    }

    /* O servidor adiciona as respostas de usuário que recebe */

    public void addResposta(String user, String texto){
        palavrasAtuais.put(user, texto);
    }

    public void calcularFrequencia(){
        final Map<String, Integer> innerCounter = new HashMap<>(); /* isto conta frequência de palavras no palavraAtuais*/
        for (String token : palavrasAtuais.keySet()) {             /* essa informação será passada ao protocolo abaixo */
            if (innerCounter.containsKey(token)) {
                int value = innerCounter.get(token);
                innerCounter.put(token, ++value);
            } else {
                innerCounter.put(token, 1);
            }
        }

        /* adicionar pontos no pontuação dado a frequência*/

        for (String palavra : innerCounter.keySet()){
           for(String nome : pontuacao.keySet()){
               if( palavra == palavrasAtuais.get(nome)){
                Integer pontAntiga = pontuacao.get(nome);
                pontuacao.put(nome, Integer.sum(pontAntiga, protocoloDePontos(innerCounter.get(palavra))));
               }
           }
        }

    }

    public Integer protocoloDePontos(Integer entrada){

        /* Se quiser rebalancear o jogo, troque os valores aqui. */

        if(entrada.equals(1)){return Integer.valueOf(25);}
        else if(entrada.equals(2)){return Integer.valueOf(15);}
        else if(entrada.equals(3)){return Integer.valueOf(10);}
        else return Integer.valueOf(5);
    }
    
}
