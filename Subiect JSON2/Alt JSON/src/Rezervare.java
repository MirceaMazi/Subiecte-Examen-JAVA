public class Rezervare {
    String idRezervare;
    int codRezervare;
    int nrLocuriRezervate;

    public Rezervare(String idRezervare, int codRezervare, int nrLocuriRezervate) {
        this.idRezervare = idRezervare;
        this.codRezervare = codRezervare;
        this.nrLocuriRezervate = nrLocuriRezervate;
    }

    public String getIdRezervare() {
        return idRezervare;
    }

    public void setIdRezervare(String idRezervare) {
        this.idRezervare = idRezervare;
    }

    public int getCodRezervare() {
        return codRezervare;
    }

    public void setCodRezervare(int codRezervare) {
        this.codRezervare = codRezervare;
    }

    public int getNrLocuriRezervate() {
        return nrLocuriRezervate;
    }

    public void setNrLocuriRezervate(int nrLocuriRezervate) {
        this.nrLocuriRezervate = nrLocuriRezervate;
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "idRezervare='" + idRezervare + '\'' +
                ", codRezervare=" + codRezervare +
                ", nrLocuriRezervate=" + nrLocuriRezervate +
                '}';
    }
}
