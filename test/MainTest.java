package test;

import model.KonopolisModel;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KonopolisModel model=new KonopolisModel();
		model.createConnection();
		model.retrieveMovie(2);
		System.out.println(model.getMovies_al().get(0).getTitle());
	}

}
