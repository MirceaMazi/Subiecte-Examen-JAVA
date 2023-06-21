public class Factura {
    String denumire;
    String repartizare;
    int valoare;

    public Factura(String denumire, String repartizare, int valoare) {
        this.denumire = denumire;
        this.repartizare = repartizare;
        this.valoare = valoare;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getRepartizare() {
        return repartizare;
    }

    public void setRepartizare(String repartizare) {
        this.repartizare = repartizare;
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "denumire='" + denumire + '\'' +
                ", repartizare='" + repartizare + '\'' +
                ", valoare=" + valoare +
                '}';
    }
}
