package stopjava;

import java.util.HashMap;
import java.util.Map;

public class Row {
    public String nomeDaCategoria;
    public HashMap<String, String> jogadorResposta;

    public Row(String categoria){
        this.nomeDaCategoria = categoria;
        this.jogadorResposta = new HashMap<String,String>();
    }

    public void inserirResposta(String jogador, String resposta){
        jogadorResposta.put(jogador, resposta);
    }

    public Map<String, Integer> calcularFrequencia(){
        Map<String, Integer> resultado = new HashMap<String, Integer>();
        /*Esse método precisa contar a frequência de palavras repetidas no jogadorResposta */
        return resultado;
    }
    
}
