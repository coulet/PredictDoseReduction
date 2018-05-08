package loria.eutils;

/**
* @author Adrien Coulet
* @date 2016
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Eutils {

	 public static final String SERVICE_URL   = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi";
	 public static final String DB    = "pubmed";
	
	 // emtpy constructor
/*	 public void Eutils(){		 
	 }
	*/ 
	 /**
	  * return the nb of element that are returned for a query
	  */
	 public int getCount(String str1, String str2){
	     int count=0;
	     try {
	     	String targetURL=SERVICE_URL+"?db="+DB+"&term=\""+ URLEncoder.encode(str1, "UTF-8")+"\"+AND+\""+ URLEncoder.encode(str2, "UTF-8")+"\"";	     
	        URL my_couch = new URL(targetURL);
	        URLConnection con = my_couch.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                con.getInputStream()));
	        String inputLine;

	        while ((inputLine = in.readLine()) != null){
	        	if(inputLine.startsWith("<eSearchResult><Count>")){
	        		//System.out.println(inputLine);
	        		//System.out.println(inputLine.substring(inputLine.indexOf("<Count>")+7, inputLine.indexOf("</Count>")));
	        		count=Integer.parseInt(inputLine.substring(inputLine.indexOf("<Count>")+7, inputLine.indexOf("</Count>")));
	        	}
	        }
	            
	        in.close();
	    }
	    catch( Exception e ) {
	      e.printStackTrace();
	    }
	
	    return count;
	 }
	 
	
	 public static void main( String[] args ) {
	    System.out.println("*************QUERY eutils TEST ************ \n");
	    String str1="tacrolimus";
	    String str2  ="fibrosis of lung";
	    Eutils myClient = new Eutils();
	    //get nb of element answer of the query
	    int publiNb = myClient.getCount(str1, str2);

		System.out.println(publiNb+" publication(s) about "+str1+" and "+str2);
	}
}

