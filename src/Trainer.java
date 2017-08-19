import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Trainer {
	
	private static Trainer t = null;
	private int trainingParameters = 4;
	private ComparisionParas trainingSetCol; 
	private ComparisionParas trainingSetShape;
	public  KDTree tree1;
	public  KDTree tree2;
	public  LeaveOutGraph graph;
	
	private Trainer(){
		trainingSetCol = new ComparisionParas(trainingParameters);
		trainingSetShape = new ComparisionParas(trainingParameters);
		tree1 = new KDTree();
		tree2 = new KDTree();
	}
	
	public static Trainer getInstance(){
		if(t == null)
			t = new Trainer();
		return t;
	}
	
	public ArrayList<String> startWithDirectory(String location){
		String path = location;
		File directory = new File(path);
		String sets[] = directory.list();
		ArrayList<String> list = new ArrayList<String>();
		if(sets != null){
			graph = new LeaveOutGraph(sets.length, 8);
			for(int i = 0 ; i < sets.length; i ++){
				Trainer.getInstance().trainAtLocation(path+"\\"+sets[i],sets[i]);
				if(!list.contains(sets[i])){
					list.add(sets[i]);
				}
			}

		}
		return list;
	}
	
	private void trainAtLocation(String loc_str, String setName){
		File directory = new File(loc_str);
		if(directory.isDirectory()){
			String images[] = directory.list();
			System.out.println("\n"+setName+":");
			for(int i = 0; i < images.length; i++){
				if(images[i].endsWith(".jpg")){
					Mat img = Imgcodecs.imread(loc_str+"\\"+images[i]);
					analyseAndStore(img, setName);
				}
			}
		}
	}
	
	public void trainingDone(){
		ComparisionParas.Node  nArr[] = trainingSetCol.returnCompletedSet();
		for(int i = 0; i < nArr.length; i++){
			if(nArr[i] == null)
				break;
			tree1.add(nArr[i]);
		}
		nArr = trainingSetShape.returnCompletedSet();
		for(int i = 0; i < nArr.length; i++){
			if(nArr[i] == null)
				break;
			tree2.add(nArr[i]);
		}
	}
	
	private void analyseAndStore(Mat img, String setName){
		Mat blur = new Mat();
		Mat blur_hsv = new Mat();
		Mat binary = new Mat();
		Mat blur_binary = new Mat();
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);
		Imgproc.GaussianBlur(img, blur, new Size(5,5), 25);
		Imgproc.cvtColor(blur, blur_hsv, Imgproc.COLOR_RGB2HSV);
		Core.inRange(blur_hsv, new Scalar(0,0,200), new Scalar(150,150,255), binary);
		binary = TrainingUtility.getInstance().extractImage(binary,binary);
		int shape[] = TrainingUtility.getInstance().shapeParas(binary);
		Imgproc.cvtColor(img, binary, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(blur, blur_binary, Imgproc.COLOR_RGB2GRAY);
		int dist[] = TrainingUtility.getInstance().meanValue(blur_hsv,binary, blur_binary);
		graph.add(shape, dist, setName);
		trainingSetShape.Add(shape, setName);
		trainingSetCol.Add(dist, setName);
	}
	
	public void reset(){
		trainingSetCol = null;
		trainingSetShape = null;
		tree1 = null;
		tree2 = null;
		trainingSetCol = new ComparisionParas(trainingParameters);
		trainingSetShape = new ComparisionParas(trainingParameters);
		tree1 = new KDTree();
		tree2 = new KDTree();
	}
	
}
