package kerho;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kirja-luokka. Samoja asioita kuin CRC-kortista, tunnusnumeron ylläpito.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class Kirja implements Cloneable {
    private int tunnusNro;
    private static int seuraavaNro = 1;
    private String nimi = "";
    private String kirjailija = "";
    private String vuosi = "";
    private String lisatietoja = "";
    private String lainassa = "vapaana";
    
    
    /**
     * Antaa kirjalle rekisterinumeron.
     * @return kirjan tunnusnumero
     * @example
     * <pre name="test">
     * Kirja kirja1 = new Kirja();
     *  kirja1.getTunnusNro() === 0;
     *  kirja1.rekisteroi();
     *  Kirja kirja2 = new Kirja();
     *  kirja2.rekisteroi();
     *  int n1 = kirja1.getTunnusNro();
     *  int n2 = kirja2.getTunnusNro();
     *  n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    
    /**
     * Kloonataan kirja.
     * @return Object kloonattu kirja
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Kirja kirja = new Kirja();
     *   kirja.parse("   1  |  Hobitti   | Tolkien");
     *   Kirja klooni = kirja.clone();
     *   klooni.toString() === kirja.toString();
     *   kirja.parse("   4  |  Aapinen   | Kananen");
     *   klooni.toString().equals(kirja.toString()) === false;
     * </pre>
     */
    @Override
    public Kirja clone() throws CloneNotSupportedException {
        Kirja klooni;
        klooni = (Kirja) super.clone();
        return klooni;
    }
    
    
    /**
     * Palautetaan kenttien määrä.
     * @return kenttien määrä
     */
    public int getKentat() {
        return 6;
    }
    
    
    /**
     * Palautetaan ensimmäinen kenttä.
     * @return ensimmäinen kenttä
     */
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
     * Alustetaan kirja.
     */
    public Kirja() {
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
        case 2: return "" + kirjailija;
        case 3: return "" + vuosi;
        case 4: return "" + lisatietoja;
        case 5: return "" + lainassa;
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
     *   Kirja kirja = new Kirja();
     *   kirja.aseta(1,"Hobitti") === null;
     *   kirja.aseta(3,"kissa") === "Vain numeroita!";
     *   kirja.aseta(3,"1937") === null; 
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
            kirjailija = tjono;
            return null;
        case 3:
            if (tjono.matches("[0-9]+") == false) return "Vain numeroita!";
            vuosi = tjono;
            return null;
        case 4:
            lisatietoja = tjono;
            return null;
        case 5:
            lainassa = tjono;
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
        case 2: return "Kirjailija";
        case 3: return "Vuosi";
        case 4: return "Lisätietoja";
        case 5: return "Lainassa/vapaana";
        default: return "";
        }
    }
    
    
    /**
     * Palauttaa tiedot eroteltuna.
     * @example
     * <pre name="test">
     *   Kirja kirja = new Kirja();
     *   kirja.parse("   1  |  Hobitti  | Tolkien");
     *   kirja.toString().startsWith("1|Hobitti|Tolkien|") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                nimi + "|" +
                kirjailija + "|" +
                vuosi + "|" +
                lisatietoja + "|" +
                lainassa;        
    }
    
    
    /**
     * Erotellaan tiedot tolpilla.
     * @param rivi rivi josta tiedot on otettu
     * @example
     * <pre name="test">
     *   Kirja kirja = new Kirja();
     *   kirja.parse("   1  |  Hobitti  | Tolkien");
     *   kirja.getTunnusNro() === 1;
     *   kirja.toString().startsWith("1|Hobitti|Tolkien|") === true;
     *   kirja.rekisteroi();
     *   int n = kirja.getTunnusNro();
     *   kirja.parse(""+(n));
     *   kirja.rekisteroi();
     *   kirja.getTunnusNro() === n+1; 
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        kirjailija = Mjonot.erota(sb, '|', kirjailija);
        vuosi = Mjonot.erota(sb, '|', vuosi);
        lisatietoja = Mjonot.erota(sb, '|', lisatietoja);
        lainassa = Mjonot.erota(sb, '|', lainassa);
    }
    
    
    /**
     * Palautetaan nimi.
     * @return kirjan nimi
     */
    public String getNimi() {
        return nimi;
    }
    
    
    /**
     * Palautetaan vuosi.
     * @return kirjan vuosi
     */
    public String getVuosi() {
        return vuosi;
    }
    
    
    /**
     * Onko lainassa.
     * @return vapaana tai lainaajan nimi
     */
    public String getLainassa() {
        return lainassa;
    }
    
    
    /**
     * Asetetaan lainaaja.
     * @param nimi lainaaja tai vapaana
     */
    public void setLainassa(String nimi) {
        lainassa = nimi;
    }
    
    
    /**
     * Palautetaan tunnusnumero.
     * @return kirjan tunnusnumero
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
     * Täytetään testikirja.
     */
    public void taytaKirja() {
        nimi = "Hobitti eli sinne ja takaisin";
        kirjailija = "J.R.R. Tolkien";
        vuosi = "1937";
    }
    
    
    /**
     * Tulostetaan kirjan tiedot.
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) + "  " + nimi + "  "
                + kirjailija + " " + vuosi);
    }
    
    
    /**
     * Tulostetaan kirjan tiedot.
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    

    /**
     * Testiohjelma kirjalle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kirja kirja1 = new Kirja();
        Kirja kirja2 = new Kirja();
        kirja1.rekisteroi();
        kirja2.rekisteroi();
        kirja1.tulosta(System.out);
        kirja2.tulosta(System.out);
        kirja1.taytaKirja();
        kirja2.taytaKirja();
        kirja1.tulosta(System.out);
        kirja2.tulosta(System.out);
    }
}