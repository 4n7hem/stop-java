import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;

public class OJogo {

    public final String[] categorias = {"Nome", "Animal", "Cor", "Verbo", "Pais", "Objeto"};
    private Set<Socket> listaDeUsuarios;
    private char letraAtual;
    private Random r = new Random();
    private boolean bateu;
    private String userBateu;
    private long tempoInicial;
    private HashMap<String, Integer> pontuacao = new HashMap<String,Integer>();
    public ArrayList<Row> palavrasAtuais = new ArrayList<Row>();
    public ArrayList<Integer> ordemAl;

    public OJogo(Set<Socket> connected){
        /* Eu sou imune a essa piada, mas que vocês sofram aí. */
        System.out.println("Que os jogos comecem.");
        ordemAl = ordemAleat();
        this.listaDeUsuarios = connected;
        for(Socket jogador : listaDeUsuarios){
                pontuacao.put(String.valueOf(jogador.getPort()), 0);
            }
        for(String categoria : categorias){
            palavrasAtuais.add(new Row(categoria));
        }
    }

    public boolean getBateu(){
      return bateu;
    }

    public String getUserBateu(){
      return userBateu;
    }

    public void bateu(String user){
      this.bateu = true;
      userBateu = user;
    }

    public void rearranjo(){
        ordemAl = ordemAleat();
    }

    public ArrayList<Integer> ordemAleat(){
      Random random = new Random();
      ArrayList<Integer> arrayList = new ArrayList<Integer>();
      while (arrayList.size() < this.categorias.length) {
        int a = random.nextInt(this.categorias.length);
        if (!arrayList.contains(a)) arrayList.add(a);
      }
      return arrayList;
    }

    /* rola o d26 */

    public void rerollLetra(){
        bateu = false;
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
        //linha = 1 cat por loop
        
        for(Row linha : palavrasAtuais){
            System.out.println("CAT: "+linha.nomeDaCategoria);
            for(String cat : linha.jogadorResposta.keySet()){
                System.out.println("USER: "+cat+" RESP: "+linha.jogadorResposta.get(cat));
            }
            Map<String, Integer> resultado = linha.calcularFrequencia();
            for(String user : resultado.keySet()){
                if(pontuacao.containsKey(user)){
                    //a freq. de repeticoes deve passar pelo protocoloDePontos
                    pontuacao.put(user, pontuacao.get(user) + protocoloDePontos(resultado.get(user)));
                } 
                else{
                    pontuacao.put(user, protocoloDePontos(resultado.get(user)));
                }               
            }
            linha.limpar();          
        }
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
        return maxEntry.getKey();
    }

    public void startTimer(){
        this.tempoInicial = System.currentTimeMillis();
    }

    public long checarTimer(){
        long deltaTempo = System.currentTimeMillis() - tempoInicial;
        return deltaTempo;
    }

    public HashMap<String, Integer> getPontuacao(){
        return pontuacao;
    }
}
