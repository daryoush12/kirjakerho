/**
 * 
 */
package fxKerho;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;

/**
 * Kontrolleri tulostukselle.
 * @author psvaltus
 * @version 27.4.2019
 *
 */
public class TulostusController implements ModalControllerInterface<String> {

    @FXML private TextArea tulostusAlue;

    @FXML void handlePeruuta() {
        ModalController.closeStage(tulostusAlue);
    }

    @FXML void handleTulosta() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if ( job != null && job.showPrintDialog(null) ) {
            WebEngine webEngine = new WebEngine();
            webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
            webEngine.print(job);
            job.endJob();
        }
    }

    //<==============================================================>
    
    /**
     * @return alue johon tulostetaan.
     */
    public TextArea getTextArea() {
        return tulostusAlue;
    }
   
   
    /**
     * Näyttää tulostusalueessa tekstin.
     * @param tulostus tulostettava teksti
     * @return kontrolleri, jolta voidaan pyytää lisää tietoa
     */
    public static TulostusController tulosta(String tulostus) {
        TulostusController tulostusController = 
          ModalController.showModeless(TulostusController.class.getResource("TulostusView.fxml"),
                                       "Tulostus", tulostus);
        return tulostusController;
    }
    
    
    /**
     * Ikkunan palautus.
     */
    @Override
    public String getResult() {
        return null;
    } 
   
    
    /**
     * Ikkunan alustus.
     * @param oletus oletusteksti
     */
    @Override
    public void setDefault(String oletus) {
        tulostusAlue.setText(oletus);
    }
   
    
    /**
     * Mitä tehdään kun dialogi on näytetty.
     */
    @Override
    public void handleShown() {
        //
    }
}