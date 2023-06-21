import java.util.List;
import java.util.Map;

public class Liceu {
    int cod;
    String nume;
    int nrSpecializari;
    List<Specializare> listaSpecializari;


    public Liceu(int cod, String nume, int nrSpecializari, List<Specializare> listaSpecializari) {
        this.cod = cod;
        this.nume = nume;
        this.nrSpecializari = nrSpecializari;
        this.listaSpecializari = listaSpecializari;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getNrSpecializari() {
        return nrSpecializari;
    }

    public void setNrSpecializari(int nrSpecializari) {
        this.nrSpecializari = nrSpecializari;
    }


    public List<Specializare> getListaSpecializari() {
        return listaSpecializari;
    }

    public void setListaSpecializari(List<Specializare> listaSpecializari) {
        this.listaSpecializari = listaSpecializari;
    }

    public int getNrLocuri(){
        return listaSpecializari.stream().mapToInt(Specializare::getNumarLocuri).sum();
    }

    @Override
    public String toString() {
        return "Liceu{" +
                "cod=" + cod +
                ", nume='" + nume + '\'' +
                ", nrSpecializari=" + nrSpecializari +
                ", listaSpecializari=" + listaSpecializari +
                '}';
    }
}
