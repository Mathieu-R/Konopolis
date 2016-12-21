package konopolis.test;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class JSplash{
	
	public void init(){
		JFrame test = new JFrame("TEST");
		ImageIcon loadImage = new ImageIcon("img/giphy.gif");
		System.out.println(loadImage.getDescription());
		JLabel imgContainer = new JLabel();
		JPanel lblContainer = new JPanel();
		imgContainer.setIcon(loadImage);
		
		
		
		test.setLayout(new BorderLayout());
		test.setPreferredSize(new Dimension(400,600));
		test.setMinimumSize(new Dimension(400,450));
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setLocationRelativeTo(null);
		test.setVisible(true);
		test.setResizable(false);


			lblContainer.setSize(400, 600);
	        lblContainer.setBorder(BorderFactory.createLineBorder(Color.black));
	        lblContainer.setLayout(new FlowLayout());
	        lblContainer.setVisible(true);
	        lblContainer.add(imgContainer,BorderLayout.NORTH);
		
		test.add(lblContainer,BorderLayout.NORTH);
		
	}
	
	
	
	
	public static void main(String[] args){
		JSplash sp = new JSplash();
		sp.init();
	}
	
}
