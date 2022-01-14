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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Kerhon kirjojen lisääminen.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class Kirjat implements Iterable<Kirja> {
    private List<Kirja> alkiot = new ArrayList<Kirja>();
    private String perusNimi = "kirjat";
    private boolean muutettu = false;
    
    
    /**
     * Kirjat-muodostaja.
     */
    public Kirjat() {
    }
    
    
    /**
     * Palauttaa kirjojen lukumäärän.
     * @return kirjojen lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Palautetaan kirja.
     * @param kid kirjan tunnusnumero
     * @return kirja tai null
     */
    public Kirja getKirja(int kid) {
        for (int i=0; i<alkiot.size(); i++) {
            if (alkiot.get(i).getTunnusNro() == kid) return alkiot.get(i);
        }
        return null;
    }
    
    
    /**
     * Muutetaan kirjan tietoja.
     * @param kirja kirja
     * @throws PoikkeusException poikkeus
     */
    public void korvaaTaiLisaa(Kirja kirja) throws PoikkeusException {
        int id = kirja.getTunnusNro();
        for (int i=0; i<alkiot.size(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, kirja);
                muutettu = true;
                return;
            }
        }
        lisaa(kirja);
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
            fo.println(alkiot.size());
            for (Kirja kirja : this) {
                fo.println(kirja.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new PoikkeusException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new PoikkeusException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    
    /**
     * Palautetaan kirjan viite.
     * @param i monennenko kirjan viite
     * @return viite kirjaan
     * @throws IndexOutOfBoundsException i ylittää rajan
     */
    public Kirja anna(int i) throws IndexOutOfBoundsException {
        if (i<0 || i>alkiot.size()) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot.get(i);
    }
    
    
    /**
     * Lisää uuden kirjan.
     * @param kirja lisättävän kirjan viite
     */
    public void lisaa(Kirja kirja) {
        alkiot.add(kirja);
        muutettu = true;
    }
    
    
    /** 
     * Poistetaan kirja.
     * @param id kirjan id
     * @return 0 jos ei poistettu ja 1 jos poistettiin
     */
    public int poista(int id) { 
        int in = etsiId(id); 
        if (in < 0) return 0; 
        alkiot.remove(in);
        muutettu = true; 
        return 1; 
    } 
    
    
    /**
     * Etsitään kirjan id.
     * @param id etsittävä id
     * @return id kirjan id
     */
    public int etsiId(int id) { 
        for (int i = 0; i<alkiot.size(); i++) 
            if (id == alkiot.get(i).getTunnusNro()) return i; 
         return -1; 
    } 
    
    
    /**
     * Lukee tiedostosta.
     * @throws PoikkeusException lukeminen epäonnistuu
     */
    public void lueTiedostosta() throws PoikkeusException {
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Kirja kirja = new Kirja();
                kirja.parse(rivi);
                lisaa(kirja);
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
     * Luokka kirjojen iteroimiseksi.
     */
    public class KirjatIterator implements Iterator<Kirja> {
        private int kohdalla = 0;
        
        /**
         * Onko olemassa vielä seuraavaa kirjaa
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä kirjoja
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        
        /**
         * Annetaan seuraava kirja.
         * @return seuraava kirja
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Kirja next() throws NoSuchElementException {
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
     * Palautetaan iteraattori kirjoistaan.
     * @return kirja iteraattori
     */
    @Override
    public Iterator<Kirja> iterator() {
        return new KirjatIterator();
    }
    
    
    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien kirjojen viitteet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä kirjoista
     */ 
    @SuppressWarnings("unused")
    public Collection<Kirja> etsi(String hakuehto, int k) { 
        Collection<Kirja> loytyneet = new ArrayList<Kirja>(); 
        for (Kirja kirja : this) { 
            loytyneet.add(kirja); 
        } 
        return loytyneet; 
    }
    

    /**
     * Testiohjelma kirjoille.
     * @param args ei käytössä
     * @example
     * #import kerho.Kirjat;
     * #import kerho.Kirja;
     * <pre name="test">
     *  Kirjat kirjat = new Kirjat();
     *  Kirja kirja1 = new Kirja();
     *  Kirja kirja2 = new Kirja();
     *  kirja1.rekisteroi();
     *  kirja1.taytaKirja();
     *  kirja2.rekisteroi();
     *  kirja2.taytaKirja();
     *  kirjat.getLkm() === 0;
     *  kirjat.lisaa(kirja1);
     *  kirjat.lisaa(kirja2);
     *  kirjat.getLkm() === 2;
     * </pre>
     */
    public static void main(String[] args) {
        Kirjat kirjat = new Kirjat();
        Kirja kirja1 = new Kirja();
        Kirja kirja2 = new Kirja();
        kirja1.rekisteroi();
        kirja1.taytaKirja();
        kirja2.rekisteroi();
        kirja2.taytaKirja();
        kirjat.lisaa(kirja1);
        kirjat.lisaa(kirja2);
        System.out.println("Testikirjat");
        for (int i=0; i<kirjat.getLkm(); i++) {
            Kirja kirja = kirjat.anna(i);
            System.out.println("Kirjan nro: " + i);
            kirja.tulosta(System.out);
        }
    }
}