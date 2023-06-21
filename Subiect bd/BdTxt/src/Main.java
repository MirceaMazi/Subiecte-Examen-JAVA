import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static List<Persoana> getPersoane(){
        List<Persoana> listaPersoane = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect bd\\BdTxt\\Date\\bursa.db")){

            Statement comanda = connection.createStatement();
            comanda.execute("select * from persoane");
            ResultSet rs = comanda.getResultSet();

            while(rs.next()){
                listaPersoane.add(new Persoana(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return listaPersoane;
    }

    public static List<Tranzactie> getTranzactii(){
        List<Tranzactie> listaTranzactii = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect bd\\BdTxt\\Date\\bursa_tranzactii.txt"))){
            listaTranzactii = fisier.lines().map(linie ->
               new Tranzactie(
                       Integer.parseInt(linie.split(",")[0]),
                       linie.split(",")[1],
                       linie.split(",")[2],
                       Integer.parseInt(linie.split(",")[3]),
                       Float.parseFloat(linie.split(",")[4])
            )).collect(Collectors.toList());

        } catch(Exception e){
            e.printStackTrace();
        }

        return listaTranzactii;
    }


    public static void main(String[] args) throws InterruptedException {
        List<Persoana> listaPersoane = getPersoane();
        listaPersoane.forEach(System.out::println);

        List<Tranzactie> listaTranzactii = getTranzactii();
        listaTranzactii.forEach(System.out::println);

        System.out.println("-------------------CERINTA 1-------------------");
        System.out.println("Persoane nerezidenti: " + listaPersoane.stream().filter(persoana -> persoana.getCNP().startsWith("8") || persoana.getCNP().startsWith("9")).collect(Collectors.toList()).size());

        System.out.println("-------------------CERINTA 2-------------------");
        listaTranzactii.stream().collect(Collectors.groupingBy(tranzactie -> tranzactie.getSimbol())).forEach((simbol, tranzactii)->{
            System.out.println(simbol + " -> " + tranzactii.size());
        });

        System.out.println("-------------------CERINTA 3-------------------");
        try(var fisier = new FileWriter("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect bd\\BdTxt\\Date\\simboluri.txt")){
            listaTranzactii.stream().map(tranzactie -> tranzactie.getSimbol()).distinct().forEach(simbol ->
            {
                try {
                    fisier.write(simbol + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Fisierul a fost creat");

        System.out.println("-------------------CERINTA 4-------------------");

        listaPersoane.forEach(persoana -> {
            System.out.println(persoana.getNume());

            listaTranzactii.stream()
                    .filter(tranzactie -> tranzactie.getCod() == persoana.getCod())
                    .collect(Collectors.groupingBy(tranzactie -> tranzactie.getSimbol()))
                    .forEach((simbol, tranzactii) -> {
                        var vanzari = tranzactii.stream()
                                .filter(tranzactie -> tranzactie.getTip().equals("vanzare"))
                                .collect(Collectors.toList());

                        var achizitii = tranzactii.stream()
                                .filter(tranzactie -> tranzactie.getTip().equals("cumparare"))
                                .collect(Collectors.toList());

                        var sumaVanzari =vanzari.stream().mapToDouble(vanzare -> vanzare.getCantitate() * vanzare.getPret()).sum();

                        var sumaAchizitii = achizitii.stream().mapToDouble(achizitie -> achizitie.getCantitate() * achizitie.getPret()).sum();

                        var profit = sumaVanzari - sumaAchizitii;

                        System.out.println(simbol + " -> " + profit);

                    });
        });

        System.out.println("-------------------CERINTA 5-------------------");
        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try(ServerSocket server = new ServerSocket(8085)){
                    System.out.println("[SERVER] Se asteapta conexiunea cu clientul");


                        try (Socket client = server.accept()) {
                            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                            System.out.println("[SERVER] S-a realizat conexiunea cu clientul...");

                            int cod = Integer.parseInt(in.readLine());
                            System.out.println("[SERVER] S-a primit codul " + cod);

                            var tranzactii = listaTranzactii.stream()
                                    .filter(tranzactie -> tranzactie.getCod() == cod)
                                    .collect(Collectors.toList());

                            out.println(tranzactii);

                            System.out.println("[SERVER] S-a trimis urmatoarea lista de tranzactii " + tranzactii);
                            System.out.println("[SERVER] Am inchis conexiunea");
                        }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Thread client = new Thread(new Runnable() {
            @Override
            public void run() {
                try(Socket client = new Socket("localhost", 8085)){
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    System.out.println("[CLIENT] S-a realizat conexiunea cu server-ul");

                    int cod = 1;
                    out.println(cod);
                    System.out.println("[CLIENT] S-a trimis codul " + cod);

                    var tranzactii = in.readLine();
                    System.out.println("[CLIENT] Am primit urmatoarea lista de la server " + tranzactii);

                    System.out.println("[Client] Am inchis conexiunea");

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        server.start();
        client.start();
        server.join();
        client.join();
    }
}