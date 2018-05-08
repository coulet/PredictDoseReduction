package loria.sf_nancy6_drug_ref;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class p450JsonGenerator2 {

	static String EHR_BASE 			 = "";
	
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
		
		FileWriter p450 = new FileWriter(new File("./data/p450.json"));//new File("./data/p450.json")
		Stride6Api6 ehr = new Stride6Api6(EHR_BASE);
		String down_experiment_file="./data/20161017_experiment/20161017_100_300_down_classification_10_folds.csv";
		String up_experiment_file="./data/20161017_experiment/20161017_100_300_up_classification_10_folds.csv";
		DataGraber2 c125 = new DataGraber2(down_experiment_file, up_experiment_file);
		
		p450.write("{\n");
		p450.write("\t\"name\":\"P450-drug\",\"id\":\"p450\", \"size\":\"205\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("p450")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("p450")+"\", "
						+ "\"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("p450")+"\", \"down_pheno_f\" : \""+c125.getDownPhenoF("p450")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("p450")+"\", "
						+ "\"down_icd_acc\" : \""+c125.getDownIcdAccuracy("p450")+"\", \"down_icd_f\" : \""+c125.getDownIcdF("p450")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("p450")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("p450")+"\", \"down_lab_f\" : \""+c125.getDownLabF("p450")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("p450")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("p450")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("p450")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("p450")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("p450")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("p450")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("p450")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("p450")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("p450")+"\", \"up_lab_f\" : \""+c125.getUpLabF("p450")+"\","
				+ "\n");
		p450.write("\t\"children\": [\n");
		//==========================================================================================
		//==========================================================================================
		//==========================================================================================
		//==========================================================================================
		p450.write("\t{\n");
		p450.write("\t\t\"name\": \"by ATC level-1 class\", \"id\":\"p450_2\",\"size\":\"205\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("p450")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("p450")+"\", "
						+ "\"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("p450")+"\", \"down_pheno_f\" : \""+c125.getDownPhenoF("p450")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("p450")+"\", "
						+ "\"down_icd_acc\" : \""+c125.getDownIcdAccuracy("p450")+"\", \"down_icd_f\" : \""+c125.getDownIcdF("p450")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("p450")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("p450")+"\", \"down_lab_f\" : \""+c125.getDownLabF("p450")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("p450")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("p450")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("p450")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("p450")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("p450")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("p450")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("p450")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("p450")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("p450")+"\", \"up_lab_f\" : \""+c125.getUpLabF("p450")+"\","
				+ "\n");
		ArrayList<String> classMembers = new ArrayList<String>();
		p450.write("\t\t\"children\": [\n");
		//==========================================================================================		
		classMembers = ehr.getATCClassMembers("A");
		int i=0;
		int l=classMembers.size();
		p450.write("\t\t{\"name\": \"A ALIMENTARY TRACT AND METABOLISM\", \"id\":\"A\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("A")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("A")+"\", "
						+ " \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("A")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("A")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("A")+"\", "
						+ " \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("A")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("A")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("A")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("A")+"\", \"down_lab_f\" : \""+c125.getDownLabF("A")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("A")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("A")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("A")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("A")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("A")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("A")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("A")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("A")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("A")+"\", \"up_lab_f\" : \""+c125.getUpLabF("A")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
					+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\", "
							+ " \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\", "
							+ " \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("B");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"B BLOOD AND BLOOD FORMING ORGANS\", \"id\":\"B\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("B")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("B")+"\", "
						+ " \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("B")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("B")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("B")+"\", "
						+ " \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("B")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("B")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("B")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("B")+"\", \"down_lab_f\" : \""+c125.getDownLabF("B")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("B")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("B")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("B")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("B")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("B")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("B")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("B")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("B")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("B")+"\", \"up_lab_f\" : \""+c125.getUpLabF("B")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
					+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("C");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"C CARDIOVASCULAR SYSTEM\", \"id\":\"C\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("C")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("C")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("C")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("C")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("C")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("C")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("C")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("C")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("C")+"\", \"down_lab_f\" : \""+c125.getDownLabF("C")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("C")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("C")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("C")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("C")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("C")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("C")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("C")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("C")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("C")+"\", \"up_lab_f\" : \""+c125.getUpLabF("C")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
					+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("D");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"D DERMATOLOGICALS\", \"id\":\"D\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("D")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("D")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("D")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("D")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("D")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("D")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("D")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("D")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("D")+"\", \"down_lab_f\" : \""+c125.getDownLabF("D")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("D")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("D")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("D")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("D")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("D")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("D")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("D")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("D")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("D")+"\", \"up_lab_f\" : \""+c125.getUpLabF("D")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
					+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("G");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"G GENITO URINARY SYSTEM AND SEX HORMONES\", \"id\":\"G\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("G")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("G")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("G")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("G")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("G")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("G")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("G")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("G")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("G")+"\", \"down_lab_f\" : \""+c125.getDownLabF("G")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("G")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("G")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("G")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("G")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("G")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("G")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("G")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("G")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("G")+"\", \"up_lab_f\" : \""+c125.getUpLabF("G")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			//System.out.println(d+"\", "+rxcui);
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
					+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("H");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"H SYSTEMIC HORMONAL PREPARATIONS\", \"id\":\"H\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("H")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("H")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("H")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("H")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("H")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("H")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("H")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("H")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("H")+"\", \"down_lab_f\" : \""+c125.getDownLabF("H")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("H")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("H")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("H")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("H")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("H")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("H")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("H")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("H")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("H")+"\", \"up_lab_f\" : \""+c125.getUpLabF("H")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("J");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"J ANTIINFECTIVES FOR SYSTEMIC USE\", \"id\":\"J\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("J")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("J")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("J")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("J")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("J")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("J")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("J")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("J")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("J")+"\", \"down_lab_f\" : \""+c125.getDownLabF("J")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("J")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("J")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("J")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("J")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("J")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("J")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("J")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("J")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("J")+"\", \"up_lab_f\" : \""+c125.getUpLabF("J")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("L");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"L ANTINEOPLASTIC AND IMMUNOMODULATING AGENTS\", \"id\":\"L\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("L")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("L")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("L")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("L")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("L")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("L")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("L")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("L")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("L")+"\", \"down_lab_f\" : \""+c125.getDownLabF("L")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("L")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("L")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("L")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("L")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("L")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("L")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("L")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("L")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("L")+"\", \"up_lab_f\" : \""+c125.getUpLabF("L")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("M");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"M MUSCULO-SKELETAL SYSTEM\", \"id\":\"M\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("M")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("M")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("M")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("M")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("M")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("M")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("M")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("M")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("M")+"\", \"down_lab_f\" : \""+c125.getDownLabF("M")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("M")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("M")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("M")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("M")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("M")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("M")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("M")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("M")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("M")+"\", \"up_lab_f\" : \""+c125.getUpLabF("M")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("N");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"N NERVOUS SYSTEM\", \"id\":\"N\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("N")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("N")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("N")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("N")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("N")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("N")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("N")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("N")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("N")+"\", \"down_lab_f\" : \""+c125.getDownLabF("N")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("N")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("N")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("N")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("N")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("N")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("N")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("N")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("N")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("N")+"\", \"up_lab_f\" : \""+c125.getUpLabF("N")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("P");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"P ANTIPARASITIC PRODUCTS, INSECTICIDES AND REPELLENTS\", \"id\":\"P\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("P")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("P")+"\", "
						+ " \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("P")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("P")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("P")+"\", "
						+ " \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("P")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("P")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("P")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("P")+"\", \"down_lab_f\" : \""+c125.getDownLabF("P")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("P")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("P")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("P")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("P")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("P")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("P")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("P")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("P")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("P")+"\", \"up_lab_f\" : \""+c125.getUpLabF("P")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\", "
							+ " \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\", "
							+ " \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================
		classMembers = ehr.getATCClassMembers("R");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"R RESPIRATORY SYSTEM\", \"id\":\"R\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("R")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("R")+"\", "
						+ " \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("R")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("R")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("R")+"\", "
						+ " \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("R")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("R")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("R")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("R")+"\", \"down_lab_f\" : \""+c125.getDownLabF("R")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("R")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("R")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("R")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("R")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("R")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("R")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("R")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("R")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("R")+"\", \"up_lab_f\" : \""+c125.getUpLabF("R")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		for(String d:classMembers){
			i++;
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t}\n");
		//==========================================================================================
		p450.write("\t]\n");
		p450.write("\t},\n");
		//==========================================================================================
		//==========================================================================================
		//==========================================================================================
		p450.write("\t{\n");
		p450.write("\t\t\"name\": \"by P450 enzyme\", \"id\":\"p450_3\", \"size\":\"205\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("p450")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("p450")+"\", "
						+ "\"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("p450")+"\", \"down_pheno_f\" : \""+c125.getDownPhenoF("p450")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("p450")+"\", "
						+ "\"down_icd_acc\" : \""+c125.getDownIcdAccuracy("p450")+"\", \"down_icd_f\" : \""+c125.getDownIcdF("p450")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("p450")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("p450")+"\", \"down_lab_f\" : \""+c125.getDownLabF("p450")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("p450")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("p450")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("p450")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("p450")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("p450")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("p450")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("p450")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("p450")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("p450")+"\", \"up_lab_f\" : \""+c125.getUpLabF("p450")+"\","
				+ "\n");
		classMembers = new ArrayList<String>();
		p450.write("\t\t\"children\": [\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("1A2");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"1A2\", \"id\":\"1A2\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("1A2")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("1A2")+"\", "
						+ " \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("1A2")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("1A2")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("1A2")+"\", "
						+ " \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("1A2")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("1A2")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("1A2")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("1A2")+"\", \"down_lab_f\" : \""+c125.getDownLabF("1A2")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("1A2")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("1A2")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("1A2")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("1A2")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("1A2")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("1A2")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("1A2")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("1A2")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("1A2")+"\", \"up_lab_f\" : \""+c125.getUpLabF("1A2")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("2B6");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"2B6\", \"id\":\"2B6\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("2B6")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("2B6")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("2B6")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("2B6")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("2B6")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("2B6")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("2B6")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("2B6")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("2B6")+"\", \"down_lab_f\" : \""+c125.getDownLabF("2B6")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("2B6")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("2B6")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("2B6")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("2B6")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("2B6")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("2B6")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("2B6")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("2B6")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("2B6")+"\", \"up_lab_f\" : \""+c125.getUpLabF("2B6")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("2C19");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"2C19\", \"id\":\"2C19\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("2C19")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("2C19")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("2C19")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("2C19")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("2C19")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("2C19")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("2C19")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("2C19")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("2C19")+"\", \"down_lab_f\" : \""+c125.getDownLabF("2C19")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("2C19")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("2C19")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("2C19")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("2C19")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("2C19")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("2C19")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("2C19")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("2C19")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("2C19")+"\", \"up_lab_f\" : \""+c125.getUpLabF("2C19")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("2C8");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"2C8\", \"id\":\"2C8\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("2C8")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("2C8")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("2C8")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("2C8")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("2C8")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("2C8")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("2C8")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("2C8")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("2C8")+"\", \"down_lab_f\" : \""+c125.getDownLabF("2C8")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("2C8")+"\", "		
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("2C8")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("2C8")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("2C8")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("2C8")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("2C8")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("2C8")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("2C8")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("2C8")+"\", \"up_lab_f\" : \""+c125.getUpLabF("2C8")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("2C9");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"2C9\", \"id\":\"2C9\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("2C9")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("2C9")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("2C9")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("2C9")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("2C9")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("2C9")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("2C9")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("2C9")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("2C9")+"\", \"down_lab_f\" : \""+c125.getDownLabF("2C9")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("2C9")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("2C9")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("2C9")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("2C9")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("2C9")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("2C9")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("2C9")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("2C9")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("2C9")+"\", \"up_lab_f\" : \""+c125.getUpLabF("2C9")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("2D6");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"2D6\", \"id\":\"2D6\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("2D6")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("2D6")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("2D6")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("2D6")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("2D6")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("2D6")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("2D6")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("2D6")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("2D6")+"\", \"down_lab_f\" : \""+c125.getDownLabF("2D6")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("2D6")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("2D6")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("2D6")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("2D6")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("2D6")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("2D6")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("2D6")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("2D6")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("2D6")+"\", \"up_lab_f\" : \""+c125.getUpLabF("2D6")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("2E1");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"2E1\", \"id\":\"2E1\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("2E1")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("2E1")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("2E1")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("2E1")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("2E1")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("2E1")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("2E1")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("2E1")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("2E1")+"\", \"down_lab_f\" : \""+c125.getDownLabF("2E1")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("2E1")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("2E1")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("2E1")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("2E1")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("2E1")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("2E1")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("2E1")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("2E1")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("2E1")+"\", \"up_lab_f\" : \""+c125.getUpLabF("2E1")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("3A4");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"3A4\", \"id\":\"3A4\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("3A4")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("3A4")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("3A4")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("3A4")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("3A4")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("3A4")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("3A4")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("3A4")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("3A4")+"\", \"down_lab_f\" : \""+c125.getDownLabF("3A4")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("3A4")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("3A4")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("3A4")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("3A4")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("3A4")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("3A4")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("3A4")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("3A4")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("3A4")+"\", \"up_lab_f\" : \""+c125.getUpLabF("3A4")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("3A5");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"3A5\", \"id\":\"3A5\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("3A5")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("3A5")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("3A5")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("3A5")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("3A5")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("3A5")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("3A5")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("3A5")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("3A5")+"\", \"down_lab_f\" : \""+c125.getDownLabF("3A5")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("3A5")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("3A5")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("3A5")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("3A5")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("3A5")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("3A5")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("3A5")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("3A5")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("3A5")+"\", \"up_lab_f\" : \""+c125.getUpLabF("3A5")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getEnzymeClassMembers("3A7");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"3A7\", \"id\":\"3A7\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("3A7")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("3A7")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("3A7")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("3A7")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("3A7")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("3A7")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("3A7")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("3A7")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("3A7")+"\", \"down_lab_f\" : \""+c125.getDownLabF("3A7")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("3A7")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("3A7")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("3A7")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("3A7")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("3A7")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("3A7")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("3A7")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("3A7")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("3A7")+"\", \"up_lab_f\" : \""+c125.getUpLabF("3A7")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t}\n");
		//==========================================================================================		
		p450.write("\t]\n");
		p450.write("\t},\n");
		//==========================================================================================
		//==========================================================================================
		//==========================================================================================
		//==========================================================================================
		p450.write("\t{\n");
		p450.write("\t\t\"name\": \"by relation type\", \"id\":\"p450_4\",\"size\":\"205\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("p450")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("p450")+"\", "
						+ "\"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("p450")+"\", \"down_pheno_f\" : \""+c125.getDownPhenoF("p450")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("p450")+"\", "
						+ "\"down_icd_acc\" : \""+c125.getDownIcdAccuracy("p450")+"\", \"down_icd_f\" : \""+c125.getDownIcdF("p450")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("p450")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("p450")+"\", \"down_lab_f\" : \""+c125.getDownLabF("p450")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("p450")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("p450")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("p450")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("p450")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("p450")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("p450")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("p450")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("p450")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("p450")+"\", \"up_lab_f\" : \""+c125.getUpLabF("p450")+"\","
				+ "\n");
		classMembers = new ArrayList<String>();
		p450.write("\t\t\"children\": [\n");
		//==========================================================================================		
		classMembers = ehr.getRelationClassMembers("substrate");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"P450 substrate\", \"id\":\"substrate\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("substrate")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("substrate")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("substrate")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("substrate")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("substrate")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("substrate")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("substrate")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("substrate")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("substrate")+"\", \"down_lab_f\" : \""+c125.getDownLabF("substrate")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("substrate")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("substrate")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("substrate")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("substrate")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("substrate")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("substrate")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("substrate")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("substrate")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("substrate")+"\", \"up_lab_f\" : \""+c125.getUpLabF("substrate")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getRelationClassMembers("inhibitor");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"P450 inhibitor\", \"id\":\"inhibitor\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("inhibitor")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("inhibitor")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("inhibitor")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("inhibitor")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("inhibitor")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("inhibitor")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("inhibitor")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("inhibitor")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("inhibitor")+"\", \"down_lab_f\" : \""+c125.getDownLabF("inhibitor")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("inhibitor")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("inhibitor")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("inhibitor")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("inhibitor")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("inhibitor")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("inhibitor")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("inhibitor")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("inhibitor")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("inhibitor")+"\", \"up_lab_f\" : \""+c125.getUpLabF("inhibitor")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""							
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}
		p450.write("\t\t\t]\n");
		p450.write("\t\t},\n");
		//==========================================================================================		
		classMembers = ehr.getRelationClassMembers("inducer");
		i=0;
		l=classMembers.size();
		p450.write("\t\t{\"name\": \"P450 inducer\", \"id\":\"inducer\", \"size\": \""+l+"\","
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize("inducer")+"\", "
				+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc("inducer")+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy("inducer")+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF("inducer")+"\","
				+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc("inducer")+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy("inducer")+"\",  \"down_icd_f\" : \""+c125.getDownIcdF("inducer")+"\","
				+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc("inducer")+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy("inducer")+"\", \"down_lab_f\" : \""+c125.getDownLabF("inducer")+"\","
				+ "\"up_sample_size\" : \""+c125.getUpSampleSize("inducer")+"\", "
				+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc("inducer")+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy("inducer")+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF("inducer")+"\","
				+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc("inducer")+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy("inducer")+"\", \"up_icd_f\" : \""+c125.getUpIcdF("inducer")+"\","
				+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc("inducer")+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy("inducer")+"\", \"up_lab_f\" : \""+c125.getUpLabF("inducer")+"\","
				+ "\n");
		p450.write("\t\t\t\"children\": [\n");
		
		for(String d:classMembers){			
			i++;			
			String rxcui = ehr.getRxcui(d)+"";
			p450.write("\t\t\t\t{\"name\": \""+d+" ("+rxcui+")\",\"id\": \""+d+"\", \"rxcui\":\""+rxcui+"\",\"size\": \"1\", "
				+ "\"down_sample_size\" : \""+c125.getDownSampleSize(rxcui)+"\", "
					+ "\"down_pheno_auc\" : \""+c125.getDownPhenoAucRoc(rxcui)+"\",  \"down_pheno_acc\" : \""+c125.getDownPhenoAccuracy(rxcui)+"\",  \"down_pheno_f\" : \""+c125.getDownPhenoF(rxcui)+"\","
					+ "\"down_icd_auc\" : \""+c125.getDownIcdAucRoc(rxcui)+"\",  \"down_icd_acc\" : \""+c125.getDownIcdAccuracy(rxcui)+"\",  \"down_icd_f\" : \""+c125.getDownIcdF(rxcui)+"\","
					+ "\"down_lab_auc\" : \""+c125.getDownLabAucRoc(rxcui)+"\", "
						+ "\"down_lab_acc\" : \""+c125.getDownLabAccuracy(rxcui)+"\", \"down_lab_f\" : \""+c125.getDownLabF(rxcui)+"\","
					+ "\"up_sample_size\" : \""+c125.getUpSampleSize(rxcui)+"\", "
					+ "\"up_pheno_auc\" : \""+c125.getUpPhenoAucRoc(rxcui)+"\", "
						+ "\"up_pheno_acc\" : \""+c125.getUpPhenoAccuracy(rxcui)+"\", \"up_pheno_f\" : \""+c125.getUpPhenoF(rxcui)+"\","
					+ "\"up_icd_auc\" : \""+c125.getUpIcdAucRoc(rxcui)+"\", "
						+ "\"up_icd_acc\" : \""+c125.getUpIcdAccuracy(rxcui)+"\", \"up_icd_f\" : \""+c125.getUpIcdF(rxcui)+"\","
					+ "\"up_lab_auc\" : \""+c125.getUpLabAucRoc(rxcui)+"\", "
						+ "\"up_lab_acc\" : \""+c125.getUpLabAccuracy(rxcui)+"\", \"up_lab_f\" : \""+c125.getUpLabF(rxcui)+"\""						
					+ "}");
			if(i<=l-1){
				p450.write(",\n");
			}else{
				p450.write("\n");
			}
		}		
		//==========================================================================================											
		p450.write("\t\t\t]\n");
		p450.write("\t\t}\n");
		//==========================================================================================											
		//==========================================================================================											
		//==========================================================================================											

		p450.write("\t]\n");
		p450.write("\t}\n");
		p450.write("\t]\n");
		p450.write("}");
		
		p450.close();
	}

}
