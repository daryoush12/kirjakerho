package kerho;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Kerhon jäsenien lisääminen.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class Jasenet implements Iterable<Jasen> {
    private int lkm = 0;
    private static final int MAX_LKM = 5;
    private Jasen alkiot[] = new Jasen[MAX_LKM];
    private String perusNimi = "jasenet";
    private boolean muutettu = false;
    
    
    /**
     * Jasenet-muodostaja.
     */
    public Jasenet() {
    }
    
    
    /**
     * Palautetaan jäsen.
     * @param id jäsenen tunnusnumero
     * @return jäsen tai null
     */
    public Jasen getJasen(int id) {
        for (int i=0; i<alkiot.length; i++) {
            if (alkiot[i].getTunnusNro() == id) return alkiot[i];
        }
        return null;
    }
    
    
    /**
     * Palauttaa jäsenten lukumäärän.
     * @return jäsenten lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /** 
     * Poistetaan jäsen.
     * @param id jäsenen id
     * @return 0 jos ei poistettu ja 1 jos poistettiin
     */
    public int poista(int id) { 
        int in = etsiId(id); 
        if (in < 0) return 0; 
        lkm--; 
        for (int i = in; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    
    /**
     * Etsitään jäsenen id.
     * @param id etsittävä id
     * @return id jäsenen id
     */
    public int etsiId(int id) { 
        for (int i = 0; i<lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
         return -1; 
    } 
    
    
    /**
     * Korvataan tai lisätään jäsen muokkauksen jälkeen.
     * @param jasen jäsen
     * @throws PoikkeusException poikkeus
     */
    public void korvaaTaiLisaa(Jasen jasen) throws PoikkeusException {
        int id = jasen.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getTunnusNro() == id) {
                alkiot[i] = jasen;
                muutettu = true;
                return;
            }
        }
        lisaa(jasen);
    }
    
    
    /**
     * Lukee tiedostosta.
     * @throws PoikkeusException lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS PoikkeusException 
     * #import java.io.File;
     * #import java.util.Iterator;
     *  Jasenet jasenet = new Jasenet();
     *  Jasen jasen1 = new Jasen(), jasen2 = new Jasen();
     *  jasen1.taytaJasen();
     *  jasen2.taytaJasen();
     *  jasenet.lueTiedostosta();
     *  int lkm1 = jasenet.getLkm();
     *  jasenet.lisaa(jasen1);
     *  jasenet.lisaa(jasen2);
     *  jasenet.tallenna();
     *  jasenet.getLkm() === lkm1 + 2;
     *  jasenet.poista(jasen1.getTunnusNro());
     *  jasenet.poista(jasen2.getTunnusNro());
     *  jasenet.getLkm() === lkm1;
     *  jasenet.tallenna();
     *  jasenet = new Jasenet();    
     *  jasenet.getLkm() === 0;
     *  jasenet.lisaa(jasen1);
     *  jasenet.lisaa(jasen2);
     *  Iterator<Jasen> i = jasenet.iterator();
     *  i.next() === jasen1;
     *  i.next() === jasen2;
     *  i.hasNext() === false;
     *  jasenet.getLkm() === 2;
     *  jasenet.poista(jasen1.getTunnusNro());
     *  jasenet.poista(jasen2.getTunnusNro());
     *  jasenet.getLkm() === 0;
     *  jasenet = new Jasenet();
     *  jasenet.lueTiedostosta(); 
     *  jasenet.tallenna();
     *  jasenet.getLkm() === lkm1;
     * </pre>
     */
    public void lueTiedostosta() throws PoikkeusException {
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Jasen jasen = new Jasen();
                jasen.parse(rivi);
                lisaa(jasen);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new PoikkeusException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new PoikkeusException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    
    /**
     * Palauttaa tiedoston nimen.
     * @return tiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    
    /**
     * Palauttaa tiedoston nimen.
     * @return tiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return perusNimi;
    }
    
    
    /**
     * Palauttaa varakopiotiedoston nimen.
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return perusNimi + ".bak";
    }
    
    
    /**
     * Tallentaa tiedostoon.
     * @throws PoikkeusException talletus epäonnistuu
     */
    public void tallenna() throws PoikkeusException {
        if ( !muutettu ) return;
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(alkiot.length);
            for (Jasen jasen : this) {
                fo.println(jasen.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new PoikkeusException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new PoikkeusException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    
    /**
     * Palautetaan jäsenen viite.
     * @param i monennenko jäsenen viite
     * @return viite jäseneen
     * @throws IndexOutOfBoundsException i ylittää rajan
     */
    public Jasen anna(int i) throws IndexOutOfBoundsException {
        if (i<0 || lkm<=i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /**
     * Lisää uuden jäsenen.
     * @param jasen lisättävän jäsenen viite
     * @example
     * <pre name="test">
     * #THROWS PoikkeusException 
     * Jasenet jasenet = new Jasenet();
     * Jasen jasen1 = new Jasen();
     * Jasen jasen2 = new Jasen();
     * jasenet.getLkm() === 0;
     * jasenet.lisaa(jasen1); jasenet.getLkm() === 1;
     * jasenet.lisaa(jasen2); jasenet.getLkm() === 2;
     * jasenet.lisaa(jasen1); jasenet.getLkm() === 3;
     * jasenet.lisaa(jasen2); jasenet.getLkm() === 4;
     * jasenet.lisaa(jasen1); jasenet.getLkm() === 5;
     * jasenet.lisaa(jasen2); jasenet.getLkm() === 6;
     * </pre>
     */
    public void lisaa(Jasen jasen) {
        if (lkm >= alkiot.length) {
            Jasen klooni[] = new Jasen[lkm + 10];
            for (int i=0; i<alkiot.length; i++) {
                klooni[i] = alkiot[i];
            }
            alkiot = klooni;
        }
        alkiot[lkm] = jasen;
        lkm++;
        muutettu = true;
    }
    
    
    /**
     * Luokka jäsenten iteroimiseksi.
     */
    public class JasenetIterator implements Iterator<Jasen> {
        private int kohdalla = 0;
        
        /**
         * Onko olemassa vielä seuraavaa jäsentä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä jäseniä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        
        /**
         * Annetaan seuraava jäsen.
         * @return seuraava jäsen
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Jasen next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei ole");
            return anna(kohdalla++);
        }
        
        
        /**
         * Tuhoamista ei ole toteutettu.
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Ei poisteta");
        }
    }
    
    
    /**
     * Palautetaan iteraattori jäsenistään.
     * @return jäsen iteraattori
     */
    @Override
    public Iterator<Jasen> iterator() {
        return new JasenetIterator();
    }
    
    
    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä jäsenistä
     */ 
    public Collection<Jasen> etsi(String hakuehto, int k) { 
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 1;
        Collection<Jasen> loytyneet = new ArrayList<Jasen>(); 
        for (Jasen jasen : this) { 
            if (WildChars.onkoSamat(jasen.anna(hk), ehto)) loytyneet.add(jasen);   
        } 
        return loytyneet; 
    }
    

    /**
     * Testiohjelma jäsenille.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Jasenet jasenet = new Jasenet();
        Jasen jasen1 = new Jasen();
        Jasen jasen2 = new Jasen();
        jasen1.rekisteroi();
        jasen1.taytaJasen();
        jasen2.rekisteroi();
        jasen2.taytaJasen();
        jasenet.lisaa(jasen1);
        jasenet.lisaa(jasen2);
        System.out.println("Testijäsenet");
        for (int i=0; i<jasenet.getLkm(); i++) {
            Jasen jasen = jasenet.anna(i);
            System.out.println("Jäsen nro: " + i);
            jasen.tulosta(System.out);
        }
    }
}