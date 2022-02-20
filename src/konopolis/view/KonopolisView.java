package src.konopolis.view;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;

import java.util.Observer;

/**
 * @author nathan
 */
public abstract class KonopolisView implements Observer, Runnable {

  protected KonopolisController control;
  protected KonopolisModel model;

  public KonopolisView(KonopolisModel model, KonopolisController control) {
    this.control = control;
    this.model = model;
    model.addObserver(this);
  }

  public abstract void init();
}
