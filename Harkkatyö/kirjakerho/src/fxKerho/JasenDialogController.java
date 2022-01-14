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
import kerho.Jasen;

/**
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class JasenDialogController implements ModalControllerInterface<Jasen>,Initializable  {
    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelJasen;
    @FXML private GridPane gridJasen;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    @FXML private void handleOK() {
        if ( jasenI != null && jasenI.getNimi().trim().equals("") ) {
            naytaVirhe("Ei ole annettu nimeä!");
            return;
        }
        if ( jasenI != null && jasenI.getPostinumero().trim().matches("[0-9]+") == false) {
            naytaVirhe("Vain numeroita postinumeroon!");
            return;
        }
        if ( jasenI != null && jasenI.getPuhelin().trim().matches("[0-9]+") == false) {
            naytaVirhe("Vain numeroita puhelinnumeroon!");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    
    @FXML private void handlePeruuta() {
        jasenI = null;
        ModalController.closeStage(labelVirhe);
    }

    //<==============================================================>
    
    private Jasen jasenI;
    private static Jasen apu = new Jasen();
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
     * Luodaan jäsentietojen kentät.
     * @param gridJasen mihin tiedot lisätään
     * @return luodut kentät
     */
    public static TextField[] luoKentat(GridPane gridJasen) {
        gridJasen.getChildren().clear();
        TextField[] edits = new TextField[apu.getKentat()];
        for (int i=0, k=apu.ekaKentta(); k<apu.getKentat(); k++, i++) {
            Label label = new Label(apu.getKysymys(k));
            gridJasen.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridJasen.add(edit, 1, i);
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
        edits = luoKentat(gridJasen);
        for (TextField edit : edits)
            if (edit != null)
                  edit.setOnKeyReleased( e -> kasitteleMuutosJaseneen((TextField)(e.getSource())));
        panelJasen.setFitToHeight(true);
    }
    
    
    /**
     * Käsitellään jäsenen muutos.
     * @param edit muutettu kenttä
     */
    protected void kasitteleMuutosJaseneen(TextField edit) {
        if (jasenI == null) return;
        int k = getFieldId(edit,apu.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = jasenI.aseta(k,s); 
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
     * Ikkunan jäsenen alustus.
     * @param jasen ikkunan jäsen
     */
    @Override
    public void setDefault(Jasen jasen) {
        jasenI = jasen;
        naytaJasen(edits, jasenI);
    }

    
    /**
     * Palautetaan ikkunan jäsen.
     * @return muutettu jäsen
     */
    @Override
    public Jasen getResult() {
        return jasenI;
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
     * Näytetään jäsenen tiedot kenttiin.
     * @param edits taulukko näytettävistä kentistä
     * @param jasen näytettävä jäsen
     */
    public static void naytaJasen(TextField[] edits, Jasen jasen) {
        if (jasen == null) return;
        for (int k=jasen.ekaKentta(); k<jasen.getKentat(); k++) {
            edits[k].setText(jasen.anna(k));
        }
    }
    
    
    /**
     * Luodaan jäsenen kysymisdialogi.
     * @param modalityStage mille ollaan modaalisia
     * @param oletus mitä näytetään oletuksena
     * @param kentta mikä kenttä näytetään
     * @return null jos peruutetaan, muuten täytetty tietue
     */
    public static Jasen kysyJasen(Stage modalityStage, Jasen oletus, int kentta) {
        return ModalController.<Jasen, JasenDialogController>showModal(
                    JasenDialogController.class.getResource("JasenDialogView.fxml"),
                    "Kerho",
                    modalityStage, oletus, ctrl -> ctrl.setKentta(kentta));
    }
}