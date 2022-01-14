package kerho;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Jasen-luokka. Samoja asioita kuin CRC-kortista, tunnusnumeron ylläpito.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class Jasen implements Cloneable {
    private int tunnusNro;
    private static int seuraavaNro = 1;
    private String nimi = "";
    private String hetu = "";
    private String katuosoite = "";
    private String postinumero = "";
    private String postiosoite = "";
    private String puhelin = "";
    private String sposti = "";
    private String lisatietoja = "";
    
    
    /**
     * Antaa jäsenelle rekisterinumeron.
     * @return jäsenen tunnusnumero
     * @example
     * <pre name="test">
     * Jasen jasen1 = new Jasen();
     *  jasen1.getTunnusNro() === 0;
     *  jasen1.rekisteroi();
     *  Jasen jasen2 = new Jasen();
     *  jasen2.rekisteroi();
     *  int n1 = jasen1.getTunnusNro();
     *  int n2 = jasen2.getTunnusNro();
     *  n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    
    /**
     * Kloonataan jäsen.
     * @return Object kloonattu jäsen
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Jasen jasen = new Jasen();
     *   jasen.parse("   1  |  Mikko Mallikas   | 12345");
     *   Jasen klooni = jasen.clone();
     *   klooni.toString() === jasen.toString();
     *   jasen.parse("   4  |  Mikko Makkonen   | 12365");
     *   klooni.toString().equals(jasen.toString()) === false;
     * </pre>
     */
    @Override
    public Jasen clone() throws CloneNotSupportedException {
        Jasen klooni;
        klooni = (Jasen) super.clone();
        return klooni;
    }
    
    
    /**
     * Palautetaan kenttien määrä.
     * @return kenttien määrä
     */
    public int getKentat() {
        return 9;
    }
    
    
    /**
     * Palautetaan ensimmäinen kenttä.
     * @return ensimmäinen kenttä
     */
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
     * Alustetaan jäsen.
     */
    public Jasen() {
        //
    }
    
    
    /**
     * Annetaan kentän sisältö.
     * @param i kentän indeksi
     * @return kentän sisältö
     */
    public String anna(int i) {
        switch (i) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + hetu;
        case 3: return "" + katuosoite;
        case 4: return "" + postinumero;
        case 5: return "" + postiosoite;
        case 6: return "" + puhelin;
        case 7: return "" + sposti;
        case 8: return "" + lisatietoja;
        default: return "";
        }
    }
    
    
    /**
     * Asettaa kenttään tiedon.
     * @param i kentän indeksi
     * @param jono tieto
     * @return null jos onnistuu muuten virhe
     * @example
     * <pre name="test">
     *   Jasen jasen = new Jasen();
     *   jasen.aseta(1,"Mikko Mallikas") === null;
     *   jasen.aseta(4,"kissa") === "Vain numeroita!";
     *   jasen.aseta(4,"12345") === null; 
     *   jasen.aseta(6,"koira") === "Vain numeroita!";
     *   jasen.aseta(6,"12345") === null;
     * </pre>
     */
    public String aseta(int i, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch (i) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2:
            hetu = tjono;
            return null;
        case 3:
            katuosoite = tjono;
            return null;
        case 4:
            if (tjono.matches("[0-9]+") == false) return "Vain numeroita!";
            postinumero = tjono;
            return null;
        case 5:
            postiosoite = tjono;
            return null;
        case 6:
            if (tjono.matches("[0-9]+") == false) return "Vain numeroita!";
            puhelin = tjono;
            return null;
        case 7:
            sposti = tjono;
            return null;
        case 8:
            lisatietoja = tjono;
            return null;
        default:
            return "";
        }
    }
    
    
    /**
     * Palauttaa kentän kysymyksen.
     * @param i kentän indeksi
     * @return kentän kysymys
     */
    public String getKysymys(int i) {
        switch (i) {
        case 0: return "Tunnusnumero";
        case 1: return "Nimi";
        case 2: return "Henkilötunnus";
        case 3: return "Katuosoite";
        case 4: return "Postinumero";
        case 5: return "Postiosoite";
        case 6: return "Puhelin";
        case 7: return "Sähköposti";
        case 8: return "Lisätietoja";
        default: return "";
        }
    }
    
    
    /**
     * Palauttaa tiedot eroteltuna.
     * @example
     * <pre name="test">
     *   Jasen jasen = new Jasen();
     *   jasen.parse("   1  |  Mikko Mallikas  | 030201-111C");
     *   jasen.toString().startsWith("1|Mikko Mallikas|030201-111C|") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                nimi + "|" +
                hetu + "|" +
                katuosoite + "|" +
                postinumero + "|" +
                postiosoite + "|" +
                puhelin + "|" +
                sposti + "|" +
                lisatietoja;           
    }
    
    
    /**
     * Erotellaan tiedot tolpilla.
     * @param rivi rivi josta tiedot on otettu
     * @example
     * <pre name="test">
     *   Jasen jasen = new Jasen();
     *   jasen.parse("   1  |  Mikko Mallikas  | 030201-111C");
     *   jasen.getTunnusNro() === 1;
     *   jasen.toString().startsWith("1|Mikko Mallikas|030201-111C|") === true;
     *   jasen.rekisteroi();
     *   int n = jasen.getTunnusNro();
     *   jasen.parse(""+(n));
     *   jasen.rekisteroi();
     *   jasen.getTunnusNro() === n+1; 
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        hetu = Mjonot.erota(sb, '|', hetu);
        katuosoite = Mjonot.erota(sb, '|', katuosoite);
        postinumero = Mjonot.erota(sb, '|', postinumero);
        postiosoite = Mjonot.erota(sb, '|', postiosoite);
        puhelin = Mjonot.erota(sb, '|', puhelin);
        sposti = Mjonot.erota(sb, '|', sposti);
        lisatietoja = Mjonot.erota(sb, '|', lisatietoja);
    }
    
    
    /**
     * Palautetaan nimi.
     * @return jäsenen nimi
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * Palautetaan postinumero.
     * @return jäsenen postinumero
     */
    public String getPostinumero() {
        return postinumero;
    }
    
    
    /**
     * Palautetaan puhelinnumero.
     * @return jäsenen puhelinnumero
     */
    public String getPuhelin() {
        return puhelin;
    }
    
    
    /**
     * Palautetaan tunnusnumero.
     * @return jäsenen tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * Asetetaan tunnusnumero.
     */
    private void setTunnusNro(int nro) {
        tunnusNro = nro;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro+1;
    }
    
    
    /**
     * Täytetään testijäseniä joilla ei olisi samoja tietoja.
     */
    public void taytaJasen() {
        int max = 1000000100;
        int min = 1000000000;
        int range = max - min + 1;
        int randnum = (int)(Math.random() * range) + min;
        String rand = String.valueOf(randnum);
        taytaJasen(rand);
    }
    
    
    /**
     * Täytetään testijäseniä joilla ei olisi samoja tietoja.
     * @param rand random-numero jottei olisi samoja tietoja
     */
    public void taytaJasen(String rand) {
        nimi = "Mallikas Mikko";
        hetu = "010245-123U";
        katuosoite = "Kotikuja 6";
        postinumero = "12345";
        postiosoite = "Kettula";
        puhelin = rand;
        sposti = "m.mallikas@huhuu.com";
        lisatietoja = "";
    }
    
    
    /**
     * Tulostetaan henkilön tiedot.
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) + "  " + nimi + "  "
                + hetu);
        out.println("  " + katuosoite + "  " + postinumero + "  " + postiosoite);
        out.println("  puh: " + puhelin);
        out.println("  e-mail: " + sposti);
        out.println("  " + lisatietoja);
    }
    
    
    /**
     * Tulostetaan henkilön tiedot.
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    
    /**
     * Testiohjelma jäsenelle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Jasen jasen1 = new Jasen();
        Jasen jasen2 = new Jasen();
        jasen1.rekisteroi();
        jasen2.rekisteroi();
        jasen1.tulosta(System.out);
        jasen2.tulosta(System.out);
        jasen1.taytaJasen();
        jasen2.taytaJasen();
        jasen1.tulosta(System.out);
        jasen2.tulosta(System.out);
    }
}