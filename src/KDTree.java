import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class KDTree {
	ComparisionParas.Node root = null;
	int parameters = 4;
	static KDTree tree = null;
	
	private KDTree() {

	}
	
	public static KDTree returnInstance(){
		if(tree == null)
			tree = new KDTree();
		return tree;
	}
	
	public void add(ComparisionParas.Node n){
		root = InternalAdd(root, n, 0);
	}
	
	private ComparisionParas.Node InternalAdd(ComparisionParas.Node root, ComparisionParas.Node n, int depth){
		if(root == null){
			root = n;
			return root;
		}
		//System.out.println(depth%parameters);
		if(n.paras[depth%parameters] < root.paras[depth%parameters]){
			root.left = InternalAdd(root.left, n, depth+1);
			root.lCount++;
			root.left.prev = root;
		}else{
			root.right = InternalAdd(root.right, n, depth+1);
			root.right.prev = root;
			root.rCount++;
		}
		return root;	
	}
	
	public void nearestNeighoubrsFor(int paras[]){
		ComparisionParas.Node subSection = search(root, paras, 0);
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		map = addNeighoubrs(map, subSection);
		
		////////////////////////////
//		int k = 5;
//		TreeMap<Double, String> treemap = new TreeMap<Double, String>();
//		treemap = newAddNeighoubr(treemap, root, paras);
//		if(treemap.size() < k)
//			k = treemap.size();
//		for(Double key : treemap.keySet()){
//			if(map.containsKey(treemap.get(key))){
//				int data = map.get(treemap.get(key));
//				map.put(treemap.get(key), ++data);
//			}else{
//				map.put(treemap.get(key), 1);
//			}
//			k--;
//			if(k == 0)
//				break;
//		}
		////////////////////////////
		
		int count = 0;
		int max = 0;
		String set = "";
		for(String key : map.keySet()){
			count += map.get(key);
			if(map.get(key) > max){
				max = map.get(key);
				set = key;
			}
		}
		System.out.println("prediction is: "+set+" with Accurancy: "+(((max*1.0)/count)*100)+"%");
	}
	
	public TreeMap<Double, String> newAddNeighoubr(TreeMap<Double, String> map,ComparisionParas.Node n, int paras[]){
		if(n == null)
			return map;
		double dist = eucledianDistance(paras, n.paras);
		map.put(dist, n.set);
		map = newAddNeighoubr(map, n.left, paras);
		map = newAddNeighoubr(map, n.left, paras);
		return map;
	}
	
	public double eucledianDistance(int data1[],int data2[]){
		double sum = 0;
		for(int i = 0; i < data1.length; i++){
			sum += data1[i]*data1[i] - data2[i]*data2[i];
		}
		return Math.sqrt(sum);
	}
	
//	public HashMap<Double, String> sortNeighoubrsAndReturnOnly(int k, HashMap<Double, String> map){
//		if(map.size() < k)
//			return map;
//		HashMap<Double, String> newMap = new HashMap<Double, String>();
//		for(int i = 0 ; i < k; i++){
//			double min = Double.MAX_VALUE;
//			for(double key : map.keySet()){
//				if(key < min){
//					
//				}
//			}
//		}
//		
//	}
	
	public HashMap<String,Integer> addNeighoubrs(HashMap<String,Integer> map, ComparisionParas.Node n){
		if(n == null)
			return map;
		if(map.containsKey(n.set)){
			int data = map.get(n.set);
			map.put(n.set, ++data);
		}else{
			map.put(n.set, 1);
		}
		map = addNeighoubrs(map, n.left);
		map = addNeighoubrs(map, n.right);
		return map;
	}
	
	private ComparisionParas.Node search(ComparisionParas.Node root, int dataPara[], int depth){
		if(root == null)
			return null;
		if(dataPara[depth%parameters] < root.paras[depth%parameters]){
			if(root.left != null && root.lCount < 4)
				return root;
			else if(root.left != null)
				return search(root.left, dataPara, depth + 1);
			else 
				return root;
//			else if(root.left == null && root.rCount < 4)
//				return root;
//			else if(root.left != null)				
//				return search(root.left, dataPara, depth + 1);
//			return search(root.right, dataPara, depth + 1);
		}else{
			if(root.right != null && root.rCount < 4)
				return root;
			else if(root.right != null)
				return search(root.right, dataPara, depth + 1);
			else 
				return root;
//			else if(root.right == null && root.lCount < 4)
//				return root;
//			else if(root.right != null)				
//				return search(root.right, dataPara, depth + 1);
//			return search(root.left, dataPara, depth + 1);
		}
	}
	
}