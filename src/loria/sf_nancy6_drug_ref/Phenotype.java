package loria.sf_nancy6_drug_ref;

public class Phenotype {

	private String atcCode;
	private String preferredName;
	private String cui;
	private double pvalue;
	private double rr;
	private int publi;
	
	/**
	 * Constructor
	 */
	public Phenotype(String atc, String prefName, String cui, double p, double r){
		this.atcCode       = atc;
		this.preferredName = prefName;
		this.cui           = cui;
		this.pvalue        = p;
		this.rr            = r;
	}
	public Phenotype(String atc, String prefName, String cui, double p, double r, int pu){
		this.atcCode       = atc;
		this.preferredName = prefName;
		this.cui           = cui;
		this.pvalue        = p;
		this.rr            = r;
		this.publi         = pu;
	}
	public Phenotype(String atc, String cui){
		this.atcCode      = atc;
		this.cui          = cui;
	}

	/**
	 * methods
	 */
	public String toText(){
		return preferredName+", "+cui;
	}
	
	public String getAtc(){
		return atcCode;
	}
	public String getPrefName(){
		return preferredName;
	}
	public String getCui(){
		return cui;
	}
	public double getPvalue(){
		return pvalue;
	}
	public double getRr(){
		return rr;
	}
	public int getNbOfPubli(){
		return publi;
	}	
}
