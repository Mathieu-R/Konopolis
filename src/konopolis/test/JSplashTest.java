package konopolis.test;

import javax.swing.*;

import konopolis.view.SplashScreen;

public class JSplashTest {
	
	
	public static void main(String[] args) {
	    // Throw a nice little title page up on the screen first
	    SplashScreen splash = new SplashScreen(5000,new ImageIcon("img/giphy.gif"),400,400);
	    // Normally, we'd call splash.showSplash() and get on with the program.
	    // But, since this is only a test...
	    splash.showSplashAndExit();
	  }
}
