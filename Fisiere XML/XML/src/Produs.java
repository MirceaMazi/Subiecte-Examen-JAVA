final public class Produs {
    private final int cod;
    private final String denumire;
    private final int nrTranzactii;

    public Produs(int cod, String denumire, int nrTranzactii) {
        this.cod = cod;
        this.denumire = denumire;
        this.nrTranzactii = nrTranzactii;
    }

    public int getCod() {
        return cod;
    }

    public String getDenumire() {
        return denumire;
    }

    public int getNrTranzactii() {
        return nrTranzactii;
    }

    @Override
    public String toString() {
        return "Produs{" +
                "cod=" + cod +
                ", denumire='" + denumire + '\'' +
                ", nrTranzactii=" + nrTranzactii +
                '}';
    }
}
