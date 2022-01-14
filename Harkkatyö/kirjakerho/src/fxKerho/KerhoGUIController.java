package fxKerho;

import java.awt.Desktop;

import static fxKerho.JasenDialogController.getFieldId;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kerho.Jasen;
import kerho.Kerho;
import kerho.PoikkeusException;

/**
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class KerhoGUIController {
    @FXML private ListChooser<Jasen> chooserJasenet;
    @FXML private ScrollPane panelJasen; 
    @FXML private GridPane gridJasen; 
    @FXML private TextField hakuehto;
    
    @FXML void handleAvaa() {
        avaa();
    }
    
    @FXML
    void handleHaku() {
        hae(0);
    }

    @FXML void handleLisaaJasen() {
        lisaaJasen();
    }

    @FXML void handleLopeta() {
        lopeta();
    }

    @FXML void handleMuokkaaJasen() {
        muokkaaJasen(1);
    }

    @FXML void handlePoistaJasen() {
        poistaJasen();
    }

    @FXML void handleTallennaJasen() {
        tallennaJasen();
    }

    @FXML void handleTietoja() {
        tietoja();
    }

    @FXML void handleTulosta() {
        TulostusController tulostusController = TulostusController.tulosta(null);
        tulostaJasenet(tulostusController.getTextArea()); 
    }
    
    @FXML void handleLainaamaan() {
        lainaamaan();
    }
    
    @FXML void handlePalauta() {
        palauta();
    }

    //<==============================================================>

    private Kerho kerho;
    private Jasen jasenI;  
    private static int jasenNro;
    private TextField edits[];
    private int kentta = 0; 
    
    
    /**
     * Alustetaan pääikkuna.
     */
    protected void alusta() {
        chooserJasenet.clear();
        chooserJasenet.addSelectionListener(e -> naytaJasen());
        edits = JasenDialogController.luoKentat(gridJasen); 
        for (TextField edit: edits) 
             if (edit != null) { 
                 edit.setEditable(false); 
                 edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaaJasen(getFieldId(e.getSource(),0)); }); 
                 edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta)); 
             }   
    }

    
    /**
     * Avataan tiedosto.
     */
    public void avaa() {
        alusta();
        lueTiedostosta();
    }
    
    
    /**
     * Alustaa kerhon lukemalla sen tiedostosta.
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    protected String lueTiedostosta() {
        try {
            kerho.lueJasenetTiedostosta();
            hae(0);
            return null;
        } catch (PoikkeusException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }
    
    
    /**
     * Lisätään jäsen.
     */
    private void lisaaJasen() {
        Jasen jasen = new Jasen();
        jasen = JasenDialogController.kysyJasen(null, jasen, 1); 
        if ( jasen == null ) return; 
        jasen.rekisteroi(); 
        kerho.lisaa(jasen);
        try {
            kerho.tallennaJasenet();
        } catch (PoikkeusException e) {
            e.printStackTrace();
        }
        hae(jasen.getTunnusNro());
    }
    
    
    /**
     * Haetaan jäsen.
     * @param nro jäsenen numero
     */
    protected void hae(int nro) {
        int jnro = nro;
        if (jnro <= 0) {
            Jasen valittu = jasenI;
            if (valittu != null) jnro = valittu.getTunnusNro();
        }
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*')<0) ehto = "*" + ehto + "*";
        chooserJasenet.clear();
        int in = 0;
        Collection<Jasen> jasenet; 
        try {
            jasenet = kerho.etsi(ehto, 1);
            int i = 0; 
            for (Jasen jasen:jasenet) { 
                if (jasen.getTunnusNro() == nro) in = i; 
                chooserJasenet.add(jasen.getNimi(), jasen); 
                i++; 
            }
        }
        catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Jäsenen hakemisessa ongelmia! " + ex.getMessage());  
        }
        chooserJasenet.getSelectionModel().select(in);
    }
    
    
    /**
     * Suljetaan ohjelma.
     */
    private void lopeta() {
        System.exit(0);
    }
    
    
    /**
     * Muokataan jäsentietoja.
     */
    private void muokkaaJasen(int i) {
        if (jasenI == null) return;
        try {
            Jasen jasen;
            jasen = JasenDialogController.kysyJasen(null, jasenI.clone(), i);
            if ( jasen == null ) return;
            kerho.korvaaTaiLisaa(jasen);
            hae(jasen.getTunnusNro());
        } catch (CloneNotSupportedException e) { 
            //
        } catch (PoikkeusException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    }
    
    
    /**
     * Poistetaan jäsen.
     */
    private void poistaJasen() {
        Jasen jasen = jasenI;
        if ( jasen == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko jäsen: " + jasen.getNimi(), "Kyllä", "Ei") )
            return;
        kerho.poistaJasenenLainat(jasen.getTunnusNro());
        kerho.poista(jasen);
        try {
            kerho.tallennaJasenet();
            kerho.tallennaLainat();
        } catch (PoikkeusException e) {
            e.printStackTrace();
        }
        int index = chooserJasenet.getSelectedIndex();
        hae(0);
        chooserJasenet.setSelectedIndex(index);
    }
    
    
    /**
     * Tallennetaan jäsentiedot.
     */
    private String tallennaJasen() {
        try {
            kerho.tallenna();
            return null;
        } catch (PoikkeusException ex) {
            Dialogs.showMessageDialog("Error! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    
    /**
     * Palautetaan kerho.
     * @return kerho
     */
    public Kerho getKerho() {
        return kerho;
    }
    
    
    /**
     * Asetetaan kerho.
     * @param kerho kerho joka näytetään
     */
    public void setKerho(Kerho kerho) {
        this.kerho = kerho;
        hae(0);
        naytaJasen();
    }
    
    
    /**
     * Palautetaan valitun jäsenen tunnus.
     * @return valitun jäsenen tunnus
     */
    public int getJasenNro() {
        return jasenNro;
    }
    
    
    /**
     * Näytetään jäsenen tiedot.
     */
    protected void naytaJasen() {
        jasenI = chooserJasenet.getSelectedObject();
        if (jasenI == null) return;
        JasenDialogController.naytaJasen(edits, jasenI);
    }
    
    
    /**
     * Tulostaa jäsenen tiedot.
     * @param os tietovirta johon tulostetaan
     * @param jasen tulostettava jäsen
     */
    public void tulosta(PrintStream os, final Jasen jasen) {
        os.println("----------------------------------------------");
        jasen.tulosta(os);
        os.println("----------------------------------------------");
    }
    
    
    /**
     * Tulostaa kaikki listassa olevat jäsenet tekstialueeseen.
     * @param text alue johon tulostetaan
     */
    public void tulostaJasenet(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki jäsenet: \n");
            for (int i=0; i<kerho.getJasenia(); i++) { 
                Jasen jasen = kerho.annaJasen(i);
                os.println(jasen.toString());
                os.println("\n");
            }
        }
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
     * Siirrytään lainausikkunaan.
     */
    private void lainaamaan() {
        if (jasenI == null) return;
        jasenNro = jasenI.getTunnusNro();
        kerho.setJasenINro(jasenNro);
        ModalController.showModal(KerhoKirjatController.class.getResource("KerhoKirjatView.fxml"), "Kirjat",  null, kerho);
    }
    
    
    /**
     * Siirrytään palautusikkunaan.
     */
    private void palauta() {
        if (jasenI == null) return;
        jasenNro = jasenI.getTunnusNro();
        kerho.setJasenINro(jasenNro);
        ModalController.showModal(KerhoLainatController.class.getResource("KerhoLainatView.fxml"), "Lainat",  null, kerho);
    }
} 