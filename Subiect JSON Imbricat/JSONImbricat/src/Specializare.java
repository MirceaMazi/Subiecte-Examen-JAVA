public class Specializare {
    int codSpecializare;
    int numarLocuri;

    public Specializare(int codSpecializare, int numarLocuri) {
        this.codSpecializare = codSpecializare;
        this.numarLocuri = numarLocuri;
    }

    public int getCodSpecializare() {
        return codSpecializare;
    }

    public void setCodSpecializare(int codSpecializare) {
        this.codSpecializare = codSpecializare;
    }

    public int getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(int numarLocuri) {
        this.numarLocuri = numarLocuri;
    }

    @Override
    public String toString() {
        return "Specializare{" +
                "codSpecializare=" + codSpecializare +
                ", numarLocuri=" + numarLocuri +
                '}';
    }
}
