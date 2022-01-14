package kerho;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Laina-luokka. "Yhdistellään" jäsenten ja kirjojen tunnusnumeroita.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class Laina {
    private int tunnusNro;
    private static int seuraavaNro = 1;
    private int id;
    private int kid;
    
    
    /**
     * Luodaan laina.
     * @param id lainaajan tunnusnumero
     * @param kid kirjan tunnusnumero
     */
    public Laina(int id, int kid) {
        this.id = id;
        this.kid = kid;
    }
    
    
    /**
     * Antaa lainalle rekisterinumeron.
     * @return lainan tunnusnumero
     * @example
     * <pre name="test">
     *  Laina laina1 = new Laina(1,1);
     *  laina1.getTunnusNro() === 0;
     *  laina1.rekisteroi();
     *  Laina laina2 = new Laina(1,2);
     *  laina2.rekisteroi();
     *  int n1 = laina1.getTunnusNro();
     *  int n2 = laina2.getTunnusNro();
     *  n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    
    /**
     * Palauttaa tiedot eroteltuna.
     * @example
     * <pre name="test">
     *   Laina laina = new Laina(1,1);
     *   laina.parse("   1  |  1  | 1");
     *   laina.toString().startsWith("1|1|1") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                id + "|" +
                kid;        
    }
    
    
    /**
     * Erotellaan tiedot tolpilla.
     * @param rivi rivi josta tiedot on otettu
     * @example
     * <pre name="test">
     *   Laina laina = new Laina(1,1);
     *   laina.parse("   1  |  1  | 1");
     *   laina.getTunnusNro() === 1;
     *   laina.toString().startsWith("1|1|1") === true;
     *   laina.rekisteroi();
     *   int n = laina.getTunnusNro();
     *   laina.parse(""+(n));
     *   laina.rekisteroi();
     *   laina.getTunnusNro() === n+1; 
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        id = Mjonot.erota(sb, '|', id);
        kid = Mjonot.erota(sb, '|', kid);
    }
    
    
    /**
     * Palautetaan id.
     * @return lainaajan tunnusnumero
     */
    public int getId() {
        return id;
    }
    
    
    /**
     * Palautetaan kid.
     * @return kirjan tunnusnumero
     */
    public int getKid() {
        return kid;
    }
    
    
    /**
     * Palautetaan tunnusnumero.
     * @return lainan tunnusnumero
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
     * Tulostetaan lainan tiedot.
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) + "  " + "jäsen id " + id
                + "kirja id " + kid);
    }
    
    
    /**
     * Tulostetaan lainan tiedot.
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    

    /**
     * Testiohjelma lainalle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Laina laina1 = new Laina(1,1);
        Laina laina2 = new Laina(1,2);
        laina1.rekisteroi();
        laina2.rekisteroi();
        laina1.tulosta(System.out);
        laina2.tulosta(System.out);
        laina1.tulosta(System.out);
        laina2.tulosta(System.out);
    }
}