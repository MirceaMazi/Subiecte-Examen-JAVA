import org.json.JSONArray;
import org.json.JSONTokener;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Main {
    static List<Produs> listaFacturi;
    static List<Tranzactie> listaTranzactii = new ArrayList<>();
    public static void main(String[] args) throws Exception{
        //Aici cream lista de obiecte produs(o citim din fisier text)
        try(var fisier = new BufferedReader(new FileReader("Date\\Produse.txt"))){
            listaFacturi = fisier.lines().map(linie -> new Produs(
                    Integer.parseInt(linie.split(",")[0]),
                    linie.split(",")[1],
                    Double.parseDouble(linie.split(",")[2])
            )).collect(Collectors.toList());
        }

        try(var fisier = new FileReader("Date\\Tranzactii.json")){
            var tokener = new JSONTokener(fisier);
            var jsonTranzactii = new JSONArray(tokener);
            for(var i = 0; i < jsonTranzactii.length(); i ++){
                var jsonTranzactie =jsonTranzactii.getJSONObject(i);
                var cod = jsonTranzactie.getInt("codProdus");
                var cantitate = jsonTranzactie.getInt(("cantitate"));
                var tip = jsonTranzactie.getEnum(Tip.class, "tip");

                listaTranzactii.add(new Tranzactie(cod, cantitate, tip));
            }
        }
        listaTranzactii.forEach(System.out::println);

        System.out.println("------------------CERINTA 1------------------");
        System.out.println("Numar produse: " + listaFacturi.size());

        System.out.println("------------------CERINTA 2------------------");
        listaFacturi.stream().sorted((obj1, obj2) -> obj1.getDenumire().compareTo(obj2.getDenumire())).forEach(System.out::println);

        System.out.println("------------------CERINTA 3------------------");

        Map<Integer, String> temp = new HashMap<>();

        listaFacturi.forEach(produs -> {
            int nrTranzactii = listaTranzactii.stream().filter((tranzactie -> tranzactie.getCodProdus() == produs.getIdProduse())).collect(Collectors.toList()).size();
            temp.put(nrTranzactii, produs.getDenumire());
        });

        List<Map.Entry<Integer, String>> listaHash = new ArrayList<>(temp.entrySet());

        Collections.sort(listaHash, Map.Entry.comparingByKey());
        Collections.reverse(listaHash);

        FileWriter fisier = new FileWriter("fisier.txt");
        listaHash.forEach(element -> {
            try {
                fisier.write("Denumire " + element.getValue() + " NrTranzactii " + element.getKey() + "\n");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        fisier.close();

        System.out.println("Fisierul se afla in solutie");

        System.out.println("------------------CERINTA 4------------------");
        List<Double> stocuri = new ArrayList<>();

        listaFacturi.forEach(produs -> {
            int intrari = listaTranzactii.stream()
                    .filter(tranzactie -> tranzactie.getCodProdus() == produs.getIdProduse())
                    .filter(tranzactie -> tranzactie.getTip().getSemn() == Tip.intrare.getSemn())
                    .mapToInt(Tranzactie::getCantitate).sum();

            int iesiri = listaTranzactii.stream()
                    .filter(tranzactie -> tranzactie.getCodProdus() == produs.getIdProduse())
                    .filter(tranzactie -> tranzactie.getTip().getSemn() == Tip.iesire.getSemn())
                    .mapToInt(Tranzactie::getCantitate).sum();

            int cantitate = intrari - iesiri;
            Double stoc = cantitate * produs.getPret();
            stocuri.add(stoc);
        });

        Double valoareTotala = 0.0;

        for(Double stoc : stocuri){
            valoareTotala += stoc;
        }

        System.out.println(valoareTotala);

        System.out.println("------------------CERINTA 5------------------");
         Thread server = new Thread(new Runnable() {
             @Override
             public void run() {
                 try(ServerSocket server = new ServerSocket(8080)){
                     System.out.println("[SERVER] Se asteapta conexiunea cu clietul...");

                     try(Socket client = server.accept()){
                         PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                         System.out.println("[SERVER] S-a realizat conexiunea cu clinetul...");

                         int cod = Integer.parseInt((in.readLine()));
                         System.out.println("[SERVER] Sa primit codul " + cod);

                         var valoare = listaFacturi.stream()
                                 .filter(produs-> produs.getIdProduse() == cod)
                                 .mapToDouble(Produs::getPret).sum();

                         out.println(valoare);

                         System.out.println("[SERVER] Am trimis urmatoarea valoare clientului: " + valoare);

                         System.out.println("[SERVER] Am inchis conexiunea");
                     }
                 } catch (IOException e){
                     throw new RuntimeException(e);
                 }
             }
         });



         Thread client = new Thread(new Runnable() {
             @Override
             public void run() {
                 try(Socket client = new Socket("localhost", 8080)){
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                     System.out.println("[CLIENT] S-a realizat conexiunea cu server-ul...");

                     int cod = 3;
                     out.println(cod);
                     System.out.println("[CLIENT] S-a trimis codul produsului...");

                     var valoare = Float.parseFloat(in.readLine());
                     System.out.println("[CLIENT] Am primit valoare de la server: " + valoare);

                     System.out.println("[CLIENT] Am inchis conexiunea");
                 } catch (IOException e){
                     throw new RuntimeException(e);
                 }
             }
         });

         server.start();
         client.start();

         server.join();
         client.join();

         System.out.println("Bagam text aici");

    }
}