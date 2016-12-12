package konopolis.view;

import konopolis.controller.KonopolisController;
import konopolis.model.KonopolisModel;

import java.util.Observer;
/**
 * @author nathan
 */
public abstract class KonopolisView implements Observer {
	
	protected KonopolisController control;
	protected KonopolisModel model;
	
	public KonopolisView(KonopolisModel model,KonopolisController control){
        this.control = control;
        this.model = model;
        model.addObserver(this);
	}

	 public abstract void init();
}

