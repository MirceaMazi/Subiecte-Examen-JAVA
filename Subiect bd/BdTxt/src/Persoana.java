public class Persoana {
    int cod;
    String CNP;
    String Nume;

    public Persoana(int cod, String CNP, String nume) {
        this.cod = cod;
        this.CNP = CNP;
        Nume = nume;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getNume() {
        return Nume;
    }

    public void setNume(String nume) {
        Nume = nume;
    }

    @Override
    public String toString() {
        return "Persoana{" +
                "cod=" + cod +
                ", CNP='" + CNP + '\'' +
                ", Nume='" + Nume + '\'' +
                '}';
    }
}
