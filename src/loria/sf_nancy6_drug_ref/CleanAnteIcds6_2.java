package loria.sf_nancy6_drug_ref;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Two step cleaning:
 *  1 - bonferroni correction with alpha = 0.05
 *  so we keep only p-value below alpha bar = alpha / k
 *  with k number of phenotype observed during the intervals with drug d
 *  
 *  2 - we remove annotations of parent with lower p-value
 *    remove form a set of enrichement set, annotation that are higher in the hierarchy BUT have a higher p-value
 *    nb:  key of hashmaps are class_rxcui_cui, and values are pvalues
 *  
 * @author coulet
 *
 */
public class CleanAnteIcds6_2 {
	
	static String EHR_BASE 			  = "collaborators"; //collaborators is for stride5";
	static String DOSE_CHG_ANNOT 	  = "user_x.dose_change_64ante_icd";
	static String DRUG_CHG_ANNOT_DB	  = "user_x.drug_change_64ante_icd_db";
	static String DRUG_CHG_ANNOT_MS	  = "user_x.drug_change_64ante_icd_ms";
	
	static String DOSE_DOWN_PHENO 	 = "user_x.dose_down_65ante_icd_p450_all";
	static String DOSE_UP_PHENO 	 = "user_x.dose_up_65ante_icd_p450_all";
	static String DRUG_CHG_PHENO_DB	 = "user_x.drug_change_65ante_icd_db_p450_all";
	static String DRUG_CHG_PHENO_MS	 = "user_x.drug_change_65ante_icd_ms_p450_all";

	static String DOSE_DOWN_PHENO_REDUCED0 	 = DOSE_DOWN_PHENO+"_rr_ic";
	static String DOSE_UP_PHENO_REDUCED0 	 = DOSE_UP_PHENO+"_rr_ic";
	static String DRUG_CHG_PHENO_DB_REDUCED0 = DRUG_CHG_PHENO_DB+"_rr_ic";
	static String DRUG_CHG_PHENO_MS_REDUCED0 = DRUG_CHG_PHENO_MS+"_rr_ic";
	
	static String DOSE_DOWN_PHENO_REDUCED1 	 = DOSE_DOWN_PHENO+"_p1";
	static String DOSE_UP_PHENO_REDUCED1 	 = DOSE_UP_PHENO+"_p1";
	static String DRUG_CHG_PHENO_DB_REDUCED1 = DRUG_CHG_PHENO_DB+"_p1";
	static String DRUG_CHG_PHENO_MS_REDUCED1 = DRUG_CHG_PHENO_MS+"_p1";
	
	static String DOSE_DOWN_PHENO_REDUCED2 	 = DOSE_DOWN_PHENO+"_p2";
	static String DOSE_UP_PHENO_REDUCED2 	 = DOSE_UP_PHENO+"_p2";
	static String DRUG_CHG_PHENO_DB_REDUCED2 = DRUG_CHG_PHENO_DB+"_p2";
	static String DRUG_CHG_PHENO_MS_REDUCED2 = DRUG_CHG_PHENO_MS+"_p2";
	
	static String DRUG_TABLE 	 	 = "user_x.p450" ; //"user_x.drug_and_drug_class";
	
	static String UP 				 = "up";
	static String DOWN 				 = "down";
	
	static String ONTO 	= "ICD9CM";
	
	static HashMap<String, ArrayList<String>> ptrs = new HashMap<String, ArrayList<String>>();
	static HashMap<String, ArrayList<String>> inMemorydescendants = new HashMap<String, ArrayList<String>>();
	static HashMap<String, String> localIdToCui = new HashMap<String, String>();
	static HashMap<String, String> cuiToLocalId = new HashMap<String, String>();


	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
		
		boolean singleDrug = Boolean.parseBoolean(args[0]); // true if only single drugs, false if only classes of drug
		String upOrDown = args[1]; // up or down

		Date start       = new Date();
		Stride6Api5Ante ehr = new Stride6Api5Ante(EHR_BASE, 
				DOSE_CHG_ANNOT, DRUG_CHG_ANNOT_DB, DRUG_CHG_ANNOT_MS, 
	    		DOSE_DOWN_PHENO, DOSE_UP_PHENO, DRUG_CHG_PHENO_DB, DRUG_CHG_PHENO_MS, 
	    		DOSE_DOWN_PHENO_REDUCED0, DOSE_UP_PHENO_REDUCED0, DRUG_CHG_PHENO_DB_REDUCED0, DRUG_CHG_PHENO_MS_REDUCED0,
	    		DOSE_DOWN_PHENO_REDUCED1, DOSE_UP_PHENO_REDUCED1, DRUG_CHG_PHENO_DB_REDUCED1, DRUG_CHG_PHENO_MS_REDUCED1,
	    		DOSE_DOWN_PHENO_REDUCED2, DOSE_UP_PHENO_REDUCED2, DRUG_CHG_PHENO_DB_REDUCED2, DRUG_CHG_PHENO_MS_REDUCED2);

		ArrayList<String> profileTables = new ArrayList<String>();
		if(upOrDown.equals(DOWN)){
			profileTables.add(DOSE_DOWN_PHENO);
		}else if(upOrDown.equals(UP)){
			profileTables.add(DOSE_UP_PHENO);
		}	    
		
	    // do the cleaning for the following classes=============================
    	ArrayList<String> allDrugClass  = new ArrayList<String>();
    	
    	if(singleDrug){
    		allDrugClass.addAll(ehr.getDrugsAsString(DRUG_TABLE));		//rxcui != 0
    	}else{
	    	
	    	allDrugClass.add("p450");
	    	
	       	// role of p450 sensitive drugs
	    	allDrugClass.add("substrate");
	    	allDrugClass.add("inhibitor");
	    	allDrugClass.add("inducer");    	
	    	// ATC class (level=1) of p450 sensitive drugs
	    	allDrugClass.add("N");
	    	allDrugClass.add("C");
	    	allDrugClass.add("A");
	    	allDrugClass.add("J");
	    	allDrugClass.add("L");
	    	allDrugClass.add("D");
	    	allDrugClass.add("R");
	    	allDrugClass.add("M");
	    	allDrugClass.add("G");
	    	allDrugClass.add("B");
	    	allDrugClass.add("P");
	    	allDrugClass.add("H");    	
	    	// p450 sensitive drug ordonned by gene
	    	allDrugClass.add("1A2");
	    	allDrugClass.add("2B6");
	    	allDrugClass.add("2C8");
	    	allDrugClass.add("2C9");
	    	allDrugClass.add("2C19");
	    	allDrugClass.add("2D6");
	    	allDrugClass.add("2E1");
	    	allDrugClass.add("3A4");
	    	allDrugClass.add("3A5");
	    	allDrugClass.add("3A7");
	    	// cross between drug role and ATC class
	    	allDrugClass.add("substrate_N");
	    	allDrugClass.add("substrate_C");
	    	allDrugClass.add("inhibitor_N");  
    	}
    	
    	// Create tables r1
    	for(String profileTable:profileTables){
    		//r0
    		ehr.createPhenotypeProfileTableR1(profileTable, profileTable+"_rr_ic");
    		//r1
    		ehr.createPhenotypeProfileTableR1(profileTable, profileTable+"_p1");
    	}
    	    	
    	// ======================================================================
    	for(String c: allDrugClass){
    		System.out.println("Cleaning for class: "+c);
			// consider class of drugs
			ArrayList<Integer> mbr = new ArrayList<Integer>();
			if(!singleDrug){
				//all p450
				if(c.equals("p450")){
					mbr = ehr.getP450DrugClassMember(c);
				}
				//drug role
				if(c.equals("substrate") || c.equals("inhibitor") || c.equals("inducer")){
					mbr = ehr.getP450DrugClassMemberByRole(c);
				}
				// ATC level 1
				if(c.length()==1){
					mbr = ehr.getP450DrugClassMemberByAtcLevel(c);
				}
				// genes
				if(c.length()==3 || (c.length()==4 && !c.equals("p450"))){
					mbr = ehr.getP450DrugClassMemberByGene(c);
				}
				// drug role x ATC level1
				if(c.contains("_")){
					mbr = ehr.getP450DrugClassMemberByRoleXAtc(c);
				}
			}else if(singleDrug){
				// case of a single drug
				System.out.println("test");
				mbr.add(Integer.parseInt(c));
			}
			System.out.println("nbre of drugs in our set : "+mbr.size());
			
			
			// ************ Ic STEP CLEANING: INFORMATION CONTENT **********************
			System.out.println("	0. Information Content consideration ...");
			double icMin=4.25;
			double icMax=12.75;
			
			if(upOrDown.equals(DOWN)){
				ehr.cleaningIcStep(DOSE_DOWN_PHENO, DOSE_DOWN_PHENO_REDUCED0, c, icMin, icMax, singleDrug);
			}else if(upOrDown.equals(UP)){
				ehr.cleaningIcStep(DOSE_UP_PHENO, DOSE_UP_PHENO_REDUCED0, c, icMin, icMax, singleDrug);
			}	
		    // *************************************************************************
		    // ************ FIRST STEP CLEANING: BONFERRONI ****************************			
			System.out.println("	1. Boneferroni correction ...");
		    double rrMin     = 0.5;  // |log(1/2)| = 0.301
		    double rrMax     = 2;    // |log(2)| = 0.301
		    double alpha     = 0.05;
	
		    int k    = 0;		    
		    int k_   = 0;
		    if(upOrDown.equals(DOWN)){
		    	k    = ehr.getAnteK(DOSE_CHG_ANNOT, DOWN, singleDrug, mbr);	
		    	System.out.println("	number of tests s: k="+k);
			}else if(upOrDown.equals(UP)){
				k_   = ehr.getAnteK(DOSE_CHG_ANNOT, UP, singleDrug, mbr);
				System.out.println("	number of tests s: k_="+k_);
			}	
		    		
		    double alphaBar = 0.0;
		    double alphaBar_ = 0.0;
		    if(k>0){
		    	if(upOrDown.equals(DOWN)){
		    		alphaBar    = alpha / k; // Bonferroni correction
				}else if(upOrDown.equals(UP)){
					alphaBar_   = alpha / k_; // Bonferroni correction
				}	
			  //double alphaBar__  = alpha / k__; // Bonferroni correction
			  //double alphaBar___ = alpha / k___; // Bonferroni correction
		    }else{
		    	alphaBar    = alpha;
		    	alphaBar_    = alpha;
		    }
			    						
		    if(upOrDown.equals(DOWN)){
		    	ehr.cleaningFirstStep(DOSE_DOWN_PHENO_REDUCED0, DOSE_DOWN_PHENO_REDUCED1, c, alphaBar, rrMax, rrMin, singleDrug);
			}else if(upOrDown.equals(UP)){
				ehr.cleaningFirstStep(DOSE_UP_PHENO_REDUCED0, DOSE_UP_PHENO_REDUCED1, c, alphaBar_, rrMin, rrMax, singleDrug);
			}				    							    
			    		    
		    System.out.println("	Done.");		    
			
		    // *************************************************************************
		    // ************ SECOND STEP CLEANING: ELIM method (Alexa et al 2006) *******
		    
		    System.out.println("	2. Alexa reduction ...");
		    for(String profileTable: profileTables){
		    	System.out.println("		cleaning "+profileTable+"_p1 ...");
		    	HashMap<String, Double> annotSet = new HashMap<String, Double>();
		    	LinkedHashMap<String, Double> orderedAnnotSet = new LinkedHashMap<String, Double>();
		    	LinkedHashMap<String, Double> orderedAnnotSet2 = new LinkedHashMap<String, Double>();
		
		    	ArrayList<String> excludedAnnotSet = new ArrayList<String>();	        	
		    	HashMap<String, Double> reducedAnnotSet = new HashMap<String, Double>();	        	
			   	    	
		    	//************ get annotations **************** 
			    annotSet = ehr.getAnnotByDrugClass(profileTable+"_p1", c, "1", "0.05", singleDrug);
			    //ehr.closeConnection();			    
			    System.out.println("			size of the original annot set : "+annotSet.size());
			    
			    //System.out.println(annotSet);
			    if(annotSet.size()>0){			    	
			    	
			    	//annotSet = ehr.getAnnotTemp(ANNOT_TABLE);
				    orderedAnnotSet = sortByValue(annotSet); 			// first sorting by pvalue
				    orderedAnnotSet.putAll(annotSet);
				    System.out.println("			size of the annot set ordered 1ce: "+orderedAnnotSet.size());
				    //System.out.println(orderedAnnotSet);
				    orderedAnnotSet2.putAll(sortByValue2(orderedAnnotSet, ehr));	// second sorting by specificity in the ontology
				    System.out.println("			size of the annot set ordered 2ce: "+orderedAnnotSet2.size());
				    //System.out.println(orderedAnnotSet2);
				    //***************************************************************				    
				    //reopen connection if closed
				    //ehr.reopenConnection();
				    //ehr.reopenConnectionIfNecessary();				    				    
				    ehr.createPhenotypeProfileTable2(profileTable+"_p2");	    	    
				    				    
				    for(String key: orderedAnnotSet2.keySet()){
				    	if(!excludedAnnotSet.contains(key)){
				    		String[] id=key.split("_");
				    		String atc   = id[0];
				    		String rxcui = id[1];
					    	String cui   = id[2];
					    	if(!reducedAnnotSet.containsKey(key)){
					    		reducedAnnotSet.put(key, orderedAnnotSet2.get(key));
					    		//System.out.println("add "+cui);
					    		if(profileTable.equals(DOSE_DOWN_PHENO)){
					    			ehr.addPhenotype1r2(atc, rxcui, cui, orderedAnnotSet2.get(key));
					    		}else if(profileTable.equals(DOSE_UP_PHENO)){
					    			ehr.addPhenotype2r2(atc, rxcui, cui, orderedAnnotSet2.get(key));
					    		}else if(profileTable.equals(DRUG_CHG_PHENO_DB)){
					    			ehr.addPhenotype3r2(atc, rxcui, cui, orderedAnnotSet2.get(key));
					    		}else if(profileTable.equals(DRUG_CHG_PHENO_MS)){
					    			ehr.addPhenotype4r2(atc, rxcui, cui, orderedAnnotSet2.get(key));
					    		}
					    	}
					    	
					    	ArrayList<String> parentsCui     = new ArrayList<String>();
					    	
					    	if(!ptrs.containsKey(cui)){					    		
					    		parentsCui.addAll(ehr.getAllAscendants(ONTO, cui));					  
					    		ptrs.put(cui, parentsCui);
					     	}else{
					     		//System.out.println("ptrs already processed");
					     		parentsCui = ptrs.get(cui);
					     	}
					    	
					    	//System.out.println(parents);
					    	if(parentsCui.size()>0){
						    	for(String cuiOfParent : parentsCui){
						    		// compare pvalue with parents	
						    		if(!cuiOfParent.equals(cui)){
							    		if(orderedAnnotSet2.containsKey(atc+"_"+rxcui+"_"+cuiOfParent)){		
							    			//System.out.println(atc+"_"+rxcui+"_"+cuiOfParent+"  is a parent (of "+key+") and is in the profile");
								    		if(orderedAnnotSet2.get(atc+"_"+rxcui+"_"+cuiOfParent)>=orderedAnnotSet2.get(key)){
								    			//System.out.println("\t and the pvalue of this parent is >=");
								    			if(!excludedAnnotSet.contains(atc+"_"+rxcui+"_"+cuiOfParent)){
								    				excludedAnnotSet.add(atc+"_"+rxcui+"_"+cuiOfParent);
								    	    	}
								    		}
							    		}
						    		}
						    	}
					    	}
				    	}
				    }
			    }
			    
			    System.out.println("			size of the reduced annot set : "+reducedAnnotSet.size());
			    //System.out.println(reducedAnnotSet);			    
		    }//end FOR profile tables	    
		    
    	}// end of different drug classes	    	
    	// =========================================================================
    	ehr.closeSst();
    	Date end = new Date();
	    System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	}

	// sort a map by its value - not by key
    static public <K, V extends Comparable<? super V>> LinkedHashMap<K, V> 
        sortByValue( Map<K, V> map )
    {
    	LinkedList<Map.Entry<K, V>> list =
            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }

			@Override
			public Comparator<Entry<K, V>> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<K, V>> thenComparing(
					Comparator<? super Entry<K, V>> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<Entry<K, V>> thenComparing(
					Function<? super Entry<K, V>, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<Entry<K, V>> thenComparing(
					Function<? super Entry<K, V>, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<K, V>> thenComparingInt(
					ToIntFunction<? super Entry<K, V>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<K, V>> thenComparingLong(
					ToLongFunction<? super Entry<K, V>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<K, V>> thenComparingDouble(
					ToDoubleFunction<? super Entry<K, V>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

        } );

        LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
   
	}

    
    // sort a map by its value - not by key, then by specificity
    static public  LinkedHashMap<String, Double> sortByValue2( LinkedHashMap<String, Double> map, Stride6Api5Ante ehr) throws UnsupportedEncodingException, SQLException{
    	LinkedHashMap<String, Double> orderedList = new LinkedHashMap<String, Double>(); 		// the final list we want to return
    	HashMap<String, Double> partialList = new HashMap<String, Double>();					// the partial list of elements with similar pvalue
    	LinkedHashMap<String, Double> orderedPartialList = new LinkedHashMap<String, Double>(); // the partial list of elements with similar pvalue ordered with topologique order
    	String lastkey = "";
    	
	    int c=0;
    	for(String key:map.keySet()){
    		c++;
    		if(partialList.size()==0){ //first element of the list
    			lastkey=key;  
    			partialList.put(key, map.get(key));
    		}else{
    			if(partialList.get(lastkey).toString().equals(map.get(key).toString())){
	    			partialList.put(key, map.get(key));
	    			//System.out.println("expend partial List : "+partialList);
    			}
    			if(!partialList.get(lastkey).toString().equals(map.get(key).toString()) || c==map.size()){ // no previous element with this pvalue    
    				// if more than one element, we want to do SECOND ORDERING
    				if(partialList.size()>1){
    					while(orderedPartialList.size()<partialList.size()){
	    					//ordering of the partial list
			    			for(String key2:partialList.keySet()){
			    				if(!orderedPartialList.containsKey(key2)){
			    					Set<String> others = new HashSet<String>();
				    				others.addAll(partialList.keySet());
				    				others.remove(key2);
				    				others.removeAll(orderedPartialList.keySet());
				    				//if the current concept has no descendent that is in the rest of the partial list 
				    				// we add it to the ordered set
				    				
				    				//1-get descendants of key2	
				    				String cui1  = key2.split("_")[2];
				    				HashSet<String> descendants = new HashSet<String>();
				    				ArrayList<String> descendantsCui     = new ArrayList<String>();
				    				
				    				//	descendantsLocalId = ncbo.getDescendants(ONTO, ncbo.getLocalIdFromCui(cui1, ONTO));
				    				if(!inMemorydescendants.containsKey(cui1)){
				    		    			descendantsCui.addAll(ehr.getAllDescendants(ONTO, cui1));
				    		    			inMemorydescendants.put(cui1, descendantsCui);				    		    		
				    		     	}else{
				    		     		//System.out.println("ptrs already processed");
				    		     		descendantsCui = inMemorydescendants.get(cui1);
				    		     	}				    				
				    				
				    				//2-get descendants that are in the others (ie concept of equal pvalue)
				    				for(String o:others){
				    					if(descendantsCui.contains(o.split("_")[2])){
				    						descendants.add(o);
				    					}
				    				}
				    				// if key has no descendants in the others : then we can add it to the ordered list
				    				if(descendants.size()==0){
				    					orderedPartialList.put(key2, partialList.get(key2));
				    				}		  
			    				}
			    			} 
    					}
    				}
    				// SINGLETON if partialList.size==1 
    				if(partialList.size()==1){
    					orderedPartialList.put(lastkey, partialList.get(lastkey));
    				}
    				//System.out.println("orderedParitalList : "+orderedPartialList);
					// copy of orderedPartialList in orderedList
					orderedList.putAll(orderedPartialList);
					//System.out.println(orderedList);
	
					// new initalisation of partial lists
					partialList = new HashMap<String, Double>();
					orderedPartialList = new LinkedHashMap<String, Double>();
					lastkey=key;  
	    			partialList.put(key, map.get(key));
    			}
    		}    		
    	}
    	return orderedList;
    }    	
}
