package src.konopolis.vue;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;

import java.util.Observer;

/**
 * View
 * Abstract Class
 * Implements Observer
 * It Observes the model
 * It delivers user actions to the controller
 */
public abstract class KonopolisView implements Observer {

    protected KonopolisModel model;
    protected KonopolisController controller;

    public KonopolisView(KonopolisModel model, KonopolisController controller) {
        this.model = model;
        this.controller = controller;
        // We add the view as model' observer
        model.addObserver(this);
    }

}
