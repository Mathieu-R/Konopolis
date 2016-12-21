package src.konopolis.view;

import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JWindow implements Runnable {
	  
	private int duration;
	private ImageIcon img; // Image
	private int width; // Width
	private int height; // Height

	public SplashScreen(int duration, ImageIcon img, int width, int height) {
		this.duration = duration;
		this.img = img;
		this.width = width;
		this.height = height;
		new Thread(this).start();
	}

	// A simple little method to show a title screen in the center
	// of the screen for the amount of time given in the constructor
	public void showSplash() {
		JPanel content = (JPanel) getContentPane();
		content.setBackground(Color.white);

		// Set the window's bounds, centering the window
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - this.width) / 2;
		int y = (screen.height - this.height) / 2;
		setBounds(x, y, this.width, this.height);

		// Build the splash screen
		JLabel label = new JLabel(img);

		content.add(label, BorderLayout.CENTER);

		// Display it
		setVisible(true);

		// Wait a little while, maybe while loading resources
		try {
			Thread.sleep(duration);
		} catch (Exception e) {

		}
		setVisible(false);
	}

	public void showSplashAndExit() {
		showSplash();
		System.exit(0);
	}

	@Override
	public void run() {
		showSplash();
	}
}
