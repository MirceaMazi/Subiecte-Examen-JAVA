import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {
    public static List<Rezervare> getRezervari(){
        List<Rezervare> listaRezervari = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON2\\Alt JSON\\Date\\rezervari.txt"))){
            listaRezervari = fisier.lines().map(linie -> new Rezervare(
                    linie.split(",")[0],
                    Integer.parseInt(linie.split(",")[1]),
                    Integer.parseInt(linie.split(",")[2])
            )).collect(Collectors.toList());
        }catch ( Exception e ){
            e.printStackTrace();
        }

        return listaRezervari;
    }

    public static List<Aventura> getAventuri(){
        List<Aventura> listaAventuri = new ArrayList<>();

        try(var fisier = new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON2\\Alt JSON\\Date\\aventuri.json")){
            var tokener = new JSONTokener(fisier);
            var jsonAventuri = new JSONArray(tokener);

            for(int i = 0; i < jsonAventuri.length(); i ++){
                var jsonAventura = jsonAventuri.getJSONObject(i);
                var cod = jsonAventura.getInt("cod_aventura");
                var denumire = jsonAventura.getString("denumire");
                var tarif = jsonAventura.getDouble("tarif");
                var locuri = jsonAventura.getInt("locuri_disponibile");

                listaAventuri.add(new Aventura(cod, denumire, tarif, locuri));
            }
        }catch ( Exception e){
            e.printStackTrace();
        }

        return listaAventuri;
    }
    public static void main(String[] args) {
        List<Rezervare> listaRezervari = getRezervari();
        listaRezervari.forEach(System.out::println);

        List<Aventura> listaAventuri = getAventuri();
        listaAventuri.forEach(System.out::println);

        System.out.println("------------------------CERINTA 1------------------------");
        listaAventuri.stream().filter(aventura -> aventura.getLocuriDisponibile() >= 20).forEach(System.out::println);

        System.out.println("------------------------CERINTA 2------------------------");
        listaAventuri.stream().forEach(aventura -> {
            var locuriOcupate = listaRezervari.stream().filter(rezervare -> rezervare.getCodRezervare() == aventura.getCod()).mapToInt(rezervare -> rezervare.getNrLocuriRezervate()).sum();

            var locuriLibere = aventura.getLocuriDisponibile() - locuriOcupate;

            if(locuriLibere >= 5){
                System.out.println("Aventura " + aventura.getDenumire() + " mai are atatea locuri libere " + locuriLibere);
            }
        });

        System.out.println("------------------------CERINTA 3------------------------");
        try(var fisier = new FileWriter("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON2\\Alt JSON\\Date\\valori.txt")){

            listaAventuri.stream().sorted((obj1, obj2) -> obj1.getDenumire().compareTo(obj2.getDenumire())).forEach(aventura -> {
                var locuriOcupate = listaRezervari.stream().filter(rezervare -> rezervare.getCodRezervare() == aventura.getCod()).mapToInt(rezervare -> rezervare.getNrLocuriRezervate()).sum();

                var sumaCastigata = aventura.getTarif() * locuriOcupate;

                try {
                    fisier.write("Denumire " + aventura.getDenumire() + ", nr locuri rezervate " + locuriOcupate + ", suma incasata " + sumaCastigata + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            System.out.println("Fisierul a fost creat");

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("------------------------CERINTA 4------------------------");
        var server = new Thread(new Runnable() {
            @Override
            public void run() {
                try(ServerSocket server = new ServerSocket(8080)){
                    System.out.println("[SERVER]Se asteapta conexiunea cu clientul...");

                    try(Socket client = server.accept()){
                        System.out.println("[SERVER]Am realizat conexiunea cu clientul...");
                        var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        var out = new PrintWriter(client.getOutputStream(), true);

                        var denumire = in.readLine();
                        System.out.println("[SERVER]Am primit urmatoarea denumire din partea clientului " + denumire);

                        var locuri = listaAventuri.stream().filter(aventura -> aventura.getDenumire().equals(denumire)).mapToInt(aventura -> aventura.getLocuriDisponibile()).findFirst().getAsInt();
                        out.println(locuri);
                        System.out.println("[SERVER]Am trimis urmtorul numar de locuri clientului " + locuri);
                        System.out.println("[SERVER]Am incheiat conexiunea cu clientul...");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        var client = new Thread(new Runnable() {
            @Override
            public void run() {
                try(Socket client = new Socket("localhost", 8080)){
                    System.out.println("[CLIENT]Am realizat conexiunea cu server-ul");
                    var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    var out = new PrintWriter(client.getOutputStream(), true);

                    String denumire = "CATARARE";
                    out.println(denumire);
                    System.out.println("[CLIENT]Am transmis urmatoarea denumire server-ului " + denumire);

                    var locuri = Integer.parseInt(in.readLine());
                    System.out.println("[CLIENT]Am primit urmatorul numar de locuri disponibile " + locuri);
                    System.out.println("[CLIENT]Am incheiat conexiunea cu server-ul");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        server.start();
        client.start();
    }
}