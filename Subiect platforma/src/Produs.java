public class Produs {
    int idProduse;
    String denumire;
    double pret;

    public Produs(int idProduse, String denumire, double pret) {
        this.idProduse = idProduse;
        this.denumire = denumire;
        this.pret = pret;
    }

    public int getIdProduse() {
        return idProduse;
    }

    public void setIdProduse(int idProduse) {
        this.idProduse = idProduse;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Produs{" +
                "idProduse=" + idProduse +
                ", denumire='" + denumire + '\'' +
                ", pret=" + pret +
                '}';
    }
}
