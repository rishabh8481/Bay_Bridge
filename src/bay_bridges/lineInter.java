package bay_bridges;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.geom.Line2D;

public class lineInter {
	public int number;
	public double x1,y1,x2,y2;
	public lineInter(int number,double x1,double y1,double x2,double y2)
	{
		this.number=number;
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}
	public static void main (String[] args) {
		//File file = new File(args[0]);
		try {
		       File file = new File(args[0]);
		        BufferedReader in = new BufferedReader(new FileReader(file));
				String line;
		    	while ((line = in.readLine()) != null) {
					line = line.replaceAll("\\s", "");
					String l[] = line.split("\\)");
					int x= l.length;
					for(int i=0;i<x;i++){
						int index = Integer.parseInt(l[i].substring(0,line.indexOf(":")));
						String[] Cor1 = l[i].substring(l[i].indexOf("([")+2,l[i].indexOf("]")).split("\\,");
						double Cor1X1 = Double.parseDouble(Cor1[0]);
						double Cor1Y1 = Double.parseDouble(Cor1[1]);
						Cor1 =l[i].substring(l[i].indexOf(",[")+2,l[i].length()-1).split("\\,");
						Cor1[1] = Cor1[1].substring(0,Cor1[1].length()-2);
						double Cor2X2 =Double.parseDouble(Cor1[0]);
						double Cor2Y2 =Double.parseDouble(Cor1[1]);
				lineInter temp =new lineInter(index,Cor1X1,Cor1Y1,Cor2X2,Cor2Y2);
				bridge.add(temp);
				bridgeMap.put(index+"", temp);
			}
		} 
		in.close();
		}catch (IOException e) {
			e.printStackTrace();
		}

		//Create 2 d array for checking intersections (Memoize)
		// 0---not memoized yet
		// 1 --they intersect
		// 2 -- they dont intersect
		checkIntersection = new int[bridge.size()+1][bridge.size()+1];
		for(int i =0;i<checkIntersection.length;i++)
		{
			
			for(int j=0;j<checkIntersection.length;j++)
			{
				if(i ==0 || j==0) checkIntersection[i][j] = 0;
				else if(i==j)     checkIntersection[i][j] = 2;
				//Check if (1,3 )intersects is calculated if it is then the result 
				// for (3,1) will also be the same
				else if(checkIntersection[j][i] !=0) checkIntersection[i][j] =  checkIntersection[j][i];
				else  checkIntersection[i][j]                                =  doIntersect(i,j);
		//		System.out.print("  "+checkIntersection[i][j]);
			}
			//System.out.println();
		}

		ArrayList<String> temp = new ArrayList<String>();
        longestBridges(0,temp);
		//Print out Solution 
		for(int m=0;m<maxSequence.size();m++) System.out.println(maxSequence.get(m));
		//End Time
	}

	public static void longestBridges(int curr,ArrayList<String> sequence)
	{
		if(curr >= bridge.size())
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
			if(! checkIntersectionBridge(bridge.get(curr).number,soFar))
			{
				soFar.add(bridge.get(curr).number + "");
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
			if(checkIntersection[bridgeNum][bridgeMap.get(union.get(i)).number]==1) return true;
		}
		return false;
	}


	//Given 2 co-ordinates (x1,y1)(x2,y2) and (X1,Y1)(X2,Y2) 
	//Retruns 1 if they intersect else returns 2
	public static int doIntersect(int start,int end)
	{
		lineInter begin  = bridgeMap.get(start+"");
		lineInter finish = bridgeMap.get(end+"");

        //Feed Values to the function
		Line2D line1    = new Line2D.Double(begin.x1,begin.y1, begin.x2, begin.y2);
		Line2D line2    = new Line2D.Double(finish.x1, finish.y1, finish.x2, finish.y2);
		boolean result  = line2.intersectsLine(line1);
		
		//Lines Intersect
		if(result == true) return 1;
		//Lines Do not intersect
		else return 2;
	}
	
	
	
	static int [][] checkIntersection;
	public static ArrayList<lineInter> bridge        = new ArrayList<lineInter>();
	public static HashMap<String,lineInter>bridgeMap = new HashMap<String, lineInter>();
	public static ArrayList<String>   maxSequence        = new ArrayList<String>();
	public static int maxPossible                        = 0;
	
}