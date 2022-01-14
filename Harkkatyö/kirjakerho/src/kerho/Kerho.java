package kerho;

import java.util.Collection;

import fi.jyu.mit.fxgui.Dialogs;

/**
 * Kerho-luokka.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class Kerho {
    private Jasenet jasenet = new Jasenet();
    private Kirjat kirjat = new Kirjat(); 
    private Lainat lainat = new Lainat(); 
    private int jasenINro;
    
    
    /**
     * Asetetaan valitun jäsenen numero attribuutiksi.
     * @param Nro valitun jäsenen numero
     */
    public void setJasenINro(int Nro) {
        jasenINro = Nro;
    }
    
    
    /**
     * Palautetaan viimeisin asetettu jäsenen numero.
     * @return jäsenen numero
     */
    public int getJasenINro() {
        return jasenINro;
    }
    
    
    /**
     * Tutkitaan onko kirjalla jo laina olemassa.
     * @param kid kirjan id
     * @return true jos on jo lainattu, muuten false
     */
    public boolean getKid(int kid) {
        return lainat.getKid(kid);
    }
    
    
    /**
     * Palauttaa haun viitteet.
     * @param hakue hakuehto
     * @param k kentän indeksi
     * @return löytyneet jäsenet
     * @throws PoikkeusException poikkeus
     */
    public Collection<Jasen> etsi(String hakue, int k) throws PoikkeusException { 
        return jasenet.etsi(hakue, k); 
    } 
    
    
    /**
     * Palauttaa haun viitteet.
     * @param hakue hakuehto
     * @param k kentän indeksi
     * @return löytyneet kirjat
     * @throws PoikkeusException poikkeus
     */
    public Collection<Kirja> etsiKirja(String hakue, int k) throws PoikkeusException { 
        return kirjat.etsi(hakue, k); 
    } 
    
    
    /**
     * Lisätään uusi jäsen.
     * @param jasen lisättävä jäsen
     */
    public void lisaa(Jasen jasen) {
        jasenet.lisaa(jasen);
    }
    
    
    /**
     * Lisätään uusi kirja.
     * @param kirja lisättävä kirja
     */
    public void lisaa(Kirja kirja) {
        kirjat.lisaa(kirja);
    }
    
    
    /**
     * Lisätään uusi laina.
     * @param laina lisättävä laina
     */
    public void lisaa(Laina laina) {
        lainat.lisaa(laina);
    }
    
    
    /**
     * Muutetaan jäsenen tietoja.
     * @param jasen jäsen
     * @throws PoikkeusException poikkeus
     * 
     */ 
    public void korvaaTaiLisaa(Jasen jasen) throws PoikkeusException { 
        jasenet.korvaaTaiLisaa(jasen); 
    } 
    
    
    /**
     * Muutetaan kirjan tietoja.
     * @param kirja kirja
     * @throws PoikkeusException poikkeus
     * 
     */ 
    public void korvaaTaiLisaa(Kirja kirja) throws PoikkeusException { 
        kirjat.korvaaTaiLisaa(kirja); 
    } 
    
    
    /**
     * Palautetaan jäsenen viite.
     * @param i monesko jäsen halutaan
     * @return viite jäseneen
     * @throws IndexOutOfBoundsException jos indeksiraja ylittyy
     */
    public Jasen annaJasen(int i) throws IndexOutOfBoundsException {
        return jasenet.anna(i);
    }
    
    
    /**
     * Palautetaan jäsen.
     * @param id jäsenen tunnusnumero
     * @return jäsen tai null
     */
    public Jasen getJasen(int id) {
        return jasenet.getJasen(id);
    }
    
    
    /**
     * Palautetaan kirjan viite.
     * @param i monesko kirja halutaan
     * @return viite kirjaan
     * @throws IndexOutOfBoundsException jos indeksiraja ylittyy
     */
    public Kirja annaKirja(int i) throws IndexOutOfBoundsException {
        return kirjat.anna(i);
    }
    
    
    /**
     * Palautetaan kirja.
     * @param kid kirjan tunnusnumero
     * @return kirja tai null
     */
    public Kirja getKirja(int kid) {
        return kirjat.getKirja(kid);
    }
    
    
    /**
     * Palautetaan lainan viite.
     * @param i monesko laina halutaan
     * @return viite lainaan
     * @throws IndexOutOfBoundsException jos indeksiraja ylittyy
     */
    public Laina annaLaina(int i) throws IndexOutOfBoundsException {
        return lainat.anna(i);
    }
    
    
    /**
     * Luetaan kerhon tiedot.
     * @throws PoikkeusException jos epäonnistuu
     */
    public void lueJasenetTiedostosta() throws PoikkeusException {
        jasenet = new Jasenet();
        jasenet.lueTiedostosta();
    }
    
    
    /**
     * Luetaan kerhon tiedot.
     * @throws PoikkeusException jos epäonnistuu
     */
    public void lueKirjatTiedostosta() throws PoikkeusException {
        kirjat = new Kirjat();
        kirjat.lueTiedostosta();
    }
    
    
    /**
     * Luetaan kerhon tiedot.
     * @throws PoikkeusException jos epäonnistuu
     */
    public void lueLainatTiedostosta() throws PoikkeusException {
        lainat = new Lainat();
        lainat.lueTiedostosta();
    }
    
    
    /**
     * Palauttaa kerhon jäsenten lukumäärän.
     * @return jäsenten lukumäärä
     */
    public int getJasenia() {
        return jasenet.getLkm();
    }
    
    
    /**
     * Palauttaa kerhon kirjojen lukumäärän.
     * @return kirjojen lukumäärä
     */
    public int getKirjoja() {
        return kirjat.getLkm();
    }
    
    
    /**
     * Palauttaa kerhon lainojen lukumäärän.
     * @return lainojen lukumäärä
     */
    public int getLainoja() {
        return lainat.getLkm();
    }
    
    
    /**
     * Poistetaan jäsen.
     * @param jasen poistettava jäsen
     */
    public void poista(Jasen jasen) {
        if (jasen == null) return;
        jasenet.poista(jasen.getTunnusNro());  
    }
    
    
    /**
     * Poistetaan kirja.
     * @param kirja poistettava kirja
     */
    public void poista(Kirja kirja) {
        if (kirja == null) return;
        kirjat.poista(kirja.getTunnusNro());  
    }
    
    
    /**
     * Poistetaan laina.
     * @param laina poistettava laina
     */
    public void poista(Laina laina) {
        if (laina == null) return;
        lainat.poista(laina.getTunnusNro());  
    }
    
    
    /**
     * Poistetaan laina samalla kun kirja poistetaan.
     * @param kid kirjan tunnusnumero
     */
    public void poistaLaina(int kid) {
        lainat.poistaLaina(kid);
    }
    
    
    /**
     * Poistetaan jäsenen kaikki lainat.
     * @param id jäsenen tunnusnumero
     */
    public void poistaJasenenLainat(int id) {
        lainat.poistaJasenenLainat(id);
    }
    
    
    /**
     * Tallentaa kerhon tiedot tiedostoon.
     * @throws PoikkeusException jos epäonnistuu
     */
    public void tallenna() throws PoikkeusException {
        jasenet.tallenna();
        kirjat.tallenna();
        lainat.tallenna();
        Dialogs.showMessageDialog("Tallennettu!");
    }
    
    
    /**
     * Tallentaa jäsenet tiedostoon.
     * @throws PoikkeusException jos epäonnistuu
     */
    public void tallennaJasenet() throws PoikkeusException {
        jasenet.tallenna();
    }
    
    
    /**
     * Tallentaa kirjat tiedostoon.
     * @throws PoikkeusException jos epäonnistuu
     */
    public void tallennaKirjat() throws PoikkeusException {
        kirjat.tallenna();
    }
    
    
    /**
     * Tallentaa lainat tiedostoon.
     * @throws PoikkeusException jos epäonnistuu
     */
    public void tallennaLainat() throws PoikkeusException {
        lainat.tallenna();
    }
    
    
    /**
     * Testiohjelma kerholle.
     * @param args ei käytössä
     * @example
     * <pre name="test">
     * #import kerho.Jasen;
     * #import kerho.Kerho;
     * #import kerho.Kirja;
     *  Kerho kerho = new Kerho();
     *  Jasen jasen1 = new Jasen();
     *  Jasen jasen2 = new Jasen();
     *  jasen1.rekisteroi();
     *  jasen2.rekisteroi();
     *  jasen1.taytaJasen();
     *  jasen2.taytaJasen();
     *  kerho.lisaa(jasen1);
     *  kerho.lisaa(jasen2);
     *  kerho.getJasenia() === 2;
     *  Kirja kirja1 = new Kirja();
     *  Kirja kirja2 = new Kirja();
     *  kirja1.rekisteroi();
     *  kirja2.rekisteroi();
     *  kirja1.taytaKirja();
     *  kirja2.taytaKirja();
     *  kerho.lisaa(kirja1);
     *  kerho.lisaa(kirja2);
     *  kerho.getKirjoja() === 2;
     *  kerho.poista(jasen1);
     *  kerho.getJasenia() === 1;
     *  kerho.poista(kirja1);
     *  kerho.getKirjoja() === 1;
     * </pre>
     */
    public static void main(String args[]) {
        Kerho kerho = new Kerho();
        Jasen jasen1 = new Jasen();
        Jasen jasen2 = new Jasen();
        jasen1.rekisteroi();
        jasen2.rekisteroi();
        jasen1.taytaJasen();
        jasen2.taytaJasen();
        kerho.lisaa(jasen1);
        kerho.lisaa(jasen2);
        Kirja kirja1 = new Kirja();
        Kirja kirja2 = new Kirja();
        kirja1.rekisteroi();
        kirja2.rekisteroi();
        kirja1.taytaKirja();
        kirja2.taytaKirja();
        kerho.lisaa(kirja1);
        kerho.lisaa(kirja2);
        System.out.println("Kerhotesti");
        for (int i=0; i<kerho.getJasenia(); i++) {
            Jasen jasen = kerho.annaJasen(i);
            System.out.println("Jäsen: " + i);
            jasen.tulosta(System.out);
        }
        for (int i=0; i<kerho.getKirjoja(); i++) {
            Kirja kirja = kerho.annaKirja(i);
            System.out.println("Kirja: " + i);
            kirja.tulosta(System.out);
        }
    }
}