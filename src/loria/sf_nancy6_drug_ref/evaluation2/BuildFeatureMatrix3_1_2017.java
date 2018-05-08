package loria.sf_nancy6_drug_ref.evaluation2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * This class enable to build a feature matrix, 
 * where lines are intervals during which patients experienced continuation or dose|drug change
 * and columnes are phenotypes experience between the drug change or before the drug change. 
 * @author x
 * 
 * args are 
 * String singleDrug {single or class}
 * String singleMatrix {single or three}
 * String order by {pvalue, rr, ic}
 * String upOrDown {up,down}
 * int nbrOfFeature ex: 10, 100, etc.
 * 	
 */

public class BuildFeatureMatrix3_1_2017 {

	static String TRAIN_YEAR 			  	  = "2008_2013"; //2008_2013
	static String TEST_YEAR 			  	  = "2014"; //2008_2013

	static String DOSE_UP 			 = "up";
	static String DOSE_DOWN 		 = "down";
	static String CONTI 		     = "conti";

	static String EHR_BASE 			 = "stride6";
	static String CSV				 = "csv";
	static String ARFF				 = "arff";
	static String BOTH				 = "both";
	
	static String SINGLE 			 = "single";
	static String CLASS				 = "class";
	
	static String ONTO1 			 = "SNOMEDCT_US";
	static String ONTO2				 = "ICD9CM";
	
	static String PVALUE			 = "pvalue";
	static String RR			 	 = "rr";
	static String IC			 	 = "ic";
		
	static FileWriter out1, out2, out3; static FileWriter outCsv1, outCsv2, outCsv3;
	
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		Date start = new Date();
		// arguments
		String singleDrug 			 = args[0]; //single or class
		String singleMatrix          = args[1]; //single or three : 
												//for one matrix merging phenotype, icd && lab or
												//one matrix for each
		String orderBy				 = args[2]; // pvalue, rr or ic
		String upOrDown				 = args[3]; // up, down
		
		int nbrOfFeatures		     = Integer.parseInt(args[4]); // 10, 100...
		
		String tableSuffix           = args[5]; // "no_exp", "all",  "all_p1_only", "all_p1_and_p2", "all_p2"	
		
		boolean writeId              = false; // true if one wants to write ids in the output file
		
		String intervalClass1Table 	 = "user_x.dose_change_"+TEST_YEAR+"_m";
		String intervalClass2Table 	 = "user_x.continuation_"+TEST_YEAR+"_m";
		
		String class11AnnotationTable = "user_x.dose_change_"+TEST_YEAR+"_ante_annotation_m";
		String class12AnnotationTable = "user_x.continuation_"+TEST_YEAR+"_ante_annotation_m";
		String class21AnnotationTable = "user_x.dose_change_"+TEST_YEAR+"_ante_icd_m";
		String class22AnnotationTable = "user_x.continuation_"+TEST_YEAR+"_ante_icd_m";
		String class31AnnotationTable = "user_x.dose_change_"+TEST_YEAR+"_ante_lab_m";
		String class32AnnotationTable = "user_x.continuation_"+TEST_YEAR+"_ante_lab_m";
		
		String phenotypeProfileDownTable = "user_x.dose_down_"+TRAIN_YEAR+"_ante_phenotype_p450_"+tableSuffix;
		String icdProfileDownTable       = "user_x.dose_down_"+TRAIN_YEAR+"_ante_icd_p450_"+tableSuffix;
		String labProfileDownTable       = "user_x.dose_down_"+TRAIN_YEAR+"_ante_lab_p450_"+tableSuffix;// no alexia reduction for labs data, beacuase no ontology
		String phenotypeProfileUpTable   = "user_x.dose_up_"+TRAIN_YEAR+"_ante_phenotype_p450_"+tableSuffix;
		String icdProfileUpTable         = "user_x.dose_up_"+TRAIN_YEAR+"_ante_icd_p450_"+tableSuffix;
		String labProfileUpTable         = "user_x.dose_up_"+TRAIN_YEAR+"_ante_lab_p450_"+tableSuffix;// no alexia reduction for labs data, beacuase no ontology
		
		String outputFormat          = ARFF;
		boolean antePrescription     = false;
		Stride6Api6 ehr = null;
		if(upOrDown.equals(DOSE_DOWN)){
			ehr = new Stride6Api6(EHR_BASE, 
					intervalClass1Table, intervalClass2Table, 
					class11AnnotationTable, class12AnnotationTable,
					class21AnnotationTable, class22AnnotationTable,
					class31AnnotationTable, class32AnnotationTable,
					phenotypeProfileDownTable, icdProfileDownTable, labProfileDownTable, DOSE_DOWN, null);
		}else{
			ehr = new Stride6Api6(EHR_BASE, 
					intervalClass1Table, intervalClass2Table, 
					class11AnnotationTable, class12AnnotationTable,
					class21AnnotationTable, class22AnnotationTable,
					class31AnnotationTable, class32AnnotationTable,
					phenotypeProfileUpTable, icdProfileUpTable, labProfileUpTable, DOSE_UP, null);
		}
		

		// build the feature matrix for the following drug classes or single drug
    	ArrayList<String> allDrugClass  = new ArrayList<String>();
    	if(singleDrug.equals(CLASS)){
    		if(upOrDown.equals(DOSE_DOWN)){
    			allDrugClass.addAll(ehr.getDrugClass(phenotypeProfileDownTable));
    		}else if(upOrDown.equals(DOSE_UP)){
    			allDrugClass.addAll(ehr.getDrugClass(phenotypeProfileUpTable));
    		}
    	}else if(singleDrug.equals(SINGLE)){
    		if(upOrDown.equals(DOSE_DOWN)){
    			allDrugClass.addAll(ehr.getSingleDrug(phenotypeProfileDownTable));
    		}else if(upOrDown.equals(DOSE_UP)){
    			allDrugClass.addAll(ehr.getSingleDrug(phenotypeProfileUpTable));
    		}
    	}
    	    	
    	for(String drugOrDrugClass: allDrugClass){
    		//if(drugOrDrugClass.equals(selection)){
	    		String filePrefix1 = "";
	    		String filePrefix2 = "";
	    		String filePrefix3 = "";
	    		if(singleMatrix.equals("single")){
	    			filePrefix1 = "./data/"+TEST_YEAR+"/featureMatrix_"+upOrDown+"_"+drugOrDrugClass+"_unique_"+orderBy+"_"+nbrOfFeatures;
	    			
	    			//preparation of the output		
	        		if(outputFormat.equals(ARFF) || outputFormat.equals(BOTH)){
	        			out1 = new FileWriter(new File(filePrefix1+".arff"));
	        		}
	        		if(outputFormat.equals(CSV) || outputFormat.equals(BOTH)){
	        			outCsv1 = new FileWriter(new File(filePrefix1+".csv"));
	        		}
	    		}else if(singleMatrix.equals("three")){
	    			filePrefix1 = "./data/"+TEST_YEAR+"/featureMatrix_"+upOrDown+"_"+drugOrDrugClass+"_phenotype_"+orderBy+"_"+nbrOfFeatures;
	    			filePrefix2 = "./data/"+TEST_YEAR+"/featureMatrix_"+upOrDown+"_"+drugOrDrugClass+"_icd_"+orderBy+"_"+nbrOfFeatures;
	    			filePrefix3 = "./data/"+TEST_YEAR+"/featureMatrix_"+upOrDown+"_"+drugOrDrugClass+"_lab_"+orderBy+"_"+nbrOfFeatures;
	    			//preparation of the output		
	        		if(outputFormat.equals(ARFF) || outputFormat.equals(BOTH)){
	        			out1 = new FileWriter(new File(filePrefix1+".arff"));
	        			out2 = new FileWriter(new File(filePrefix2+".arff"));
	        			out3 = new FileWriter(new File(filePrefix3+".arff"));	        			
	        		}
	        		if(outputFormat.equals(CSV) || outputFormat.equals(BOTH)){
	        			outCsv1 = new FileWriter(new File(filePrefix1+".csv"));
	        			outCsv2 = new FileWriter(new File(filePrefix2+".csv"));
	        			outCsv3 = new FileWriter(new File(filePrefix3+".csv"));
	        		}
	    		}
	    		
	    		
				// consider class of drugs
				ArrayList<Integer> mbr = new ArrayList<Integer>(); //rxcui
				if(singleDrug.equals(CLASS)){
					//all p450
					if(drugOrDrugClass.equals("p450")){
						mbr = ehr.getP450DrugClassMember(drugOrDrugClass);
					}
					//drug role
					if(drugOrDrugClass.equals("substrate") || drugOrDrugClass.equals("inhibitor") || drugOrDrugClass.equals("inducer")){
						mbr = ehr.getP450DrugClassMemberByRole(drugOrDrugClass);
					}
					// ATC level 1
					if(drugOrDrugClass.length()==1){
						mbr = ehr.getP450DrugClassMemberByAtcLevel(drugOrDrugClass);
					}
					// genes
					if(drugOrDrugClass.length()==3 || (drugOrDrugClass.length()==4 && !drugOrDrugClass.equals("p450"))){
						mbr = ehr.getP450DrugClassMemberByGene(drugOrDrugClass);
					}
					// drug role x ATC level1
					if(drugOrDrugClass.contains("_")){
						mbr = ehr.getP450DrugClassMemberByRoleXAtc(drugOrDrugClass);
					}
		    	}else if(singleDrug.equals(SINGLE)){
					// case of a single drug
					mbr.add(Integer.parseInt(drugOrDrugClass)); //rxcui
				}
				System.out.println("nbre of drugs in "+drugOrDrugClass+" class : "+mbr.size());
				
				// get features of the matrix, i.e., phenotype representatives of the drug class (and of its dose|drug changes)
				ArrayList<String> allFeatures = new ArrayList<String>(); //cuis
				ArrayList<String> features1 = new ArrayList<String>(); //cuis
				ArrayList<String> features2 = new ArrayList<String>(); //cuis
				ArrayList<String> features3 = new ArrayList<String>(); //lab component ids
				if(upOrDown.equals(DOSE_DOWN)){
					features1.addAll(ehr.getPhenotypeProfile2(phenotypeProfileDownTable, drugOrDrugClass, singleDrug, nbrOfFeatures, orderBy));
					features2.addAll(ehr.getPhenotypeProfile2(icdProfileDownTable, drugOrDrugClass, singleDrug, nbrOfFeatures, orderBy));
					features3.addAll(ehr.getPhenotypeProfile2(labProfileDownTable, drugOrDrugClass, singleDrug, nbrOfFeatures, orderBy));
				}else if(upOrDown.equals(DOSE_UP)){
					features1.addAll(ehr.getPhenotypeProfile2(phenotypeProfileUpTable, drugOrDrugClass, singleDrug, nbrOfFeatures, orderBy));
					features2.addAll(ehr.getPhenotypeProfile2(icdProfileUpTable, drugOrDrugClass, singleDrug, nbrOfFeatures, orderBy));
					features3.addAll(ehr.getPhenotypeProfile2(labProfileUpTable, drugOrDrugClass, singleDrug, nbrOfFeatures, orderBy));
				}
				System.out.println("	nbre of phenotype features of the matrix: "+features1.size());
				System.out.println("	nbre of icd features of the matrix: "+features2.size());
				System.out.println("	nbre of lab features of the matrix: "+features3.size());	
				
				// get the intent of each feature 
				HashMap<String, ArrayList<String>> featureIntent1 = new HashMap<String, ArrayList<String>>();
				HashMap<String, ArrayList<String>> featureIntent2 = new HashMap<String, ArrayList<String>>();
				HashMap<String, ArrayList<String>> featureIntent3 = new HashMap<String, ArrayList<String>>();
				HashMap<String, ArrayList<String>> allFeatureIntent = new HashMap<String, ArrayList<String>>();
				int intentSize1 = 0;
				int intentSize2 = 0;
				int intentSize3 = 0;
	
				for(String f:features1){
					//ArrayList<String> localIds = new ArrayList<String>();
					ArrayList<String> intent   = new ArrayList<String>();
		    		intent.add(f);    		
		    		intent.addAll(ehr.getAllDescendants(ONTO1, f));
					intentSize1+=intent.size();
					featureIntent1.put(f, intent);
				}
				System.out.println("	size of features1, including intent : "+intentSize1);
				for(String f:features2){
					ArrayList<String> localIds= new ArrayList<String>();
					ArrayList<String> intent= new ArrayList<String>();
		    		intent.add(f);    		
		    		intent.addAll(ehr.getAllDescendants(ONTO2, f));
					intentSize2+=intent.size();
					featureIntent2.put("icd_"+f, intent);
				}
				
				for(String f:features3){
					ArrayList<String> localIds= new ArrayList<String>();
					ArrayList<String> intent= new ArrayList<String>();
		    		intent.add(f);    		
		    		// intent.addAll(ehr.getAllDescendants(ONTO2, f));
					intentSize3+=intent.size();
					featureIntent3.put(f, intent);
				}
				System.out.println("	size of features3, including intent : "+intentSize3);	
				
				//get ids of intervals
				ArrayList<String> intervals  = new ArrayList<String>(); //iid class 1 & 2 
				ArrayList<String> intervals1 = new ArrayList<String>(); //iid class 1 => 1 in arff file
				ArrayList<String> intervals2 = new ArrayList<String>(); //iid class 2 => 0 in arff file
				int limit1 = 0;		
				if(upOrDown.equals(DOSE_DOWN)){
					intervals1.addAll(ehr.getIntervals(intervalClass1Table, mbr, DOSE_DOWN));
				}else if(upOrDown.equals(DOSE_UP))	{
					intervals1.addAll(ehr.getIntervals(intervalClass1Table, mbr, DOSE_UP));
				}
				int limit2 = intervals.size(); // to balance the size of classes 
				intervals2.addAll(ehr.getIntervals(intervalClass2Table, mbr, CONTI));
				
				if(intervals2.size()>intervals1.size()){
					//then we limit the class2 example to the size of class 1
					intervals.addAll(intervals1);
					Collections.shuffle(intervals2);
					intervals.addAll(intervals2.subList(0, intervals1.size()));
				}else if(intervals2.size()<intervals1.size()){
					// inverse case
					Collections.shuffle(intervals1);
					intervals.addAll(intervals1.subList(0, intervals2.size()));
					intervals.addAll(intervals2);
				}else if(intervals2.size()==intervals1.size()){
					// then we add the whole
					intervals.addAll(intervals1);
					intervals.addAll(intervals2);			
				}				
				
				System.out.println("	nbre of instances of the matrix: "+intervals.size());
				// write headers of the output file(s)
				if(singleMatrix.equals("single")){
					allFeatures.addAll(features1);
					for(String f:features2){
						allFeatures.add("icd_"+f);
					}
					allFeatures.addAll(features3);
					writeHeaders(outputFormat, out1, intervals, allFeatures, writeId, "");
				}else if(singleMatrix.equals("three")){
					writeHeaders(outputFormat, out1, intervals, features1, writeId, "");
					writeHeaders(outputFormat, out2, intervals, features2, writeId, "icd_");
					writeHeaders(outputFormat, out3, intervals, features3, writeId, "");
				}
					
				for(String i:intervals){
					ArrayList<String> phenotype = new ArrayList<String>();//phenotype that annotate the this interval
					ArrayList<String> icd = new ArrayList<String>();//icd diag during this interval
					ArrayList<String> lab = new ArrayList<String>();//lab results during this interval
					phenotype.addAll(ehr.getPhenotypeAnnotations(antePrescription, i));
					icd.addAll(ehr.getIcds(antePrescription, i));
					lab.addAll(ehr.getLabs(antePrescription, i));
					//phenotype.retainAll(features);//intersection with features
			
					if(singleMatrix.equals("single")){
						allFeatureIntent.putAll(featureIntent1);
						allFeatureIntent.putAll(featureIntent2);
						allFeatureIntent.putAll(featureIntent3);
						ArrayList<String> phenotypePlusIcdPlusLab = new ArrayList<String>();
						phenotypePlusIcdPlusLab.addAll(phenotype);
						phenotypePlusIcdPlusLab.addAll(icd);
						phenotypePlusIcdPlusLab.addAll(lab);
						writeLine(outputFormat, out1, i, allFeatureIntent, phenotypePlusIcdPlusLab, writeId);
					}else if(singleMatrix.equals("three")){
						writeLine(outputFormat, out1, i, featureIntent1, phenotype, writeId);
						writeLine(outputFormat, out2, i, featureIntent2, icd, writeId);
						writeLine(outputFormat, out3, i, featureIntent3, lab, writeId);
					}
				}
				
				
				if(outputFormat.equals(ARFF) || outputFormat.equals(BOTH)){
					if(singleMatrix.equals("single")){
						out1.close();
					}else if(singleMatrix.equals("three")){
						out1.close();
						out2.close();
						out3.close();
					}
				}
				if(outputFormat.equals(CSV) || outputFormat.equals(BOTH)){
					if(singleMatrix.equals("single")){
						outCsv1.close();
					}else if(singleMatrix.equals("three")){
						outCsv1.close();outCsv2.close();outCsv3.close();
					}
				}
    		//}// selection
    	}
    	// ============================================================================
		Date end = new Date();
	    System.out.println(end.getTime() - start.getTime() + " total milliseconds");
	}

	private static void writeLine(String outputFormat, FileWriter out, String i, HashMap<String, ArrayList<String>> featureIntent, ArrayList<String> phenotype, boolean writeId) throws IOException {
		if(outputFormat.equals(ARFF) || outputFormat.equals(BOTH)){
			if(writeId){
				out.write(i+","); // id of the interval
			}
			for(String f:featureIntent.keySet()){
				ArrayList<String> inter = new ArrayList<String>();
				inter.addAll(featureIntent.get(f));
				inter.retainAll(phenotype);				
				if(inter.size()>0){
					out.write("1,");
				}else{
					out.write("0,");
				}
			}
			if(i.startsWith("d")){
				out.write("1\n");
			}else if(i.startsWith("c")){
				out.write("0\n");
			}
			//out1.flush();
		}
		if(outputFormat.equals(CSV) || outputFormat.equals(BOTH)){
			
		}
		
	}

	private static void writeHeaders(String outputFormat, FileWriter out,
			ArrayList<String> intervals, ArrayList<String> features, boolean writeId, String prefix) throws IOException {
		
		if(outputFormat.equals(ARFF) || outputFormat.equals(BOTH)){
			out.write("@relation x\n\n");
			if(writeId){
				out.write("@attribute id {");
				int c=0;
				int cMax=intervals.size();
				for(String i: intervals){
					c++;
					out.write(prefix+i);
					//if not the last element
					if(c<cMax){
						out.write(",");
					}
				}
				out.write("}\n");		
			}
			for(String f: features){
				out.write("@attribute "+f+" {0,1}\n");				
			}
			out.write("@attribute class {0,1}\n\n");
			out.write("@data\n");
			out.flush();
			
		}
		if(outputFormat.equals(CSV) || outputFormat.equals(BOTH)){
			//TODO
		}		
	}	
}
