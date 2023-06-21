import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static List<Factura> getFacturi(){
        List<Factura> listaFacturi = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Fisiere binare\\Binar\\src\\Facturi.txt"))){
            listaFacturi = fisier.lines().map(linie -> new Factura(
                    linie.split(",")[0],
                    Integer.parseInt(linie.split(",")[1]),
                    Integer.parseInt(linie.split(",")[2])))
                    .collect(Collectors.toList());
        }catch(Exception e){
            e.printStackTrace();
        }

        return listaFacturi;
    }
    public static void main(String[] args) throws Exception{
        var listaFacturi = getFacturi();
        listaFacturi.forEach(System.out::println);

        //---------------------------Salvare ca binar---------------------------
        try(var fisier = new DataOutputStream(new FileOutputStream("facturi.dat"))){
            try {
                fisier.writeInt(listaFacturi.size());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            listaFacturi.forEach(factura -> {

                try {
                    fisier.writeUTF(factura.getNume());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    fisier.writeInt(factura.getVarsta());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    fisier.writeInt(factura.getSumaPlata());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        //---------------------------Citire din binar---------------------------
        List<Factura> listaFacturi1 = new ArrayList<>();
        try(var fisier = new DataInputStream(new FileInputStream("facturi.dat"))){

            int n = fisier.readInt();
            for(var i = 0; i < n; i ++){
                String nume = fisier.readUTF();
                int varsta = fisier.readInt();
                int sumaPlata = fisier.readInt();
                listaFacturi1.add(new Factura(nume, varsta, sumaPlata));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        listaFacturi1.forEach(System.out::println);

    }
}