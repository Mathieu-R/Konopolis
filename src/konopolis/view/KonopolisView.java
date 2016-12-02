package src.konopolis.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.model.Movie;
import src.konopolis.model.Room;
import src.konopolis.model.SeatTakenException;
import src.konopolis.model.SeatUnknownException;
import src.konopolis.model.Show;
/**
 * @author nathan
 *
 */
public abstract class KonopolisView implements Observer {
	Scanner sc;
	
	protected KonopolisController control;
	protected KonopolisModel model;
	
	public KonopolisView(KonopolisModel model,KonopolisController control){
        this.control = control;
        this.model = model;
        update(null,null);
        sc = new Scanner(System.in);
        model.addObserver(this);
        init();
	}

    public void update(Object o,Object arg){
	    init();
    }

	 public abstract void init();
}

