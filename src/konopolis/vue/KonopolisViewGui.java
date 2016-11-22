package src.konopolis.vue;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;

import java.util.Observable;
import java.util.Observer;

public class KonopolisViewGui extends KonopolisView implements Observer {

    public KonopolisViewGui(KonopolisModel model, KonopolisController controller) {
        super(model, controller);
    }

    public void update(Observable obs, Object obj) {
        // Do something
    }

}
