import java.util.Random;

public class OJogo {

    private char letraAtual;
    private Random r = new Random();

    public OJogo(){

        this.letraAtual = (char) (r.nextInt('z' - 'a') + 'a');
        System.out.println("Que os jogos comecem");

    }
    
}
