package bay;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
/*
 * check line intersection by writing an intersection algorithm.
 */
public class Main1 {
	static int [][] checkIntersection;
	public static HashMap<String, Main1> Map = new HashMap<String, Main1>();
	public static ArrayList<Main1> bay = new ArrayList<Main1>();
	public static ArrayList<String> maxSequence = new ArrayList<String>();
	public static int maxPossible = 0;
	public int num;
	public double x1,y1,x2,y2;
	public Main1(int num,double x1,double y1,double x2,double y2)
	{
		this.num=num;
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}
	
	@SuppressWarnings("resource")
	public static void main(String args[]){
		int i,j;
		try {
			BufferedReader in = null;
			in = new BufferedReader(new FileReader("C:/Users/A/Desktop/CodeEval/bay bridges/bsy.txt"));
			String line;
        	while ((line = in.readLine()) != null) {
				line = line.replaceAll("\\s", "");
				String l[] = line.split("\\)");
				int x= l.length;
				for(i=0;i<x;i++){
					int index = Integer.parseInt(l[i].substring(0,line.indexOf(":")));
					String[] Cor1 = l[i].substring(l[i].indexOf("([")+2,l[i].indexOf("]")).split("\\,");
					double Cor1X1 = Double.parseDouble(Cor1[0]);
					double Cor1Y1 = Double.parseDouble(Cor1[1]);
					Cor1 =l[i].substring(l[i].indexOf(",[")+2,l[i].length()-1).split("\\,");
					Cor1[1] = Cor1[1].substring(0,Cor1[1].length()-2);
					double Cor2X2 =Double.parseDouble(Cor1[0]);
					double Cor2Y2 =Double.parseDouble(Cor1[1]);
				Main1 temp =new Main1(index,Cor1X1,Cor1Y1,Cor2X2,Cor2Y2);
				bay.add(temp);
				Map.put(index+"", temp); //hashMap object Map stores all the information of the line and co ordinates 
				}
			}
        	checkIntersection = new int[Map.size()+1][Map.size()+1];
			//Create 2 d array for checking intersections (Memoize)
			// 0---not memoized yet
			// 1 --they intersect
			// 2 -- they dont intersect
			for(i =0;i<checkIntersection.length;i++)
			{
				for(j=0;j<checkIntersection.length;j++)
				{
					if(i ==0 || j==0) checkIntersection[i][j] = 0;
					else if(i==j) checkIntersection[i][j] = 2;
					else if(checkIntersection[j][i] !=0 ) checkIntersection[i][j] =  checkIntersection[j][i];
					else  checkIntersection[i][j] = doIntersect(i,j);
				}
			}
			ArrayList<String> temp1 = new ArrayList<String>();
	        longestBridges(0,temp1);
			for(int m=0;m<maxSequence.size();m++) System.out.println("op"+maxSequence.get(m));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void longestBridges(int curr,ArrayList<String> sequence)
	{
		if(curr >= bay.size())
		{
			if(sequence.size()> maxPossible)
			{
				maxPossible= sequence.size();
				maxSequence.clear();
				maxSequence.addAll(sequence);
			}
		}
		else{
			ArrayList<String>soFar = new ArrayList<String>(sequence);
			//Without case
			longestBridges(curr+1,soFar);
			//With case
			//Check if whatever we add intersects with any point in the 
			//Sequence if no then add it and continue
			if(! checkIntersectionBridge(bay.get(curr).num,soFar))
			{
				soFar.add(bay.get(curr).num + "");
				longestBridges(curr+1,soFar);
			}
		}
	}
	//Checks if a given bridge intersects with those of the list
	//Returns true if the bridges intersect else false
	public static boolean checkIntersectionBridge(int bridgeNum,ArrayList<String>union)
	{
		for(int i = 0 ;i<union.size();i++)
		{
			if(checkIntersection[bridgeNum][Map.get(union.get(i)).num]==1) return true;
		}
		return false;
	}
	
	//function for checking the intersection in the line segment 
	private static int doIntersect(int i, int j) {
		Main1 begin  = Map.get(i+"");
		Main1 finish = Map.get(j+"");
		//System.out.println("begin : " +begin.num+"finish : "+finish.num);
        //Feed Values to the function
		Line2D line1    = new Line2D.Double(begin.x1,begin.y1, begin.x2, begin.y2);
		Line2D line2    = new Line2D.Double(finish.x1, finish.y1, finish.x2, finish.y2);
		boolean result  = checkDoIntersect(line1,line2);
		//System.out.println(result);
		//Lines Intersect
		if(result == true) return 1;
		//Lines Do not intersect
		else return 2;
	}
	
	//function for checking whether the ----given two---- line segments intersects or not
	private static boolean checkDoIntersect(Line2D line1, Line2D line2) {
		int o1 = orientation(line1 , line2.getX1(), line2.getY1());
		int o2 = orientation(line1 , line2.getX2(), line2.getY2());
		int o3 = orientation(line2 , line1.getX1(), line1.getY1());
		int o4 = orientation(line2 , line1.getX2(), line1.getY2());
		// General case
		if (o1 != o2 && o3 != o4)
			return true;
		if (o1 == 0 && onSegment(line1.getX1(),line1.getY1(),line1.getX2(),line1.getY2(),line2.getX1(),line2.getY1())) return true;
		if (o2 == 0 && onSegment(line1.getX1(),line1.getY1(),line2.getX2(),line2.getY2(),line2.getX1(),line2.getY1())) return true;
		if (o3 == 0 && onSegment(line1.getX2(),line1.getY2(),line1.getX1(),line1.getY1(),line2.getX2(),line2.getY2())) return true;
		if (o4 == 0 && onSegment(line1.getX2(),line1.getY2(),line2.getX1(),line2.getY1(),line2.getX2(),line2.getY2())) return true;
		return false; 
	}
	// for checking whether the point lies on the line segment
	private static boolean onSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
		if (x2 <= Math.max(x1, x3) && x2 >= Math.min(x1, x3) &&	y2 <= Math.max(y1, y3) && y2 >= Math.min(y1, y3))
		return true;
		return false;
	}
	// for checking directions
	private static int orientation(Line2D line2, double x22, double y22) {
		double val = ((line2.getY2() - line2.getY1()) * (x22 - line2.getX2())) - ((line2.getX2() - line2.getX1()) * (y22 - line2.getY2()));
		if (val == 0) return 0; 
		return (val > 0)? 1: 2; 
	}
}
