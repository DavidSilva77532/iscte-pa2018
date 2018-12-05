package pt.iscte.pidesco.ConventionChecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class ConventionRules {
	private Map<String, String> rules;
	private final String FILELOCATION = ConventionCheckerView.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/rules.file";
	
	public ConventionRules() {
		rules = new HashMap<String, String>();
		readRulesFile(rules); 
	}
	
	public Map<String, String> getRules() {
		return rules;
	}
	
	/**
	 * Update the rule file accordingly
	 * @param key convention object name
	 * @param value rule to be applied to the object
	 */
	public void updateRule(String key, String value) {
		rules.put(key, value);
	}
	
	public int getSize() {
		return rules.size();
	}
	
	public Set<String> getKeys() {
		return rules.keySet();
	}
	
	/**
	 * Save the rules to the file
	 */
	public void saveFile() {
		try {
			BufferedWriter	writer = new BufferedWriter(new FileWriter(FILELOCATION));
			
			try {
				 for(String key : rules.keySet()) {
					 writer.append(key);
					 writer.append('=');
					 writer.append(rules.get(key));
					 writer.newLine();
				 }
			     
			 
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read Rule file to populate our plugin view
	 * 
	 * @param p_rules
	 */
	private void readRulesFile(Map<String, String> p_rules) {
		try {
			// Read file and populate our map
			Scanner scanner = new Scanner(new File(FILELOCATION));
			while(scanner.hasNextLine()) {
				String[] ruleFile = scanner.nextLine().trim().split("\\s*=\\s*");
				p_rules.put(ruleFile[0], ruleFile[1]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
