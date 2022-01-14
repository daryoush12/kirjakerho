package fxKerho;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import kerho.Kerho;
import kerho.Kirja;
import kerho.Laina;
import kerho.PoikkeusException;

/**
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class KerhoLainatController implements ModalControllerInterface<Kerho> {
    @FXML private ListChooser<Laina> chooserLainat;

    @FXML void handleLopeta() {
        lopeta();
    }

    @FXML void handlePalauta() {
        palauta();
    }

    @FXML void handleTakaisin() {
        takaisin();
    }

    @FXML void handleTallennaLaina() {
        tallenna();
    }

    @FXML void handleTietoja() {
        tietoja();
    }
    
    //<==============================================================>

    private Laina lainaI;
    private Kerho kerho;
    
    
    /**
     * Palautetaan eli "poistetaan" laina.
     */
    private void palauta() {
        lainaI = chooserLainat.getSelectedObject();
        Laina laina = lainaI;
        int kid = lainaI.getKid();
        Kirja kirja = kerho.getKirja(kid);
        if (laina == null) return;
        if (!Dialogs.showQuestionDialog("Poisto", "Palautetaanko kirja?", "Kyllä", "Ei") )
            return;
        kerho.poista(laina);
        kirja.setLainassa("vapaana");
        try {
            kerho.tallennaLainat();
            kerho.tallennaKirjat();
        } catch (PoikkeusException e) {
            e.printStackTrace();
        }
        int index = chooserLainat.getSelectedIndex();
        hae();
        chooserLainat.setSelectedIndex(index);
    }
    
    
    /**
     * Palataan takaisin.
     */
    private void takaisin() {
        ModalController.closeStage(chooserLainat);
    }
    
    
    /**
     * Tallennetaan muutokset.
     */
    private String tallenna() {
        try {
            kerho.tallennaLainat();
            return null;
        } catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Ei pystytty tallentamaan! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    
    /**
     * Suljetaan ohjelma.
     */
    private void lopeta() {
        System.exit(0);
    }
    
    
    /**
     * Siirrytään suunnitelmasivulle.
     */
    private void tietoja() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/psvaltus");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    
    /**
     * Haetaan laina.
     */
    private void hae() {
        chooserLainat.clear();
        int in = 0;
        int kid;
        for (int i=0; i<kerho.getLainoja(); i++) {
            Laina laina = kerho.annaLaina(i);
            if (laina.getId() == kerho.getJasenINro()) {
                kid = laina.getKid();
                for (int j=0; i<kerho.getKirjoja(); j++) {
                    Kirja kirja = kerho.annaKirja(j);
                    if (kirja.getTunnusNro() == kid) {
                        chooserLainat.add(kirja.getNimi(), laina);
                        break;
                    }
                }
            }
        }
        chooserLainat.setSelectedIndex(in);
    }
    
    
    /**
     * Näytetään lainan tiedot.
     */
    protected void naytaLaina() {
        lainaI = chooserLainat.getSelectedObject();
        if (lainaI == null) return;
    }
    
    
    /**
     * Alustaa kerhon lukemalla sen tiedostosta.
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    protected String lueTiedostosta() {
        try {
            kerho.lueLainatTiedostosta();
            hae();
            kerho.lueKirjatTiedostosta();
            return null;
        } catch (PoikkeusException e) {
            hae();
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }
  
    
    /**
     * Ikkunan palautus.
     */
    @Override
    public Kerho getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    
    /**
     * Mitä tehdään kun ikkuna on näytetty.
     */
    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    
    /**
     * Alustetaan laina-ikkunan controlleri.
     * @param kerho avattu kerho
     */
    @Override
    public void setDefault(Kerho kerho) {
        this.kerho = kerho;
        lueTiedostosta();
    }
}