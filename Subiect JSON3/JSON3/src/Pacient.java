public class Pacient {
    long CNP;
    String nume;
    int cod;
    int varsta;

    public Pacient(long CNP, String nume, int cod, int varsta) {
        this.CNP = CNP;
        this.nume = nume;
        this.cod = cod;
        this.varsta = varsta;
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

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    @Override
    public String toString() {
        return "Pacient{" +
                "CNP=" + CNP +
                ", nume='" + nume + '\'' +
                ", cod=" + cod +
                ", varsta=" + varsta +
                '}';
    }
}
