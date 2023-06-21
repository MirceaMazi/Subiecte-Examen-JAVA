public class Candidat {
    long CNP;
    String nume;
    double notaBac;
    int cod;

    public Candidat(long CNP, String nume, double notaBac, int cod) {
        this.CNP = CNP;
        this.nume = nume;
        this.notaBac = notaBac;
        this.cod = cod;
    }

    public long getCNP() {
        return CNP;
    }

    public void setCNP(long CNP) {
        this.CNP = CNP;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public double getNotaBac() {
        return notaBac;
    }

    public void setNotaBac(double notaBac) {
        this.notaBac = notaBac;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return "Candidat{" +
                "CNP=" + CNP +
                ", nume='" + nume + '\'' +
                ", notaBac=" + notaBac +
                ", cod=" + cod +
                '}';
    }
}
