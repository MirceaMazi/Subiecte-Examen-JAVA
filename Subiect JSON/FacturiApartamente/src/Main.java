import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    public static List<Apartament> getApartamente(){

        List<Apartament> listaApartamente = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON\\FacturiApartamente\\Date\\intretinere_apartamente.txt"))){

            listaApartamente = fisier.lines().map(linie -> new Apartament(
                    Integer.parseInt(linie.split(",")[0]),
                    Integer.parseInt(linie.split(",")[1]),
                    Integer.parseInt(linie.split(",")[2])
            )).collect(Collectors.toList());

        } catch (Exception e){
            e.printStackTrace();
        }

        return listaApartamente;
    }

    public static List<Factura> getFacturi(){
        List<Factura> listaFacturi = new ArrayList<>();

        try(var fisier = new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON\\FacturiApartamente\\Date\\intretinere_facturi.json")){
            var tokener = new JSONTokener(fisier);
            var jsonFacturi = new JSONArray(tokener);

            for(var i = 0; i < jsonFacturi.length(); i ++){
                var jsonFactura = jsonFacturi.getJSONObject(i);
                String denumire = jsonFactura.getString("denumire");
                String repartizare = jsonFactura.getString("repartizare");
                int valoare = jsonFactura.getInt("valoare");

                listaFacturi.add(new Factura(denumire, repartizare, valoare));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return listaFacturi;
    }
    public static void main(String[] args) {
        List<Apartament> listaApartamente = getApartamente();
        listaApartamente.forEach(System.out::println);

        List<Factura> listaFacturi = getFacturi();
        listaFacturi.forEach(System.out::println);

        System.out.println("----------------------CERINTA 1----------------------");
        System.out.println("Apartamentul ce are suprafata maxima: " + listaApartamente.stream()
                .max(Comparator.comparingInt(Apartament::getSuprafata)).get());

        //Metoda a doua de realizare a exercitiului:
//        System.out.println("Apartamentul ce are suprafata maxima: " + listaApartamente.stream()
//                .sorted(Comparator.comparingInt(Apartament::getSuprafata).reversed()).findFirst().get());

        System.out.println("----------------------CERINTA 2----------------------");
        System.out.println("Numarul total de persoane care locuiesc in imobil: " + listaApartamente
                .stream().mapToInt(apartament -> apartament.getNrPersoane()).sum());

        System.out.println("----------------------CERINTA 3----------------------");
        listaFacturi.stream().collect(Collectors.groupingBy(factura -> factura.getRepartizare()))
                .forEach((repartizare, facturi) ->
                        System.out.println("Repartizare: " + repartizare + ", suma: " + facturi.stream().mapToInt(factura -> factura.getValoare()).sum()));

        System.out.println("----------------------CERINTA 4----------------------");
        Map<String, Integer> plataPerRepartizare = new HashMap<>();

        listaFacturi.stream().collect(Collectors.groupingBy(factura -> factura.getRepartizare()))
                .forEach((repartizare, facturi) -> {
                    int sumaPlati = facturi.stream().mapToInt(factura -> factura.getValoare()).sum();

                    plataPerRepartizare.put(repartizare, sumaPlati);
                });

        try(var fisier = new PrintWriter("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON\\FacturiApartamente\\Date\\fisier.txt")){

            listaApartamente.stream().sorted(Comparator.comparingInt(Apartament::getNrApartament))
                    .forEach(apartament -> {
                        fisier.write(apartament.getNrApartament() + " ");
                        fisier.write(apartament.getSuprafata() + " ");
                        fisier.write(apartament.getNrPersoane() + " ");
                        fisier.write(plataPerRepartizare.get("suprafata") * apartament.getSuprafata() + " ");
                        fisier.write(plataPerRepartizare.get("persoana") * apartament.getNrPersoane() + " ");
                        fisier.write(String.valueOf(plataPerRepartizare.get("suprafata") * apartament.getSuprafata() + plataPerRepartizare.get("persoana") * apartament.getNrPersoane()));
                        fisier.write("\n");
                    });

            System.out.println("Fisierul a fost creat!");
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("----------------------CERINTA 5----------------------");
        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
               try(ServerSocket server = new ServerSocket(8080)){
                   System.out.println("[SERVER]Se asteapta legatura cu clientul...");

                   try(Socket client = server.accept()){
                       System.out.println("[SERVER]Am realizat conexiunea cu clientul...");

                       PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                       BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                       int nrApartament = Integer.parseInt(in.readLine());
                       System.out.println("[SERVER]Am primit apartamentul " + nrApartament);

                       int suprafata = listaApartamente.stream().filter(apartament -> apartament.getNrApartament() == nrApartament).findFirst().get().getSuprafata();

                       out.println(suprafata);
                       System.out.println("[SERVER]Am trimis suprafata " + suprafata);
                       System.out.println("[SERVER]Am incheiat conexiunea");
                   }
               } catch(Exception e){
                   e.printStackTrace();
               }
            }
        });

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try(Socket client = new Socket("localhost", 8080)){
                    System.out.println("[CLIENT]Am realizat legatura cu server-ul...");

                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    int nrApartament = 1;
                    out.println(nrApartament);
                    System.out.println("[CLIENT]Am trimis esrverului urmatorul apartament " + nrApartament);

                    int suprafata = Integer.parseInt(in.readLine());
                    System.out.println("[CLIENT]Am primit de la server urmatoarea suprafata " + suprafata);

                    System.out.println("[CLIENT]Am incheiat conexiunea cu server-ul...");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        server.start();
        client.start();
    }

}