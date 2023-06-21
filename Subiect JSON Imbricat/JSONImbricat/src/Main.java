import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static List<Candidat> getCandidati(){
        List<Candidat> listaCandidati = new ArrayList<>();

        try(var fisier = new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON Imbricat\\JSONImbricat\\Date\\candidati.json")){

            var tokener = new JSONTokener(fisier);
            var jssonCandidati = new JSONArray(tokener);
            for(int i = 0; i < jssonCandidati.length(); i ++){
                List<Optiune> listaOptiuni = new ArrayList<>();
                var jsonCandidat = jssonCandidati.getJSONObject(i);
                int cod = jsonCandidat.getInt("cod_candidat");
                String nume = jsonCandidat.getString("nume_candidat");
                float media = jsonCandidat.getFloat("media");
                var tokener2 = jsonCandidat.getJSONArray("optiuni").toString();
                var jsonOptiuni = new JSONArray(tokener2);
                for(int j = 0; j < jsonOptiuni.length(); j ++){
                    var jsonOptiune = jsonOptiuni.getJSONObject(j);
                    int codL = jsonOptiune.getInt("cod_liceu");
                    int codS = jsonOptiune.getInt("cod_specializare");
                    listaOptiuni.add(new Optiune(codL, codS));
                }
                listaCandidati.add(new Candidat(cod, nume, media, listaOptiuni));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return listaCandidati;
    }

    public static List<Liceu> getLicee(){
        List<Liceu> listaLicee = new ArrayList<>();
        try(var fisier = new BufferedReader(new FileReader("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON Imbricat\\JSONImbricat\\Date\\licee.txt"))){
            var linia1 = fisier.readLine().split(",");
            var linia2 = fisier.readLine().split(",");

            while(linia1 != null && linia2 != null){
                var numarSpecializari = Integer.parseInt(linia1[2]);

                List<Specializare> listaSpecializari = new ArrayList<>();
                for(int i = 0; i < numarSpecializari * 2; i += 2) {
                    int cod = Integer.parseInt(linia2[i]);
                    int nrLocuri = Integer.parseInt(linia2[i + 1]);
                    listaSpecializari.add(new Specializare(cod, nrLocuri));
                }

                int cod = Integer.parseInt(linia1[0]);
                String nume = linia1[1];
                int nrSpecializari = Integer.parseInt(linia1[2]);
                listaLicee.add(new Liceu(cod, nume, nrSpecializari, listaSpecializari));

                linia1 = fisier.readLine().split(",");
                linia2 = fisier.readLine().split(",");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return listaLicee;
    }

    public static void main(String[] args) {
        List<Candidat> listaCandidati = getCandidati();
        listaCandidati.forEach(System.out::println);

        List<Liceu> listaLicee = getLicee();
        listaLicee.forEach(System.out::println);

        System.out.println("----------------------------CERINTA 1----------------------------");
        listaCandidati.stream().filter(candidat -> candidat.getMedia() > 9).forEach(System.out::println);

        System.out.println("----------------------------CERINTA 2----------------------------");
        listaLicee.stream().sorted(Comparator.comparingInt(Liceu::getNrLocuri).reversed()).forEach(liceu -> {
            System.out.println("Liceul cu codul " + liceu.getCod() + " are urmatorul numar de locuri " + liceu.getNrLocuri());
        });

        System.out.println("----------------------------CERINTA 3----------------------------");
        try(var fisier = new PrintWriter("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON Imbricat\\JSONImbricat\\Date\\jurnal.txt")){
            listaCandidati.stream().sorted(Comparator.comparingInt(Candidat::getNrOptiuni).reversed())
                    .collect(Collectors.groupingBy(candidat -> candidat.getListaOptiuni().size()))
                    .forEach((numarSpecializari, candidati) -> {
                        if(candidati.size() == 1){
                           fisier.write("Candidatul " + candidati.get(0).getNume() + " are media " + candidati.get(0).getMedia() + " si a aplicat la atatea specializari " + numarSpecializari + "\n");
                        } else if(candidati.size() > 1){
                            candidati.stream().sorted(Comparator.comparingDouble(Candidat::getMedia)).forEach(candidat ->
                                    fisier.write("Candidatul " + candidat.getNume() + " are media " + candidat.getMedia() + " si a aplicat la atatea specializari " + numarSpecializari + "\n"));
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Fisierul a fost creat cu succes!");

        System.out.println("----------------------------CERINTA 4----------------------------");
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Subiect JSON Imbricat\\JSONImbricat\\Date\\candidati.db")){
            Statement comanda = connection.createStatement();
            comanda.execute("create table IF NOT EXISTS CANDIDATI (cod_candidat integer,nume_candidat text,medie double,numar_optiuni integer)");

            PreparedStatement comandaInsert = connection.prepareStatement("insert into candidati(cod_candidat, nume_candidat, medie, numar_optiuni) values (?,?,?,?)");

            listaCandidati.forEach(candidat -> {
                try {
                    comandaInsert.setInt(1, candidat.getCod());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    comandaInsert.setString(2, candidat.getNume());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    comandaInsert.setDouble(3, candidat.getMedia());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    comandaInsert.setInt(4, candidat.getNrOptiuni());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    comandaInsert.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Tabela a fost creata");
    }
}