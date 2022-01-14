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
 * Lainat-luokka. Ylläpitää lainastoa.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class Lainat implements Iterable<Laina> {
    private List<Laina> alkiot = new ArrayList<Laina>();
    private String perusNimi = "lainat";
    private boolean muutettu = false;
    
    
    /**
     * Lainat-muodostaja.
     */
    public Lainat() {
    }
    
    
    /**
     * Palauttaa lainojen lukumäärän.
     * @return lainojen lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Poistetaan laina kirjan tunnusluvulla.
     * @param kid kirjan tunnusluku
     */
    public void poistaLaina(int kid) {
        for (int i=0; i<alkiot.size(); i++) {
            if (alkiot.get(i).getKid() == kid) poista(alkiot.get(i).getTunnusNro());
        }
    }
    
    
    /**
     * Poistetaan jäsenen kaikki lainat.
     * @param id jäsenen tunnusnumero
     */
    public void poistaJasenenLainat(int id) {
        for (int i=0; i<alkiot.size(); i++) {
            if (alkiot.get(i).getId() == id) poista(alkiot.get(i).getTunnusNro());
        }
    }
    
    
    /**
     * Lukee tiedostosta.
     * @throws PoikkeusException lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS PoikkeusException 
     * #import java.io.File;
     * #import java.util.Iterator;
     *  Lainat lainat = new Lainat();
     *  Laina laina1 = new Laina(1,1), laina2 = new Laina(2,2);
     *  lainat.lisaa(laina1);
     *  lainat.lisaa(laina2);
     *  Iterator<Laina> i = lainat.iterator();
     *  i.next() === laina1;
     *  i.next() === laina2;
     *  i.hasNext() === false;
     *  lainat.getLkm() === 2;
     *  lainat.poista(laina1.getTunnusNro());
     *  lainat.poista(laina2.getTunnusNro());
     *  lainat.getLkm() === 0;
     *  lainat.tallenna();
     *  lainat.lueTiedostosta();
     *  int lkm1 = lainat.getLkm();
     *  lainat.tallenna();
     *  lainat = new Lainat();    
     *  lainat.getLkm() === 0;
     *  lainat.lisaa(laina1);
     *  lainat.lisaa(laina2);
     *  lainat.getLkm() === lkm1 + 2;
     *  lainat.poista(laina1.getTunnusNro());
     *  lainat.poista(laina2.getTunnusNro());
     *  lainat.getLkm() === lkm1;
     *  lainat.tallenna();
     *  lainat = new Lainat();
     *  lainat.lueTiedostosta(); 
     *  lainat.getLkm() === lkm1;
     * </pre>
     */
    public void lueTiedostosta() throws PoikkeusException {
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Laina laina = new Laina(1,1);
                laina.parse(rivi);
                lisaa(laina);
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
            fo.println(alkiot.size());
            for (Laina laina : this) {
                fo.println(laina.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new PoikkeusException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new PoikkeusException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    
    /**
     * Palautetaan lainan viite.
     * @param i monennenko lainan viite
     * @return viite lainaan
     * @throws IndexOutOfBoundsException i ylittää rajan
     */
    public Laina anna(int i) throws IndexOutOfBoundsException {
        if (i<0 || i>alkiot.size()) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot.get(i);
    }
    
    
    /**
     * Lisää uuden lainan.
     * @param laina lisättävän lainan viite
     * @example
     * <pre name="test">
     * #THROWS PoikkeusException 
     * Lainat lainat = new Lainat();
     * Laina laina1 = new Laina(1,1);
     * Laina laina2 = new Laina(2,2);
     * lainat.getLkm() === 0;
     * lainat.lisaa(laina1); lainat.getLkm() === 1;
     * lainat.lisaa(laina2); lainat.getLkm() === 2;
     * </pre>
     */
    public void lisaa(Laina laina) {
        alkiot.add(laina);
        muutettu = true;
    }
    
    
    /** 
     * Poistetaan laina.
     * @param id lainan id
     * @return 0 jos ei poistettu ja 1 jos poistettiin
     * @example
     * <pre name="test">
     * #THROWS PoikkeusException 
     * Lainat lainat = new Lainat();
     * Laina laina1 = new Laina(1,1);
     * Laina laina2 = new Laina(2,2);
     * lainat.getLkm() === 0;
     * lainat.lisaa(laina1); lainat.getLkm() === 1;
     * lainat.lisaa(laina2); lainat.getLkm() === 2;
     * lainat.poista(laina1.getTunnusNro()); lainat.getLkm() === 1;
     * lainat.poista(laina2.getTunnusNro()); lainat.getLkm() === 0;
     * </pre>
     */
    public int poista(int id) { 
        int in = etsiId(id); 
        if (in < 0) return 0; 
        alkiot.remove(in);
        muutettu = true; 
        return 1; 
    } 
    
    
    /**
     * Etsitään lainan id.
     * @param id etsittävä id
     * @return id lainan id
     */
    public int etsiId(int id) { 
        for (int i = 0; i<alkiot.size(); i++) 
            if (id == alkiot.get(i).getTunnusNro()) return i; 
         return -1; 
    } 
    
    
    /**
     * Tutkitaan onko kirjalla jo laina olemassa.
     * @param kid kirjan id
     * @return true jos on jo lainattu, muuten false
     */
    public boolean getKid(int kid) {
        for (int i=0; i<alkiot.size(); i++) {
            if (alkiot.get(i).getKid() == kid) return true;
        }
        return false;
    }
    
    
    /**
     * Luokka lainojen iteroimiseksi.
     */
    public class LainatIterator implements Iterator<Laina> {
        private int kohdalla = 0;
        
        /**
         * Onko olemassa vielä seuraavaa lainaa.
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä lainoja
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        
        /**
         * Annetaan seuraava laina.
         * @return seuraava laina
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Laina next() throws NoSuchElementException {
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
     * Palautetaan iteraattori lainoistaan.
     * @return laina iteraattori
     */
    @Override
    public Iterator<Laina> iterator() {
        return new LainatIterator();
    }
    
    
    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien lainojen viitteet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi 
     * @return tietorakenteen löytyneistä lainoista
     */ 
    @SuppressWarnings("unused")
    public Collection<Laina> etsi(String hakuehto, int k) { 
        Collection<Laina> loytyneet = new ArrayList<Laina>(); 
        for (Laina laina : this) { 
            loytyneet.add(laina); 
        } 
        return loytyneet; 
    }
    

    /**
     * Testiohjelma lainoille.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
    // TODO Auto-generated method stub
    
    }

}
