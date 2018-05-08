package loria.sf_nancy6_drug_ref;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
public class CleanAnteLabs6_2 {
	
	static String EHR_BASE 			  = ""; 
	static String DOSE_CHG_ANNOT 	  = "user_x.dose_change_64ante_lab";
	static String DRUG_CHG_ANNOT_DB	  = "user_x.drug_change_64ante_lab_db";
	static String DRUG_CHG_ANNOT_MS	  = "user_x.drug_change_64ante_lab_ms";
	
	static String DOSE_DOWN_PHENO 	 = "user_x.dose_down_65ante_lab_p450_all";
	static String DOSE_UP_PHENO 	 = "user_x.dose_up_65ante_lab_p450_all";
	static String DRUG_CHG_PHENO_DB	 = "user_x.drug_change_65ante_lab_db_p450_all";
	static String DRUG_CHG_PHENO_MS	 = "user_x.drug_change_65ante_lab_ms_p450_all";

	static String DOSE_DOWN_PHENO_REDUCED0 	 = DOSE_DOWN_PHENO+"_rr_ic";
	static String DOSE_UP_PHENO_REDUCED0 	 = DOSE_UP_PHENO+"_rr_ic";
	static String DRUG_CHG_PHENO_DB_REDUCED0 = DRUG_CHG_PHENO_DB+"_rr_ic";
	static String DRUG_CHG_PHENO_MS_REDUCED0 = DRUG_CHG_PHENO_MS+"_rr_ic";
	
	static String DOSE_DOWN_PHENO_REDUCED1 	 = DOSE_DOWN_PHENO+"_p1";
	static String DOSE_UP_PHENO_REDUCED1 	 = DOSE_UP_PHENO+"_p1";
	static String DRUG_CHG_PHENO_DB_REDUCED1 = DRUG_CHG_PHENO_DB+"_p1";
	static String DRUG_CHG_PHENO_MS_REDUCED1 = DRUG_CHG_PHENO_MS+"_p1";
	
	static String DRUG_TABLE 	 	 = "user_x.p450" ; //"user_x.drug_and_drug_class";
	
	static String UP 				 = "up";
	static String DOWN 				 = "down";
	
	//static String ONTO 	= "ICD";
	
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
	    		null, null, null, null);

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
		    	    
		    
    	}// end of different drug classes	    	
    	// =========================================================================
    	ehr.closeSst();
    	Date end = new Date();
	    System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	}    
}
