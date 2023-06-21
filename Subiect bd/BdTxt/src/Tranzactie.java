public class Tranzactie {
    int cod;
    String simbol;
    String tip;
    int cantitate;
    float pret;

    public Tranzactie(int cod, String simbol, String tip, int cantitate, float pret) {
        this.cod = cod;
        this.simbol = simbol;
        this.tip = tip;
        this.cantitate = cantitate;
        this.pret = pret;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getSimbol() {
        return simbol;
    }

    public void setSimbol(String simbol) {
        this.simbol = simbol;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(float pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "cod=" + cod +
                ", simbol='" + simbol + '\'' +
                ", tip='" + tip + '\'' +
                ", cantitate=" + cantitate +
                ", pret=" + pret +
                '}';
    }
}
