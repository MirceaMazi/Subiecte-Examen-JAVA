import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Main {
    public static List<Candidat> getCandidati(){
        List<Candidat> listaCandidati = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect Bd JSON\\BdJSON\\Date\\inscrieri.txt"))){
            listaCandidati = fisier.lines().map(linie -> new Candidat(
                    Long.parseLong(linie.split(",")[0]),
                    linie.split(",")[1],
                    Double.parseDouble(linie.split(",")[2]),
                    Integer.parseInt(linie.split(",")[3])
            )).collect(Collectors.toList());
        }catch(Exception e){
            e.printStackTrace();
        }

        return listaCandidati;
    }

    public static List<Specializare> getSpecializari(){
        List<Specializare> listaSpecializari = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect Bd JSON\\BdJSON\\Date\\facultate.db")){
            Statement comanda = connection.createStatement();
            comanda.execute("select * from specializari");

            ResultSet rezultat = comanda.getResultSet();

            while(rezultat.next()){
                listaSpecializari.add(new Specializare(
                        rezultat.getInt(1), rezultat.getString(2), rezultat.getInt(3)
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return  listaSpecializari;
    }


    public static void main(String[] args) throws InterruptedException {
        List<Candidat> listaCandidati = getCandidati();
        listaCandidati.forEach(System.out::println);

        List<Specializare> listaSpecializari = getSpecializari();
        listaSpecializari.forEach(System.out::println);

        System.out.println("-----------------------CERINTA 1-----------------------");
        System.out.println("Numarul total de locuri in cadrul facultatii " + listaSpecializari.stream().mapToInt(specializare -> specializare.getLocuri()).sum());
        System.out.println("Specializarea cu numarul maxim de locuri " + listaSpecializari.stream().max(Comparator.comparingInt(Specializare::getLocuri)).get().getLocuri());

        System.out.println("-----------------------CERINTA 2-----------------------");
        listaSpecializari.forEach(specializare -> {
            int numarLocuriLibere = specializare.getLocuri() - listaCandidati.stream().filter(candidat -> candidat.getCod() == specializare.getCod()).collect(Collectors.toList()).size();

            if(numarLocuriLibere > 10){
                System.out.println("Denumirea specializarii: " + specializare.getDenumire() + ", codul specializarii: " + specializare.getCod() + ", numarul de locuri ramase: " + numarLocuriLibere);
            }
        });
        System.out.println("-----------------------CERINTA 3-----------------------");
        try(var fisier = new PrintWriter("Date\\json_insrieri.json")){
            JSONArray inscrieriSpecializari = new JSONArray();

            listaSpecializari.forEach(specializare -> {
                int nrInscrieri = listaCandidati.stream().filter(candidat -> candidat.getCod() == specializare.getCod()).collect(Collectors.toList()).size();

                double medieNote = listaCandidati.stream().filter(candidat -> candidat.getCod() == specializare.getCod()).mapToDouble(candidat -> candidat.getNotaBac()).average().getAsDouble();

                JSONObject obj = new JSONObject();
                obj.put("cod_specializare", specializare.getCod());
                obj.put("denumire", specializare.getDenumire());
                obj.put("numar_inscrieri", nrInscrieri);
                obj.put("medie", medieNote);

                inscrieriSpecializari.put(obj);
            });

            fisier.write(inscrieriSpecializari.toString(4));

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Fisierul a fost creat");

        System.out.println("-----------------------CERINTA 4-----------------------");

        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try(ServerSocket server = new ServerSocket(8080)){
                    System.out.println("[SERVER]Se asteapta conexiunea cu clientul...");

                    try(Socket client = server.accept()){
                        PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        System.out.println("[SERVER]S-a realizat conexiunea cu clientul...");
                        var denumire = in.readLine();
                        System.out.println("[SERVER]Am primit urmatoarea specializare " + denumire);

                        int locuriSpecializare = listaSpecializari.stream().filter(specializare -> specializare.getDenumire().equals(denumire))
                                        .mapToInt(specializare -> specializare.getLocuri()).findFirst().getAsInt();

                        int nrCandidati = listaCandidati.stream().filter(candidat -> candidat.getCod() == listaSpecializari.stream().filter(specializare -> specializare.getDenumire().equals(denumire)).findFirst().get().getCod()).collect(Collectors.toList()).size();

                        int locuriLibere = locuriSpecializare - nrCandidati;
                        out.println(locuriLibere);
                        System.out.println("[SERVER]Am transmis urmatorul numar de locuri libere " + locuriLibere);

                        System.out.println("[SERVER]Am incheiat conexiunea cu clientul...");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try(Socket client = new Socket("localhost", 8080)){
                    System.out.println("[Client]Am realizat conexiunea cu server-ul");

                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    String denumire = "Cibernetica";
                    out.println(denumire);
                    System.out.println("[CLIENT]Am transmis urmatoarea denumire server-ului " + denumire);

                    int locuriLibere = Integer.parseInt(in.readLine());
                    System.out.println("[CLIENT]Am primit urmatorul numar de locuri libere de la server " + locuriLibere);

                    System.out.println("[CLient]Am incheiat conexiunea cu server-ul");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        server.start();
        sleep(1000);
        client.start();
        server.join();
        client.join();


    }
}