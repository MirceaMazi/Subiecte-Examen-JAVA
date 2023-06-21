import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
   public static List<Produs> getProduse() throws ParserConfigurationException, IOException, SAXException {
       List<Produs> listaProduse = new ArrayList<>();
       var factury = DocumentBuilderFactory.newDefaultInstance();
       var builder = factury.newDocumentBuilder();
       var document = builder.parse("E:\\Facultate\\Anul II\\SEM II\\JAVA\\Recapitulare\\Fisiere XML\\XML\\src\\produse.xml");
       var noduriProduse = document.getDocumentElement().getElementsByTagName("produs");
       for(int i = 0; i < noduriProduse.getLength(); i ++){
           var nodProdus = (Element)noduriProduse.item(i);
           var cod = Integer.parseInt(nodProdus.getElementsByTagName("cod").item(0).getTextContent());
           var denumire = nodProdus.getElementsByTagName("denumire").item(0).getTextContent();
           var nrTranzactii = nodProdus.getElementsByTagName("tranzactie").getLength();
           listaProduse.add(new Produs(cod, denumire, nrTranzactii));
       }
       return listaProduse;


   }
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        List<Produs> listaProduse = getProduse();
        listaProduse.forEach(System.out::println);

    }
}