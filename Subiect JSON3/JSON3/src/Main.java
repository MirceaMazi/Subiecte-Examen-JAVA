import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Main {
    public static List<Pacient> getPacienti(){
        List<Pacient> listaPacienti = new ArrayList<>();
        try( var fisier = new BufferedReader(new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON3\\JSON3\\Date\\pacienti.txt"))){
            listaPacienti = fisier.lines().map(linie -> new Pacient(
                    Long.parseLong(linie.split(",")[0]),
                    linie.split(",")[1],
                    Integer.parseInt(linie.split(",")[3]),
                    Integer.parseInt(linie.split(",")[2])
            )).collect(Collectors.toList());
        }catch ( Exception e){
            e.printStackTrace();
        }

        return listaPacienti;
    }

    public static List<Sectie> getSectii(){
        List<Sectie> listaSectii = new ArrayList<>();
        try(var fisier = new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON3\\JSON3\\Date\\sectii.json")){
            var tokener = new JSONTokener(fisier);
            var jsonSectii = new JSONArray(tokener);
            for(int i = 0; i < jsonSectii.length(); i ++){
                var jsonSectie = jsonSectii.getJSONObject(i);
                int cod = jsonSectie.getInt("cod_sectie");
                String denumire = jsonSectie.getString("denumire");
                int locuri = jsonSectie.getInt("numar_locuri");
                listaSectii.add(new Sectie(cod, denumire, locuri));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return listaSectii;
    }
    public static void main(String[] args) throws InterruptedException {
        List<Pacient> listaPacienti = getPacienti();
        listaPacienti.forEach(System.out::println);
        List<Sectie> listaSectii = getSectii();
        listaSectii.forEach(System.out::println);

        System.out.println("---------------------------CERINTA 1---------------------------");
        listaSectii.stream().filter(sectie -> sectie.getNrLocuri() > 10).forEach(System.out::println);

        System.out.println("---------------------------CERINTA 2---------------------------");
        Map<Double, Sectie> listaOrdonata = new HashMap<>();

        listaSectii.stream().forEach(sectie -> {
            if(listaPacienti.stream().filter(pacient -> pacient.getCod() == sectie.getCod()).mapToDouble(Pacient::getVarsta).average().isPresent()) {
                double varstaMedie = listaPacienti.stream().filter(pacient -> pacient.getCod() == sectie.getCod()).mapToDouble(Pacient::getVarsta).average().getAsDouble();
                listaOrdonata.put(varstaMedie, sectie);
            }
        });

        listaOrdonata.keySet().stream().sorted(Comparator.reverseOrder()).forEach(cheie ->
                System.out.println("Sectia " + listaOrdonata.get(cheie) + " are varsta medie " + cheie));

        System.out.println("---------------------------CERINTA 3---------------------------");
        try(var fisier = new PrintWriter("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON3\\JSON3\\Date\\jurnal.txt")){
            listaSectii.stream().forEach(sectie ->{
                long numarPacienti = listaPacienti.stream().filter(pacient -> pacient.getCod() == sectie.getCod()).count();

                fisier.write("Nume sectie " + sectie.getDenumire() + ", cod sectie " + sectie.getCod() + ", numar pacienti " + numarPacienti + "\n");
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Fisierul a fost creat!");

        System.out.println("---------------------------CERINTA 4---------------------------");
        var server = new Thread (new Runnable() {
            @Override
            public void run() {
                try(ServerSocket server = new ServerSocket(8080)){
                    System.out.println("[SERVER]Se asteapta conexiunea cu clientul...");
                    try(Socket client = server.accept()){
                        System.out.println("[SERVER]Am realizat conexiunea cu clientul...");
                        var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        var out = new PrintWriter(client.getOutputStream(), true);

                        var cod = Integer.parseInt(in.readLine());
                        System.out.println("[SERVER]Am primit urmatorul cod " + cod);

                        long numarPacienti = listaPacienti.stream().filter(pacient -> pacient.getCod() == cod).count();
                        var numarLocuriLibere = listaSectii.stream().filter(sectie -> sectie.getCod() == cod).findFirst().get().getNrLocuri() - numarPacienti;

                        out.println(numarLocuriLibere);
                        System.out.println("[SERVER]Am transmis urmatorul numar de locuri libere " + numarLocuriLibere);
                        System.out.println("[SERVER]Am incheiat conexiunea");

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        var client = new Thread(new Runnable() {
            @Override
            public void run() {
                try(Socket client = new Socket("localhost", 8080)){
                    System.out.println("[CLIENT]M-am conectat la server...");
                    var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    var out = new PrintWriter(client.getOutputStream(), true);

                    var cod = 1;
                    out.println(cod);
                    System.out.println("[CLIENT]Am transmis urmatorul cod " + cod);

                    var nrLocuri = Integer.parseInt(in.readLine());
                    System.out.println("[CLIENT]Am primit urmatorul numar de lucri libere " + nrLocuri);
                    System.out.println("[CLIENT]Am incheia tconexiunea cu server-ul");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        server.start();
        client.start();
        sleep(1000);
        server.join();
        client.join();
    }
}