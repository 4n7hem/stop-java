import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class OJogo {

    public final String[] categorias = {"Nome", "Animal", "Cor", "Verbo", "País", "Objeto"};
    private char letraAtual;
    private Random r = new Random();
    private long tempoInicial;
    private HashMap<String, Integer> pontuacao = new HashMap<String,Integer>();
    private ArrayList<Row> palavrasAtuais;

    public OJogo(){
        /* Eu sou imune a essa piada, mas que vocês sofram aí. */
        System.out.println("Que os jogos comecem.");
        for(String categoria : categorias){
            palavrasAtuais.add(new Row(categoria));
        }
    }

    /* rola o d20 */

    public void rerollLetra(){
        char letraAnterior = this.letraAtual;
        while(letraAnterior == this.letraAtual){
            this.letraAtual = (char) (r.nextInt(26) + 'a');
        }        
    }

    /* O char da letra atual da rodada, para o servidor mandar aos clientes */

    public char getLetra(){
        return letraAtual;
    }

    /* O servidor adiciona as respostas de usuário que recebe */

    public void addResposta(String user, String texto, String categoria){
        for(Row linha : palavrasAtuais){
            if(linha.nomeDaCategoria == categoria){
                linha.inserirResposta(user, texto);
                break;
            }
        }
    }

    public void calcularFrequencia(){
       /* Com as frequencias de palavras de cada Row, passe pelo protocolo de pontos */
        /* E adicione pontuações aos jogadores corretos em pontuação */
    }

    public Integer protocoloDePontos(Integer entrada){

        /* Se quiser rebalancear o jogo, troque os valores aqui. */

        if(entrada.equals(1)){return Integer.valueOf(25);}
        else if(entrada.equals(2)){return Integer.valueOf(15);}
        else if(entrada.equals(3)){return Integer.valueOf(10);}
        else return Integer.valueOf(5);
    }
    
    public void limparRespostas(){
        this.palavrasAtuais.clear();
    }

    public String terminarOJogo(){

        Map.Entry<String,Integer> maxEntry = null;

        for(Map.Entry<String, Integer> entry : pontuacao.entrySet()){
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
                maxEntry = entry;
            }
        }
        this.limparRespostas();
        this.pontuacao.clear();

        /* Vamos passar quem venceu à camada acima, para que o server possa anunciar. */
        return "O vencedor foi: " + maxEntry.getKey();
    }

    public void startTimer(){
        this.tempoInicial = System.currentTimeMillis();
    }

    public long checarTimer(){
        long deltaTempo = System.currentTimeMillis() - tempoInicial;
        return deltaTempo;
    }

    
}


