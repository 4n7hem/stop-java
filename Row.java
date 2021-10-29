
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

    public int contarRepeticao(String palavra){
        int count = 0;
        for(Map.Entry<String, String> entry : jogadorResposta.entrySet()){
            //o lowercase é para filtrar palavras iguais mas com caracteres diferentes
            if(entry.getValue().equals(palavra.toLowerCase())){
                count++;
            }
        }
        return count;
    }

    public Map<String, Integer> calcularFrequencia(){ //conta a quantidade de repeticoes de respostas
        Map<String, Integer> resultado = new HashMap<String, Integer>();
        
        for(Map.Entry<String, String> entry : jogadorResposta.entrySet()){
            String palavra = entry.getValue();
            String jogador = entry.getKey();
            int frequencia = contarRepeticao(palavra);
            //registra o user e quantas vezes a resposta dele foi usada na categoria
            resultado.put(jogador, frequencia); 
        }
        //o retorno é passado de volta ao Jogo.
        return resultado;
    }    
    
}