public class Aventura {
    int cod;
    String denumire;
    Double tarif;
    int locuriDisponibile;

    public Aventura(int cod, String denumire, Double tarif, int locuriDisponibile) {
        this.cod = cod;
        this.denumire = denumire;
        this.tarif = tarif;
        this.locuriDisponibile = locuriDisponibile;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Double getTarif() {
        return tarif;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    public int getLocuriDisponibile() {
        return locuriDisponibile;
    }

    public void setLocuriDisponibile(int locuriDisponibile) {
        this.locuriDisponibile = locuriDisponibile;
    }

    @Override
    public String toString() {
        return "Aventura{" +
                "cod=" + cod +
                ", denumire='" + denumire + '\'' +
                ", tarif=" + tarif +
                ", locuriDisponibile=" + locuriDisponibile +
                '}';
    }
}
