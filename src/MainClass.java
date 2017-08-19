import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JApplet;

import org.opencv.core.Core;

public class MainClass extends JApplet implements Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1808048169739900625L;
	Tester tester = new Tester();
	
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		MainClass obj = new MainClass();
		obj.run();
	}
	
	public void init(){
		this.setSize(200, 200);
	}
	
	public void paint(Graphics g){
		g.setColor(Color.RED);
		g.fillRect(10, 10, 100, 100);
	}
	
	public void run(){
		UserInterface.getInstance().addObserver(this);
		UserInterface.getInstance().show();
	}

	private void startTraining(String location){

		ArrayList<String> list = new ArrayList<String>();
		if((new File(location).exists())){
			list = Trainer.getInstance().startWithDirectory(location);
			Trainer.getInstance().trainingDone();
		}

		if(Trainer.getInstance().tree1.isEmpty() && Trainer.getInstance().tree2.isEmpty()){
			UserInterface.getInstance().alertWithMsg("Invalid Location\n> The source Folder Should have sub folders\n> Each sub-folder name is a Lable for Trainig Set\n> Sub-Folders should have respective images for training");
		}else{
			UserInterface.getInstance().trainingSuccessfull(list);
		}
	}
	
	
	
	@Override
	public void update(Observable o, Object arg) {
		String argument[] = (String[]) arg;
		switch (argument[0]) {
		case "Start training":
			startTraining(argument[1]);
			break;
		case "Predict":
			tester.predict(argument[1],argument[2]);
			break;
		case "Find":
			tester.findImagesForKey(argument[1]);
			System.out.println("Finding "+argument[1]+"......");
			break;
		case "Reset":
			Trainer.getInstance().reset();
			break;
		default:
			
			break;
		}
	}
	
}
