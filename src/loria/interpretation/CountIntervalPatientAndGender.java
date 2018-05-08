package loria.interpretation;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import loria.sf_nancy6_drug_ref.Stride6Api6;

/**
 * populate the table CountIntervalPatientGender
 *  
 * @author coulet
 *
 */
public class CountIntervalPatientAndGender {

	
	static String EHR_BASE 			  = "user_x"; //collaborators is for stride5";
	static String DOSE_CHG      	  = EHR_BASE+".dose_change_63m";
	static String CONTINUATION        = EHR_BASE+".continuation_63m";
	static String DEMOG      	  	  = "stride6.demographics";
	static String COUNT      	  	  = EHR_BASE+".p450_iid_pid_count";

	static String DRUG_TABLE 	 	 = "user_x.p450" ; //"user_x.drug_and_drug_class";
	
	static String UP 				 = "up";
	static String DOWN 				 = "down";	
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
						
		Stride6Api6 ehr = new Stride6Api6(EHR_BASE, 
				DOSE_CHG, CONTINUATION, DEMOG, DRUG_TABLE);

    	// Create COUNT table    	
    	ehr.createCountTable(COUNT);    
    	
    	// Get all drugs 
    	ArrayList<String> allDrugs  = new ArrayList<String>();
    	
    	allDrugs.addAll(ehr.getDrugsAsString(DRUG_TABLE));		//rxcui != 0

       	// ======================================================================
    	for(String c: allDrugs){
	    	
			// Count intervals and patients for this drug or drug class and insert it in the COUNT table			
			ehr.insertCount(COUNT, c);
			
			
			
    	}
	}
}
