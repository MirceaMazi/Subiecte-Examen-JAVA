import java.util.List;

public class Candidat {
    int cod;
    String nume;
    float media;
    List<Optiune> listaOptiuni;

    public Candidat(int cod, String nume, float media, List<Optiune> listaOptiuni) {
        this.cod = cod;
        this.nume = nume;
        this.media = media;
        this.listaOptiuni = listaOptiuni;
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

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public List<Optiune> getListaOptiuni() {
        return listaOptiuni;
    }

    public void setListaOptiuni(List<Optiune> listaOptiuni) {
        this.listaOptiuni = listaOptiuni;
    }

    public int getNrOptiuni(){
        return this.listaOptiuni.size();
    }

    @Override
    public String toString() {
        return "Candidat{" +
                "cod=" + cod +
                ", nume='" + nume + '\'' +
                ", media=" + media +
                ", listaOptiuni=" + listaOptiuni +
                '}';
    }
}
