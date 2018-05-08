package loria.interpretation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.*;

import loria.sf_nancy6_drug_ref.Stride6Api6;

/**
 * populate the table CountIntervalPatientGender
 *  
 * @author coulet
 *
 */
public class FormatResults {

	
	static String EHR_BASE 			  = "user_x"; 
	static String PROFILE_ICD 	  	  = EHR_BASE+".dose_down_65ante_icd_p450_all_h";
	static String PROFILE_LAB		  = EHR_BASE+".dose_down_65ante_lab_p450_all_h";
	static String PROFILE_PHENOTYPE	  = EHR_BASE+".dose_down_65ante_phenotype_p450_all_h";
	static String DRUG_TABLE 	 	  = EHR_BASE+".p450_results" ; //"user_x.drug_and_drug_class";

	
	static String UP 				 = "up";
	static String DOWN 				 = "down";	
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException, JSONException {
						
		Stride6Api6 ehr = new Stride6Api6(EHR_BASE);
		
		boolean singleDrug = true;//Boolean.parseBoolean(args[0]); // true if only single drugs, false if only classes of drug
		String upOrDown = "down";//args[1]; // up or down

	    // do the cleaning for the following classes=============================
    	ArrayList<String> allDrugClass  = new ArrayList<String>();
    	
    	if(singleDrug){
    		allDrugClass.addAll(ehr.getDrugsAsString(DRUG_TABLE)); //rxcui != 0
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
	    	allDrugClass.add("G");
	    	allDrugClass.add("B");
	    	allDrugClass.add("H");    	
	    	// p450 sensitive drug ordonned by gene
	    	allDrugClass.add("1A2");
	    	allDrugClass.add("2B6");
	    	allDrugClass.add("2C8");
	    	allDrugClass.add("2C9");
	    	allDrugClass.add("2C19");
	    	allDrugClass.add("2D6");
	    	allDrugClass.add("3A4");
	    	allDrugClass.add("3A5");
	    	allDrugClass.add("3A7");
    	}
		
    	// Create output file	
    	FileWriter fw = new FileWriter(new File ("./data/results_in_csv"));   
    	
    	// Get all drugs 
    	ArrayList<String> allDrugs  = new ArrayList<String>();
    	
    	allDrugs.addAll(ehr.getDrugsAsString(DRUG_TABLE));		//rxcui != 0

       	// ======================================================================
    	for(String c: allDrugs){
	    	
    		// get 10 fold results
    		String auc_icd = "", auc_pheno="", auc_lab="";
    		String acc_icd = "", acc_pheno="", acc_lab="";    	
			String fm_icd  = "", fm_pheno="", fm_lab="";
			
			//BufferedReader br = new BufferedReader(
		    //        new FileReader("./data/p450.json"));
			String json = readFile("./data/p450.json");
			//System.out.println(json);
			JSONObject obj = new JSONObject(json);
			//System.out.println(obj.getJSONObject("name"));
			//String pageName = obj.getJSONObject("pageInfo").getString("pageName");
			String rxcui = "";
			JSONArray arr1 = obj.getJSONArray("children");
			for (int i = 0; i < arr1.length(); i++)
			{
				JSONObject obj2 =  arr1.getJSONObject(i);
				JSONArray arr2 = obj2.getJSONArray("children");
				 
				for (int j = 0; j < arr2.length(); j++)
				{					
					JSONObject obj3 =  arr2.getJSONObject(j);
					JSONArray arr3 = obj3.getJSONArray("children");
					for (int k = 0; k < arr3.length(); k++)
					{												
						rxcui = arr3.getJSONObject(k).getString("rxcui");
						//System.out.println(rxcui);
						if(rxcui.equals(c) && upOrDown.equals(DOWN)){
							auc_icd=arr3.getJSONObject(k).getString("down_icd_auc");
							acc_icd=arr3.getJSONObject(k).getString("down_icd_acc");
							fm_icd=arr3.getJSONObject(k).getString("down_icd_f");
							auc_pheno=arr3.getJSONObject(k).getString("down_pheno_auc");
							acc_pheno=arr3.getJSONObject(k).getString("down_pheno_acc");
							fm_pheno=arr3.getJSONObject(k).getString("down_pheno_f");
							auc_lab=arr3.getJSONObject(k).getString("down_lab_auc");
							acc_lab=arr3.getJSONObject(k).getString("down_lab_acc");
							fm_lab=arr3.getJSONObject(k).getString("down_lab_f");
							//System.out.println("I found "+c+" "+auc+" "+acc+" "+fm);
							break;
						}						
					}
					if(rxcui.equals(c)){
						break;
					}					
				}
				if(rxcui.equals(c)){
					break;
				}
			}
			
    		
    		// get lines
    		ehr.getResultsInCsv(PROFILE_ICD,DRUG_TABLE, "ICD", c, fw, auc_icd, acc_icd, fm_icd);
    		ehr.getResultsInCsv(PROFILE_ICD,DRUG_TABLE, "PHENOTYPE", c, fw, auc_pheno, acc_pheno, fm_pheno);
    		ehr.getResultsInCsv(PROFILE_ICD,DRUG_TABLE, "LAB", c, fw, auc_lab, acc_lab, fm_lab);
    				
    	}
    	fw.close();
	}
	private static String readFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
}
