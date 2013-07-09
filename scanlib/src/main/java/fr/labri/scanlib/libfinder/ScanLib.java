package fr.labri.scanlib.libfinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ScanLib {

	private static ScanLib instance = null;

	private static Session session = null;
	
	public static Map<String,String> database = new TreeMap<String, String>();

	private ScanLib() {
		//TODO
		try {
			 
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {
				public void startElement(String uri, String localName,String qName, 
		                Attributes attributes) throws SAXException {
					
				}
			};
		} catch (Exception e) {
		       e.printStackTrace();
		 }
		
		
	}

	public static ScanLib getInstance() {
		if (null == instance) { // Premier appel
			instance = new ScanLib();
		}
		return instance;
	}
	
	public static Set<String> nothing = new HashSet<String>();
	public static Map<String,String> index = new HashMap<String, String>();

	static int totalImport=0;
	static int totalProjets=0;
	
	public static Set<String> getLibraries(Set<String> imports) {
		//TODO 
		return new HashSet<String>();
	}
}
