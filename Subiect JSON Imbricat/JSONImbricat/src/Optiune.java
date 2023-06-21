public class Optiune {
    int codLiceu;
    int codSpecializare;

    public Optiune(int codLiceu, int codSpecializare) {
        this.codLiceu = codLiceu;
        this.codSpecializare = codSpecializare;
    }

    public int getCodLiceu() {
        return codLiceu;
    }

    public void setCodLiceu(int codLiceu) {
        this.codLiceu = codLiceu;
    }

    public int getCodSpecializare() {
        return codSpecializare;
    }

    public void setCodSpecializare(int codSpecializare) {
        this.codSpecializare = codSpecializare;
    }

    @Override
    public String toString() {
        return "Optiune{" +
                "codLiceu=" + codLiceu +
                ", codSpecializare=" + codSpecializare +
                '}';
    }
}
