public class Pizza
{
    public String _id;
    public String nome;
    public String ingredienti;
    public float prezzo;

    public Pizza() {}

    public Pizza(String nome, String ingredienti, float prezzo) {
        this.nome = nome;
        this.ingredienti = ingredienti;
        this.prezzo = prezzo;
    }

    @Override
    public String toString(){
        return "pizza: " + nome + "; ingredienti: " + ingredienti + "; prezzo: " + prezzo + "€";
    }
}

