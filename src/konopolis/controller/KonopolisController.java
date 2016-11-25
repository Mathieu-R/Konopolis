package src.konopolis.controller;

import src.konopolis.model.KonopolisModel;
import src.konopolis.vue.KonopolisView;

import java.util.HashMap;

/**
 * Controller
 * It applies the modifications to the model
 * It applies the modifications to the view
 */
public class KonopolisController {

    private KonopolisModel model;
    private KonopolisView view;

    /**
     * Add a model to the controller
     * @param model, this model
     */
    public KonopolisController(KonopolisModel model) {
        this.model = model;
    }

    public HashMap<Integer, String> getAllMoviesTitles() {
        return model.retrieveAllMoviesTitles();
    }

    public void getMovieInfo() {

    }

    /**
     * Add a view to the controller
     * @param view, the view
     */
    public void addView(KonopolisView view) {
        this.view = view;
    }


}
