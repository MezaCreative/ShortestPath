/**
 * ShortestPath.java 
 * 
 * 		A simple short path algorithm class.
 * 		This program computes the shortest path between two cities
 * 
 * 		@author Esteban Meza
 * 		@version 20190401
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class ShortestPath {

	/*
	 * main method prompts the user for the name of the file. Extracts cities from the file.
	 * Then finds the shortest path to different cities for a desired city.
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the file name: ");
		String input = in.nextLine();
		while (input.equals("")){
			System.out.println("Please enter a valid name");
			input = in.nextLine();
		}
		Map<String, List<Path> > map1 = readPathsFromFile(input);
		displayAdjacencyList(map1);
		System.out.println();
		System.out.print("Enter a start city (empty line to quit): ");
		String start = in.nextLine();
		
		while (!start.isEmpty()) {
		System.out.println();
		Map<String, Double> mDist = findDistances(start, map1);
		displayShortest(start, mDist);	
		System.out.println();
		System.out.print("Enter a start city (empty line to quit): ");
		start = in.nextLine();
		}
		
		System.out.println("Goodbye");

	}

	/*
	 * readPathsFromFile
	 * 		reads paths from a file and exports it to a map
	 * @param fname - name of file to read
	 * @return Map<String, List<Path> - Map with city names as keeys and paths as values
	 * NOTE: I changed adj_list to cities. 
	 */
	public static Map<String, List<Path> > readPathsFromFile (String fname) {
		Map<String, List<Path> > cities = new  TreeMap<String, List<Path> >();
		Scanner inFile = prompt(fname);

		//continues to check
		while (inFile.hasNext()) {
			String input = inFile.nextLine();
			String [] tokens = input.split(",");
			List<Path> tList;
			List<Path> tList2;
			//boolean exist = false;
			if ((cities.get(tokens[0]) == null) )  {
				tList = new ArrayList<Path>();
				cities.put(tokens[0], tList);

			}
			else {
				tList = cities.get(tokens[0]);

			}

			Path path1 = new Path(tokens[1], Double.parseDouble(tokens[2]));
			tList.add(path1);

			if (cities.get(tokens[1]) == null) {
				tList2 = new ArrayList<Path>();
				cities.put(tokens[1],tList2);	
			}
			else {
				tList2 = cities.get(tokens[1]);
			}

			Path path2 = new Path(tokens[0], Double.parseDouble(tokens[2]));
			tList2.add(path2);


		}
//		System.out.println("Done");
		return cities;
	}
	/**
	 * displayAdjancecyList
	 * 		Neatly displays the adjacent cities. It took me a while but i figured out format
	 * @param map - the map to read from and neatly print its values
	 */
	public static void displayAdjacencyList(Map< String,List<Path> > map){
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();

		System.out.format("%-15s", "Start City"); 
		System.out.format("%-15s", "Paths");
		System.out.println();
		System.out.print(dashedLine());
		
		while(iter.hasNext()) {
			String key = iter.next();
			
			System.out.format("%-15s", key + ":");
			List<Path> list = map.get(key);
			for (int i=0; i<list.size(); i++) {

				if (i == list.size() -1) {
					System.out.format("%-9s" , "(" + list.get(i).getEndpoint() + ": " + list.get(i).getCost() + ")");
				}
				else {


					System.out.format("%-9s" ,"(" + list.get(i).getEndpoint() + ": " + list.get(i).getCost() + "),");
				}
			}
			System.out.println();
		}


	}
	/**
	 * findDistances
	 * 		Computes the alogirthm to find the shortest distance.
	 * 		 Formula was used from the project instructions
	 * @param start - Starting city
	 * @param cities - List of cities that are adjacent
	 * @return Map<String, Double> Return a neat Map of the distances for cities from the start
	 */
	public static Map<String, Double> findDistances (String start, Map<String, List<Path>> cities)  {

		Map<String,Double> shortestDistances = new TreeMap<String,Double>();
		PriorityQueue<Path> pathQ = new PriorityQueue<Path>();

		Path begin = new Path(start, 0.0);
		pathQ.add(begin);

		while (!pathQ.isEmpty()) {
			Path current = pathQ.remove();
			if (!shortestDistances.containsKey(current.getEndpoint())) {
				double d = current.getCost();
				String dest = current.getEndpoint();
				shortestDistances.put(dest, d);


				//Priority Queue update
				List<Path> tList = cities.get(dest);
				for (int i = 0; i< tList.size(); i++) {
					double newCost = tList.get(i).getCost() + d;
					Path newPath = new Path(tList.get(i).getEndpoint(), newCost);
					pathQ.add(newPath);
				}
			}

		}
		return shortestDistances;

	}
	/**
	 * displayShortest
	 * 		Neatly displays the cities with the smallest path distance from the starting location
	 * @param start - The starting location
	 * @param shortest - A map of the cities with the shortest distances to neatly display
	 * 
	 */
	public static void displayShortest(String start, Map<String, Double> shortest){
		Set<String> keySet = shortest.keySet();
		Iterator<String> iter = keySet.iterator();
		
		System.out.format("Distances from %5s to each city: ", start);
		System.out.println();
		System.out.format("%-15s", "Dest. City");
		System.out.format("%-15s", "Distance");
		System.out.println();
		System.out.print(dashedLine());
		
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.format("%-15s", key);
			System.out.format("%-15s" , shortest.get(key));
			System.out.println();;
		}
		
		
		
	}

	/*
	 * dashedLine
	 * 		makes dashed lines
	 */
	public static String dashedLine()
	{
	    StringBuilder sb = new StringBuilder(20);
	    for(int n = 0; n < 20; ++n)
	        sb.append('-');
	    sb.append(System.lineSeparator());
	    return sb.toString();
	}


	/*
	 * Prompts the use for the input file
	 */
	@SuppressWarnings("null")
	public static Scanner prompt (String name) {
		Scanner newFile;
		try {
			File inputFile = new File (name);
			newFile = new Scanner(inputFile);

		}
		catch (IOException exception){
			newFile = null;
			newFile.close();
			throw new IllegalArgumentException("Could not find file...Aborting");
		}
		return newFile;
	}
}
