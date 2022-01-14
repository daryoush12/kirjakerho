package fxKerho;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kerho.Kirja;

/**
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class KirjaDialogController implements ModalControllerInterface<Kirja>,Initializable  {
    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelKirja;
    @FXML private GridPane gridKirja;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    @FXML private void handleOK() {
        if (kirjaI != null && kirjaI.getNimi().trim().equals("")) {
            naytaVirhe("Ei ole annettu nimeä!");
            return;
        }
        if ( kirjaI != null && kirjaI.getVuosi().trim().matches("[0-9]+") == false) {
            naytaVirhe("Vain numeroita vuosilukuun!");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handlePeruuta() {
        kirjaI = null;
        ModalController.closeStage(labelVirhe);
    }

    //<==============================================================>
    
    private Kirja kirjaI;
    private static Kirja apu = new Kirja();
    private TextField[] edits; 
    private int kentta = 0; 
    
    
    /**
     * Tyhjentään tekstikentät.
     * @param edits tyhjennettävät kentät
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit : edits)
            if (edit != null) edit.setText(""); 
    }
    
    
    /**
     * Luodaan kirjatietojen kentät.
     * @param gridKirja mihin tiedot lisätään
     * @return luodut kentät
     */
    public static TextField[] luoKentat(GridPane gridKirja) {
        gridKirja.getChildren().clear();
        TextField[] edits = new TextField[apu.getKentat()];
        for (int i=0, k=apu.ekaKentta(); k<apu.getKentat(); k++, i++) {
            Label label = new Label(apu.getKysymys(k));
            gridKirja.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridKirja.add(edit, 1, i);
        }
        return edits;
    }
    
    
    /**
     * Palautetaan komponentin tunnus.
     * @param obj komponentti
     * @param oletus oletusarvo
     * @return komponentin tunnus
     */
    public static int getFieldId(Object obj, int oletus) {
        if (!( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }


    /**
     * Alustaa ikkunan.
     */
    protected void alusta() {
        edits = luoKentat(gridKirja);
        for (TextField edit : edits)
            if (edit != null)
                  edit.setOnKeyReleased( e -> kasitteleMuutosKirjaan((TextField)(e.getSource())));
        panelKirja.setFitToHeight(true);
    }
    
    
    /**
     * Käsitellään kirjan muutos.
     * @param edit muutettu kenttä
     */
    protected void kasitteleMuutosKirjaan(TextField edit) {
        if (kirjaI == null) return;
        int k = getFieldId(edit,apu.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = kirjaI.aseta(k,s); 
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } 
        else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Näytetään virhe.
     * @param virhe mikä virhe
     */
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    
    /**
     * Alustetaan ikkunan kirja.
     * @param kirja ikkunan kirja
     */
    @Override
    public void setDefault(Kirja kirja) {
        kirjaI = kirja;
        naytaKirja(edits, kirjaI);
    }

    
    /**
     * Palautetaan ikkunan kirja.
     * @return muutettu kirja
     */
    @Override
    public Kirja getResult() {
        return kirjaI;
    }
    
    
    /**
     * Asetetaan kenttä.
     * @param kentta asetettava kenttä
     */
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }
    
    
    /**
     * Mitä tehdään kun dialogi on näytetty.
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apu.ekaKentta(), Math.min(kentta, apu.getKentat()-1)); 
        edits[kentta].requestFocus(); 
    }
    
    
    /**
     * Näytetään kirjan tiedot kenttiin.
     * @param edits taulukko näytettävistä kentistä
     * @param kirja näytettävä kirja
     */
    public static void naytaKirja(TextField[] edits, Kirja kirja) {
        if (kirja == null) return;
        for (int k=kirja.ekaKentta(); k<kirja.getKentat(); k++) {
            edits[k].setText(kirja.anna(k));
        }
    }
    
    
    /**
     * Luodaan kirjan kysymisdialogi.
     * @param modalityStage mille ollaan modaalisia
     * @param oletus mitä näytetään oletuksena
     * @param kentta mikä kenttä näytetään
     * @return null jos peruutetaan, muuten täytetty tietue
     */
    public static Kirja kysyKirja(Stage modalityStage, Kirja oletus, int kentta) {
        return ModalController.<Kirja, KirjaDialogController>showModal(
                    KirjaDialogController.class.getResource("KirjaDialogView.fxml"),
                    "Kerho",
                    modalityStage, oletus, ctrl -> ctrl.setKentta(kentta));
    }
}