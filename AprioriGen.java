/*

Author: Ankush Mishra
Date: 27.02.16
Requirements: algs4.jar, provided by Princeton University to be placed in the ClassPath
Download From here: http://algs4.cs.princeton.edu/code/algs4.jar

Or Use the Provided StdOut, StdIn and In classes, along with this file and place it in the same directory as this.

Compile: javac AprioriGen.java
Execution:
 - java AprioriGen TransactionFile.txt
 - java AprioriGen TransactionFile.txt MinSupport MinConfidence


Test Files:
 Two, test files have been provided for testing out transactions.


 MinSupport and MinConfidence in the range of 0-1 


- Assumptions, no repeat transactions(An actual possibility that it could happen)
- No Transactions have the same item more than once

 */


import java.util.*;
// import edu.princeton.cs.algs4.StdIn;
// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;

public class AprioriGen{
    private Vector<String> trans = null; //each String contains a line of Transaction with items seperated by whitespace
    private Vector<String> itemset =  null; //Contains all the items in the transaction used, or all the items available for sale.
    private Vector<String> candidates = null, freqItems = null;
    private double minsup, minconf; //In percentage
    private int fk = 1;


    public AprioriGen(String file, double mins , double minc){
	minsup = mins;
	minconf = minc;
	StdOut.println("Starting AprioriGen on "  + file + " with MinSupport " + minsup + " with MinConfidence " + minconf);
	getTrans(file);
	// StdOut.println("Transactions: ");
	// for(String s: trans) StdOut.println(s);
	freqItemsets();
	StdOut.println("\nFrequent Item Sets: ");
	for(String s: freqItems) StdOut.println("[" + s + "]");
	StdOut.println("\nAssociation Rules Generated: ");
	ap_gen();
    }

    public AprioriGen(String file){
	minsup = 0.6;
	minconf = 0.5;
	StdOut.println("Starting AprioriGen on " + file + " with MinSupport " + minsup + " with MinConfidence " + minconf);
	getTrans(file);
	// StdOut.println("Transactions: ");
	// for(String s: trans) StdOut.println(s);
	freqItemsets();
	StdOut.println("\nFrequent Item Sets: ");
	for(String s: freqItems) StdOut.println("[" + s + "]");
	StdOut.println("\nAssociation Rules Generated: ");
	ap_gen();
    }

    public Vector<String> itemVector(String[] arr){
	//Returns a vector with Transaction items split into each element.
	Vector<String> v = new Vector<String>();
	for(String s: arr) if(!v.contains(s)) v.add(s);
	Collections.sort(v);
	return v;
    }

    public Vector<String> itemVector(Vector<String> items, String[] arr){
	//Returns a previously existing vector with Transaction items split into each element.
	Vector<String> v = null;
	if(items != null){
	    v = new Vector<String>(items);
	    for(String s: arr) if(!v.contains(s)) v.add(s);
	    Collections.sort(v);
	}
	else v = itemVector(arr);

	return v;
    }

    public void getTrans(String file){
	//Takes in the Transactions stored in the file and places it in the memory
	//And makes 
	In in = new In(file);
	String[] alltrans = in.readAllLines();
	for(int i = 0; i < alltrans.length; i++)
	    alltrans[i] = alltrans[i].toLowerCase();

	trans = itemVector(alltrans);
	

	for(int i = 0; i < alltrans.length; i++)
	    itemset = itemVector(itemset, alltrans[i].split("\\s"));
    }

    private String convtoString(String[] arr){
	String s = "";
	for(String temp: arr)
	    s = s + temp + " ";
	 return s;
     }

    public Vector<String> apriori_gen(Vector<String> varCands, int k){
	//Generates Candidate sets, for size of int k
	//Where k is the current items in one set

	Vector<String> temp = new Vector<String>();

	if(k == 2){
	    for(int i = 0; i < varCands.size(); i++){
		for(int j = i+1; j < varCands.size(); j++){
		    String s = varCands.get(i) + " " + varCands.get(j);
			temp.add(s);
		}
	    }
	}
	else{

	    
	    for(int i = 0; i < varCands.size(); i++){
	    	for(int j = i + 1; j < varCands.size(); j++){
		    String[] si = varCands.get(i).split("\\s");
		    String[] sj = varCands.get(j).split("\\s");
		    String[] subi = Arrays.copyOf(si, si.length - 1);
		    String[] subj = Arrays.copyOf(sj, sj.length - 1);
		    
		    if(Arrays.equals(subi, subj)){
			String fin = convtoString(subi) + si[si.length-1] + " " + sj[sj.length-1];
			temp.add(fin);
		    }
		    
		}
	    }
	}
	
	varCands.clear();
	return temp;


    }

    public int supportCount(Vector<String> varCand){
	//Gets the support count of varCand which is single set.
	int count =  0;

	for(int i = 0; i < trans.size(); i++){
	    Vector<String> tempTrans;
	    tempTrans = itemVector(trans.get(i).split("\\s"));

	    if(tempTrans.containsAll(varCand)){
		//StdOut.println(tempTrans + ": " + tempCand);
		count++;
	    }
	}
	return count;
	
    }

    public boolean subset(int k){
	int[] count =  new int[candidates.size()];
    	int mincount = (int) (trans.size() *  minsup);

    	Vector<String> temp = new Vector<String>();
    	//temp = supportCount(candidates, fc, mincount);
	
	for(int j = 0; j < candidates.size(); j++){
	    Vector<String> tempCand;
	    tempCand = itemVector(candidates.get(j).split("\\s"));
	    count[j] = supportCount(tempCand);
	}

	//for(int i: count) StdOut.print(i + " ");

	for(int i = 0; i < candidates.size(); i++){
	    if(count[i] >= mincount){
		temp.add(candidates.get(i));
	    }
	}

	
    	if(candidates != null) candidates.clear();
    	candidates = temp;
    	return (temp.size() > 0);
	
    }

    public void freqItemsets(){

	int k = 2;
	Vector<String> oldcandidates = null;
	if(itemset.size() > 0){
	    candidates = new Vector<String>(itemset);
	    while(subset(k)){
		//StdOut.println(k);
		if(oldcandidates != null) oldcandidates.clear();
		oldcandidates = new Vector<String>(candidates);
		candidates = apriori_gen(candidates, k++);
	    }
  
	    freqItems = new Vector<String>(oldcandidates);
	    oldcandidates.clear();
	    //StdOut.println(freqItems + "\n" + candidates);
	    candidates.clear();
	    fk = k - 2;
	    //StdOut.println(fk);
	}
    }

    public void ap_genrules(Vector<String> freqI, Vector<String> consequent, int m){
	while(fk > m + 1){
	    //StdOut.println(consequent);
	    for(int i = 0; i < consequent.size(); i++) {  
		String s = consequent.get(i);
		Vector<String> tempfk = new Vector<String>(freqI), tempRem = new Vector<String>(freqI);
		tempRem.removeAll(Arrays.asList(s.split("\\s")));		
		double conf = (double) supportCount(tempfk) / supportCount(tempRem);
		//StdOut.println(tempfk + ": " + tempRem + "->" + s + " " + conf + ": " + supportCount(tempfk) + ", " + supportCount(tempRem) );
		if(conf >= minconf)
		    StdOut.println(tempRem + "->" + Arrays.asList(s.split("\\s")));
		else
		    consequent.remove(s);
	    }
	    consequent = apriori_gen(consequent, ++m);
	    //ap_genrules(freqI, consequent, m);
	}
    }

    public void ap_gen(){
	for(String s: freqItems){
	    ap_genrules(itemVector(s.split("\\s")), itemVector(s.split("\\s")), 1);
	}
    }

    public static void main(String[] args){
	AprioriGen a;
	if(args.length == 3)
	    a = new AprioriGen(args[0], Double.parseDouble(args[1]), Double.parseDouble(args[2]) );
	else
	    a = new AprioriGen(args[0]);
    }
}
