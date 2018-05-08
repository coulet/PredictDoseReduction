package loria.sf_nancy6_drug_ref;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import loria.eutils.Eutils;

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
public class Humanize6 {
	
	static String EHR_BASE 			  = ""; //collaborators is for stride5";
	static String DOSE_CHG_ANNOT 	  = "user_x.dose_change_64ante_annotation";
	static String DRUG_CHG_ANNOT_DB	  = "user_x.drug_change_64ante_annotation_db";
	static String DRUG_CHG_ANNOT_MS	  = "user_x.drug_change_64ante_annotation_ms";
	
	// TO CHANGE =============
	static String DOSE_DOWN_PHENO 	 = "user_x.dose_down_65ante_phenotype_p450";
	static String DOSE_UP_PHENO 	 = "user_x.dose_up_65ante_phenotype_p450";
	static String DRUG_CHG_PHENO_DB	 = "user_x.drug_change_65ante_phenotype_db_p450";
	static String DRUG_CHG_PHENO_MS	 = "user_x.drug_change_65ante_phenotype_ms_p450";	
	// =======================
	
	static String DOSE_DOWN_PHENO_REDUCED2 	 = DOSE_DOWN_PHENO+"_all_p2";
	static String DOSE_UP_PHENO_REDUCED2 	 = DOSE_UP_PHENO+"_all_p2";
	static String DRUG_CHG_PHENO_DB_REDUCED2 = DRUG_CHG_PHENO_DB+"_all_p2";
	static String DRUG_CHG_PHENO_MS_REDUCED2 = DRUG_CHG_PHENO_MS+"_all_p2";
	
	static String DOSE_DOWN_PHENO_HUMAN   = DOSE_DOWN_PHENO+"_all_h";
	static String DOSE_UP_PHENO_HUMAN 	  = DOSE_UP_PHENO+"_all_h";
	static String DRUG_CHG_PHENO_DB_HUMAN = DRUG_CHG_PHENO_DB+"_all_h";
	static String DRUG_CHG_PHENO_MS_HUMAN = DRUG_CHG_PHENO_MS+"_all_h";
	
	static String DRUG_TABLE 	 	 = "user_x.p450" ; //"user_x.drug_and_drug_class";
	static String LAB_TABLE 	 	 = "user_x.lab_m" ;	
	
	static String UP 				 = "up";
	static String DOWN 				 = "down";
	
	static String ONTO 	= "SNOMEDCT_US";
	
	
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
				
		boolean singleDrug = Boolean.parseBoolean(args[0]); // true if only single drugs, false if only classes of drug
		String doseDownAnnotTable = args[1]; // 
		String doseUpAnnotTable = args[2]; // 
		
		Date start       = new Date();
		Stride6Api5Ante ehr = new Stride6Api5Ante(EHR_BASE, 
				DOSE_CHG_ANNOT, DRUG_CHG_ANNOT_DB, DRUG_CHG_ANNOT_MS, 
				doseDownAnnotTable, doseUpAnnotTable, DRUG_CHG_PHENO_DB, DRUG_CHG_PHENO_MS, 
	    		DOSE_DOWN_PHENO_REDUCED2, DOSE_UP_PHENO_REDUCED2, DRUG_CHG_PHENO_DB_REDUCED2, DRUG_CHG_PHENO_MS_REDUCED2, 
	    		DOSE_DOWN_PHENO_HUMAN, DOSE_UP_PHENO_HUMAN, DRUG_CHG_PHENO_DB_HUMAN, DRUG_CHG_PHENO_MS_HUMAN);

	    ArrayList<String> profileTables = new ArrayList<String>(Arrays.asList(doseDownAnnotTable+"_all_p2", doseUpAnnotTable+"_all_p2"/*, DRUG_CHG_PHENO_DB, DRUG_CHG_PHENO_MS*/));
	    	      
	    Eutils mye = new Eutils();
    	// Create tables _h
    	for(String profileTable:profileTables){
    		//h
    		ehr.createPhenotypeProfileTableH(profileTable, profileTable.substring(0, profileTable.lastIndexOf("_")+1)+"h");    	
    	}
	    
	    //get the set of CUI and their label=============================
    	ArrayList<String> cui = new ArrayList<String>();
	    HashMap<String,String> cuiLabel = new HashMap<String,String>();
	    for(String profileTable:profileTables){
    		// get list of cui
    		cui.addAll(ehr.getCui(profileTable));
	    }
	    System.out.println("number of cui "+cui.size());
		// for each cui get pref label
		for(String c:cui){
			String label = "";
			if(profileTables.contains("user_x.dose_down_65ante_lab_p450_all_p2")){
				label = ehr.getLabComponentLabel(LAB_TABLE, c);
			}else{
				label = ehr.getCuiLabel(ONTO, c);
			}
			//System.out.println(c+", "+label);
			if(label.length()>0){
				cuiLabel.put(c, label);
			}else{
				System.out.println("no label found for cui "+c);
			}
    		   		    	    		   	
    	}
	    //get the set of RXCUI and their label=============================
    	HashMap<Integer, String> rxcuiLabel = new HashMap<Integer,String>();
    	HashMap<String, String> classLabel = new HashMap<String,String>();
	    if(singleDrug){
	    	rxcuiLabel.putAll(ehr.getDrugsAndLabel(DRUG_TABLE));
	    	System.out.println("number of rxcui "+rxcuiLabel.size());
	    }else{	       	
	    	classLabel.put("p450","p450");
	    	
	       	// role of p450 sensitive drugs
	    	classLabel.put("substrate", "p450 substrate");
	    	classLabel.put("inhibitor", "p450 inhibitor");
	    	classLabel.put("inducer", "p450 inducer");    	
	    	// ATC class (level=1) of p450 sensitive drugs
	    	classLabel.put("A", "(ALIMENTARY+OR+METABOLISM)");
	    	classLabel.put("B", "BLOOD");
	    	classLabel.put("C", "(CARDIOVASCULAR+OR+HEART)");
	    	classLabel.put("D", "(DERMATOLOGICAL+OR+SKIN)");
	    	classLabel.put("G", "(GENITAL+OR+URINARY+OR+\"SEX+HORMON\")");
	    	classLabel.put("H", "(HORMON+OR+HORMONAL)");    	
	    	classLabel.put("J", "(ANTIINFECTIVE+OR+INFECTION)");
	    	classLabel.put("L", "(ANTINEOPLASTIC+OR+CANCER+OR+NEOPLASM+OR+IMMUNOMODULATING+OR+IMMUNE)");
	    	classLabel.put("M", "MUSCULO-SKELETAL+OR+MUSCLE");
	    	classLabel.put("N", "NERVOUS");
	    	classLabel.put("P", "(ANTIPARASITIC+OR+INSECTICIDE+OR+REPELLENT)");
	    	classLabel.put("R", "(RESPIRATORY+OR+LUNG)");
	    	// p450 sensitive drug ordonned by gene
	    	classLabel.put("1A2", "CYP1A2");
	    	classLabel.put("2B6", "CYP2B6");
	    	classLabel.put("2C8", "CYP2C8");
	    	classLabel.put("2C9", "CYP2C9");
	    	classLabel.put("2C19", "CYP2C19");
	    	classLabel.put("2D6", "CYP2D6");
	    	classLabel.put("2E1", "CYP2E1");
	    	classLabel.put("3A4", "CYP3A4");
	    	classLabel.put("3A5", "CYP3A5");
	    	classLabel.put("3A7", "CYP3A7");
	    	// cross between drug role and ATC class
	    	classLabel.put("substrate_N", "");
	    	classLabel.put("substrate_C", "");
	    	classLabel.put("inhibitor_N", "");  	
	    	System.out.println("number of drug class "+classLabel.size());
	    }
	    
    	    	    	    	
    	// ======================================================================
    	// for each profile table
    	for(String profileTable:profileTables){
    		if(singleDrug){
    			// get and insert all pairs rxcui/cui 
    			ehr.insertHumanProfiles1(profileTable, profileTable.substring(0, profileTable.lastIndexOf("_")+1)+"h", rxcuiLabel, cuiLabel, mye);    			
    		}else{
    			// get and insert all pairs atc/cui 
    			ehr.insertHumanProfiles2(profileTable, profileTable.substring(0, profileTable.lastIndexOf("_")+1)+"h", classLabel, cuiLabel, mye);    			
    		}

    	}// end of different drug classes	    	
    	// =========================================================================
    	ehr.closeSst();
    	Date end = new Date();
	    System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	}
}
