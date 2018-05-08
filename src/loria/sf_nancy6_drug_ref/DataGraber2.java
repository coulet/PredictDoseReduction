package loria.sf_nancy6_drug_ref;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DataGraber2 {

	HashMap<String, String[]> down = new HashMap<String, String[]>();
	HashMap<String, String[]> up = new HashMap<String, String[]>();
	
	public DataGraber2(String down_experiment_file, String up_experiment_file) throws IOException {

		try{
  			InputStream ips=new FileInputStream(new File(down_experiment_file)); 
  			InputStreamReader ipsr=new InputStreamReader(ips);
  			BufferedReader br=new BufferedReader(ipsr);
  			String line;
  			while ((line=br.readLine())!=null){
  				//System.out.println(line);
  				String fields[] = line.split("\t");//line.split(";");
  				if(fields.length>=16 && !fields[0].startsWith("drug class or drug")){
  					down.put(fields[0], new String[] {fields[2],			// sample size
  							fields[4],fields[5],fields[6],fields[7],		//#f 1\tAUC-ROC 1\tAccuracy 1\tF-m 1
  							fields[8],fields[9],fields[10],fields[11],		//#f 2\tAUC-ROC 2\tAccuracy 2\tF-m 2
  							fields[12],fields[13],fields[14],fields[15],	//#f 3\tAUC-ROC 3\tAccuracy 3\tF-m 3
  							fields[16],fields[17],fields[18],fields[19]});	//#f all\tAUC-ROC all\tAccuracy all\tF-m all
  					//System.out.println(fields[0]+" "+down.get(fields[0])[0]);
  				}
  				
  			}
  			br.close(); 
  		}		
    	catch (FileNotFoundException fnfe) {
              ;
        }
		
		try{
  			InputStream ips=new FileInputStream(new File(up_experiment_file)); 
  			InputStreamReader ipsr=new InputStreamReader(ips);
  			BufferedReader br=new BufferedReader(ipsr);
  			String line;
  			while ((line=br.readLine())!=null){
  				String fields[] = line.split(";");
  				if(fields.length>=12){
  					up.put(fields[0], new String[] {fields[2],				// sample size
  							fields[4],fields[5],fields[6],fields[7], 		//PHENO #f 1\tAUC-ROC 1\tAccuracy 1\tF-m 1
  							fields[8],fields[9],fields[10],fields[11],		//ICD   #f 2\tAUC-ROC 2\tAccuracy 2\tF-m 2
  							fields[12],fields[13],fields[14],fields[15],	//LAB   #f 3\tAUC-ROC 3\tAccuracy 3\tF-m 3
  							fields[16],fields[17],fields[18],fields[19]});	//ALL   #f all\tAUC-ROC all\tAccuracy all\tF-m all
  				}
  			}
  			br.close(); 
  		}		
    	catch (FileNotFoundException fnfe) {
              ;
        }	
	}
	public String getDownPhenoF(String c) {	
		if(down.containsKey(c)){
			return down.get(c)[4];
		}else{
			return "NaN";
		}
	}
	public String getDownIcdF(String c) {
		if(down.containsKey(c)){
			return down.get(c)[8];
		}else{
			return "NaN";
		}
	}
	public String getDownLabF(String c) {
		if(down.containsKey(c)){
			return down.get(c)[12];
		}else{
			return "NaN";
		}
	}
	
	public String getUpPhenoF(String c) {	
		/*System.out.println("c="+c);
		System.out.println(up.containsKey(c));
		System.out.println(up.get(c));
		*/
		if(up.containsKey(c) && up.get(c).length>0){
			return up.get(c)[4];
		}else{
			return "NaN";
		}
	}
	public String getUpIcdF(String c) {		
		if(up.containsKey(c)){
			return up.get(c)[8];
		}else{
			return "NaN";
		}
	}
	public String getUpLabF(String c) {		
		if(up.containsKey(c)){
			return up.get(c)[12];
		}else{
			return "NaN";
		}
	}

	public String getDownPhenoAucRoc(String c) {
		if(down.containsKey(c)){
			return down.get(c)[2];
		}else{
			return "NaN";
		}
	}

	public String getDownIcdAucRoc(String c) {
		if(down.containsKey(c)){
			return down.get(c)[6];
		}else{
			return "NaN";
		}
	}

	public String getDownLabAucRoc(String c) {
		if(down.containsKey(c)){
			return down.get(c)[10];
		}else{
			return "NaN";
		}
	}

	public String getUpPhenoAucRoc(String c) {
		if(up.containsKey(c) && up.get(c).length>0){
			return up.get(c)[2];
		}else{
			return "NaN";
		}
	}
	public String getUpIcdAucRoc(String c) {
		if(up.containsKey(c) && up.get(c).length>0){
			return up.get(c)[6];
		}else{
			return "NaN";
		}
	}
	public String getUpLabAucRoc(String c) {
		if(up.containsKey(c) && up.get(c).length>0){
			return up.get(c)[10];
		}else{
			return "NaN";
		}
	}

	public String getDownPhenoAccuracy(String c) {
		if(down.containsKey(c)){
			return down.get(c)[3];
		}else{
			return "NaN";
		}
	}

	public String getDownIcdAccuracy(String c) {
		if(down.containsKey(c)){
			return down.get(c)[7];
		}else{
			return "NaN";
		}
	}

	public String getDownLabAccuracy(String c) {
		if(down.containsKey(c)){
			return down.get(c)[11];
		}else{
			return "NaN";
		}
	}

	public String getUpPhenoAccuracy(String c) {
		if(up.containsKey(c) && up.get(c).length>0){
			return up.get(c)[3];
		}else{
			return "NaN";
		}
	}
	public String getUpIcdAccuracy(String c) {
		if(up.containsKey(c) && up.get(c).length>0){
			return up.get(c)[7];
		}else{
			return "NaN";
		}
	}
	public String getUpLabAccuracy(String c) {
		if(up.containsKey(c) && up.get(c).length>0){
			return up.get(c)[11];
		}else{
			return "NaN";
		}
	}

	public String getDownSampleSize(String c) {
		if(down.containsKey(c)){
			return down.get(c)[0];
		}else{
			return "NaN";
		}
	}

	public String getUpSampleSize(String c) {
		if(up.containsKey(c)){
			return up.get(c)[0];
		}else{
			return "NaN";
		}
	}
}
