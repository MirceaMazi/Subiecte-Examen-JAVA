public class Factura {
    String nume;
    int varsta;
    int sumaPlata;

    public Factura(String nume, int varsta, int sumaPlata) {
        this.nume = nume;
        this.varsta = varsta;
        this.sumaPlata = sumaPlata;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public int getSumaPlata() {
        return sumaPlata;
    }

    public void setSumaPlata(int sumaPlata) {
        this.sumaPlata = sumaPlata;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", sumaPlata=" + sumaPlata +
                '}';
    }
}
