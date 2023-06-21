public class Tranzactie {
    int codProdus;
    int cantitate;
    Tip tip;

    public Tranzactie(int codProdus, int cantitate, Tip tip) {
        this.codProdus = codProdus;
        this.cantitate = cantitate;
        this.tip = tip;
    }

    public int getCodProdus() {
        return codProdus;
    }

    public void setCodProdus(int codProdus) {
        this.codProdus = codProdus;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public Tip getTip() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "codProdus=" + codProdus +
                ", cantitate=" + cantitate +
                ", tip=" + tip +
                '}';
    }
}
