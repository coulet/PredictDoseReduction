package loria.sf_nancy6_drug_ref;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.math3.distribution.HypergeometricDistribution;


/**
 * This class return, for a set of drugs, the number of phenotype observed during intervals of drug changes
 * @author coulet
 *
 */

public class ComputeAnteIcdDiagProfile6_all {

	static String DRUG_TABLE 	 	 = "user_x.p450" ; //"user_x.drug_and_drug_class";
	static String DOSE_CHG		 	 = "user_x.dose_change_63m";
	static String DOSE_CHG_ANNOT 	 = "user_x.dose_change_64ante_icd_m";
	static String DRUG_CHG_ANNOT_DB	 = "user_x.drug_change_64ante_icd_db";
	static String DRUG_CHG_ANNOT_MS	 = "user_x.drug_change_63ante_icd_ms";
	static String CONTI_ANNOT 		 = "user_x.continuation_64ante_icd_m";
	
	static String UP 				 = "up";
	static String DOWN 				 = "down";
	
	static String SINGLE 			 = "single";
	static String CLASS				 = "class";
	
	static String ATC 				 = "atc";
	static String P450 				 = "p450";
	static String CLASSIFICATION	 = P450;
	
	static String ONTO 				 = "SNOMEDCT";
	
	static String ALL				 = "all_drugs";
	static String DRUG_REF		 	 = "single_drug";
	static String REF 				 = "single_drug"; //"all_drugs";// "single_drug";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		// ============================================================
		// ========================= PRELIM   =========================
		// ============================================================
		int cuiToStartWith = Integer.parseInt(args[0]);
		int rxcuiToStartWith = Integer.parseInt(args[1]); // 7646
		String singleOrClass = args[2]; // single or class string
		String DOSE_DOWN_PHENO="";
		String DOSE_UP_PHENO="";
		String DRUG_CHG_PHENO_DB="";
		String DRUG_CHG_PHENO_MS="";
		if(REF.equals(DRUG_REF)){
			DOSE_DOWN_PHENO 	 = "user_x.dose_down_65ante_icd_p450_all";
			DOSE_UP_PHENO 	     = "user_x.dose_up_65ante_icd_p450_all";
			DRUG_CHG_PHENO_DB	 = "user_x.drug_change_65ante_icd_db_p450_all";
			DRUG_CHG_PHENO_MS	 = "user_x.drug_change_65ante_icd_ms_p450_all";
		}else if(REF.equals(ALL)){
			DOSE_DOWN_PHENO 	 = "user_x.dose_down_65ante_icd_p450_pr_all";
			DOSE_UP_PHENO 	     = "user_x.dose_up_65ante_icd_p450_pr_all";
			DRUG_CHG_PHENO_DB	 = "user_x.drug_change_65ante_icd_db_p450_pr_all";
			DRUG_CHG_PHENO_MS	 = "user_x.drug_change_65ante_icd_ms_p450_pr_all";
		}
		Date start 			= new Date();
		Stride6Api4Ante ehr = new Stride6Api4Ante(DRUG_TABLE, DOSE_CHG, DOSE_CHG_ANNOT, DRUG_CHG_ANNOT_DB, DRUG_CHG_ANNOT_MS, CONTI_ANNOT,  
	    		DOSE_DOWN_PHENO, DOSE_UP_PHENO, DRUG_CHG_PHENO_DB, DRUG_CHG_PHENO_MS);				
	    
	    ArrayList<String> profileTables = new ArrayList<String>(Arrays.asList(DOSE_DOWN_PHENO, DOSE_UP_PHENO/*, DRUG_CHG_PHENO_DB, DRUG_CHG_PHENO_MS*/));
	    
	    //create phenotype profile tables if necessary
	    for(String profileTable: profileTables){
	    	ehr.createPhenotypeProfileTable(profileTable);	    	
	    }    	     	

    	// get sets of drugs and drug classes
    	ArrayList<Integer> allDrug 		= new ArrayList<Integer>();
    	ArrayList<String> allDrugClass  = new ArrayList<String>();
    	if(singleOrClass.equals(SINGLE)){
    		allDrug.addAll(ehr.getDrug(DRUG_TABLE));		//rxcui != 0
    		System.out.println("nbre of single drugs to consider: "+allDrug.size());
//    	allDrug.add(11289); //warfarin
//    	allDrugClass.addAll(ehr.getClass(DRUG_TABLE));	//rxcui == 0
    	}else if(singleOrClass.equals(CLASS)){
    	    	
	    	// role of p450 sensitive drugs
	    	allDrugClass.add("p450");
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
    	
    	
		// ============================================================
		// ========================= ACTION   =========================
		// ============================================================
	    
    	// ======================== SINGLE DRUG =======================
    	if(singleOrClass.equals(SINGLE)){
	    	for(Integer d:allDrug){
	    		if(d>=rxcuiToStartWith){
		    		// classes of the drugs
					ArrayList<String> classes = new ArrayList<String>();
					classes = ehr.getDrugClass(d);
					
		    		// m1
			    	int m1 = 0; int m1_= 0; //int m1__ = 0; int m1___ = 0;
			    	m1    = ehr.getm1(DOSE_CHG_ANNOT, DOWN, d);
			    	m1_   = ehr.getm1(DOSE_CHG_ANNOT, UP, d);
			    	//m1__  = ehr.getm1(DRUG_CHG_ANNOT_DB, "", d);
			    	//m1___ = ehr.getm1(DRUG_CHG_ANNOT_MS, "", d);
			    	// m2
			    	int m2 = 0;int m2_= 0;
			    	if(REF.equals(DRUG_REF)){
			    		m2    = ehr.getm2(d);
			    		m2_   = m2;
			    	//	m2__  = m2;
			    	//	m2___ = m2;   
			    	}else if(REF.equals(ALL)){
			    		m2    = ehr.getm2prime(DOSE_CHG_ANNOT, DOWN, d); // with drug change based on DB
			    		m2_   = ehr.getm2prime(DOSE_CHG_ANNOT, UP, d);   // with drug change based on DB
			    	//	m2__  = ehr.getm2prime(DRUG_CHG_ANNOT_DB, "", mbr);
			    	//	m2___ = ehr.getm2prime(DRUG_CHG_ANNOT_MS, "", mbr);    	    	
			    	}
			    	
			    	// M
			    	int M = 0; int M_= 0; //int M__ = 0; int M___ = 0;
			    	M = m1+m2;M_ = m1_+m2_;//M__ = m1__+m2;M___ = m1___+m2;
			    
			    	// get sets of phenotypes
			    	ArrayList<String> doseDownPhenotype 	= new ArrayList<String>();
			    	ArrayList<String> doseUpPhenotype   	= new ArrayList<String>();
			    	//ArrayList<String> drugChangePhenotypeDb = new ArrayList<String>();
			    	//ArrayList<String> drugChangePhenotypeMs = new ArrayList<String>();
			    	ArrayList<String> allPhenotype 			= new ArrayList<String>();
			    	doseDownPhenotype.addAll(ehr.getCui(DOSE_CHG_ANNOT, DOWN, d));
			    	doseUpPhenotype.addAll(ehr.getCui(DOSE_CHG_ANNOT, UP, d));
			    	//drugChangePhenotypeDb.addAll(ehr.getCui(DRUG_CHG_ANNOT_DB, "", d));
			    	//drugChangePhenotypeMs.addAll(ehr.getCui(DRUG_CHG_ANNOT_MS, "", d));
			    	//1
			    	allPhenotype.addAll(doseDownPhenotype);
			    	//2
			    	doseUpPhenotype.removeAll(allPhenotype);
			    	allPhenotype.addAll(doseUpPhenotype);
			    	/*
			    	//3
			    	drugChangePhenotypeDb.removeAll(allPhenotype);
			    	allPhenotype.addAll(drugChangePhenotypeDb);
			    	//4
			    	drugChangePhenotypeMs.removeAll(allPhenotype);
			    	allPhenotype.addAll(drugChangePhenotypeMs);
			    	*/
		//	    	allPhenotype.add("C0012634");
			    	
			    	// for each phenotype associated with the drug 
			    	System.out.println("\t for drug "+d+", "+allPhenotype.size()+"phenotype to test.");
			    	for(String p:allPhenotype){
			    		//System.out.println(p);
			    		int n2 = 0;
			    		if(REF.equals(DRUG_REF)){
			    			n2 = ehr.getn2(d,p);
			    		}
			    		
			    		
			    		// dose down phenotype
			    		if(doseDownPhenotype.contains(p)){
			    			int n1 = 0;
			    			n1 = ehr.getn1(DOSE_CHG_ANNOT, DOWN,d, p);
			    			if(REF.equals(ALL)){
		    	    			n2 = ehr.getn2prime(DOSE_CHG_ANNOT, DOWN, d, p);
		    	    		}
			    			int N = 0;
			    			N = n1+n2;
			    			//System.out.println(p);
			    			//System.out.println("n1="+n1+" 	m1="+m1+" 	n2="+n2+" 	m2="+m2);
			    			// p-value
			    			HypergeometricDistribution dist = new HypergeometricDistribution(M, m1, N);
			    			double pvalue = dist.probability(n1);
			    			if(Double.isNaN(pvalue)){
			    				pvalue=0.0;
			    			}
			    			if(pvalue<=0.05){
			    				//information content
			    				double ic = 0.0;
			    				ic=ehr.getIcdIc(p);
		
				    			// risk ratio
				    			double rr = 0.0;
				    			if(n2>0 && m2>0){
				    				rr = ((double)n1/(double)m1) / ((double)n2/(double)m2);
				    			}
			//	    			System.out.println(rr+" 	"+pvalue);
			//	    			if(rr>=1){
					    			if(classes.size()>0){
						    			for(String c:classes){
							    			ehr.addPhenotype(DOSE_DOWN_PHENO, c, d, p, pvalue, rr, ic);				    		
						    			}
					    			}else{
					    				ehr.addPhenotype(DOSE_DOWN_PHENO, "", d, p, pvalue, rr, ic);
					    			}
			//	    			}
			    			}
			    		}
			    		
			    		// dose up phenotype
			    		if(doseUpPhenotype.contains(p)){
			    			int n1_ = 0;
			    			n1_ = ehr.getn1(DOSE_CHG_ANNOT, UP, d, p);
			    			if(REF.equals(ALL)){
		    	    			n2 = ehr.getn2prime(DOSE_CHG_ANNOT, DOWN, d, p);
		    	    		}
			    			int N_ = 0;
			    			N_ = n1_+n2;
			    			// p-value
			    			HypergeometricDistribution dist = new HypergeometricDistribution(M_, m1_, N_);
			    			double pvalue = dist.probability(n1_);
			    			if(Double.isNaN(pvalue)){
			    				pvalue=0.0;
			    			}
			    			if(pvalue<=0.05){
			    				//information content
			    				double ic = 0.0;
			    				ic=ehr.getIcdIc(p);
				    			// risk ratio
				    			double rr = 0.0;
				    			if(n2>0 && m2>0){
				    				rr = ((double)n1_/(double)m1_) / ((double)n2/(double)m2);
				    			}
				//    			if(rr>=1){
		
					    			if(classes.size()>0){
						    			for(String c:classes){
							    			ehr.addPhenotype(DOSE_UP_PHENO, c, d, p, pvalue, rr, ic);	
						    			}
					    			}else{
					    				ehr.addPhenotype(DOSE_UP_PHENO, "", d, p, pvalue, rr, ic);
					    			}
				//    			}
			    			}
			    		}
		/*	    		
			    		// drug change phenotype -db
			    		if(drugChangePhenotypeDb.contains(p)){
			    			int n1__ = 0;
			    			n1__ = ehr.getn1(DRUG_CHG_ANNOT_DB, "", d, p);
			    			int N__ = 0;
			    			N__ = n1__+n2;
			    			// p-value
			    			HypergeometricDistribution dist = new HypergeometricDistribution(M__, m1__, N__);
			    			double pvalue = dist.probability(n1__);
			    			if(Double.isNaN(pvalue)){
			    				pvalue=0.0;
			    			}
			    			if(pvalue<=0.05){
			    	    		//information content
			    	    		double ic = 0.0;
			    	    		ic=ehr.getIcdIc(p);
			    				// risk ratio
			    				double rr = 0.0;
				    			if(n2>0 && m2>0){
				    				rr = ((double)n1__/(double)m1__) / ((double)n2/(double)m2);
				    			}	    			
				    			if(classes.size()>0){
					    			for(String c:classes){
						    			ehr.addPhenotype(DRUG_CHG_PHENO_DB, c, d, p, pvalue, rr);
					    			}
				    			}else{
				    				ehr.addPhenotype(DRUG_CHG_PHENO_DB, "", d, p, pvalue, rr);
				    			}
				    		}
			    		}
			    		
			    		// drug change phenotype -ms
			    		if(drugChangePhenotypeMs.contains(p)){
			    			int n1___ = 0;
			    			n1___ = ehr.getn1(DRUG_CHG_ANNOT_MS, "", d, p);
			    			int N___ = 0;
			    			N___ = n1___+n2;
			    			// p-value
			    			HypergeometricDistribution dist = new HypergeometricDistribution(M___, m1___, N___);
			    			double pvalue = dist.probability(n1___);
			    			if(Double.isNaN(pvalue)){
			    				pvalue=0.0;
			    			}
			    			if(pvalue<=0.05){
			    	    		//information content
			    	    		double ic = 0.0;
			    	    		ic=ehr.getIcdIc(p);
				    			// risk ratio
				    			double rr = 0.0;
				    			if(n2>0 && m2>0){
				    				rr = ((double)n1___/(double)m1___) / ((double)n2/(double)m2);
				    			}
				    			if(classes.size()>0){
					    			for(String c:classes){
						    			ehr.addPhenotype(DRUG_CHG_PHENO_MS, c, d, p, pvalue, rr);
					    			}
				    			}else{
				    				ehr.addPhenotype(DRUG_CHG_PHENO_MS, "", d, p, pvalue, rr);
				    			}
				    		}	
			    		}
		*/	    		
			    	}
	    		}
		    }
    	}else if(singleOrClass.equals(CLASS)){
	    
	    	// ======================== DRUG CLASS ========================
	    	for(String c:allDrugClass){
	    		System.out.println("+++drug class: "+c);
	    		// members of the class
	    		ArrayList<Integer> mbr = new ArrayList<Integer>();
	    		
	    		// if ATC class
	    		if(CLASSIFICATION.equals(ATC)){
		    		if(c.startsWith("J02") && !c.equals("J02")){    			
				    	//get members of an ATC drug class
			    		mbr = ehr.getAtcDrugClassMember(c+"%");
		    		}	    		
	    		}else 
	    		// if P450 class	
	    		if(CLASSIFICATION.equals(P450)){
	    			//all
	    			if(c.equals("p450")){
	    				System.out.println("all...");
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
	    			if(c.length()==3 || (c.length()==4 && (!c.equals("p450"))) ){
	    				mbr = ehr.getP450DrugClassMemberByGene(c);
	    			}
	    			// drug role x ATC level1
	    			if(c.contains("_")){
	    				mbr = ehr.getP450DrugClassMemberByRoleXAtc(c);
	    			}
	    		}	    		
		    		
	    		System.out.println("	size : "+mbr.size());
	    		if(mbr.size()>0){
	    			// m1
	    	    	int m1 = 0; int m1_= 0; //int m1__ = 0; int m1___ = 0;
	    	    	m1    = ehr.getm1(DOSE_CHG_ANNOT, DOWN, mbr);
	    	    	m1_   = ehr.getm1(DOSE_CHG_ANNOT, UP, mbr);
	    	    	//m1__  = ehr.getm1(DRUG_CHG_ANNOT_DB, "", mbr);
	    	    	//m1___ = ehr.getm1(DRUG_CHG_ANNOT_MS, "", mbr);

	    	    	// m2
	    	    	int m2 = 0; int m2_= 0; //int m2__ = 0; int m2___ = 0;
	    	    	if(REF.equals(DRUG_REF)){
	    	    		m2    = ehr.getm2(mbr);
	    	    		m2_   = m2;
	    	    	//	m2__  = m2;
	    	    	//	m2___ = m2;   
	    	    	}else if(REF.equals(ALL)){
	    	    		m2    = ehr.getm2prime(DOSE_CHG_ANNOT, DOWN, mbr); // with drug change based on DB
	    	    		m2_   = ehr.getm2prime(DOSE_CHG_ANNOT, UP, mbr);   // with drug change based on DB
	    	    	//	m2__  = ehr.getm2prime(DRUG_CHG_ANNOT_DB, "", mbr);
	    	    	//	m2___ = ehr.getm2prime(DRUG_CHG_ANNOT_MS, "", mbr);    	    	
	    	    	}
	    	    	// M
	    	    	int M = 0; int M_= 0; //int M__ = 0; int M___ = 0;
	    	    	M = m1+m2;M_ = m1_+m2_;//M__ = m1__+m2__;M___ = m1___+m2___;
	    	    	
	    	    	// get sets of phenotypes
	    	    	ArrayList<String> doseDownPhenotype 	= new ArrayList<String>();
	    	    	ArrayList<String> doseUpPhenotype   	= new ArrayList<String>();
	    	    	//ArrayList<String> drugChangePhenotypeDb = new ArrayList<String>();
	    	    	//ArrayList<String> drugChangePhenotypeMs = new ArrayList<String>();
	    	    	ArrayList<String> allPhenotype 			= new ArrayList<String>();
	    	    	doseDownPhenotype.addAll(ehr.getCui(DOSE_CHG_ANNOT, DOWN, mbr));
	    	    	doseUpPhenotype.addAll(ehr.getCui(DOSE_CHG_ANNOT, UP, mbr));
	    	    	//drugChangePhenotypeDb.addAll(ehr.getCui(DRUG_CHG_ANNOT_DB, "", mbr));
	    	    	//drugChangePhenotypeMs.addAll(ehr.getCui(DRUG_CHG_ANNOT_MS, "", mbr));
	    	    	//1
	    	    	allPhenotype.addAll(doseDownPhenotype);
	    	    	//2
	    	    	doseUpPhenotype.removeAll(allPhenotype);
	    	    	allPhenotype.addAll(doseUpPhenotype);
	    	    	/*
	    	    	//3
	    	    	drugChangePhenotypeDb.removeAll(allPhenotype);
	    	    	allPhenotype.addAll(drugChangePhenotypeDb);
	    	    	//4
	    	    	drugChangePhenotypeMs.removeAll(allPhenotype);
	    	    	allPhenotype.addAll(drugChangePhenotypeMs);
	    	    	*/
	    	    	// allPhenotype.add("C0012634");
	    	    	
	    	    	// for each phenotype associated with the drug 
	    	    	System.out.println("\t for drug class "+c+", "+allPhenotype.size()+" phenotypes to test.");
	    	    	
	    	    	ehr.prepareN1AndN2Stt(mbr);
	    	    	
	    	    	for(String p:allPhenotype){    	    		   	    		
	    	    		    	    		
	    	    		int n2 = 0;
	    	    		if(REF.equals(DRUG_REF)){
	    	    			n2 = ehr.getn2(p);
	    	    		}
	    	    	
	    	    		// dose down phenotype
	    	    		if(doseDownPhenotype.contains(p)){    	    			    	    			    	    			
	    	    			
	    	    			int cuiNb = Integer.parseInt(p.substring(1)); 
	    	    			if(cuiNb>cuiToStartWith){
	    	    				//System.out.println(p);
		    	    			int n1 = 0;
		    	    			n1 = ehr.getn1(DOSE_CHG_ANNOT, DOWN, p);
		    	    			if(REF.equals(ALL)){
		        	    			n2 = ehr.getn2prime(DOSE_CHG_ANNOT, DOWN, mbr, p);
		        	    		}
		    	    			int N = 0;
		    	    			N = n1+n2;
		    	    			//System.out.println(p);
		    	    			//System.out.println(n1+" 	"+m1+" 	"+n2+" 	"+m2);
		    	    			// p-value
		    	    			HypergeometricDistribution dist = new HypergeometricDistribution(M, m1, N);
		    	    			double pvalue = dist.probability(n1);
		    	    			if(Double.isNaN(pvalue)){
		    	    				pvalue=0.0;
		    	    			}
		    	    			if(pvalue<=0.05){
		    	    				//information content
		    	    				double ic = 0.0;
		    	    				ic=ehr.getIcdIc(p);	    	    				
			    	    			// risk ratio
			    	    			double rr = 0.0;
			    	    			if(n2>0 && m2>0){
			    	    				rr = ((double)n1/(double)m1) / ((double)n2/(double)m2);
			    	    			}		    	    			
			    	    			//System.out.println(p+": "+rr+" 	"+pvalue);
			    	    	//		if(rr>=1){
			    	    				ehr.addPhenotype(DOSE_DOWN_PHENO, c, 0, p, pvalue, rr, ic);
			    	    	//		}
		    	    			}
		    	    		}
	    	    		}
	    	    		
	    	    		// dose up phenotype
	    	    		if(doseUpPhenotype.contains(p)){
	    	    			int n1_ = 0;
	    	    			n1_ = ehr.getn1(DOSE_CHG_ANNOT, UP, p);
	    	    			if(REF.equals(ALL)){
	        	    			n2 = ehr.getn2prime(DOSE_CHG_ANNOT, UP, mbr, p);
	        	    		}
	    	    			int N_ = 0;
	    	    			N_ = n1_+n2;
	    	    			// p-value
	    	    			HypergeometricDistribution dist = new HypergeometricDistribution(M_, m1_, N_);
	    	    			double pvalue = dist.probability(n1_);
	    	    			if(Double.isNaN(pvalue)){
	    	    				pvalue=0.0;
	    	    			}
	    	    			if(pvalue<=0.05){    	    				
	    	    				//information content
	    	    				double ic = 0.0;
	    	    				ic=ehr.getIcdIc(p);
		    	    			// risk ratio
		    	    			double rr = 0.0;
		    	    			if(n2>0 && m2>0){
		    	    				rr = ((double)n1_/(double)m1_) / ((double)n2/(double)m2);
		    	    			}
		    	    			//System.out.println(p+": "+rr+" 	"+pvalue);
		    	    		//	if(rr>1){
		    	    				ehr.addPhenotype(DOSE_UP_PHENO, c, 0, p, pvalue, rr, ic);
		    	    		//	}
	    	    			}
	    	    		}
	/*    	    		
	    	    		// drug change phenotype -db
	    	    		if(drugChangePhenotypeDb.contains(p)){
	    	    			int n1__ = 0;
	    	    			n1__ = ehr.getn1(DRUG_CHG_ANNOT_DB, "", p);
	    	    			if(REF.equals(ALL)){
	        	    			n2 = ehr.getn2prime(DRUG_CHG_ANNOT_DB, "", mbr, p);
	        	    		}
	    	    			int N__ = 0;
	    	    			N__ = n1__+n2;
	    	    			// p-value
	    	    			HypergeometricDistribution dist = new HypergeometricDistribution(M__, m1__, N__);
	    	    			double pvalue = dist.probability(n1__);
	    	    			if(Double.isNaN(pvalue)){
	    	    				pvalue=0.0;
	    	    			}
	    	    			if(pvalue<=0.05){
	    	    				//information content
		    	    			double ic = 0.0;
		    	    			ic=ehr.getIcdIc(p);
		    	    			// risk ratio
		    	    			double rr = 0.0;
		    	    			if(n2>0 && m2>0){
		    	    				rr = ((double)n1__/(double)m1__) / ((double)n2/(double)m2);
		    	    			}
		    	    			// System.out.println(p+": "+rr+" 	"+pvalue);
		    	    			if(rr>1){
		    	    				ehr.addPhenotype(DRUG_CHG_PHENO_DB, c, 0, p, pvalue, rr, ic);
		    	    			}
		    	    		}
	    	    		}
	    	    		// drug change phenotype -ms
	    	    		if(drugChangePhenotypeMs.contains(p)){
	    	    			int n1___ = 0;
	    	    			n1___ = ehr.getn1(DRUG_CHG_ANNOT_MS, "", p);
	    	    			if(REF.equals(ALL)){
	        	    			n2 = ehr.getn2prime(DRUG_CHG_ANNOT_MS, "", mbr, p);
	        	    		}
	    	    			int N___ = 0;
	    	    			N___ = n1___+n2;
	    	    			// p-value
	    	    			HypergeometricDistribution dist = new HypergeometricDistribution(M___, m1___, N___);
	    	    			double pvalue = dist.probability(n1___);
	    	    			if(Double.isNaN(pvalue)){
	    	    				pvalue=0.0;
	    	    			}
	    	    			if(pvalue<=0.05){
	    	    				//information content
		    	    			double ic = 0.0;
		    	    			ic=ehr.getIcdIc(p);
		    	    			// risk ratio
		    	    			double rr = 0.0;
		    	    			if(n2>0 && m2>0){
		    	    				rr = ((double)n1___/(double)m1___) / ((double)n2/(double)m2);
		    	    			}
		    	    			// System.out.println(p+": "+rr+" 	"+pvalue);
		    	    			if(rr>1){
		    	    				ehr.addPhenotype(DRUG_CHG_PHENO_MS, c, 0, p, pvalue, rr, ic);
		    	    			}
		    	    		}
	    	    		}
	*/    	    		
		    		}
	    	    	ehr.closeN1AndN2PreparedStt();
	    		}
		    }
    	}
	    
		// ============================================================
	    // ===============================END==========================
		// ============================================================
	    ehr.closeSst();
		Date end = new Date();
	    System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	}

}
