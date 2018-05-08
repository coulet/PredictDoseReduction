package loria.sf_nancy6_drug_ref;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlotProfile {

	static String EHR_BASE 			 = "";
	static String SINGLE 			 = "single";
	static String CLASS				 = "class";

	
	public static void main(String[] args) throws InterruptedException, IOException, SQLException, ClassNotFoundException {

		String singleDrug = "class";//"class";//args[0]; //single or class
		String upOrDown = "down"; //args[1] //up or down
		Stride6Api6 ehr = new Stride6Api6(EHR_BASE);
		String phenotypeProfileTable = "user_x.dose_down_65ante_phenotype_p450_all_h";
		String icdProfileTable = "user_x.dose_down_65ante_icd_p450_all_h";
		String labProfileTable = "user_x.dose_down_65ante_lab_p450_all_h";

		// build the feature matrix for the following drug classes or single drug
    	ArrayList<String> allDrugClass  = new ArrayList<String>();
    	if(singleDrug.equals(CLASS)){
    		allDrugClass.addAll(ehr.getDrugClassName(phenotypeProfileTable, icdProfileTable, labProfileTable));
    		//allDrugClass.addAll(ehr.getDrugClassNameJustLab(/*phenotypeProfileTable, icdProfileTable,*/ labProfileTable));
    		System.out.println(allDrugClass.size()+" drug classes to consider");
    		for(final String d:allDrugClass){
    			
    			// drugs with rxcui
    			plotProfile("phenotype", "atc_code", d, upOrDown);
    			plotProfile("icd", "atc_code", d, upOrDown);
    			plotProfile("lab", "atc_code", d, upOrDown);
    			
    		}
    	}else if(singleDrug.equals(SINGLE)){
    		allDrugClass.addAll(ehr.getSingleDrugName(phenotypeProfileTable, icdProfileTable, labProfileTable));
    		System.out.println(allDrugClass.size()+" single drugs to consider");
    		for(final String d:allDrugClass){
    			// drugs with rxcui
    			plotProfile("phenotype", "rxcui", d, upOrDown);
    			plotProfile("icd", "rxcui", d, upOrDown);
    			plotProfile("lab", "rxcui", d, upOrDown);
    		}
    	}		
	}

	public static void plotProfile(String table, String attribute, String var, String upOrDown) throws InterruptedException {

		/*
		String table="phenotype";//"icd" "lab"
		String attribute="rxcui";
		String var="warfarin";
		*/
		System.out.println(table+" "+ attribute+" "+var);
		
		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		Process shell1 = null;
		Process shell2 = null;
		
		try {			
			shell1 = Runtime.getRuntime().exec(new String[] { "/usr/local/bin/Rscript", 
					"./PlotProfile.r", 
					table, attribute, var, upOrDown});
			reader1 = new BufferedReader(new InputStreamReader(shell1.getInputStream()));
            String line;
            while ((line = reader1.readLine()) != null) {
                System.out.println(line);
            }
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		try {
			shell2 = Runtime.getRuntime().exec(new String[] {"/usr/local/bin/convert","./data/profile/order_by_pvalue/"+table+"_profile_"+upOrDown+"_"+var+".png",
					"-rotate","90","./data/profile/order_by_pvalue/"+table+"_profile_"+upOrDown+"_"+var+".png"});
			reader2 = new BufferedReader(new InputStreamReader(shell2.getInputStream()));
            String line;
            while ((line = reader2.readLine()) != null) {
                System.out.println(line);
            }
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}		
}
