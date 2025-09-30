public class Pizza
{
    public String nome;
    public String ingredienti;
    float prezzo;

    @Override
    public String toString(){
        return "pizza: " + nome + "; ingredienti: " + ingredienti + "; prezzo: " + prezzo + "€";
    }
}

