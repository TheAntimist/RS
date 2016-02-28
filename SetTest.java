import java.util.*;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import static org.junit.Assert.*;

public class SetTest{

    Vector<String> itemset = null;
    Vector<String> candidates = null,  freqItems = null; //oldcandidates = null,
    Vector<String> trans = null;
    double minsup = 0.6, minconf = 0.5; //In percentage
    int fk = 1;

    @org.junit.Before
    public void setup(){
	In in = new In("test.txt");
	String[] alltrans = in.readAllLines();
	for(int i = 0; i < alltrans.length; i++)
	    alltrans[i] = alltrans[i].toLowerCase();

	trans = new Vector<String>(Arrays.asList(alltrans));
	for(int i = 0; i < alltrans.length; i++)
	    itemset = itemVector(itemset, alltrans[i].split("\\s"));
	
	//StdOut.println(trans);
	//StdOut.println();
	//StdOut.println(itemset + "\n");
    }

    // public Vector<String> itemSet(



    public Vector<String> itemVector(String[] arr){
	Vector<String> v = new Vector<String>();
	for(String s: arr) if(!v.contains(s)) v.add(s);
	Collections.sort(v);
	return v;
    }

    public Vector<String> itemVector(Vector<String> items, String[] arr){
	//Returns a vector with Transaction items split into each element.
	Vector<String> v = null;
	if(items != null){
	    v = new Vector<String>(items);
	    for(String s: arr) if(!v.contains(s)) v.add(s);
	}
	else{
	    v = itemVector(arr);
	}
	Collections.sort(v);
	return v;
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



    
    /*    @org.junit.Test
    public void test(){
	In in = new In("test.txt");
	Vector<String> setofstrings = new Vector<String>();
	int i = 0;
	//	in.readString();
	while(in.hasNextLine()){
	    String s = in.readLine();
	    //StdOut.println(i++ + ": " + s);
	    setofstrings.add(s);
	    
	}
	//Collections.sort(setofstrings);
	String[] res = setofstrings.get(0).split("\\s");
	// Arrays.sort(res);
	for(String s: setofstrings) StdOut.println(s);
	// for(String s: res) StdOut.println(s);
	Vector<String> sets = null;
	sets = itemVector(res);
	for(String s: sets) StdOut.println(s);
	//if(sets.containsAll(
	in.close();
	}*/
    

    public int supportCount(Vector<String> varCand){
	//Gets the support count of varCand which is single set.
	int count =  0;
	Vector<String> tempCand;
	//	tempCand = itemVector(varCands.split("\\s"));

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
	
    // public boolean subset(int k){
    // 	int[] count = new int[candidates.size()];
    // 	int mincount = (int) (trans.size() *  minsup);

    // 	//StdOut.println("\n" + candidates);
    // 	Vector<String> temp = new Vector<String>();
    // 	ArrayList<Integer> fc = new ArrayList<Integer>();
	
    // 	for(int i = 0; i < trans.size(); i++){
    // 	    Vector<String> tempTrans;
    // 	    tempTrans = itemVector(trans.get(i).split("\\s"));
    // 	    for(int j = 0; j < candidates.size(); j++){
    // 		Vector<String> tempCand;
    // 		tempCand = itemVector(candidates.get(j).split("\\s"));

    // 		if(tempTrans.containsAll(tempCand)){
    // 		    //StdOut.println(tempTrans + ": " + tempCand);
    // 		    count[j]++;
    // 		}
    // 	    }
    // 	}
    // 	//for(int i: count) StdOut.print(i + " ");

    // 	for(int i = 0; i < candidates.size(); i++){
    // 	    if(count[i] >= mincount){
    // 		temp.add(candidates.get(i));
    // 		fc.add(count[i]);
    // 	    }
    // 	}
	
    // 	if (temp.size() > 0){
    // 	    if(candidates != null) candidates.clear();
    // 	    candidates = temp;
    // 	    StdOut.println("\n" + k + ": " + candidates);
    // 	    if(freqCount != null) freqCount.clear();
    // 	    freqCount = fc;
    // 	    return true;
    // 	}
    // 	else return false;

    // }

    public void freqItemset(){

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
	    StdOut.println(consequent);
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

    //@org.junit.Test
    public void ap_gen(){
	for(String s: freqItems){
	    ap_genrules(itemVector(s.split("\\s")), itemVector(s.split("\\s")), 1);
	}
    }

    @org.junit.Test
    public void randTests(){
    	// for(int i = 0; i < trans.size(); i++){
    	//     Vector<String> tempTrans;
    	//     tempTrans = itemVector(trans.get(i).split("\\s"));
    	//     StdOut.println(tempTrans);
    	// }
    	// candidates = new Vector<String>(itemset);
    	// for(int i = 2; i <= 5; i++){
    	//     candidates = apriori_gen(candidates, i);
    	//     StdOut.println(i + ": " + candidates);
    	// }
	freqItemset();
        //Vector<String> temp = apriori_gen(itemVector(freqItems.get(0).split("\\s")), 2);
	ap_gen();
    	
    }
}
