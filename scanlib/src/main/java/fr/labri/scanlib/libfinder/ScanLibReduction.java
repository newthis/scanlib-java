package fr.labri.scanlib.libfinder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScanLibReduction {

	private static ScanLibReduction instance = null;

	private static Session session = null;

	public static Map<String,List<String>> database = new HashMap<String, List<String>>();

	public static void main(String[] args) throws ParserConfigurationException, TransformerException {

		Configuration cfg = new Configuration();
		Properties props = new Properties();
		props.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		props.setProperty("hibernate.connection.url", "jdbc:h2:scanlib;LOCK_TIMEOUT=10000");
		props.setProperty("hibernate.connection.username", "SA");
		props.setProperty("hibernate.connection.password", "");
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		props.setProperty("hibernate.hbm2ddl.auto", "update");
		props.setProperty("hibernate.show_sql", "false");
		props.setProperty("hibernate.current_session_context_class", "thread");
		props.setProperty("hibernate.jdbc.batch_size", "20");
		cfg.mergeProperties(props);
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		session = cfg.buildSessionFactory(serviceRegistry).openSession();

		List<Object[]> res = session.createSQLQuery("Select * From REGEXP").list();
		for(Object[] obj  : res) {
			if(!database.containsKey(obj[0].toString()))
				database.put(obj[0].toString(), new ArrayList<String>());
			database.get(obj[0].toString()).add(obj[1].toString());
		}

		System.out.println(database.keySet().size());

		for(int i = 0 ; i < 10 ; i++) {
			for(String lib : database.keySet()) {
				if(database.get(lib).size()==1) {
					continue;
				}
				List<String> tmp = new ArrayList<String>(database.get(lib));
				String shortTest = longestCommonPrefix(tmp);
				if(!shortTest.isEmpty()) {
					boolean fail=false;
					for(String other : database.keySet()) {
						if(other.equals(lib)==false) {
							for(String othereg : database.get(other)) {
								if(othereg.startsWith(shortTest)) {
									fail=true;
									break;
								}
							}
						}
					}
					if(!fail) {
						database.get(lib).clear();
						database.get(lib).add(shortTest);
					}
				}
			}
		}
		
		int cpt = 0;
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("expressions");
		doc.appendChild(rootElement);
		for(String lib : database.keySet()) {

			// staff elements
			Element library = doc.createElement("Library");
			rootElement.appendChild(library);
	 
			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue(lib);
			library.setAttributeNode(attr);
			
			for(String imp : database.get(lib)) {
				Element regexp = doc.createElement("regex");
				regexp.appendChild(doc.createTextNode(imp.replaceAll("\\.","\\\\\\\\.")));
				library.appendChild(regexp);
			}	
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("scanlib-data.xml"));

		transformer.transform(source, result);
 
		System.out.println("File saved!");
		System.out.println("Il y a au final "+cpt+" imports");
	}


	public static String longestCommonPrefix(List<String> strs) {
		// Start typing your Java solution below
		// DO NOT write main() function
		if(strs.size()==0) return"";
		String shortest = strs.get(0);
		for(String str: strs){
			if(str.isEmpty()) return "";
			if(str.length()<shortest.length())
				shortest = str;
		}
		int length = shortest.length();
		while(true){
			boolean good = true;
			for(String str:strs){
				if(!str.substring(0,length).equals(shortest.substring(0,length))){
					length--;
					good =false;
					break;
				}
			}
			if(good) return shortest.substring(0,length);
			if(length==0) return "";
		}
	}
}
