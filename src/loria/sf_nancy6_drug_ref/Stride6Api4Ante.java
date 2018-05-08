package loria.sf_nancy6_drug_ref;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * manipulate data from DB1
 * xx.stanford.edu
 * need vpn and ssh ON
 * version 4 is a light version made for pheotype profile computation
 * version 3 is a light version made for annotation expension
 * version 2 is for interval identification & co.
 * @author x
 *
 */

public class Stride6Api4Ante {

	static String DB_SERVER  = "";
	static String DB         = "user_x";
	static String DRIVER     = "com.mysql.jdbc.Driver";
	static String USER_NAME  = "";
	static String USER_PSWD  = "";			
	
	Connection sqlCon = null;
	
	String DRUG_TABLE;
	String DOSE_CHG;
	String DOSE_CHG_ANNOT;
	String DRUG_CHG_ANNOT_DB;
	String DRUG_CHG_ANNOT_MS;
	String CONTI_ANNOT;
	String DOSE_DOWN_PHENO;
	String DOSE_UP_PHENO;
	String DRUG_CHG_PHENO_DB;
	String DRUG_CHG_PHENO_MS;
	
	static String UP   = "up";
	static String DOWN = "down";
	
	PreparedStatement getCuiSt,getCuiSt_,getCuiSt__,getCuiSt___;
	PreparedStatement insertDoseDownPhenotype;
	PreparedStatement insertDoseUpPhenotype;
	PreparedStatement insertDrugChangePhenotypeDb;
	PreparedStatement insertDrugChangePhenotypeMs;
	PreparedStatement getm1St, getm1St_, getm1St__, getm1St___;
	PreparedStatement getm2Sst;
	PreparedStatement getm2primeSst;
	PreparedStatement getn1St, getn1St_, getn1St__, getn1St___;
	PreparedStatement getn2St;
	PreparedStatement getn2primeSt;
	PreparedStatement getn1St4drugClass, getn1St4drugClass_, getn1St4drugClass__, getn1St4drugClass___;
	PreparedStatement getn2primeSt4drugClass, getn2primeSt4drugClass_, getn2primeSt4drugClass__, getn2primeSt4drugClass___;
	PreparedStatement getn2St4drugClass;
	PreparedStatement getDrugClass;
	PreparedStatement getDrugClassMember;
//	PreparedStatement getIc;  // pubmed IC
	PreparedStatement getIc2; // stride6 IC
	PreparedStatement getIcdIc;
	PreparedStatement getLabIc;


	
    HashMap<String, Integer> dosems = null;
    HashMap<String, Integer> drugms = null;
    HashMap<String, Integer> contims = null;
	
	public Stride6Api4Ante(String t1, String t2, String t3, String t4, 
			String t5, String t6, String t7, String t8, String t9, String t10
			) throws ClassNotFoundException, SQLException {	
		DRUG_TABLE=t1;
		DOSE_CHG=t2;
		DOSE_CHG_ANNOT=t3;
		DRUG_CHG_ANNOT_DB=t4;
		DRUG_CHG_ANNOT_MS=t5;
		CONTI_ANNOT=t6;
		DOSE_DOWN_PHENO=t7;
		DOSE_UP_PHENO=t8;	
		DRUG_CHG_PHENO_DB=t9;
		DRUG_CHG_PHENO_MS=t10;
        Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}

	// prepare statement at the instantiation of the class
	private void prepareInitialStt() throws SQLException {
		getCuiSt    = sqlCon.prepareStatement("SELECT distinct a.cui "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui=? AND a.iid=c.iid AND c.dose_down=1 ORDER BY a.cui;");
		getCuiSt_   = sqlCon.prepareStatement("SELECT distinct a.cui "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui=? AND a.iid=c.iid AND c.dose_up=1 ORDER BY a.cui;");
		getCuiSt__  = sqlCon.prepareStatement("SELECT distinct cui "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" "
				+ "WHERE rxcui=? "
				+ "ORDER BY cui;");
		getCuiSt___ = sqlCon.prepareStatement("SELECT distinct cui "
				+ "FROM "+DRUG_CHG_ANNOT_MS+" "
				+ "WHERE rxcui=? "
				+ "ORDER BY cui;");
		insertDoseDownPhenotype = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_DOWN_PHENO+" VALUES (?,?,?,?,?,?);");
		insertDoseUpPhenotype = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_UP_PHENO+" VALUES (?,?,?,?,?,?);");
		insertDrugChangePhenotypeDb = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_DB+" VALUES (?,?,?,?,?,?);");
		insertDrugChangePhenotypeMs = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_MS+" VALUES (?,?,?,?,?,?);");
		getm1St    = sqlCon.prepareStatement("SELECT COUNT(distinct a.iid) "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui=? AND "
				+ "a.iid=c.iid AND "
				+ "c.dose_down=1;");
		getm1St_   = sqlCon.prepareStatement("SELECT COUNT(distinct a.iid) "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui=? AND "
				+ "a.iid=c.iid AND "
				+ "c.dose_up=1;");
		getm1St__  = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" "
				+ "WHERE rxcui=?;");
		getm1St___ = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+DRUG_CHG_ANNOT_MS+" "
				+ "WHERE rxcui=?;");
		getm2Sst = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+CONTI_ANNOT+" "
				+ "WHERE rxcui=?;");
		getm2primeSst = sqlCon.prepareStatement("SELECT t1.s+t2.s+t3.s+t4.s " // works only for dose change
				+ "FROM "													  // create a new statement if need of m2' for drug changes
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_up=? AND a.rxcui!=?) AS t1, "
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_down=?) AS t2,"
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DRUG_CHG_ANNOT_DB+") AS t3, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+CONTI_ANNOT+") AS t4;");
		getn1St    = sqlCon.prepareStatement("SELECT COUNT(distinct a.iid) "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui=? AND "
				+ "a.cui=? AND "
				+ "a.iid=c.iid AND "
				+ "c.dose_down=1;");
		getn1St_   = sqlCon.prepareStatement("SELECT COUNT(distinct a.iid) "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui=? AND "
				+ "a.cui=? AND "
				+ "a.iid=c.iid AND "
				+ "c.dose_up=1;");
		getn1St__  = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" "
				+ "WHERE rxcui=? AND "
				+ "cui=?;");
		getn1St___ = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+DRUG_CHG_ANNOT_MS+" "
				+ "WHERE rxcui=? AND "
				+ "cui=?;");
		getn2St = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+CONTI_ANNOT+" "
				+ "WHERE rxcui=? AND "
				+ "cui=?;");
		getn2primeSt = sqlCon.prepareStatement("SELECT t1.s+t2.s+t3.s+t4.s " // works only for dose change, not for drug changes			
				+ "FROM "													 // create a novel statement if need of n2' for drug changes
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_up=? AND a.rxcui!=? AND "
				+ "cui=?) AS t1, "
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_down=? AND "
				+ "cui=?) AS t2, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" WHERE "
				+ "cui=?) AS t3, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+CONTI_ANNOT+" WHERE "
				+ "cui=?) AS t4;");
		getDrugClass = sqlCon.prepareStatement("SELECT distinct atc "
				+ "FROM "+DRUG_TABLE+" "
				+ "WHERE rxcui=?;");
		getDrugClassMember = sqlCon.prepareStatement("SELECT distinct rxcui "
				+ "FROM "+DRUG_TABLE+" "
				+ "WHERE atc_code LIKE ? AND rxcui != 0  AND too_many_patients=0;");
		/*getIc = sqlCon.prepareStatement("SELECT note as ic "
				+ "FROM stride5.medline_ic_new m, terminology3.str2cid c "
				+ "WHERE c.cui=? AND c.cid=m.cid;");*/
		getIc2 = sqlCon.prepareStatement("SELECT ic "
				+ "FROM user_x.stride6_ic "
				+ "WHERE cui=?;");
		getIcdIc = sqlCon.prepareStatement("SELECT ic "
				+ "FROM user_x.stride6_icd_ic "
				+ "WHERE cui=?;");
		getLabIc = sqlCon.prepareStatement("SELECT ic "
				+ "FROM user_x.stride6_lab_ic "
				+ "WHERE cui=?;");
	}

	// Closing PpStt
	public void closeSst() throws SQLException {
		getCuiSt.close();getCuiSt_.close();getCuiSt__.close();getCuiSt___.close();
		insertDoseDownPhenotype.close();
		insertDoseUpPhenotype.close();
		insertDrugChangePhenotypeDb.close();
		insertDrugChangePhenotypeMs.close();
		getm1St.close();getm1St_.close();getm1St__.close();getm1St___.close();
		getm2Sst.close();
		getn1St.close();getn1St_.close();getn1St__.close();getn1St___.close();
		getn2St.close();
		getDrugClass.close();
		getDrugClassMember.close();
		//getIc.close();
		getIc2.close();
		getIcdIc.close();
		getLabIc.close();
		getm2primeSst.close();
		getn2primeSt.close();
	}
		
	// to store phenotype profile (including p-values and rr)
	public void createPhenotypeProfileTable(String tableName) throws SQLException {
		Statement stat = sqlCon.createStatement();
				
		String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
				+ "atc_code char(11) NOT NULL DEFAULT '', "
				+ "rxcui mediumint(8) NOT NULL DEFAULT 0, "
				+ "cui char(8) NOT NULL, "
				+ "pvalue double,"
				+ "rr double,"
				+ "ic double,"			
				+ "PRIMARY KEY pk (atc_code, rxcui, cui), "
				+ "KEY atc_ix (atc_code), "
				+ "KEY rxcui_ix (rxcui), "  	
				+ "KEY cui_ix(cui) "							
				+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		stat.execute(query);
		stat.close();		
	}
	
	// returns cui i.e. phenotypes of a table 
	public ArrayList<String> getCui(String annotationTable, String upOrDown, Integer d) throws SQLException {
		ArrayList<String> cui = new ArrayList<String>();
		ResultSet rs = null;
		
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(DOWN)){
				getCuiSt.setInt(1, d);				
				rs = getCuiSt.executeQuery();
			}else if(upOrDown.equals(UP)){
				getCuiSt_.setInt(1, d);
				rs = getCuiSt_.executeQuery();
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB) && upOrDown.equals("")){
			getCuiSt__.setInt(1, d);
			rs = getCuiSt__.executeQuery();
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS) && upOrDown.equals("")){
			getCuiSt___.setInt(1, d);
			rs = getCuiSt___.executeQuery();
		}
							
		while (rs.next()) {
			cui.add(rs.getString(1));
		}
		rs.close();
		return cui;
	}
	
	// returns rxcuis from a drug table or a drug view
	public ArrayList<Integer> getDrug(String drugTable) throws SQLException {
		ArrayList<Integer> drugs = new ArrayList<Integer>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT distinct rxcui FROM "+drugTable+" WHERE rxcui!=0 AND rxcui IS NOT NULL AND too_many_patients=0 ORDER BY rxcui ASC;");		
		while (rs.next()) {
			drugs.add(rs.getInt(1));
		}		
		rs.close();
		stat.close();
		return drugs;
	}
	
	// returns atc code from a drug table or a drug view
	public ArrayList<String> getClass(String drugTable) throws SQLException {
		ArrayList<String> classes = new ArrayList<String>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT distinct atc_code FROM "+drugTable+" WHERE rxcui = 0 AND atc_code!='' AND too_many_patients=0 ORDER BY atc_code ASC;");		
		while (rs.next()) {
			classes.add(rs.getString(1));
		}	
		rs.close();
		stat.close();
		return classes;
	}

	// get from db1 the value of m1 for a drug
	public int getm1(String annotationTable, String upOrDown, Integer d) throws SQLException {

		int m1 = 0;
		ResultSet rs = null;
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(DOWN)){
				getm1St.setInt(1, d);
				rs = getm1St.executeQuery();
			}else if(upOrDown.equals(UP)){
				getm1St_.setInt(1, d);
				rs = getm1St_.executeQuery();
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB) && upOrDown.equals("")){
			getm1St__.setInt(1, d);
			rs = getm1St__.executeQuery();
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS) && upOrDown.equals("")){
			getm1St___.setInt(1, d);
			rs = getm1St___.executeQuery();
		}		 		
		while (rs.next()) {
			m1 = rs.getInt(1);
			break;
		}
		rs.close();
		return m1;
	}	
	
	// get from db1 the value of m2 for a drug
	public int getm2(Integer d) throws SQLException {
		int m2 = 0;
		getm2Sst.setInt(1, d);
		ResultSet rs = getm2Sst.executeQuery();
		while (rs.next()) {
			m2 = rs.getInt(1);
			break;
		}
		rs.close();
		return m2;
	}
	
	// get from db1 the value of n2 for a drug
	public int getn2(Integer d, String p) throws SQLException {
			int n2 = 0;
			getn2St.setInt(1, d);
			getn2St.setString(2, p);
			ResultSet rs = getn2St.executeQuery();
			while (rs.next()) {
				n2 = rs.getInt(1);
				break;
			}
			rs.close();
			return n2;
		}

	// get from db1 the value of n1 for a drug
	public int getn1(String annotationTable, String upOrDown, Integer d, String p) throws SQLException {
		int n1 = 0;
		ResultSet rs = null;
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(DOWN)){
				getn1St.setInt(1, d);
				getn1St.setString(2, p);
				rs = getn1St.executeQuery();
			}else if(upOrDown.equals(UP)){
				getn1St_.setInt(1, d);
				getn1St_.setString(2, p);
				rs = getn1St_.executeQuery();
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB) && upOrDown.equals("")){
			getn1St__.setInt(1, d);
			getn1St__.setString(2, p);
			rs = getn1St__.executeQuery();
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS) && upOrDown.equals("")){
			getn1St___.setInt(1, d);
			getn1St___.setString(2, p);
			rs = getn1St___.executeQuery();
		}		 		
		while (rs.next()) {
			n1 = rs.getInt(1);
			break;
		}
		rs.close();
		return n1;
	}

	// add a line to phenotype profile table
	public void addPhenotype(String phenotypeTable, String c, Integer d, String p,
			double pvalue, double rr, double ic) throws SQLException {
		if(phenotypeTable.equals(DOSE_DOWN_PHENO)){
			insertDoseDownPhenotype.setString(1, c);
			insertDoseDownPhenotype.setInt(2, d);
			insertDoseDownPhenotype.setString(3, p);
			insertDoseDownPhenotype.setDouble(4, pvalue);
			insertDoseDownPhenotype.setDouble(5, rr);
			insertDoseDownPhenotype.setDouble(6, ic);
			insertDoseDownPhenotype.executeUpdate();
		}
		if(phenotypeTable.equals(DOSE_UP_PHENO)){
			insertDoseUpPhenotype.setString(1, c);
			insertDoseUpPhenotype.setInt(2, d);
			insertDoseUpPhenotype.setString(3, p);
			insertDoseUpPhenotype.setDouble(4, pvalue);
			insertDoseUpPhenotype.setDouble(5, rr);
			insertDoseUpPhenotype.setDouble(6, ic);
			insertDoseUpPhenotype.executeUpdate();
		}
		if(phenotypeTable.equals(DRUG_CHG_PHENO_DB)){
			insertDrugChangePhenotypeDb.setString(1, c);
			insertDrugChangePhenotypeDb.setInt(2, d);
			insertDrugChangePhenotypeDb.setString(3, p);
			insertDrugChangePhenotypeDb.setDouble(4, pvalue);
			insertDrugChangePhenotypeDb.setDouble(5, rr);
			insertDrugChangePhenotypeDb.setDouble(6, ic);
			insertDrugChangePhenotypeDb.executeUpdate();
		}
		if(phenotypeTable.equals(DRUG_CHG_PHENO_MS)){
			insertDrugChangePhenotypeMs.setString(1, c);
			insertDrugChangePhenotypeMs.setInt(2, d);
			insertDrugChangePhenotypeMs.setString(3, p);
			insertDrugChangePhenotypeMs.setDouble(4, pvalue);
			insertDrugChangePhenotypeMs.setDouble(5, rr);
			insertDrugChangePhenotypeMs.setDouble(6, ic);
			insertDrugChangePhenotypeMs.executeUpdate();
		}
		
		
	}

	// return the ATC class of a rxui
	public ArrayList<String> getDrugClass(Integer rxcui) throws SQLException {
		ArrayList<String> classes = new ArrayList<String>();
		getDrugClass.setInt(1, rxcui);
		ResultSet rs = getDrugClass.executeQuery();
		while (rs.next()) {
			classes.add(rs.getString(1));
		}
		rs.close();
		return classes;
	}
	// return the rxcui of drugs memeber of an ATC class c
	public ArrayList<Integer> getAtcDrugClassMember(String c) throws SQLException {
		ArrayList<Integer> members = new ArrayList<Integer>();
		getDrugClassMember.setString(1, c);
		ResultSet rs = getDrugClassMember.executeQuery();
		while (rs.next()) {
			members.add(rs.getInt(1));
		}
		rs.close();
		return members;
	}
		
	// return the rxcui of all drugs member of a P450 class 
	public ArrayList<Integer> getP450DrugClassMember(String c) throws SQLException {
		ArrayList<Integer> members = new ArrayList<Integer>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT DISTINCT rxcui "
				+ "FROM user_x.p450 "
				+ "WHERE rxcui IS NOT NULL AND too_many_patients=0 "
				+ "ORDER BY rxcui ASC;");			
		while (rs.next()) {
			members.add(rs.getInt(1));
		}
		rs.close();
		stat.close();				
		return members;
	}
	// return the rxcui of drugs member of a P450 class c (with a role name)
	public ArrayList<Integer> getP450DrugClassMemberByRole(String c) throws SQLException {
		ArrayList<Integer> members = new ArrayList<Integer>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT DISTINCT rxcui "
				+ "FROM user_x.p450 "
				+ "WHERE relation_type=\""+c+"\" "
				+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
				+ "ORDER BY rxcui ASC;");
		while (rs.next()) {
			members.add(rs.getInt(1));
		}
		rs.close();
		stat.close();				

		return members;
	}
	
	// return the rxcui of drugs member of a P450 class c (with a role name)
	public ArrayList<Integer> getP450DrugClassMemberByAtcLevel(String c) throws SQLException {
		ArrayList<Integer> members = new ArrayList<Integer>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT DISTINCT rxcui "
				+ "FROM user_x.p450 "
				+ "WHERE substring(atc,1,1)=\""+c+"\" "
				+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
				+ "ORDER BY rxcui ASC;");
		while (rs.next()) {
			members.add(rs.getInt(1));
		}
		rs.close();
		stat.close();				

		return members;
	}
	
	// return the rxcui of drugs member of a P450 class c (with a role name)
	public ArrayList<Integer> getP450DrugClassMemberByGene(String c) throws SQLException {
		ArrayList<Integer> members = new ArrayList<Integer>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT DISTINCT rxcui "
				+ "FROM user_x.p450 "
				+ "WHERE p450=\""+c+"\" "
				+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
				+ "ORDER BY rxcui ASC;");
		while (rs.next()) {
			members.add(rs.getInt(1));
		}
		rs.close();
		stat.close();				

		return members;
	}
	
	// return the rxcui of drugs member of a P450 class c (with a role name)
	public ArrayList<Integer> getP450DrugClassMemberByRoleXAtc(String c) throws SQLException {
		String [] tab = c.split("_");
		ArrayList<Integer> members = new ArrayList<Integer>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT DISTINCT rxcui "
				+ "FROM user_x.p450 "
				+ "WHERE relation_type=\""+tab[0]+"\" "
				+ "AND substring(atc,1,1)=\""+tab[1]+"\" "
				+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
				+ "ORDER BY rxcui ASC;");
		while (rs.next()) {
			members.add(rs.getInt(1));
		}
		rs.close();
		stat.close();				

		return members;
	}
	
	// ======================================================================
	// ======================================================================
	// ================== SAME CLASSES FOR A SET OF DRUGS ===================
	// ======================================================================
	// ======================================================================
	
	// get from db1 the value of m1 for a drug
	public int getm1(String annotationTable, String upOrDown, ArrayList<Integer> c) throws SQLException {
		
		int m1 = 0;
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = null;
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(DOWN)){
				String query = "SELECT COUNT(distinct a.iid) "
						+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
						+ "WHERE a.rxcui IN ("
						+ this.getListOfMembers(c)
						+") AND "
						+ "a.iid=c.iid AND "
						+ "c.dose_down=1;";
				//System.out.println(query);
				rs = stat.executeQuery(query);
			}else if(upOrDown.equals(UP)){
				rs = stat.executeQuery("SELECT COUNT(distinct a.iid) "
						+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
						+ "WHERE a.rxcui IN ("
						+ this.getListOfMembers(c)
						+") AND "
						+ "a.iid=c.iid AND "
						+ "c.dose_up=1;");
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB) && upOrDown.equals("")){
			rs = stat.executeQuery("SELECT COUNT(distinct iid) "
					+ "FROM "+DRUG_CHG_ANNOT_DB+" "
					+ "WHERE rxcui IN ("
					+ this.getListOfMembers(c)
					+ ");");
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS) && upOrDown.equals("")){
			rs = stat.executeQuery("SELECT COUNT(distinct iid) "
					+ "FROM "+DRUG_CHG_ANNOT_MS+" "
					+ "WHERE rxcui IN ("
					+ this.getListOfMembers(c)
					+ ");");
		}		 		
		while (rs.next()) {
			m1 = rs.getInt(1);
			break;
		}
		rs.close();
		stat.close();				

		return m1;
	}

	// generate a string out of an arraylist to concatenate with sql queries
	private String getListOfMembers(ArrayList<Integer> c) {
		String s = "";
		int count=0;
		for(Integer m:c){
			if(count==0){
				s=m+"";
				count++;
			}else{
				s=s+","+m;
			}
		}
		return s;
	}		
		
	// get from db1 the value of m2 for a drug (when ref set is continuation of same drug)
	public int getm2(ArrayList<Integer> c) throws SQLException {
		int m2 = 0;
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = stat.executeQuery("SELECT COUNT(distinct iid) "
				+ "FROM "+CONTI_ANNOT+" "
				+ "WHERE rxcui IN ("
				+ this.getListOfMembers(c)
				+ ");");
		while (rs.next()) {
			m2 = rs.getInt(1);
			break;
		}
		rs.close();
		stat.close();				

		return m2;
	}
	
	// get from db1 the value of m2 for a drug when the ref set is all drug, all interval type
		public int getm2prime(String annotationTable, String upOrDown, Integer rxcui) throws SQLException {
			int m2 = 0;

			Statement stat = sqlCon.createStatement();		
			if(annotationTable.equals(DOSE_CHG_ANNOT)){				
				getm2primeSst.setInt(2, rxcui);				
				if(upOrDown.equals(UP)){					
					getm2primeSst.setInt(1, 1); // dose up=1
					getm2primeSst.setInt(3, 1); // dose down=1												
				}else if(upOrDown.equals(DOWN)){					
					getm2primeSst.setInt(1, 0); // dose up=0
					getm2primeSst.setInt(3, 0); // dose down=0
				}
			}/*
			if(annotationTable.equals(DRUG_CHG_ANNOT_DB)){
				rs = stat.executeQuery("SELECT t1.s+t2.s+t3.s "
						+ "FROM "
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+DRUG_CHG_ANNOT_DB+" "
						+ "WHERE rxcui NOT IN ("+mbrStr+")) AS t1,"
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+CONTI_ANNOT+") AS t2, "
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+DOSE_CHG_ANNOT+") AS t3;");		
			}
			if(annotationTable.equals(DRUG_CHG_ANNOT_MS)){
				rs = stat.executeQuery("SELECT t1.s+t2.s+t3.s "
						+ "FROM "
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+DRUG_CHG_ANNOT_MS+" "
						+ "WHERE rxcui NOT IN ("+mbrStr+")) AS t1, "
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+CONTI_ANNOT+") AS t2, "
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+DOSE_CHG_ANNOT+") AS t3;");
			}*/
			ResultSet rs = getm2primeSst.executeQuery();				 		
			while (rs.next()) {
				m2 = rs.getInt(1);
				break;
			}
			rs.close();
			stat.close();				

			return m2;
		}
	
	// get from db1 the value of m2 for a drug when the ref set is all drug, all interval type
	public int getm2prime(String annotationTable, String upOrDown, ArrayList<Integer> mbr) throws SQLException {
		int m2 = 0;
		String mbrStr = this.getListOfMembers(mbr);

		Statement stat = sqlCon.createStatement();		
		ResultSet rs = null;
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(UP)){
				rs = stat.executeQuery("SELECT t1.s+t2.s+t3.s+t4.s "
						+ "FROM "
						+ "(SELECT COUNT(DISTINCT a.iid) as s "
						+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
						+ "WHERE a.iid=c.iid "
						+ "AND c.dose_up=1 AND a.rxcui NOT IN ("+mbrStr+")) AS t1, "
						+ "(SELECT COUNT(DISTINCT a.iid) as s "
						+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
						+ "WHERE a.iid=c.iid "
						+ "AND c.dose_down=1) AS t2,"
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+DRUG_CHG_ANNOT_DB+") AS t3, "
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+CONTI_ANNOT+") AS t4;");
						
			}else if(upOrDown.equals(DOWN)){
				rs = stat.executeQuery("SELECT t1.s+t2.s+t3.s+t4.s "
						+ "FROM "
						+ "(SELECT COUNT(DISTINCT a.iid) as s "
						+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
						+ "WHERE a.iid=c.iid "
						+ "AND c.dose_down=1 AND a.rxcui NOT IN ("+mbrStr+")) AS t1, "
						+ "(SELECT COUNT(DISTINCT a.iid) as s "
						+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
						+ "WHERE a.iid=c.iid "
						+ "AND c.dose_up=1) AS t2,"
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+DRUG_CHG_ANNOT_DB+") AS t3, "
						+ "(SELECT COUNT(DISTINCT iid) as s "
						+ "FROM "+CONTI_ANNOT+") AS t4;");
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB)){
			rs = stat.executeQuery("SELECT t1.s+t2.s+t3.s "
					+ "FROM "
					+ "(SELECT COUNT(DISTINCT iid) as s "
					+ "FROM "+DRUG_CHG_ANNOT_DB+" "
					+ "WHERE rxcui NOT IN ("+mbrStr+")) AS t1,"
					+ "(SELECT COUNT(DISTINCT iid) as s "
					+ "FROM "+CONTI_ANNOT+") AS t2, "
					+ "(SELECT COUNT(DISTINCT iid) as s "
					+ "FROM "+DOSE_CHG_ANNOT+") AS t3;");		
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS)){
			rs = stat.executeQuery("SELECT t1.s+t2.s+t3.s "
					+ "FROM "
					+ "(SELECT COUNT(DISTINCT iid) as s "
					+ "FROM "+DRUG_CHG_ANNOT_MS+" "
					+ "WHERE rxcui NOT IN ("+mbrStr+")) AS t1, "
					+ "(SELECT COUNT(DISTINCT iid) as s "
					+ "FROM "+CONTI_ANNOT+") AS t2, "
					+ "(SELECT COUNT(DISTINCT iid) as s "
					+ "FROM "+DOSE_CHG_ANNOT+") AS t3;");
		}
			 				 		
		while (rs.next()) {
			m2 = rs.getInt(1);
			break;
		}
		rs.close();
		stat.close();				

		return m2;
	}
	
	// returns cui i.e. phenotypes of a table 
	public ArrayList<String> getCui(String annotationTable, String upOrDown, ArrayList<Integer> c) throws SQLException {
		ArrayList<String> cui = new ArrayList<String>();
		Statement stat = sqlCon.createStatement();		
		ResultSet rs = null;
		
		
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(DOWN)){
				rs = stat.executeQuery("SELECT distinct a.cui "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui IN ("
				+ this.getListOfMembers(c)
				+ ") AND a.iid=c.iid AND c.dose_down=1 ORDER BY a.cui;");
			}else if(upOrDown.equals(UP)){
				rs = stat.executeQuery("SELECT distinct a.cui "
						+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
						+ "WHERE a.rxcui IN ("
						+ this.getListOfMembers(c)
						+ ") AND a.iid=c.iid AND c.dose_up=1 ORDER BY a.cui;");
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB) && upOrDown.equals("")){
			rs = stat.executeQuery("SELECT distinct cui "
					+ "FROM "+DRUG_CHG_ANNOT_DB+" "
					+ "WHERE rxcui IN ("
					+ this.getListOfMembers(c)
					+ ") "
					+ "ORDER BY cui;");
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS) && upOrDown.equals("")){
			rs = stat.executeQuery("SELECT distinct cui "
					+ "FROM "+DRUG_CHG_ANNOT_MS+" "
					+ "WHERE rxcui IN ("
					+ this.getListOfMembers(c)
					+ ") "
					+ "ORDER BY cui;");
		}
					
		while (rs.next()) {
			cui.add(rs.getString(1));
		}
		rs.close();
		stat.close();
		return cui;
	}
	
	// get from db1 the value of n2 for a drug
	public int getn2(String p) throws SQLException {
		int n2 = 0;
		getn2St4drugClass.setString(1, p);
		ResultSet rs = getn2St4drugClass.executeQuery();
		while (rs.next()) {
			n2 = rs.getInt(1);
			break;
		}
		rs.close();
		return n2;
	}
	
	// get from db1 the value of n2 for a drug, considering as a ref any intervals
	public int getn2prime(String annotationTable, String upOrDown, Integer rxcui, String p) throws SQLException {
		int n2 = 0;
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(UP)){
				getn2primeSt.setInt(1, 1); 		// dose_up = 1
				getn2primeSt.setInt(2, rxcui);
				getn2primeSt.setString(3, p);
				getn2primeSt.setInt(4, 1); 		// dose_down = 1
				getn2primeSt.setString(5, p);
				getn2primeSt.setString(6, p);
				getn2primeSt.setString(7, p);
						
			}else if(upOrDown.equals(DOWN)){
				getn2primeSt.setInt(1, 0); 		// dose_up = 1
				getn2primeSt.setInt(2, rxcui);
				getn2primeSt.setString(3, p);
				getn2primeSt.setInt(4, 0); 		// dose_down = 1
				getn2primeSt.setString(5, p);
				getn2primeSt.setString(6, p);
				getn2primeSt.setString(7, p);			
			}
		}
		ResultSet rs = getn2primeSt.executeQuery();
			 				 		
		while (rs.next()) {
			n2 = rs.getInt(1);
			break;
		}
		rs.close();
		return n2;
	}
	
	// get from db1 the value of n2 for a drug class, considering as a ref any intervals
	public int getn2prime(String annotationTable, String upOrDown, ArrayList<Integer> mbr, String p) throws SQLException {
		int n2 = 0;
		ResultSet rs = null;
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(UP)){
				getn2primeSt4drugClass.setString(1, p);
				getn2primeSt4drugClass.setString(2, p);
				getn2primeSt4drugClass.setString(3, p);
				getn2primeSt4drugClass.setString(4, p);
				rs = getn2primeSt4drugClass.executeQuery();
						
			}else if(upOrDown.equals(DOWN)){
				getn2primeSt4drugClass_.setString(1, p);
				getn2primeSt4drugClass_.setString(2, p);
				getn2primeSt4drugClass_.setString(3, p);
				getn2primeSt4drugClass_.setString(4, p);				
				rs = getn2primeSt4drugClass_.executeQuery();
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB)){
			getn2primeSt4drugClass__.setString(1, p);
			getn2primeSt4drugClass__.setString(2, p);
			getn2primeSt4drugClass__.setString(3, p);
			rs = getn2primeSt4drugClass__.executeQuery();	
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS)){
			getn2primeSt4drugClass___.setString(1, p);
			getn2primeSt4drugClass___.setString(2, p);
			getn2primeSt4drugClass___.setString(3, p);			
			rs = getn2primeSt4drugClass___.executeQuery();
		}
			 				 		
		while (rs.next()) {
			n2 = rs.getInt(1);
			break;
		}
		rs.close();
		return n2;
	}
	
	// get from db1 the value of n1 for a drug
	public int getn1(String annotationTable, String upOrDown, String p) throws SQLException {
		int n1 = 0;
		ResultSet rs = null;
		if(annotationTable.equals(DOSE_CHG_ANNOT)){
			if(upOrDown.equals(DOWN)){
				getn1St4drugClass.setString(1, p);
				rs = getn1St4drugClass.executeQuery();				
			}else if(upOrDown.equals(UP)){
				getn1St4drugClass_.setString(1, p);
				rs = getn1St4drugClass_.executeQuery();
			}
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_DB) && upOrDown.equals("")){
			getn1St4drugClass__.setString(1, p);
			rs = getn1St4drugClass__.executeQuery();
		}
		if(annotationTable.equals(DRUG_CHG_ANNOT_MS) && upOrDown.equals("")){
			getn1St4drugClass___.setString(1, p);
			rs = getn1St4drugClass___.executeQuery();
		}		 		
		while (rs.next()) {
			n1 = rs.getInt(1);
			break;
		}
		rs.close();
		return n1;
	}
	
	// create prepared statment for drug class c
	public void prepareN1AndN2Stt(ArrayList<Integer> c) throws SQLException{
		String mbrStr = this.getListOfMembers(c);
		getn1St4drugClass    = sqlCon.prepareStatement("SELECT COUNT(distinct a.iid) "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui IN ("
				+ mbrStr
				+ ") AND "
				+ "a.cui=? AND "
				+ "a.iid=c.iid AND "
				+ "c.dose_down=1;");
		getn1St4drugClass_   = sqlCon.prepareStatement("SELECT COUNT(distinct a.iid) "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.rxcui IN ("
				+ mbrStr
				+ ") AND "
				+ "a.cui=? AND "
				+ "a.iid=c.iid AND "
				+ "c.dose_up=1;");
		getn1St4drugClass__  = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" "
				+ "WHERE rxcui IN ("
				+ mbrStr
				+ ") AND "
				+ "cui=?;");
		getn1St4drugClass___ = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+DRUG_CHG_ANNOT_MS+" "
				+ "WHERE rxcui IN ("
				+ mbrStr
				+ ") AND "
				+ "cui=?;");
		getn2St4drugClass = sqlCon.prepareStatement("SELECT COUNT(distinct iid) "
				+ "FROM "+CONTI_ANNOT+" "
				+ "WHERE rxcui IN ("
				+ mbrStr
				+ ") AND "
				+ "cui=?;");

		getn2primeSt4drugClass = sqlCon.prepareStatement("SELECT t1.s+t2.s+t3.s+t4.s "
				+ "FROM "
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_up=1 AND a.rxcui NOT IN ("+mbrStr+") AND "
				+ "cui=?) AS t1, "
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_down=1 AND "
				+ "cui=?) AS t2, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" WHERE "
				+ "cui=?) AS t3, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+CONTI_ANNOT+" WHERE "
				+ "cui=?) AS t4;");
		
		getn2primeSt4drugClass_ = sqlCon.prepareStatement("SELECT t1.s+t2.s+t3.s+t4.s "
				+ "FROM "
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_down=1 AND a.rxcui NOT IN ("+mbrStr+") AND "
				+ "cui=?) AS t1, "
				+ "(SELECT COUNT(DISTINCT a.iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" a, "+DOSE_CHG+" c "
				+ "WHERE a.iid=c.iid "
				+ "AND c.dose_up=1 AND "
				+ "cui=?) AS t2,"
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" WHERE "
				+ "cui=?) AS t3, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+CONTI_ANNOT+" WHERE "
				+ "cui=?) AS t4;");
		
		getn2primeSt4drugClass__ = sqlCon.prepareStatement("SELECT t1.s+t2.s+t3.s "
				+ "FROM "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DRUG_CHG_ANNOT_DB+" "
				+ "WHERE rxcui NOT IN ("+mbrStr+") AND "
				+ "cui=?) AS t1,"
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+CONTI_ANNOT+" WHERE "
				+ "cui=?) AS t2, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" WHERE "
				+ "cui=?) AS t3;");
		
		getn2primeSt4drugClass___ = sqlCon.prepareStatement("SELECT t1.s+t2.s+t3.s "
				+ "FROM "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DRUG_CHG_ANNOT_MS+" "
				+ "WHERE rxcui NOT IN ("+mbrStr+")  AND "
				+ "cui=?) AS t1,"
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+CONTI_ANNOT+" WHERE "
				+ "cui=?) AS t2, "
				+ "(SELECT COUNT(DISTINCT iid) as s "
				+ "FROM "+DOSE_CHG_ANNOT+" WHERE "
				+ "cui=?) AS t3;");
	}
	public void closeN1AndN2PreparedStt() throws SQLException {
		getn1St4drugClass.close();getn1St4drugClass_.close();getn1St4drugClass__.close();getn1St4drugClass___.close();
		getn2St4drugClass.close();getn2primeSt4drugClass.close();getn2primeSt4drugClass_.close();getn2primeSt4drugClass__.close();getn2primeSt4drugClass___.close();
	}

	// get the Information content of the concept	
	public double getIc(String p) throws SQLException {
		Double ic = 0.0;
		ResultSet rs = null;
	
		/*getIc.setString(1, p);
		rs = getIc.executeQuery();*/   //for IC from MEDLINE 
	 	
		getIc2.setString(1, p);			//for IC from STRIDE 6 
		rs = getIc2.executeQuery();   
		
		while (rs.next()) {
			ic = rs.getDouble(1);		
		}
		rs.close();
		return ic;
	}

	public double getIcdIc(String p) throws SQLException {
		Double ic = 0.0;
		ResultSet rs = null;
	 	
		getIcdIc.setString(1, p);			//for IC for ICD9 diag 
		rs = getIcdIc.executeQuery();   
		
		while (rs.next()) {
			ic = rs.getDouble(1);		
		}
		rs.close();
		return ic;
	}

	public double getLabIc(String p) throws SQLException {
		Double ic = 0.0;
		ResultSet rs = null;
	 	
		getLabIc.setString(1, p);			//for IC for Lab results 
		rs = getLabIc.executeQuery();   
		
		while (rs.next()) {
			ic = rs.getDouble(1);		
		}
		rs.close();
		return ic;
	}

}


