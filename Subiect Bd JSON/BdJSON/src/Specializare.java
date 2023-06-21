public class Specializare {
    int cod;
    String denumire;
    int locuri;

    public Specializare(int cod, String denumire, int locuri) {
        this.cod = cod;
        this.denumire = denumire;
        this.locuri = locuri;
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

    public int getLocuri() {
        return locuri;
    }

    public void setLocuri(int locuri) {
        this.locuri = locuri;
    }

    @Override
    public String toString() {
        return "Specializare{" +
                "cod=" + cod +
                ", denumire='" + denumire + '\'' +
                ", locuri=" + locuri +
                '}';
    }
}
