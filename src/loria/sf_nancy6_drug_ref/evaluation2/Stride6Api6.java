package loria.sf_nancy6_drug_ref.evaluation2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * manipulate data from DB1
 * xx.stanford.edu
 * need vpn and ssh ON
 * version 3 is a light version made for annotation expension
 * version 2 is for interval identification & co.
 * @author coulet
 *
 */

public class Stride6Api6 {

	static String DB_SERVER  = "";
	static String DB         = "";
	static String DRIVER     = "com.mysql.jdbc.Driver";
	static String USER_NAME  = "";
	static String USER_PSWD  = "";			
	
	static String UP 				 = "up";
	static String DOWN 				 = "down";
	static String CONTI 			 = "conti";
	static String DOSE_CHG           = "user_x.dose_change_6m";
	static String DRUG_CHG2          = "user_x.drug_change_db_6m";
	static String CONTINUATION2      = "user_x.continuation_6m";
	static String DOSE_CHG_ANNOT2    = "user_x.dose_change_annotation";
	static String DRUG_CHG_ANNOT_DB	 = "user_x.drug_change_annotation_db";
	static String DRUG_CHG_ANNOT_MS	 = "user_x.drug_change_annotation_ms";
	
	static String DOSE_CHG_ANTE_ANNOT2   = "user_x.dose_change_6ante_annotation";
	static String DRUG_CHG_ANTE_ANNOT_DB = "user_x.drug_change_6ante_annotation_db";
	static String DRUG_CHG_ANTE_ANNOT_MS = "user_x.drug_change_6ante_annotation_ms";
	
	static String SINGLE 			 = "single";
	static String CLASS				 = "class";
		
	String EHR_BASE;
	String ATC_BASE;
	String DRUG_CHG;
	String CONTINUATION;
	String DOSE_CHG_ANNOT;
	String DRUG_CHG_ANNOT;
	String CONTINUATION_ANNOT;
	String DOSE_CHG_PHENO;
	String DRUG_CHG_PHENO;
	String DRUG_INDICATION_TABLE = "user_x.medispan_drug_indication2";
	String DRUG_EVENT_TABLE = "user_x.medispan_drug_event";
	
	String ANNOT_TABLE;
	String REDUCED_ANNOT_TABLE;
	String EXPENDED_ANNOT_TABLE;
	
	// 5 table for building feature matrix
	String PATIENT_CLASS1_TABLE;
	String PATIENT_CLASS2_TABLE;
	String CLASS1_ANNOTATION_TABLE;
	String CLASS2_ANNOTATION_TABLE;
	String CLASS11_ANNOTATION_TABLE;
	String CLASS12_ANNOTATION_TABLE;
	String CLASS21_ANNOTATION_TABLE;
	String CLASS22_ANNOTATION_TABLE;
	String CLASS31_ANNOTATION_TABLE;
	String CLASS32_ANNOTATION_TABLE;
	String PHENOTYPE_PROFILE_TABLE;
	String DOSE_CHG_DIRECTION; //  UP or DOWN
	
	static String DOSE_DOWN_PHENO;
	static String DOSE_UP_PHENO;
	static String DRUG_CHG_PHENO_DB;
	static String DRUG_CHG_PHENO_MS;

	static String DOSE_DOWN_PHENO_REDUCED1;
	static String DOSE_UP_PHENO_REDUCED1;
	static String DRUG_CHG_PHENO_DB_REDUCED1;
	static String DRUG_CHG_PHENO_MS_REDUCED1;
	
	static String DOSE_DOWN_PHENO_REDUCED2;
	static String DOSE_UP_PHENO_REDUCED2;
	static String DRUG_CHG_PHENO_DB_REDUCED2;
	static String DRUG_CHG_PHENO_MS_REDUCED2;
	
	static String DOSE_DOWN_PHENO_REDUCED3;
	static String DOSE_UP_PHENO_REDUCED3;
	static String DRUG_CHG_PHENO_DB_REDUCED3;
	static String DRUG_CHG_PHENO_MS_REDUCED3;
	
	static String DOSE_DOWN_PHENO_REDUCED4;
	static String DOSE_UP_PHENO_REDUCED4;
	static String DRUG_CHG_PHENO_DB_REDUCED4;
	static String DRUG_CHG_PHENO_MS_REDUCED4;
	
	static String DOSE_DOWN_PHENO_EXPENDED;
	static String DOSE_UP_PHENO_EXPENDED;
	static String DRUG_CHG_PHENO_DB_EXPENDED;
	static String DRUG_CHG_PHENO_MS_EXPENDED;

	Connection sqliteCon = null;
	Connection sqlCon    = null;

	PreparedStatement insertAtcCode;
	PreparedStatement insertAtcMapping;
	PreparedStatement insertDrugChangePValue;
	PreparedStatement insertDrugIndication;
	PreparedStatement insertDrugEvent;
	PreparedStatement getRr1;
	PreparedStatement getRr2;
	PreparedStatement getRr3;
	PreparedStatement getRr4;
	PreparedStatement addPhenotype1r1;
	PreparedStatement addPhenotype2r1;
	PreparedStatement addPhenotype3r1;
	PreparedStatement addPhenotype4r1;
	PreparedStatement addPhenotype1r2;
	PreparedStatement addPhenotype2r2;
	PreparedStatement addPhenotype3r2;
	PreparedStatement addPhenotype4r2;
	PreparedStatement addPhenotype1r3;
	PreparedStatement addPhenotype2r3;
	PreparedStatement addPhenotype3r3;
	PreparedStatement addPhenotype4r3;
	PreparedStatement addPhenotype1r4;
	PreparedStatement addPhenotype2r4;
	PreparedStatement addPhenotype3r4;
	PreparedStatement addPhenotype4r4;
	PreparedStatement addPhenotype1e;
	PreparedStatement addPhenotype2e;
	PreparedStatement addPhenotype3e;
	PreparedStatement addPhenotype4e;
	PreparedStatement addExpendedPhenotype;
	PreparedStatement getAllDescendants;
	PreparedStatement getAllAscendants;
	PreparedStatement getNbOfNotes;
	PreparedStatement getNbOfVisits;
	PreparedStatement getNbOfLabs;
	PreparedStatement addIc;	
	PreparedStatement addIcdIc;
	PreparedStatement addLabIc;
	PreparedStatement getPhenotypeAnnotations4Class1;
	PreparedStatement getPhenotypeAnnotations4Class2;
	PreparedStatement getPhenotypeAnnotations4Class11;
	PreparedStatement getPhenotypeAnnotations4Class12;
	PreparedStatement getPhenotypeAnnotations4Class21;
	PreparedStatement getPhenotypeAnnotations4Class22;
	PreparedStatement getPhenotypeAnnotations4Class31;
	PreparedStatement getPhenotypeAnnotations4Class32;

	
	public Stride6Api6(String patientBase) throws SQLException, ClassNotFoundException {
		EHR_BASE=patientBase;
        Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}
	
	public Stride6Api6(String patientBase, String atc) throws SQLException, ClassNotFoundException {
		EHR_BASE=patientBase;
		ATC_BASE = atc;
        Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}

	public Stride6Api6(String patientBase, String annotBase,
			String reducedAnnotBase) throws SQLException, ClassNotFoundException {
		EHR_BASE=patientBase;
		ANNOT_TABLE = annotBase;
		REDUCED_ANNOT_TABLE = reducedAnnotBase;
        Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}
	
	public Stride6Api6(String patientBase, String annotBase,
			String reducedAnnotBase, String expendedAnnotBase) throws SQLException, ClassNotFoundException {
		EHR_BASE=patientBase;
		ANNOT_TABLE = annotBase;
		REDUCED_ANNOT_TABLE = reducedAnnotBase;
		EXPENDED_ANNOT_TABLE = expendedAnnotBase;
        Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}

	public Stride6Api6(String patientBase, 
			String p1, String p2, String p3, String p4, 
			String p1r1, String p2r1, String p3r1, String p4r1, 
			String p1r2, String p2r2, String p3r2, String p4r2) throws ClassNotFoundException, SQLException {
		EHR_BASE=patientBase;	
		DOSE_DOWN_PHENO=p1;
		DOSE_UP_PHENO=p2;
		DRUG_CHG_PHENO_DB=p3;
		DRUG_CHG_PHENO_MS=p4;

		DOSE_DOWN_PHENO_REDUCED1=p1r1;
		DOSE_UP_PHENO_REDUCED1=p2r1;
		DRUG_CHG_PHENO_DB_REDUCED1=p3r1;
		DRUG_CHG_PHENO_MS_REDUCED1=p4r1;
		
		DOSE_DOWN_PHENO_REDUCED2=p1r2;
		DOSE_UP_PHENO_REDUCED2=p2r2;
		DRUG_CHG_PHENO_DB_REDUCED2=p3r2;
		DRUG_CHG_PHENO_MS_REDUCED2=p4r2;	
		
		Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}
	
	public Stride6Api6(String patientBase,  
			String p1r2, String p2r2, String p3r2, String p4r2, 
			String p1r3, String p2r3, String p3r3, String p4r3) throws ClassNotFoundException, SQLException {
		EHR_BASE=patientBase;	
		
		DOSE_DOWN_PHENO_REDUCED2=p1r2;
		DOSE_UP_PHENO_REDUCED2=p2r2;
		DRUG_CHG_PHENO_DB_REDUCED2=p3r2;
		DRUG_CHG_PHENO_MS_REDUCED2=p4r2;
		
		DOSE_DOWN_PHENO_REDUCED3=p1r3;
		DOSE_UP_PHENO_REDUCED3=p2r3;
		DRUG_CHG_PHENO_DB_REDUCED3=p3r3;
		DRUG_CHG_PHENO_MS_REDUCED3=p4r3;
		
		Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}
	
	public Stride6Api6(String patientBase,  
			String p1r3, String p2r3, String p3r3, String p4r3, 
			String p1e, String p2e, String p3e, String p4e, String e) throws ClassNotFoundException, SQLException {
		EHR_BASE=patientBase;	
				
		DOSE_DOWN_PHENO_REDUCED3=p1r3;
		DOSE_UP_PHENO_REDUCED3=p2r3;
		DRUG_CHG_PHENO_DB_REDUCED3=p3r3;
		DRUG_CHG_PHENO_MS_REDUCED3=p4r3;
		
		DOSE_DOWN_PHENO_EXPENDED=p1e;
		DOSE_UP_PHENO_EXPENDED=p2e;
		DRUG_CHG_PHENO_DB_EXPENDED=p3e;
		DRUG_CHG_PHENO_MS_EXPENDED=p4e;		
		Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}

	public Stride6Api6(String patientBase,  
			String p1r3, String p2r3, String p3r3, String p4r3, 
			String p1r4, String p2r4, String p3r4, String p4r4, String a, String b) throws ClassNotFoundException, SQLException {
		EHR_BASE=patientBase;	
				
		DOSE_DOWN_PHENO_REDUCED3=p1r3;
		DOSE_UP_PHENO_REDUCED3=p2r3;
		DRUG_CHG_PHENO_DB_REDUCED3=p3r3;
		DRUG_CHG_PHENO_MS_REDUCED3=p4r3;
		
		DOSE_DOWN_PHENO_REDUCED4=p1r4;
		DOSE_UP_PHENO_REDUCED4=p2r4;
		DRUG_CHG_PHENO_DB_REDUCED4=p3r4;
		DRUG_CHG_PHENO_MS_REDUCED4=p4r4;	
		
		Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);      
        this.prepareInitialStt();
	}

	/**
	*constructor for feature matrix construction
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	**/
	public Stride6Api6(String patientBase, String patientClass1Table,
			String patientClass2Table, String class1AnnotationTable,
			String class2AnnotationTable, String phenotypeProfileTable,
			String upOrDown) throws ClassNotFoundException, SQLException {
		EHR_BASE=patientBase;	
		
		PATIENT_CLASS1_TABLE = patientClass1Table;
		PATIENT_CLASS2_TABLE = patientClass2Table;
		CLASS1_ANNOTATION_TABLE = class1AnnotationTable;
		CLASS2_ANNOTATION_TABLE = class2AnnotationTable;
		PHENOTYPE_PROFILE_TABLE = phenotypeProfileTable;		
		DOSE_CHG_DIRECTION = upOrDown;

		Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB/*+AUTO_RECONNECT*/, USER_NAME, USER_PSWD);  
        this.prepareInitialStt();
	}

	public Stride6Api6(String patientBase, 
			String patientClass1Table, String patientClass2Table, 
			String class11AnnotationTable, String class12AnnotationTable, 
			String class21AnnotationTable, String class22AnnotationTable, 
			String class31AnnotationTable, String class32AnnotationTable, 
			String phenotypeProfileTable, String icdProfileTable, String labProfileTable, 
			String upOrDown, Object voidObject) throws SQLException, ClassNotFoundException {
		EHR_BASE=patientBase;	
		
		PATIENT_CLASS1_TABLE = patientClass1Table;
		PATIENT_CLASS2_TABLE = patientClass2Table;
		CLASS11_ANNOTATION_TABLE = class11AnnotationTable;
		CLASS12_ANNOTATION_TABLE = class12AnnotationTable;
		CLASS21_ANNOTATION_TABLE = class21AnnotationTable;
		CLASS22_ANNOTATION_TABLE = class22AnnotationTable;
		CLASS31_ANNOTATION_TABLE = class31AnnotationTable;
		CLASS32_ANNOTATION_TABLE = class32AnnotationTable;
		PHENOTYPE_PROFILE_TABLE = phenotypeProfileTable;		
		DOSE_CHG_DIRECTION = upOrDown;

		Class.forName(DRIVER);
        sqlCon = DriverManager.getConnection(DB_SERVER+DB/*+AUTO_RECONNECT*/, USER_NAME, USER_PSWD);  
        this.prepareInitialStt();
	}

	public void closeConnection() throws SQLException {
		sqlCon.close();
		this.closeSst();					
	}
	public void reopenConnection() throws SQLException, ClassNotFoundException {
		sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
		
		Class.forName(DRIVER);
	    sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);  
	    this.prepareInitialStt();
	}
	
	public void reopenConnectionIfNecessary() throws SQLException {
		// TODO Auto-generated method stub
		if(!sqlCon.isValid(0)){
			sqlCon.close();
			sqlCon = DriverManager.getConnection(DB_SERVER+DB, USER_NAME, USER_PSWD);
			this.prepareInitialStt();
		}
		
	}
	
	// prepare statement at the instantiation of the class
	private void prepareInitialStt() throws SQLException {
		
		insertAtcCode = sqlCon.prepareStatement("INSERT INTO "+ATC_BASE+" " 
				+ "VALUES (?, ?);");
		insertAtcMapping = sqlCon.prepareStatement("INSERT INTO user_x.atc2rxnorm " 
				+ "VALUES (?, ?);");
		insertDrugChangePValue = sqlCon.prepareStatement("INSERT IGNORE INTO "+REDUCED_ANNOT_TABLE+" VALUES (?,?,?);");
		insertDrugIndication = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_INDICATION_TABLE+" VALUES (?,?);");
		insertDrugEvent = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_EVENT_TABLE+" VALUES (?,?);");
		getRr1 = sqlCon.prepareStatement("SELECT rr FROM "+DOSE_DOWN_PHENO+" WHERE atc_code=? AND rxcui=? AND cui=?;");
		getRr2 = sqlCon.prepareStatement("SELECT rr FROM "+DOSE_UP_PHENO+" WHERE atc_code=? AND rxcui=? AND cui=?;");
		getRr3 = sqlCon.prepareStatement("SELECT rr FROM "+DRUG_CHG_PHENO_DB+" WHERE atc_code=? AND rxcui=? AND cui=?;");
		getRr4 = sqlCon.prepareStatement("SELECT rr FROM "+DRUG_CHG_PHENO_MS+" WHERE atc_code=? AND rxcui=? AND cui=?;");
		addPhenotype1r1 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_DOWN_PHENO_REDUCED1+" VALUES (?,?,?,?,?);");
		addPhenotype2r1 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_UP_PHENO_REDUCED1+" VALUES (?,?,?,?,?);");
		addPhenotype3r1 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_DB_REDUCED1+" VALUES (?,?,?,?,?);");
		addPhenotype4r1 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_MS_REDUCED1+" VALUES (?,?,?,?,?);");
		addPhenotype1r2 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_DOWN_PHENO_REDUCED2+" VALUES (?,?,?,?,?);");
		addPhenotype2r2 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_UP_PHENO_REDUCED2+" VALUES (?,?,?,?,?);");
		addPhenotype3r2 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_DB_REDUCED2+" VALUES (?,?,?,?,?);");		
		addPhenotype4r2 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_MS_REDUCED2+" VALUES (?,?,?,?,?);");
		addPhenotype1r3 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_DOWN_PHENO_REDUCED3+" VALUES (?,?,?,?,?,?);");
		addPhenotype2r3 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_UP_PHENO_REDUCED3+" VALUES (?,?,?,?,?,?);");
		addPhenotype3r3 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_DB_REDUCED3+" VALUES (?,?,?,?,?,?);");		
		addPhenotype4r3 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_MS_REDUCED3+" VALUES (?,?,?,?,?,?);");
		addPhenotype1r4 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_DOWN_PHENO_REDUCED4+" VALUES (?,?,?,?,?,?,?,?);");
		addPhenotype2r4 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_UP_PHENO_REDUCED4+" VALUES (?,?,?,?,?,?,?,?);");
		addPhenotype3r4 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_DB_REDUCED4+" VALUES (?,?,?,?,?,?,?,?);");		
		addPhenotype4r4 = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_MS_REDUCED4+" VALUES (?,?,?,?,?,?,?,?);");
		addPhenotype1e = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_DOWN_PHENO_EXPENDED+" VALUES (?,?);");
		addPhenotype2e = sqlCon.prepareStatement("INSERT IGNORE INTO "+DOSE_UP_PHENO_EXPENDED+" VALUES (?,?);");
		addPhenotype3e = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_DB_EXPENDED+" VALUES (?,?);");		
		addPhenotype4e = sqlCon.prepareStatement("INSERT IGNORE INTO "+DRUG_CHG_PHENO_MS_EXPENDED+" VALUES (?,?);");
		addExpendedPhenotype = sqlCon.prepareStatement("INSERT IGNORE INTO "+EXPENDED_ANNOT_TABLE+" VALUES (?,?);");
		getAllDescendants = sqlCon.prepareStatement("SELECT distinct cui1 FROM terminology4.isaclosure WHERE ontology=? AND cui2=? AND dist>0;");
		getAllAscendants = sqlCon.prepareStatement("SELECT distinct cui2 FROM terminology4.isaclosure WHERE ontology=? AND cui1=? AND dist>0 AND cui2!='CXXXXXXX' ORDER BY dist;");
		getNbOfNotes = sqlCon.prepareStatement("SELECT COUNT(DISTINCT tm.nid) FROM stride6.term_mentions tm, terminology4.str2cui c WHERE c.CUI IN (SELECT distinct i.cui1 FROM terminology4.isaclosure i WHERE i.cui2=? AND i.ontology='SNOMEDCT_US') AND c.tid=tm.tid");
		getNbOfVisits = sqlCon.prepareStatement("SELECT COUNT(DISTINCT v.visit_id) FROM stride6.visit_master v, user_x.icd2cui_m i WHERE i.cui IN (SELECT distinct isa.cui1 FROM terminology4.isaclosure isa WHERE isa.cui2=? AND isa.ontology='ICD9CM') AND i.code=v.code AND sab=\"ICD\";");
		getNbOfLabs = sqlCon.prepareStatement("SELECT COUNT(DISTINCT lab_id) FROM stride6.lab_results_shc WHERE component_id=?;");
		addIc = sqlCon.prepareStatement("INSERT IGNORE INTO user_x.stride6_ic VALUES (?,?);");
		addIcdIc = sqlCon.prepareStatement("INSERT IGNORE INTO user_x.stride6_icd_ic VALUES (?,?);");
		addLabIc = sqlCon.prepareStatement("INSERT IGNORE INTO user_x.stride6_lab_ic VALUES (?,?);");
		getPhenotypeAnnotations4Class1 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS1_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
		getPhenotypeAnnotations4Class2 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS2_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
		getPhenotypeAnnotations4Class11 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS11_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
		getPhenotypeAnnotations4Class12 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS12_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
		getPhenotypeAnnotations4Class21 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS21_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
		getPhenotypeAnnotations4Class22 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS22_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
		getPhenotypeAnnotations4Class31 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS31_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
		getPhenotypeAnnotations4Class32 = sqlCon.prepareStatement("SELECT DISTINCT cui FROM "+CLASS32_ANNOTATION_TABLE+" WHERE iid=? ORDER BY cui;");
	}

	// Closing PpStt
	public void closeSst() throws SQLException {
		insertAtcCode.close();
		insertAtcMapping.close();
		insertDrugChangePValue.close();
		insertDrugIndication.close();
		insertDrugEvent.close();
		getRr1.close();
		getRr2.close();
		getRr3.close();
		getRr4.close();
		addPhenotype1r1.close();
		addPhenotype2r1.close();
		addPhenotype3r1.close();
		addPhenotype4r1.close();
		addPhenotype1r2.close();
		addPhenotype2r2.close();
		addPhenotype3r2.close();
		addPhenotype4r2.close();
		addPhenotype1r3.close();
		addPhenotype2r3.close();
		addPhenotype3r3.close();
		addPhenotype4r3.close();
		addPhenotype1r4.close();
		addPhenotype2r4.close();
		addPhenotype3r4.close();
		addPhenotype4r4.close();
		addPhenotype1e.close();
		addPhenotype2e.close();
		addPhenotype3e.close();
		addPhenotype4e.close();
		addExpendedPhenotype.close();
		getAllDescendants.close();
		getAllAscendants.close();
		getNbOfNotes.close();
		getNbOfVisits.close();
		getNbOfLabs.close();
		addIc.close();
		addIcdIc.close();
		addLabIc.close();
		getPhenotypeAnnotations4Class1.close();
		getPhenotypeAnnotations4Class2.close();
		getPhenotypeAnnotations4Class11.close();
		getPhenotypeAnnotations4Class12.close();
		getPhenotypeAnnotations4Class21.close();
		getPhenotypeAnnotations4Class22.close();
		getPhenotypeAnnotations4Class31.close();
		getPhenotypeAnnotations4Class32.close();
	}
		
	public void createAtcTable(String tableName) throws SQLException {
		Statement stat = sqlCon.createStatement();
		
		stat.execute("DROP TABLE IF EXISTS "+tableName+";");	
		
		String query = "CREATE TABLE "+tableName+" ("
				+ "code char(7) NOT NULL, "
				+ "label varchar(255) NOT NULL, "
				+ "PRIMARY KEY pk (code), "
				+ "KEY code_ix(code)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";					
		
		stat.execute(query);		
		stat.close();	
	}

	public void addAtcCode(String c, String l) throws SQLException {
		insertAtcCode.setString(1, c);		
		insertAtcCode.setString(2, l);		
		
		//System.out.println(insertDrugChangePValue.toString());
		insertAtcCode.executeUpdate();
	}

	public void addMapping(String c, String rxcui) throws SQLException {
		insertAtcMapping.setString(1, c);		
		insertAtcMapping.setString(2, rxcui);		
		insertAtcMapping.executeUpdate();
		
	}

	public HashMap<String, Double> getAnnot(String ANNOT_TABLE) throws SQLException {
    	HashMap<String, Double> annotSet = new HashMap<String, Double>();

		Statement stat = sqlCon.createStatement();
		String query = "select atc_code, rxcui, cui, pvalue FROM "+ANNOT_TABLE+" WHERE pvalue<0.05 AND rr>=3.5;";
		ResultSet rs = stat.executeQuery(query);		
        while (rs.next()) {
        	annotSet.put(rs.getString(1)+"_"+rs.getInt(2)+"_"+rs.getString(3), rs.getDouble(4));	  
        }	    	
        rs.close();
		return annotSet;
	}	
	// get annotations for one drug solely 
	public HashMap<String, Double> getAnnotByDrug(String ANNOT_TABLE, int rxcui, String rr, String pv) throws SQLException {
    	HashMap<String, Double> annotSet = new HashMap<String, Double>();

		Statement stat = sqlCon.createStatement();
		String query = "select atc_code, rxcui, cui, pvalue FROM "+ANNOT_TABLE+" WHERE pvalue<="+pv+" AND rr>="+rr+" AND rxcui="+rxcui+" group by cui;"; //GROUP BY rxcui,cui ;";
		System.out.println(query);
		ResultSet rs = stat.executeQuery(query);		
        while (rs.next()) {
        	annotSet.put(rs.getString(1)+"_"+rs.getInt(2)+"_"+rs.getString(3), rs.getDouble(4));	  
        }	    	
        rs.close();
		return annotSet;
	}	
	
	// get annotations for a set of drug 
	public HashMap<String, Double> getAnnotByDrugClass(String ANNOT_TABLE, String c, String rr, String pv) throws SQLException {
	    	HashMap<String, Double> annotSet = new HashMap<String, Double>();			
	    	
			Statement stat = sqlCon.createStatement();
			String query = "select atc_code, rxcui, cui, pvalue FROM "+ANNOT_TABLE+" WHERE pvalue<="+pv+" AND rr>="+rr+" AND atc_code=\""+c+"\" group by cui;"; //GROUP BY rxcui,cui ;";
			//System.out.println(query);
			ResultSet rs = stat.executeQuery(query);		
	        while (rs.next()) {
	        	annotSet.put(rs.getString(1)+"_"+rs.getInt(2)+"_"+rs.getString(3), rs.getDouble(4));	  
	        }	    	
	        rs.close();
			return annotSet;
	}	
	
	// to store p-values
		public void createPhenotypeProfileTable(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
					+ "rxcui mediumint(8) NOT NULL, "
					+ "cui char(8) NOT NULL, "
					+ "pvalue double NOT NULL,"
					+ "PRIMARY KEY pk (rxcui, cui), "
					+ "KEY cui_ix(cui), "
					+ "KEY rxcui_ix (rxcui) "  				
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";					
			
			stat.execute(query);		
			stat.close();		
		}
		
		// create table for profiles after bonferroni's correction
		public void createPhenotypeProfileTableR1(String tableName, String reducedTableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+reducedTableName+" LIKE "+tableName+";";										
			
			stat.execute(query);		
			stat.close();		
		}		
		
		public void createPhenotypeProfileTable2(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
					+ " atc_code char(11) NOT NULL DEFAULT '',"
					+ "rxcui mediumint(8) NOT NULL, "
					+ "cui char(8) NOT NULL, "
					+ "pvalue double NOT NULL,"
					+ " rr double DEFAULT NULL,"
					+ "PRIMARY KEY pk (atc_code, rxcui, cui), "
					+ "KEY atc_ix (atc_code),"
					+ "KEY cui_ix(cui), "
					+ "KEY rxcui_ix (rxcui) "  				
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";					
			
			stat.execute(query);		
			stat.close();		
		}
		
		public void createExpendedPhenotypeProfileTable(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
					+ "rxcui mediumint(8) NOT NULL, "
					+ "cui char(8) NOT NULL, "
					+ "PRIMARY KEY pk (rxcui, cui), "
					+ "KEY cui_ix(cui), "
					+ "KEY rxcui_ix (rxcui) "  				
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";					
			
			stat.execute(query);		
			stat.close();		
		}
		
		public void createGroupPhenotypeProfileTable(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
					+ "class varchar(15) NOT NULL, "
					+ "cui char(8) NOT NULL, "
					+ "pvalue double NOT NULL,"
					+ "PRIMARY KEY pk (class, cui), "
					+ "KEY cui_ix(cui), "
					+ "KEY class_ix (class) "  				
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";					
			
			stat.execute(query);		
			stat.close();		
		}
		
		public void createPhenotypeProfileR3Table(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
					+ " atc_code char(11) NOT NULL DEFAULT '',"
					+ "phenotype varchar(255), "
					+ "cui char(8) NOT NULL, "
					+ "pvalue double NOT NULL, "
					+ "rr double DEFAULT NULL, "
					+ "publi_in_pubmed INTEGER DEFAULT NULL, "
					+ "PRIMARY KEY pk (atc_code, cui), "
					+ "KEY atc_ix (atc_code),"
					+ "KEY cui_ix(cui)"
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";							
			
			stat.execute(query);
			stat.close();	
		}
		
		public void createPhenotypeProfileR4Table(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
					+ " atc_code char(11) NOT NULL DEFAULT '',"
					+ "phenotype varchar(255), "
					+ "cui char(8) NOT NULL, "
					+ "pvalue double NOT NULL, "
					+ "rr double DEFAULT NULL, "
					+ "publi_in_pubmed INTEGER DEFAULT NULL, "
					+ "indication_rate DOUBLE DEFAULT NULL, "
					+ "event_rate DOUBLE DEFAULT NULL, "
					+ "PRIMARY KEY pk (atc_code, cui), "
					+ "KEY atc_ix (atc_code),"
					+ "KEY cui_ix(cui)"
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";							
			
			stat.execute(query);
			stat.close();	
		}
		
		public void createPhenotypeProfileETable(String tableName, String originalTable) throws SQLException {
			Statement stat = sqlCon.createStatement();
					
			String query = "CREATE TABLE IF NOT EXISTS "+tableName+" SELECT DISTINCT atc_code, cui FROM "+originalTable+";";			
			stat.execute(query);			
			//query = "ALTER TABLE "+tableName+" ADD PRIMARY KEY(atc_code, cui);";			
			//stat.execute(query);
			stat.close();	
		}
			
		
		public void addDrugChgPValue2d(String c , String cui,
				double pvalue) throws SQLException {

			insertDrugChangePValue.setString(1, c);
			insertDrugChangePValue.setString(2, cui);
			insertDrugChangePValue.setDouble(3, pvalue);		

			//System.out.println(insertDoseChangeAnnotExp.toString());
			insertDrugChangePValue.executeUpdate();
		}
		
		public void addPhenotype1r1(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype1r1.setString(1, atc);
			addPhenotype1r1.setString(2, rxcui);
			addPhenotype1r1.setString(3, cui);
			addPhenotype1r1.setDouble(4, pvalue);
			Double rr= this.getRr1(atc, rxcui, cui);
			addPhenotype1r1.setDouble(5, rr);

			addPhenotype1r1.executeUpdate();
		}
		public void addPhenotype2r1(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype2r1.setString(1, atc);
			addPhenotype2r1.setString(2, rxcui);
			addPhenotype2r1.setString(3, cui);
			addPhenotype2r1.setDouble(4, pvalue);
			Double rr= this.getRr2(atc, rxcui, cui);
			addPhenotype2r1.setDouble(5, rr);

			addPhenotype2r1.executeUpdate();
		}
		public void addPhenotype3r1(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype3r1.setString(1, atc);
			addPhenotype3r1.setString(2, rxcui);
			addPhenotype3r1.setString(3, cui);
			addPhenotype3r1.setDouble(4, pvalue);
			Double rr= this.getRr3(atc, rxcui, cui);
			addPhenotype3r1.setDouble(5, rr);

			addPhenotype3r1.executeUpdate();
		}
		public void addPhenotype4r1(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype4r1.setString(1, atc);
			addPhenotype4r1.setString(2, rxcui);
			addPhenotype4r1.setString(3, cui);
			addPhenotype4r1.setDouble(4, pvalue);
			Double rr= this.getRr4(atc, rxcui, cui);
			addPhenotype4r1.setDouble(5, rr);

			addPhenotype4r1.executeUpdate();
		}
		
		public void addPhenotype1r2(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype1r2.setString(1, atc);
			addPhenotype1r2.setString(2, rxcui);
			addPhenotype1r2.setString(3, cui);
			addPhenotype1r2.setDouble(4, pvalue);
			Double rr= this.getRr1(atc, rxcui, cui);
			addPhenotype1r2.setDouble(5, rr);

			addPhenotype1r2.executeUpdate();
		}
		public void addPhenotype2r2(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype2r2.setString(1, atc);
			addPhenotype2r2.setString(2, rxcui);
			addPhenotype2r2.setString(3, cui);
			addPhenotype2r2.setDouble(4, pvalue);
			Double rr= this.getRr2(atc, rxcui, cui);
			addPhenotype2r2.setDouble(5, rr);

			addPhenotype2r2.executeUpdate();
		}
		public void addPhenotype3r2(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype3r2.setString(1, atc);
			addPhenotype3r2.setString(2, rxcui);
			addPhenotype3r2.setString(3, cui);
			addPhenotype3r2.setDouble(4, pvalue);
			Double rr= this.getRr3(atc, rxcui, cui);
			addPhenotype3r2.setDouble(5, rr);

			addPhenotype3r2.executeUpdate();
		}
		public void addPhenotype4r2(String atc, String rxcui, String cui,
				double pvalue) throws SQLException {

			addPhenotype4r2.setString(1, atc);
			addPhenotype4r2.setString(2, rxcui);
			addPhenotype4r2.setString(3, cui);
			addPhenotype4r2.setDouble(4, pvalue);
			Double rr= this.getRr4(atc, rxcui, cui);
			addPhenotype4r2.setDouble(5, rr);

			addPhenotype4r2.executeUpdate();
		}
		
		private Double getRr1(String atc, String rxcui, String cui) throws SQLException {
			Double rr = 0.0;
			getRr1.setString(1, atc);
			getRr1.setString(2, rxcui);
			getRr1.setString(3, cui);
			ResultSet rs = getRr1.executeQuery();
			while (rs.next()) {
	        	rr = rs.getDouble(1);
	        	break;
	        }	    	
	        rs.close();			
			return rr;
		}
		private Double getRr2(String atc, String rxcui, String cui) throws SQLException {
			Double rr = 0.0;
			getRr2.setString(1, atc);
			getRr2.setString(2, rxcui);
			getRr2.setString(3, cui);
			ResultSet rs = getRr2.executeQuery();
			while (rs.next()) {
	        	rr = rs.getDouble(1);
	        	break;
	        }	    	
	        rs.close();			
			return rr;
		}
		private Double getRr3(String atc, String rxcui, String cui) throws SQLException {
			Double rr = 0.0;
			getRr3.setString(1, atc);
			getRr3.setString(2, rxcui);
			getRr3.setString(3, cui);
			ResultSet rs = getRr3.executeQuery();
			while (rs.next()) {
	        	rr = rs.getDouble(1);
	        	break;
	        }	    	
	        rs.close();			
			return rr;
		}
		private Double getRr4(String atc, String rxcui, String cui) throws SQLException {
			Double rr = 0.0;
			getRr4.setString(1, atc);
			getRr4.setString(2, rxcui);
			getRr4.setString(3, cui);
			ResultSet rs = getRr4.executeQuery();
			while (rs.next()) {
	        	rr = rs.getDouble(1);
	        	break;
	        }	    	
	        rs.close();			
			return rr;
		}

		public void createDrugIndicationTable(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
			
			//stat.execute("DROP TABLE IF EXISTS "+tableName+";");	
			
			String query = "CREATE TABLE  IF NOT EXISTS "+tableName+" (`rxcui` mediumint(8) DEFAULT NULL,"
					+ "  `indication` char(8) DEFAULT NULL,"
					+ " UNIQUE KEY `label` (`rxcui`,`indication`),"
					+ "KEY `rxcui` (`rxcui`),"
					+ "KEY `disease` (`indication`)"
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
			stat.execute(query);		
			stat.close();	
		}
		
		public void createDrugEventTable(String tableName) throws SQLException {
			Statement stat = sqlCon.createStatement();
			
			stat.execute("DROP TABLE IF EXISTS "+tableName+";");	
			
			String query = "CREATE TABLE "+tableName+" (`rxcui` mediumint(8) DEFAULT NULL,"
					+ "  `event` char(8) DEFAULT NULL,"
					+ " UNIQUE KEY `label` (`rxcui`,`event`), "
					+ "KEY `rxcui` (`rxcui`),"
					+ "KEY `disease` (`event`)"
					+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
			stat.execute(query);		
			stat.close();	
		}
		
		// returns cuis
		public ArrayList<Integer> getDrugsFromTable(String drugTable) throws SQLException {
			ArrayList<Integer> cui = new ArrayList<Integer>();
			Statement stat = sqlCon.createStatement();				
			ResultSet rs = stat.executeQuery("SELECT distinct rxcui FROM "+drugTable+" ORDER BY rxcui ASC;");		
			while (rs.next()) {
				if(rs.getInt(1)>0){
					cui.add(rs.getInt(1));
				}
			}		
			return cui;
		}


		public ArrayList<String> getIndications(String ingredientCui) throws SQLException {
			ArrayList<String> indics = new ArrayList<String>();
			Statement stat = sqlCon.createStatement();
			String query = "SELECT distinct i.indication "
					+ "FROM medispan._medispan_rxnorm_ingredients d, medispan.indication i "
					+ "WHERE d.ingredient='"+ingredientCui+"' AND d.rxnorm_cui=i.drug AND (i.source='01' OR source='02') ;";
			ResultSet rs = stat.executeQuery(query);
			//System.out.println(query);
			while (rs.next()) {
				if(rs.getString(1).length()>0){
					indics.add(rs.getString(1));
				}
			}		
			return indics;
		}
		
		public ArrayList<String> getEvents(String ingredientCui) throws SQLException {
			ArrayList<String> indics = new ArrayList<String>();
			Statement stat = sqlCon.createStatement();
			String query = "SELECT distinct e.event "
					+ "FROM medispan._medispan_rxnorm_ingredients d, medispan.event e "
					+ "WHERE d.ingredient='"+ingredientCui+"' AND d.rxnorm_cui=e.drug AND e.severity_level IN ('01', '02') ;";
			ResultSet rs = stat.executeQuery(query);
			System.out.println(query);
			while (rs.next()) {
				if(rs.getString(1).length()>0){
					indics.add(rs.getString(1));
				}
			}		
			return indics;
		}
		
		public void addDrugIndication(Integer rxcui , String cui) throws SQLException {

			insertDrugIndication.setInt(1, rxcui);
			insertDrugIndication.setString(2, cui);

			//System.out.println(insertDrugIndication.toString());
			insertDrugIndication.executeUpdate();
		}
		public void addDrugEvent(Integer rxcui , String cui) throws SQLException {

			insertDrugEvent.setInt(1, rxcui);
			insertDrugEvent.setString(2, cui);

			//System.out.println(insertDrugEvent.toString());
			insertDrugEvent.executeUpdate();
		}


		// returns rxcuis from a drug table or a drug view
		public ArrayList<Integer> getDrug(String drugTable) throws SQLException {
			ArrayList<Integer> drugs = new ArrayList<Integer>();
			Statement stat = sqlCon.createStatement();		
			ResultSet rs = stat.executeQuery("SELECT distinct rxcui FROM "+drugTable+" WHERE rxcui!=0 ORDER BY rxcui ASC;");		
			while (rs.next()) {
				drugs.add(rs.getInt(1));
			}		
			return drugs;
		}

		// get a set of phenotype characterizing a drug/dose change
		public ArrayList<String> getPhenotypeList(String phenoTable, int rxcui) throws SQLException {

			ArrayList<String> phenotype = new ArrayList<String>();
			Statement stat = sqlCon.createStatement();
			String query = "SELECT cui "
					+ "FROM "+phenoTable+" "
					+ "WHERE rxcui='"+rxcui+"';";
			ResultSet rs = stat.executeQuery(query);
			//System.out.println(query);
			while (rs.next()) {
				if(rs.getString(1).length()>0){
					phenotype.add(rs.getString(1));
				}
			}		
			return phenotype;
		}

		// get a phenotype profile
		public ArrayList<String> getPhenotypeProfile(String phenotypeProfileTable, String drugOrDrugClass, String singleDrug, int limit) throws SQLException {

			ArrayList<String> phenotype = new ArrayList<String>();			
			Statement stat = sqlCon.createStatement();
			
			String query = "";
			if(singleDrug.equals(CLASS)){
				query = "SELECT t.cui FROM (SELECT cui "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE atc_code='"+drugOrDrugClass+"' "
						+ "ORDER BY pvalue LIMIT "+limit+") as t ORDER BY t.cui;";										
	    	}else if(singleDrug.equals(SINGLE)){
				query = "SELECT t.cui FROM (SELECT cui "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE rxcui="+drugOrDrugClass+" "
						+ "ORDER BY pvalue LIMIT "+limit+") as t ORDER BY t.cui;";
			}
			ResultSet rs = stat.executeQuery(query);
			//System.out.println(query);
			while (rs.next()) {
				if(rs.getString(1).length()>0){
					phenotype.add(rs.getString(1));
				}
			}
			rs.close();
			return phenotype;
		}
		
		// get a phenotype profile
		public ArrayList<String> getPhenotypeProfile2(String phenotypeProfileTable, String drugOrDrugClass, String singleDrug, int limit, String orderBy) throws SQLException {

			ArrayList<String> phenotype = new ArrayList<String>();			
			Statement stat = sqlCon.createStatement();
			
			String query = "";
			if(singleDrug.equals(CLASS)){
				query = "SELECT t.cui FROM (SELECT DISTINCT cui "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE atc_code='"+drugOrDrugClass+"' "
						+ "ORDER BY "+orderBy+" ";
				if(orderBy.equals("rr") || orderBy.equals("ic")){
					query += "DESC ";
				}
				query += "LIMIT "+limit+") as t ORDER BY t.cui;";										
	    	}else if(singleDrug.equals(SINGLE)){
				query = "SELECT t.cui FROM (SELECT DISTINCT cui "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE rxcui="+drugOrDrugClass+" "
						+ "ORDER BY "+orderBy+" ";
				if(orderBy.equals("rr") || orderBy.equals("ic")){
					query += "DESC ";
				}
				query += "LIMIT "+limit+") as t ORDER BY t.cui;";
			}
			ResultSet rs = stat.executeQuery(query);
			System.out.println(query);
			while (rs.next()) {
				if(rs.getString(1).length()>0){
					phenotype.add(rs.getString(1));
				}
			}
			rs.close();
			return phenotype;
		}

		public void addExpendedPhenotype(String expPhenotypeTable, int rxcui,
				ArrayList<String> expendedPhenotype) throws SQLException {
			
			for(String cui:expendedPhenotype){
				addExpendedPhenotype.setInt(1, rxcui);		
				addExpendedPhenotype.setString(2, cui);		
				addExpendedPhenotype.executeUpdate();
			}
			
		}

		// to compute s&s
		public void createToxicityControlTable(String d) throws SQLException {
			Statement stat = sqlCon.createStatement();
			
			stat.execute("DROP TABLE IF EXISTS user_x."+d+"_toxicity_control;");	
			
			String query = "CREATE TABLE user_x."+d+"_toxicity_control ENGINE=MyISAM DEFAULT CHARSET=latin1 AS "
					+ "SELECT n.pid, m.nid, n.timeoffset, m.tid "
					+ "FROM collaborators.stride5_mgrep m, collaborators.stride5_note n "
					+ "WHERE tid IN ("
					+ "  select distinct t.tid "
					+ "  from terminology3.str2cid c, terminology3.tid2cid t "
					+ "  where c.grp=1 AND c.str LIKE '%"+d+"%' AND c.str NOT LIKE '%va drug interaction%' "
					+ "  and (c.str LIKE '%allergy%' OR c.str LIKE '%poisoning%' OR c.str LIKE '%adverse reaction%' OR c.str LIKE '%overdos%' OR c.str LIKE '%sensitiv%' OR c.str LIKE '%-induce%'OR c.str LIKE '%toxicity%' OR c.str LIKE '%contraindicated%' OR c.str LIKE '%side effect%') "
					+ "  AND t.suppress=0 and t.cid=c.cid) "
					+ "AND negated=0 AND familyHistory=0 AND m.nid=n.nid;";
			//System.out.println(query);
			stat.execute(query);		
			
			String query2 ="ALTER TABLE user_x."+d+"_toxicity_control ADD INDEX (pid);";
			stat.execute(query2);
			String query3 ="ALTER TABLE user_x."+d+"_toxicity_control ADD INDEX (timeoffset);";
			stat.execute(query3);
			
			stat.close();	
		}


		public void createDDPTable(String annotTable, String d, String rxcui, String rr, String pvalue) throws SQLException {
			Statement stat = sqlCon.createStatement();
			
			stat.execute("DROP TABLE IF EXISTS user_x."+d+"_ddp;");	
			
			String query = "CREATE TABLE user_x."+d+"_ddp ENGINE=MyISAM DEFAULT CHARSET=latin1 AS "
					+ "SELECT n.pid, m.nid, n.timeoffset, m.tid "
					+ "FROM collaborators.stride5_mgrep m, collaborators.stride5_note n, terminology3.tid2cid t, terminology3.str2cid c "
					+ "WHERE negated=0 AND familyHistory=0 AND m.nid=n.nid AND m.tid=t.tid AND t.cid=c.cid AND c.cui IN ("
					+ "  SELECT cui "
					+ "  FROM user_x."+annotTable+" WHERE rxcui="+rxcui+" AND rr >"+rr+" AND pvalue<"+pvalue+");";
			stat.execute(query);		
			
			String query2 ="ALTER TABLE user_x."+d+"_ddp ADD INDEX (pid);";
			stat.execute(query2);
			String query3 ="ALTER TABLE user_x."+d+"_ddp ADD INDEX (timeoffset);";
			stat.execute(query3);
			
			stat.close();	
		}
		public void createDDPTable(String annotTable, String d, String rxcui) throws SQLException {
			Statement stat = sqlCon.createStatement();
			
			stat.execute("DROP TABLE IF EXISTS user_x."+d+"_ddp;");	
			
			String query = "CREATE TABLE user_x."+d+"_ddp ENGINE=MyISAM DEFAULT CHARSET=latin1 AS "
					+ "SELECT n.pid, m.nid, n.timeoffset, m.tid "
					+ "FROM collaborators.stride5_mgrep m, collaborators.stride5_note n, terminology3.tid2cid t, terminology3.str2cid c "
					+ "WHERE negated=0 AND familyHistory=0 AND m.nid=n.nid AND m.tid=t.tid AND t.cid=c.cid AND c.cui IN ("
					+ "  SELECT cui "
					+ "  FROM user_x."+annotTable+" WHERE rxcui="+rxcui+");";
			stat.execute(query);		
			
			String query2 ="ALTER TABLE user_x."+d+"_ddp ADD INDEX (pid);";
			stat.execute(query2);
			String query3 ="ALTER TABLE user_x."+d+"_ddp ADD INDEX (timeoffset);";
			stat.execute(query3);
			
			stat.close();	
		}

		public void createTreatedTable(String d, String rxcui) throws SQLException {
			Statement stat = sqlCon.createStatement();
			
			stat.execute("DROP TABLE IF EXISTS user_x."+d+";");	
			
			String query = "CREATE TABLE user_x."+d+" ENGINE=MyISAM DEFAULT CHARSET=latin1 AS "
					+ "select p.pid, p.timeoffset "
					+ "from collaborators.stride5_prescription p, collaborators.stride5_ingredient i "
					+ "where i.rxcui="+rxcui+" AND i.ingr_set_id=p.ingr_set_id;";
			stat.execute(query);		
			
			String query2 ="ALTER TABLE user_x."+d+" ADD INDEX (pid);";
			stat.execute(query2);
			String query3 ="ALTER TABLE user_x."+d+" ADD INDEX (timeoffset);";
			stat.execute(query3);
			
			stat.close();	
		}


		public int getAll(String d) throws SQLException {
			int all=0;
			
			Statement stat = sqlCon.createStatement();
			String query = "select count(distinct d.pid) "
					+ "from user_x."+d+" d "
					+ "where d.pid NOT IN ("
					+ "  select distinct t2.pid "
					+ "  from user_x."+d+" d2, user_x."+d+"_toxicity_control t2 "
					+ "  where t2.timeoffset<d2.timeoffset "
					+ "  and d2.pid=t2.pid);";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				all=rs.getInt(1);
				break;
			}		
			stat.close();				

			return all;
		}


		public int getVpAndFn(String d) throws SQLException {
			int vpAndFn=0;
			
			Statement stat = sqlCon.createStatement();
			String query = "select count(distinct d.pid) "
					+ "from user_x."+d+" d, user_x."+d+"_toxicity_control t "
					+ "where d.pid=t.pid and d.`timeoffset`<=t.`timeoffset` and d.pid NOT IN ("
					+ "  select distinct t2.pid"
					+ "  from user_x."+d+" d2, user_x."+d+"_toxicity_control t2"
					+ "  where t2.timeoffset<d2.timeoffset and d2.pid=t2.pid);";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				vpAndFn=rs.getInt(1);
				break;
			}		
			stat.close();				

			return vpAndFn;
		}


		public int getVp(String d) throws SQLException {
			int vp=0;
			
			Statement stat = sqlCon.createStatement();
			String query = "select count(distinct d.pid) "
					+ "from user_x."+d+" d, user_x."+d+"_toxicity_control t, user_x."+d+"_ddp p "
					+ "where d.pid=t.pid and d.`timeoffset`<=t.`timeoffset` and d.pid=p.pid and d.`timeoffset`<=p.`timeoffset` and d.pid NOT IN ("
					+ "  select distinct t2.pid"
					+ "  from user_x."+d+" d2, user_x."+d+"_toxicity_control t2"
					+ "   where t2.timeoffset<d2.timeoffset and d2.pid=t2.pid);";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				vp=rs.getInt(1);
				break;
			}		
			stat.close();				

			return vp;
		}


		public int getFpAndVn(String d) throws SQLException {
			int fpAndVn=0;
			
			Statement stat = sqlCon.createStatement();
			String query = "select count(distinct d.pid) "
					+ "from user_x."+d+" d where d.pid NOT IN ("
					+ "   select distinct t2.pid"
					+ "   from user_x."+d+" d2, user_x."+d+"_toxicity_control t2"
					+ "   where d2.pid=t2.pid);";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				fpAndVn=rs.getInt(1);
				break;
			}		
			stat.close();				

			return fpAndVn;
		}


		public int getVn(String d) throws SQLException {
			int vn=0;
			
			Statement stat = sqlCon.createStatement();
			String query = "select count(distinct d.pid) "
					+ "from user_x."+d+" d "
					+ "where d.pid NOT IN ("
					+ "  select distinct t2.pid"
					+ "  from user_x."+d+" d2, user_x."+d+"_toxicity_control t2"
					+ "  where d2.pid=t2.pid) and d.pid NOT IN ("
					+ "   select distinct p.pid  from user_x."+d+" d3, user_x."+d+"_ddp p"
					+ "  where d3.`timeoffset`<=p.`timeoffset` and  d3.pid=p.pid);";
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				vn=rs.getInt(1);
				break;
			}		
			stat.close();				
			return vn;
		}

		// k is the number of phenotypes (annotated during a dose or drug change)
		public int getK(String annotationTable, String upOrDown, boolean singleDrug, ArrayList<Integer> mbr) throws SQLException {
			int k=0; // nb of phenotype observed in intervals after prescription of d (or one of d)
			
			String mbrStr = this.getListOfMembers(mbr);
				 
			Statement stat = sqlCon.createStatement();
			String query = "SELECT COUNT(DISTINCT cui) FROM ";
						
			if(annotationTable.equals(DOSE_CHG_ANNOT2)){
				if(upOrDown.equals(DOWN)){
					query += DOSE_CHG_ANNOT2+" a, "+DOSE_CHG+" c "
							+ "WHERE a.rxcui IN ("
							+ mbrStr
							+ ") AND a.iid=c.iid AND c.dose_down=1;";
				}else if(upOrDown.equals(UP)){
							query += DOSE_CHG_ANNOT2+" a, "+DOSE_CHG+" c "
									+ "WHERE a.rxcui IN ("
									+ mbrStr
									+ ") AND a.iid=c.iid AND c.dose_up=1;";
				}					
			}
			if(annotationTable.equals(DRUG_CHG_ANNOT_DB) && upOrDown.equals("")){
				query += DRUG_CHG_ANNOT_DB+" "
						+ "WHERE rxcui IN ("
						+ mbrStr
						+ ");";
			}
			if(annotationTable.equals(DRUG_CHG_ANNOT_MS) && upOrDown.equals("")){
				query += DRUG_CHG_ANNOT_MS+" "
						+ "WHERE rxcui IN ("
						+ mbrStr
						+ ");";
			}		
			//System.out.println(query);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				k=rs.getInt(1);
				break;
			}		
			stat.close();				
			return k;
		}
		
		// k is the number of phenotypes (annotated before a dose or drug change)
		public int getAnteK(String annotationTable, String upOrDown, boolean singleDrug, ArrayList<Integer> mbr) throws SQLException {
			int k=0; // nb of phenotype observed in intervals after prescription of d (or one of d)
			
			String mbrStr = this.getListOfMembers(mbr);
				 
			Statement stat = sqlCon.createStatement();
			String query = "SELECT COUNT(DISTINCT cui) FROM ";
						
			if(annotationTable.equals(DOSE_CHG_ANTE_ANNOT2)){
				if(upOrDown.equals(DOWN)){
					query += DOSE_CHG_ANTE_ANNOT2+" a, "+DOSE_CHG+" c "
							+ "WHERE a.rxcui IN ("
							+ mbrStr
							+ ") AND a.iid=c.iid AND c.dose_down=1;";
				}else if(upOrDown.equals(UP)){
							query += DOSE_CHG_ANTE_ANNOT2+" a, "+DOSE_CHG+" c "
									+ "WHERE a.rxcui IN ("
									+ mbrStr
									+ ") AND a.iid=c.iid AND c.dose_up=1;";
				}					
			}
			if(annotationTable.equals(DRUG_CHG_ANTE_ANNOT_DB) && upOrDown.equals("")){
				query += DRUG_CHG_ANTE_ANNOT_DB+" "
						+ "WHERE rxcui IN ("
						+ mbrStr
						+ ");";
			}
			if(annotationTable.equals(DRUG_CHG_ANTE_ANNOT_MS) && upOrDown.equals("")){
				query += DRUG_CHG_ANTE_ANNOT_MS+" "
						+ "WHERE rxcui IN ("
						+ mbrStr
						+ ");";
			}		
			System.out.println(query);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				k=rs.getInt(1);
				break;
			}		
			stat.close();				
			return k;
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
			stat.close();				
			return members;
		}
		
		// return the rxcui of drugs member of a P450 class c
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
			stat.close();				

			return members;
		}
		

		public void cleaningFirstStep(String annotationTable, String cleanAnnotationTable, String c, 
				double alphaBar, double rrMin) throws SQLException {
			
			Statement stat = sqlCon.createStatement();		

			String query = "INSERT INTO "+cleanAnnotationTable+" "
					+ "SELECT * FROM "+annotationTable+" WHERE pvalue<="+alphaBar+" AND rr>="+rrMin+" AND atc_code=\""+c+"\""
					+ ";";
			System.out.println(query);
			stat.execute(query);		
			stat.close();				
		}

			// get all atc codes
			public ArrayList<String> getDrugClasses() throws SQLException {
				ArrayList<String> dc = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("(SELECT DISTINCT atc_code "
						+ "FROM "+DOSE_DOWN_PHENO_EXPENDED+") UNION" 
						+ "(SELECT DISTINCT atc_code "
						+ "FROM "+DOSE_UP_PHENO_EXPENDED+") UNION "
						+ "(SELECT DISTINCT atc_code "
						+ "FROM "+DRUG_CHG_PHENO_DB_EXPENDED+");");
				while (rs.next()) {
					dc.add(rs.getString(1));
				}
				stat.close();				

				return dc;
			}

			// create the specificity and sensitivity table
			public void createSpeAndSensTable(String tableName) throws SQLException {
				Statement stat = sqlCon.createStatement();
				
				String query = "CREATE TABLE IF NOT EXISTS "+tableName+" ("
						+ "atc_code char(11) NOT NULL DEFAULT '', "
						+ "chg char(10) NOT NULL, "
						+ "n1 int(8), "
						+ "n2 int(8), "
						+ "m1 int(8), "
						+ "m2 int(8), "
						+ "sensitivity double,"
						+ "specificity double,"
						+ "PRIMARY KEY pk (atc_code, chg), "
						+ "KEY atc_ix (atc_code),"
						+ "KEY chg_ix(chg) "
						+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";					
				//System.out.println(query);
				stat.execute(query);		
				stat.close();					
			}

			// add a sens and spe to the corresponding table
			public void addSpeAndSens(String sensSpeTable, String atc, String table, int n1,
					int n2, int m1, int m2, double sens, double spe) throws SQLException {
				
				if(Double.isNaN(sens))
					sens=0.0;
					
				if(Double.isNaN(spe))
					spe=0.0;
				
				String change = table.substring(table.indexOf(".")+1, table.indexOf("_phenotype"));//index of the second _				
				Statement stat = sqlCon.createStatement();				
				String query = "INSERT IGNORE INTO "+sensSpeTable+" VALUES ("
						+ "'"+atc+"', "
						+ "'"+change+"', "
						+ n1+", "
						+ n2+", "
						+ m1+", "
						+ m2+", "
						+ sens+", "
						+ spe+");";	
				//System.out.println(query);
				stat.execute(query);		
				stat.close();										
			}

			// patient with 2 intervals with p of profile in the first interval
			public int getN1(String atc, String table) throws SQLException {
				System.out.println(atc);
				int n1=0;
				Statement stat = sqlCon.createStatement();		
				String query="";
				String prefix1 = "SELECT COUNT(DISTINCT i.pid) "
						+ "FROM ("
						+ "SELECT DISTINCT a.pid, a.iid "
						+ "FROM "+DOSE_CHG+" a, "+DOSE_CHG+" b "
						+ "WHERE a.pid=b.pid AND "
						+ "a.t2<=b.t1 AND ";
				String prefix2 = "SELECT COUNT(DISTINCT i.pid) "
						+ "FROM ("
						+ "SELECT DISTINCT a.pid, a.iid "
						+ "FROM "+DRUG_CHG2+" a, "+DRUG_CHG2+" b "
						+ "WHERE a.pid=b.pid AND "
						+ "a.t2<=b.t1 AND ";
				if(atc.equals("p450")){								// all p450 related drugs
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "a.dose_down=b.dose_down AND a.dose_down=1) "
								+ "AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";		
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "a.dose_up=b.dose_up AND a.dose_up=1) "
								+ "AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query = prefix2 + "a.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "b.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) ) "
								+ "AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}					
				}else if(atc.equals("substrate") || atc.equals("inhibitor") || atc.equals("inducer") ){ // relation types
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "a.dose_down=b.dose_down AND a.dose_down=1) "
								+ "AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";		
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "a.dose_up=b.dose_up AND a.dose_up=1) "
								+ "AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query = prefix2 + "a.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "b.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) ) "
								+ "AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}					
				}else if(atc.length()==1){ // ATC families
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "a.dose_down=b.dose_down AND a.dose_down=1) "
								+ "AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";		
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "a.dose_up=b.dose_up AND a.dose_up=1) "
								+ "AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query = prefix2 + "a.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "b.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) ) "
								+ "AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}
				}else{ // p450 enzyme name
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "a.dose_down=b.dose_down AND a.dose_down=1) "
								+ "AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";		
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query = prefix1 + "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "a.dose_up=b.dose_up AND a.dose_up=1) "
								+ "AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query = prefix2 + "a.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "b.rxcui1 IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) ) "
								+ "AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}					
				}
				//System.out.println(query);
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					n1=rs.getInt(1);
					break;
				}
				stat.close();
				return n1;
			}

			// patient with 2 continuation with p of profile in the first interval
			public int getN2(String atc, String table) throws SQLException {
				int n2=0;
				Statement stat = sqlCon.createStatement();		
				String query="SELECT COUNT(DISTINCT i.pid) "
						+ "FROM ("
						+ "SELECT DISTINCT a.pid, a.iid "
						+ "FROM "+CONTINUATION2+" a, "+CONTINUATION2+" b "
						+ "WHERE a.pid=b.pid AND "
						+ "a.t2<=b.t1 AND ";
				String core = "";
				if(atc.equals("p450")){	
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) "
								+ ") AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) "
								+ ") AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) ) "
								+ "AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}					
				}else if(atc.equals("substrate") || atc.equals("inhibitor") || atc.equals("inducer") ){ // relation types
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) "
								+ ") AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";		
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) "
								+ ") AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) "
								+ ") AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}					
				}else if(atc.length()==1){ // ATC families
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc)  "
								+ ") AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";		
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc)  "
								+ ") AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) "
								+ ") AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}
				}else{ // p450 enzyme name
					if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) "
								+ ") AS i, "+DOSE_DOWN_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";		
					}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) "
								+ ") AS i, "+DOSE_UP_PHENO_EXPENDED+" p, "+DOSE_CHG_ANNOT2+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
						query += "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
								+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) ) "
								+ "AS i, "+DRUG_CHG_PHENO_DB_EXPENDED+" p, "+DRUG_CHG_ANNOT_DB+" i2 "
								+ "WHERE p.atc_code='"+atc+"' AND p.cui=i2.cui AND i2.iid=i.iid";
					}
				}
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					n2=rs.getInt(1);
					break;
				}
				stat.close();
				return n2;
			}

			// compute m1
			public int getM1(String atc, String table) throws SQLException {
				int m1=0;
				Statement stat = sqlCon.createStatement();		
				String query="";
				String prefix1 = "SELECT COUNT(DISTINCT a.pid) "
						+ "FROM "+DOSE_CHG+" a, "+DOSE_CHG+" b "
						+ "WHERE a.pid=b.pid AND "
						+ "a.t2<=b.t1 AND ";
				String prefix2 = "SELECT COUNT(DISTINCT a.pid) "
						+ "FROM "+DRUG_CHG2+" a, "+DRUG_CHG2+" b "
						+ "WHERE a.pid=b.pid AND "
						+ "a.t2<=b.t1 AND ";
				String core = "";
				
				// what atc?
				if(atc.equals("p450")){								// all p450 related drugs
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
							+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc)";
					
				}else if(atc.equals("substrate") || atc.equals("inhibitor") || atc.equals("inducer") ){ // relation types
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
							+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc)";
				}else if(atc.length()==1){ // ATC families
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
							+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc)";
				}else{ // p450 enzyme name
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
									+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc)";
				}				
			
				//what table?
				if(table.equals(DOSE_DOWN_PHENO_EXPENDED)){
					query = prefix1 +  core + " AND "
							+ "a.dose_down=b.dose_down AND a.dose_down=1;";		
				}else if(table.equals(DOSE_UP_PHENO_EXPENDED)){
					query = prefix1 +  core + " AND "
							+ "a.dose_up=b.dose_up AND a.dose_up=1;";
				}else if(table.equals(DRUG_CHG_PHENO_DB_EXPENDED)){
					query = prefix2 +  core.replaceAll("a.rxcui", "a.rxcui1").replaceAll("b.rxcui", "b.rxcui1") + ";";
				}					

				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					m1=rs.getInt(1);
					break;
				}
				stat.close();
				return m1;
			}
			
			// compute m2
			public int getM2(String atc, String table) throws SQLException {
				int m2=0;
				Statement stat = sqlCon.createStatement();		
				String query="SELECT COUNT(DISTINCT a.pid) "
						+ "FROM "+CONTINUATION2+" a, "+CONTINUATION2+" b "
						+ "WHERE a.pid=b.pid AND "
						+ "a.t2<=b.t1 AND ";
				String core = "";

				
				// what atc?
				if(atc.equals("p450")){								// all p450 related drugs
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc) AND "
							+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL order by atc asc)";
					
				}else if(atc.equals("substrate") || atc.equals("inhibitor") || atc.equals("inducer") ){ // relation types
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc) AND "
							+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.relation_type='"+atc+"' order by atc asc)";
				}else if(atc.length()==1){ // ATC families
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc) AND "
							+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.atc LIKE '"+atc+"%' order by atc asc)";
				}else{ // p450 enzyme name
					core = "a.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc) AND "
									+ "b.rxcui IN (select distinct p450.rxcui from user_x.p450 where rxcui IS NOT NULL AND p450.p450='"+atc+"' order by atc asc)";
				}				
			
				query += core + ";";					

				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					m2=rs.getInt(1);
					break;
				}
				stat.close();
				return m2;
			}

			// get all atc codes
			public ArrayList<String> getAtcCode() throws SQLException {
				ArrayList<String> atc = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("(SELECT DISTINCT atc_code "
						+ "FROM "+DOSE_DOWN_PHENO_REDUCED3+") UNION" 
						+ "(SELECT DISTINCT atc_code "
						+ "FROM "+DOSE_UP_PHENO_REDUCED3+") UNION "
						+ "(SELECT DISTINCT atc_code "
						+ "FROM "+DRUG_CHG_PHENO_DB_REDUCED3+") UNION "
						+ "(SELECT DISTINCT atc_code "
						+ "FROM "+DRUG_CHG_PHENO_MS_REDUCED3+");");
				while (rs.next()) {
					atc.add(rs.getString(1));
				}
				stat.close();				

				return atc;
			}


			public ArrayList<Integer> getDrugsOfAtcClass(String drugTable, String atc) throws SQLException {
				ArrayList<Integer> drug = new ArrayList<Integer>();
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT rxcui "
						+ "FROM "+drugTable+" "
						+ "WHERE rxcui IS NOT NULL "; 
					
				if(atc.equals("p450")){								// all p450 related drugs
						//nothing -- no restriction
				}else if(atc.equals("substrate") || atc.equals("inhibitor") || atc.equals("inducer") ){ // relation types
						query+=" AND relation_type=\'"+atc+"\' "; 			
				}else if(atc.length()==1){ // ATC families
					query+=" AND atc LIKE \'"+atc+"%\' "; 	
				}else{ // p450 enzyme name
					query+=" AND p450=\'"+atc+"\' "; 	
				}
				query+="ORDER BY rxcui;";
				//System.out.println(query);
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					drug.add(rs.getInt(1));
				}
				stat.close();
				return drug;
			}

			// return a set of drugs indicated to treat a phenotype (according to Medispan)
			public ArrayList<Integer> getIndicatedDrugs(String cui) throws SQLException {
				ArrayList<Integer> drug = new ArrayList<Integer>();
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT rxcui "
						+ "FROM user_x.medispan_drug_indication "
						+ "WHERE indication='"+cui+"' " 
						+ "ORDER BY rxcui;";
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					drug.add(rs.getInt(1));
				}
				stat.close();
				return drug;
			}
			
			// return a set of drugs that may cause a phenotype (according to Medispan)
			public ArrayList<Integer> getCausalDrugs(String cui) throws SQLException {
				ArrayList<Integer> drug = new ArrayList<Integer>();
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT rxcui "
						+ "FROM user_x.medispan_drug_event "
						+ "WHERE event='"+cui+"' " 
						+ "ORDER BY rxcui;";
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					drug.add(rs.getInt(1));
				}
				stat.close();
				return drug;
			}

			public ArrayList<String> getIntervals(
					String intervalClass1Table, ArrayList<Integer> mbr, String change) throws SQLException {
				String prefix="";
				ArrayList<String> iid = new ArrayList<String>();

				String mbrStr = this.getListOfMembers(mbr);
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT iid "
						+ "FROM "+intervalClass1Table
						+ " WHERE ";
				if(change.equals(CONTI)){
					prefix="c_";//conti
					query+="rxcui IN("+mbrStr+") " 
							+ "ORDER BY iid";
				}else if(change.equals(DOWN)){
					prefix="dd_";//dose down
					query+="dose_down=1 AND rxcui IN("+mbrStr+") " 
							+ "ORDER BY iid"; 
				}else if(change.equals(UP)){
					prefix="du_";//dose up
					query += "dose_up=1 AND rxcui IN("+mbrStr+") " 
							+ "ORDER BY iid";
				}	
				query += ";";
																
				//System.out.println(query);
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					iid.add(prefix+rs.getInt(1));
				}
				stat.close();
							
				return iid;
			}

			public ArrayList<String> getPhenotypeAnnotations(boolean antePrescription, String i, String class1AnnotationTable, String class2AnnotationTable) throws SQLException{
					ArrayList<String> phenotype = new ArrayList<String>();
					ResultSet rs = null;						

					if(!antePrescription){
						if(i.startsWith("d")){
							getPhenotypeAnnotations4Class1.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
							rs= getPhenotypeAnnotations4Class1.executeQuery();
						}else if(i.startsWith("c")){
							getPhenotypeAnnotations4Class2.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
							rs= getPhenotypeAnnotations4Class2.executeQuery();
						}
					}
					while (rs.next()) {
						phenotype.add(rs.getString(1));
					}
					return phenotype;
			}	
			public ArrayList<String> getPhenotypeAnnotations(boolean antePrescription, String i) throws SQLException{
				ArrayList<String> phenotype = new ArrayList<String>();
				ResultSet rs = null;						

				if(!antePrescription){
					if(i.startsWith("d")){
						getPhenotypeAnnotations4Class11.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
						rs= getPhenotypeAnnotations4Class11.executeQuery();
					}else if(i.startsWith("c")){
						getPhenotypeAnnotations4Class12.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
						rs= getPhenotypeAnnotations4Class12.executeQuery();
					}
				}
				while (rs.next()) {
					phenotype.add(rs.getString(1));
				}
				return phenotype;
		}	
			public ArrayList<String> getIcds(boolean antePrescription, String i) throws SQLException{
				ArrayList<String> icd = new ArrayList<String>();
				ResultSet rs = null;						

				if(!antePrescription){
					if(i.startsWith("d")){
						getPhenotypeAnnotations4Class21.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
						rs= getPhenotypeAnnotations4Class21.executeQuery();
					}else if(i.startsWith("c")){
						getPhenotypeAnnotations4Class22.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
						rs= getPhenotypeAnnotations4Class22.executeQuery();
					}
				}
				while (rs.next()) {
					icd.add(rs.getString(1));
				}
				return icd;
			}	
			public ArrayList<String> getLabs(boolean antePrescription, String i) throws SQLException{
				ArrayList<String> lab = new ArrayList<String>();
				ResultSet rs = null;						

				if(!antePrescription){
					if(i.startsWith("d")){
						getPhenotypeAnnotations4Class31.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
						rs= getPhenotypeAnnotations4Class31.executeQuery();
					}else if(i.startsWith("c")){
						getPhenotypeAnnotations4Class32.setInt(1, Integer.parseInt(i.substring(i.indexOf("_")+1)));
						rs= getPhenotypeAnnotations4Class32.executeQuery();
					}
				}
				while (rs.next()) {
					lab.add(rs.getString(1));
				}
				return lab;
		}	

			// return the cui descendant of a cui according to the isaclosure defined in terminology4
			public ArrayList<String> getAllDescendants(String onto, String cui1) throws SQLException {
				ArrayList<String> desc = new ArrayList<String>();
				getAllDescendants.setString(1, onto);			
				getAllDescendants.setString(2, cui1);			
				ResultSet rs = getAllDescendants.executeQuery();
				while (rs.next()) {
					desc.add(rs.getString(1));
		        }	    	
		        rs.close();			
				return desc;
			}
			
			// return the cui descendant of a cui according to the isaclosure defined in terminology4
			public ArrayList<String> getAllAscendants(String cui1) throws SQLException {
				ArrayList<String> asc = new ArrayList<String>();
				getAllAscendants.setString(1, cui1);			
				ResultSet rs = getAllAscendants.executeQuery();
				while (rs.next()) {
					asc.add(rs.getString(1));
		        }	    	
		        rs.close();			
				return asc;
			}

			public ArrayList<String> getDrugClass(String phenotypeProfileTable) throws SQLException {
				ArrayList<String> d = new ArrayList<String>();				
				Statement stat = sqlCon.createStatement();		
				String query="SELECT distinct atc_code "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE rxcui=0 ORDER BY atc_code;";				
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					d.add(rs.getString(1));
				}
				rs.close();				
				stat.close();				
				return d;
			}
			public ArrayList<String> getSingleDrug(String phenotypeProfileTable) throws SQLException {
				ArrayList<String> d = new ArrayList<String>();				
				Statement stat = sqlCon.createStatement();						
				String query2="SELECT distinct rxcui "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE rxcui!=0 ORDER BY rxcui;";				
				ResultSet rs2 = stat.executeQuery(query2);
				while (rs2.next()) {
					d.add(rs2.getInt(1)+"");
				}
				rs2.close();
				stat.close();				
				return d;
			}
			public ArrayList<String> getDrugClassName(String phenotypeProfileTable, String icdProfileTable, String labProfileTable) throws SQLException {
				ArrayList<String> d = new ArrayList<String>();				
				Statement stat = sqlCon.createStatement();		
				String query="SELECT distinct atc_code "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE rxcui='0' "
						+ "UNION "
						+ "SELECT distinct atc_code "
						+ "FROM "+icdProfileTable+" "
						+ "WHERE rxcui='0'  "
						+ "UNION "
						+ "SELECT distinct atc_code "
						+ "FROM "+labProfileTable+" "
						+ "WHERE rxcui='0' ORDER BY atc_code "
						+ ";";				
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					d.add(rs.getString(1));
				}
				rs.close();				
				stat.close();				
				return d;
			}
			public ArrayList<String> getSingleDrugName(String phenotypeProfileTable, String icdProfileTable, String labProfileTable) throws SQLException {
				ArrayList<String> d = new ArrayList<String>();				
				Statement stat = sqlCon.createStatement();						
				String query2="SELECT distinct rxcui "
						+ "FROM "+phenotypeProfileTable+" "
						+ "WHERE rxcui!='0' "
						+ "UNION "
						+ "SELECT distinct rxcui "
						+ "FROM "+icdProfileTable+" "
						+ "WHERE rxcui!='0' "
						+ "UNION "
						+ "SELECT distinct rxcui "
						+ "FROM "+labProfileTable+" "
						+ "WHERE rxcui!='0';";	
				System.out.println(query2);
				ResultSet rs2 = stat.executeQuery(query2);
				while (rs2.next()) {
					d.add(rs2.getString(1));
				}
				rs2.close();
				stat.close();				
				return d;
			}

			// phenotype for which we want to compute IC. 
			public ArrayList<String> getPhenotype() throws SQLException {
				/*ArrayList<String> pheno = new ArrayList<String>();
				
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT t2.cui "
						+ "FROM user_x.phenotype_m t1, terminology4.str2cui t2 "
						+ "WHERE t1.cui=t2.cui AND t2.ontology='SNOMEDCT_US';";	
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					pheno.add(rs.getString(1));
				}
				rs.close();
				*/
				ArrayList<String> pheno = new ArrayList<String>();
				
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT CUI "
						+ "FROM terminology4.str2cui "
						+ "WHERE ontology='SNOMEDCT_US' ORDER BY CUI;";	
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					pheno.add(rs.getString(1));
				}
				rs.close();
				return pheno;
			}
			
			// phenotype for which we want to compute IC. 
			public ArrayList<String> getPhenotype(String onto) throws SQLException {
				/*ArrayList<String> pheno = new ArrayList<String>();
				
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT t2.cui "
						+ "FROM user_x.phenotype_m t1, terminology4.str2cui t2 "
						+ "WHERE t1.cui=t2.cui AND t2.ontology='SNOMEDCT_US';";	
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					pheno.add(rs.getString(1));
				}
				rs.close();
				*/
				ArrayList<String> pheno = new ArrayList<String>();
				
				Statement stat = sqlCon.createStatement();		
				String query="SELECT DISTINCT CUI "
						+ "FROM terminology4.str2cui "
						+ "WHERE ontology=\'"+onto+"\' AND suppress_umls='N' ORDER BY CUI;";	
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					pheno.add(rs.getString(1));
				}
				rs.close();
				return pheno;
			}

			// number of notes mentionning p or one of its kids in SNOMEDCT_US
			public int getNbOfNotesMentionning(String p) throws SQLException {
				int n=0;
				
				getNbOfNotes.setString(1, p);//cui of the phentoype			
				ResultSet rs = getNbOfNotes.executeQuery();
				while (rs.next()) {
					n=rs.getInt(1);
					break;
		        }	    	
		        rs.close();			
				
				return n;
			}
			
			// number of notes mentionning p or one of its kids in SNOMEDCT_US
			public int getNbOfVisitsWithIcdCode(String p) throws SQLException {
				int n=0;
				
				getNbOfVisits.setString(1, p);//cui of the phentoype			
				ResultSet rs = getNbOfVisits.executeQuery();
				while (rs.next()) {
					n=rs.getInt(1);
					break;
		        }	    	
		        rs.close();			
				
				return n;
			}
			// number of lab result associated with the lab component id p
			public int getNbOfLabResultWithLabComponent(Integer c) throws SQLException {
				int n=0;
				
				getNbOfLabs.setInt(1, c);//cui of the phentoype			
				ResultSet rs = getNbOfLabs.executeQuery();
				while (rs.next()) {
					n=rs.getInt(1);
					break;
		        }	    	
		        rs.close();			
				
				return n;
			}
						
			// create the local table that stores IC for snomedct phenotypes
			public void createIcTable(String icTableName) throws SQLException {
				Statement stat = sqlCon.createStatement();
				
				String query = "CREATE TABLE IF NOT EXISTS "+icTableName+" ("
						+ "cui char(8) PRIMARY KEY, "
						+ "ic double NOT NULL "
						+ ") ENGINE=MyISAM DEFAULT CHARSET=latin1;";			
				
				stat.execute(query);		
				stat.close();		
				
			}

			// call the prepared statement ot add a IC value for a snomedct phenotype value
			public void addIc(String p, Double ic) throws SQLException {
				addIc.setString(1, p);
				addIc.setDouble(2, ic);
				addIc.executeUpdate();				
			}
			// call the prepared statement ot add a IC value for a snomedct phenotype value
			public void addIcdIc(String p, Double ic) throws SQLException {
				addIcdIc.setString(1, p);
				addIcdIc.setDouble(2, ic);
				addIcdIc.executeUpdate();				
			}

			// get all the distinct values for existing lab component id
			public Collection<? extends Integer> getLabComponentIds(String labResultTable) throws SQLException {
				ArrayList<Integer> componentIds = new ArrayList<Integer>();				
				Statement stat = sqlCon.createStatement();						
				String query2="SELECT distinct component_id "
						+ "FROM "+labResultTable+" "
						+ "ORDER BY component_id;";				
				ResultSet rs2 = stat.executeQuery(query2);
				while (rs2.next()) {
					componentIds.add(rs2.getInt(1));
				}
				rs2.close();
				stat.close();				
				return componentIds;
			}

			public void addLabIc(String labComponent, Double ic) throws SQLException {
				addLabIc.setString(1, labComponent);
				addLabIc.setDouble(2, ic);
				addLabIc.executeUpdate();
			}
			
			public ArrayList<String> getATCClassMembers(String c) throws SQLException {
				ArrayList<String> d = new ArrayList<String>();				
				Statement stat = sqlCon.createStatement();						
				String query2="SELECT distinct drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE rxcui IS NOT NULL AND too_many_patients=0 AND atc LIKE \'"+c+"%\' ORDER BY rxcui;";	
				//System.out.println(query2);
				ResultSet rs2 = stat.executeQuery(query2);
				while (rs2.next()) {
					d.add(rs2.getString(1));
				}
				rs2.close();
				stat.close();				
				return d;
			}

			public ArrayList<String> getEnzymeClassMembers(String e) throws SQLException {
				ArrayList<String> d = new ArrayList<String>();				
				Statement stat = sqlCon.createStatement();						
				String query2="SELECT distinct drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE rxcui IS NOT NULL AND too_many_patients=0 AND p450=\'"+e+"\' ORDER BY rxcui;";	
				//System.out.println(query2);
				ResultSet rs2 = stat.executeQuery(query2);
				while (rs2.next()) {
					d.add(rs2.getString(1));
				}
				rs2.close();
				stat.close();				
				return d;
			}
			
			public ArrayList<String> getRelationClassMembers(String r) throws SQLException {
				ArrayList<String> d = new ArrayList<String>();				
				Statement stat = sqlCon.createStatement();						
				String query2="SELECT distinct drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE rxcui IS NOT NULL AND too_many_patients=0 AND relation_type=\""+r+"\" ORDER BY rxcui;";	
				//System.out.println(query2);
				ResultSet rs2 = stat.executeQuery(query2);
				while (rs2.next()) {
					d.add(rs2.getString(1));
				}
				rs2.close();
				stat.close();				
				return d;
			}
			
			public int getRxcui(String d) throws SQLException {
				int rxcui = 0;				
				Statement stat = sqlCon.createStatement();						
				String query2="SELECT distinct rxcui "
						+ "FROM user_x.p450 "
						+ "WHERE drug_name=\""+d+"\";";	
				//System.out.println(query2);
				ResultSet rs2 = stat.executeQuery(query2);
				while (rs2.next()) {
					rxcui=rs2.getInt(1);
					break;
				}
				rs2.close();
				stat.close();				
				return rxcui;
			}

			// get cui associated with a drug class
			public ArrayList<String> getDrugClassProfile(String profileTable, String c) throws SQLException {
				ArrayList<String> cui = new ArrayList<String>();
				
				Statement stat = sqlCon.createStatement();						
				String query="SELECT DISTINCT cui "
						+ "FROM "+profileTable+" "
						+ "WHERE rxcui=\'0\' AND atc_code=\""+c+"\";";
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					cui.add(rs.getString(1));
				}
				rs.close();
				stat.close();	
								
				return cui;
			}
			// get cui associated with a single drug
			public ArrayList<String> getSingleDrugProfile(String profileTable, String c) throws SQLException {
				ArrayList<String> cui = new ArrayList<String>();
				
				Statement stat = sqlCon.createStatement();						
				String query="SELECT DISTINCT cui "
						+ "FROM "+profileTable+" "
						+ "WHERE rxcui=\""+c+"\";";	
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					cui.add(rs.getString(1));
				}
				rs.close();
				stat.close();	
								
				return cui;
			}

			public ArrayList<String> getAllSingleDrugs(String profileTable) throws SQLException{
					
					ArrayList<String> dr = new ArrayList<String>();

					Statement stat = sqlCon.createStatement();						
					String query="SELECT DISTINCT rxcui "
							+ "FROM "+profileTable+" "
							+ "WHERE rxcui!=\'0\';";	
					ResultSet rs = stat.executeQuery(query);
					while (rs.next()) {
						dr.add(rs.getString(1));
					}
					rs.close();
					stat.close();	
									
					return dr;
			}
			
			
			// return the rxcui of all drugs member of a P450 class 
			public ArrayList<String> getAllSingleDrugs(String profileTable, String c) throws SQLException {
				ArrayList<String> members = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("SELECT DISTINCT drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE rxcui IS NOT NULL AND too_many_patients=0 "
						+ "ORDER BY drug_name ASC;");			
				while (rs.next()) {
					members.add(rs.getString(1));
				}
				stat.close();				
				return members;
			}
			
			// return the rxcui of all drugs member of a P450 class 
			public ArrayList<String> getP450DrugClassMember2(String c) throws SQLException {
				ArrayList<String> members = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("SELECT DISTINCT drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE rxcui IS NOT NULL AND too_many_patients=0 "
						+ "ORDER BY drug_name ASC;");			
				while (rs.next()) {
					members.add(rs.getString(1));
				}
				stat.close();				
				return members;
			}
			
			// return the rxcui of drugs member of a P450 class c
			public ArrayList<String> getP450DrugClassMemberByRole2(String c) throws SQLException {
				ArrayList<String> members = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("SELECT DISTINCT drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE relation_type=\""+c+"\" "
						+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
						+ "ORDER BY drug_name ASC;");
				while (rs.next()) {
					members.add(rs.getString(1));
				}
				stat.close();				

				return members;
			}

			// return the rxcui of drugs member of a P450 class c (with a role name)
			public ArrayList<String> getP450DrugClassMemberByAtcLevel2(String c) throws SQLException {
				ArrayList<String> members = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("SELECT DISTINCT drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE substring(atc,1,1)=\""+c+"\" "
						+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
						+ "ORDER BY drug_name ASC;");
				while (rs.next()) {
					members.add(rs.getString(1));
				}
				stat.close();				

				return members;
			}
			
			// return the rxcui of drugs member of a P450 class c (with a role name)
			public ArrayList<String> getP450DrugClassMemberByGene2(String c) throws SQLException {
				ArrayList<String> members = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("SELECT DISTINCT drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE p450=\""+c+"\" "
						+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
						+ "ORDER BY drug_name ASC;");
				while (rs.next()) {
					members.add(rs.getString(1));
				}
				stat.close();				

				return members;
			}
			
			// return the rxcui of drugs member of a P450 class c (with a role name)
			public ArrayList<String> getP450DrugClassMemberByRoleXAtc2(String c) throws SQLException {
				String [] tab = c.split("_");
				ArrayList<String> members = new ArrayList<String>();
				Statement stat = sqlCon.createStatement();		
				ResultSet rs = stat.executeQuery("SELECT DISTINCT drug_name "
						+ "FROM user_x.p450 "
						+ "WHERE relation_type=\""+tab[0]+"\" "
						+ "AND substring(atc,1,1)=\""+tab[1]+"\" "
						+ "AND rxcui IS NOT NULL AND too_many_patients=0 "
						+ "ORDER BY drug_name ASC;");
				while (rs.next()) {
					members.add(rs.getString(1));
				}
				stat.close();				

				return members;
			}

			public boolean isIndication(String d, String p) throws SQLException {
				Boolean isIndi = false;
				int res = 0;
				Statement stat = sqlCon.createStatement();						
				String query="SELECT COUNT(*) "
						+ "FROM user_x.medispan_drug_indication i, user_x.p450 p, user_x.str2cui_m s "
						+ "WHERE p.drug_name=\""+d+"\" AND p.rxcui=i.rxcui AND s.str=\""+p+"\" AND s.cui=i.indication;";	
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					res=rs.getInt(1);
					break;
				}
				if(res>0){
					isIndi=true;
				}
				rs.close();
				stat.close();	
					
				return isIndi;
			}
			
			public boolean isEvent(String d, String p) throws SQLException {
				Boolean isEvent = false;
				int res = 0;
				Statement stat = sqlCon.createStatement();						
				String query="SELECT COUNT(*) "
						+ "FROM user_x.medispan_drug_event e, user_x.p450 p, user_x.str2cui_m s "
						+ "WHERE p.drug_name=\""+d+"\" AND p.rxcui=e.rxcui AND s.str=\""+p+"\" AND s.cui=e.event;";	
				ResultSet rs = stat.executeQuery(query);
				while (rs.next()) {
					res=rs.getInt(1);
					break;
				}
				if(res>0){
					isEvent=true;
				}
				rs.close();
				stat.close();	
					
				return isEvent;
			}

			
			public void insertDrugClassIndicationAndEventRates(String profileTable, String c, String d,
					String p, double indicRate, double eventRate) throws SQLException {
				Statement stat = sqlCon.createStatement();
				
				String query = "UPDATE IGNORE "+profileTable+" SET indication_rate="+indicRate
						+ " , event_rate="+eventRate
						+ " WHERE atc_code=\""+c+"\""
						+ " AND rxcui=\""+d+"\""
						+ " AND cui=\""+p+"\""
						+ ";";		
				stat.execute(query);		
				stat.close();				
			}

			public void insertDrugIndicationAndEventRates(String profileTable,
					String d, String p, double indicRate,
					double eventRate) throws SQLException {
				Statement stat = sqlCon.createStatement();
				
				String query = "UPDATE IGNORE "+profileTable+" SET indication_rate="+indicRate
						+ " , event_rate="+eventRate
						+ " WHERE rxcui=\""+p+"\""
						+ " AND cui=\""+p+"\""
						+ ";";
				stat.execute(query);		
				stat.close();	
				
			}

			
}


