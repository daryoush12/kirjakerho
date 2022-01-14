package fxKerho;

import static fxKerho.JasenDialogController.getFieldId;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kerho.Kerho;
import kerho.Kirja;
import kerho.Laina;
import kerho.PoikkeusException;

/**
 * Kontrolleri kirjaikkunaan
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class KerhoKirjatController implements ModalControllerInterface<Kerho> {
    @FXML private ListChooser<Kirja> chooserKirjat;
    @FXML private ScrollPane panelKirja; 
    @FXML private GridPane gridKirja; 
    
    @FXML void handleLopeta() {
        lopeta();
    }
    
    @FXML void handleTietoja() {
        tietoja();
    }

    @FXML void handleLainaa() {
        lainaa();
    }

    @FXML void handleLisaaKirja() {
        lisaaKirja();
    }

    @FXML void handleMuokkaaKirja() {
        muokkaaKirja(1);
    }

    @FXML void handlePoistaKirja() {
        poistaKirja();
    }

    @FXML void handleTallennaKirja() {
        tallennaKirja();
    }
    
    @FXML void handleTakaisin() {
        ModalController.closeStage(chooserKirjat);
    }
    
    @FXML void handleTulosta() {
        TulostusController tulostusController = TulostusController.tulosta(null);
        tulostaKirjat(tulostusController.getTextArea()); 
    }
    
    //<==============================================================>

    private Kirja kirjaI;
    private Kerho kerho;
    private TextField edits[];
    private int kentta = 0; 
    
    
    /**
     * Lisätään kirja.
     */
    private void lisaaKirja() {
        Kirja kirja = new Kirja();
        kirja = KirjaDialogController.kysyKirja(null, kirja, 1); 
        if (kirja == null) return; 
        kirja.rekisteroi(); 
        kerho.lisaa(kirja);
        try {
            kerho.tallennaKirjat();
        } catch (PoikkeusException e) {
            e.printStackTrace();
        }
        hae(kirja.getTunnusNro());
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
     * Haetaan kirja.
     * @param nro kirjan numero
     */
    protected void hae(int nro) {
        chooserKirjat.clear();
        int in = 0;
        Collection<Kirja> kirjat; 
        try {
            kirjat = kerho.etsiKirja("", 1); 
            int i = 0; 
            for (Kirja kirja:kirjat) { 
                if (kirja.getTunnusNro() == nro) in = i; 
                chooserKirjat.add(kirja.getNimi(), kirja); 
                i++; 
            }
        }
        catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Kirjan hakemisessa ongelmia! " + ex.getMessage());  
        }
        chooserKirjat.getSelectionModel().select(in);
    }
    
    
    /**
     * Muokataan kirjatietoja.
     */
    private void muokkaaKirja(int i) {
        if (kirjaI == null) return;
        try {
            Kirja kirja;
            kirja = KirjaDialogController.kysyKirja(null, kirjaI.clone(), i);
            if (kirja == null) return;
            kerho.korvaaTaiLisaa(kirja);
            hae(kirja.getTunnusNro());
        } catch (CloneNotSupportedException e) { 
            //
        } catch (PoikkeusException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    }
    
    
    /**
     * Poistetaan kirja.
     */
    private void poistaKirja() {
        Kirja kirja = kirjaI;
        if (kirja == null) return;
        if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko kirja: " + kirja.getNimi(), "Kyllä", "Ei") )
            return;
        int kid = kirja.getTunnusNro();
        kerho.poistaLaina(kid);
        kerho.poista(kirja);
        try {
            kerho.tallennaKirjat();
            kerho.tallennaLainat();
        } catch (PoikkeusException e) {
            e.printStackTrace();
        }
        int index = chooserKirjat.getSelectedIndex();
        hae(0);
        chooserKirjat.setSelectedIndex(index);
    }
    
    
    /**
     * Tallennetaan kirjatiedot.
     */
    private String tallennaKirja() {
        try {
            kerho.tallenna();
            return null;
        } catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Error! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    
    /**
     * Näytetään kirjan tiedot.
     */
    protected void naytaKirja() {
        kirjaI = chooserKirjat.getSelectedObject();
        if (kirjaI == null) return;
        KirjaDialogController.naytaKirja(edits, kirjaI);
    }
    
    
    /**
     * Tulostaa kirjan tiedot.
     * @param os tietovirta johon tulostetaan
     * @param kirja tulostettava kirja
     */
    public void tulosta(PrintStream os, final Kirja kirja) {
        os.println("----------------------------------------------");
        kirja.tulosta(os);
        os.println("----------------------------------------------");
    }
    
    
    /**
     * Tulostaa kaikki listassa olevat kirjat tekstialueeseen.
     * @param text alue johon tulostetaan
     */
    public void tulostaKirjat(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki kirjat: \n");
            for (int i=0; i<kerho.getKirjoja(); i++) { 
                Kirja kirja = kerho.annaKirja(i);
                os.println(kirja.toString());
                os.println("\n");
            }
        }
    }
    
    
    /**
     * Siirrytään lainausikkunaan.
     * @return null tai virhe
     */
    private String lainaa() {
        if (chooserKirjat.getSelectedObject() == null) return null;
        int kid = chooserKirjat.getSelectedObject().getTunnusNro();
        Kirja kirja = chooserKirjat.getSelectedObject();
        String nimi = kerho.getJasen(kerho.getJasenINro()).getNimi();
        if (kerho.getKid(kid) == true) {
            Dialogs.showMessageDialog("Kirja on jo lainassa!");
            return null;
        }
        Laina laina = new Laina(kerho.getJasenINro(), chooserKirjat.getSelectedObject().getTunnusNro());
        laina.rekisteroi();
        kerho.lisaa(laina);
        kirja.setLainassa(nimi);
        try {
            kerho.tallennaLainat();
            kerho.tallennaKirjat();
        } catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Ei pystytty tallentamaan! " + ex.getMessage());
            return ex.getMessage();
        }
        Dialogs.showMessageDialog("Lainaus onnistui!");
        return null;
    }
    
    
    /**
     * Alustaa kerhon lukemalla sen tiedostosta.
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    protected String lueTiedostosta() {
        try {
            kerho.lueKirjatTiedostosta();
            hae(0);
            kerho.lueLainatTiedostosta();
            return null;
        } catch (PoikkeusException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }
    
    
    /**
     * Alustetaan kirjaikkuna.
     */
    protected void alusta() {
        chooserKirjat.clear();
        chooserKirjat.addSelectionListener(e -> naytaKirja());
        edits = KirjaDialogController.luoKentat(gridKirja); 
        for (TextField edit: edits) 
             if (edit != null) { 
                 edit.setEditable(false); 
                 edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaaKirja(getFieldId(e.getSource(),0)); }); 
                 edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta)); 
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
     * Alustetaan kirja-ikkunan controlleri.
     * @param kerho avattu kerho
     */
    @Override
    public void setDefault(Kerho kerho) {
        this.kerho = kerho;
        alusta();
        lueTiedostosta();
    }
}