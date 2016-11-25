package src.konopolis.vue;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class KonopolisViewConsole extends KonopolisView implements Observer {

    public KonopolisViewConsole(KonopolisModel model, KonopolisController controller) {
        super(model, controller);
        update(null, null);
    }

    public void update(Observable obs, Object obj) {
        // Do something
        init();
    }

    public void init() {
        System.out.println("Liste des films:");
        for (Map.Entry<Integer, String> movieEntry: model.retrieveAllMoviesTitles().entrySet()) {
            System.out.println(movieEntry.getKey() + ") " + movieEntry.getValue());
        }

    }

}
