package pa.iscde.conventionchecker.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import pa.iscde.conventionchecker.view.ConventionCheckerView;


public class ConventionRules {
	private Map<String, String> rules;
	private final String FOLDERLOCATION = ConventionCheckerView.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/rules";
	private String currentProfile = "default";
	private String fileLocation = FOLDERLOCATION + "/" + currentProfile + ".file";

	/**
	 * Creates the rule object reading the default rules from the existing file
	 */
	public ConventionRules() {
		rules = new HashMap<String, String>();
		readRulesFile(); 
	}
	
	
	/**
	 * changes profile reading the rules for the new profile
	 * 
	 * @param p_profileName
	 */
	public void changeProfile(String p_profileName) {
		currentProfile = p_profileName;
		readRulesFile(); 
	}
	
	/**
	 * @return a map with all the rules
	 */
	public Map<String, String> getRules() {
		return rules;
	}
	
	/**
	 * Update the rule file accordingly
	 * 
	 * @param key convention object name
	 * @param value rule to be applied to the object
	 */
	public void updateRule(String key, String value) {
		rules.put(key, value);
	}
	
	/**
	 * @return the number of the rules
	 */
	public int getSize() {
		return rules.size();
	}
	
	/**
	 * @return the keys of the rules
	 */
	public Set<String> getKeys() {
		return rules.keySet();
	}
	
	public void createNewFile(String p_profileName, Map<String, String> p_rules) {
		try {
			BufferedWriter	writer = new BufferedWriter(new FileWriter(FOLDERLOCATION + "/" + p_profileName + ".file"));
			writer.write(""); // force create file if not exists
			try {
				 for(String key : p_rules.keySet()) {
					 writer.append(key + "=" + Objects.toString(p_rules.get(key), ""));
					 writer.newLine();
				 }

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the rules to the file
	 */
	public void saveFile() {
		try {
			BufferedWriter	writer = new BufferedWriter(new FileWriter(fileLocation));

			try {
				 for(String key : rules.keySet()) {
					 writer.append(key + "=" + rules.get(key));
					 writer.newLine();
				 }

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if the file already exists for a given profile
	 *
	 * @param p_profileName
	 */
	public boolean profileExists(String p_profileName) {
		File f = new File(FOLDERLOCATION + "/" + p_profileName + ".file");
		if(f.exists() && !f.isDirectory()) { 
		   return true;
		}

		return false;
	}
	
	/**
	 * Get all profiles for the combo box
	 * @return an array of profiles
	 */
	public String[] getProfiles() {
		String[] ret;
		ArrayList<String> list = new ArrayList();
		File folder = new File(FOLDERLOCATION);
		 for (File f : folder.listFiles()) {
		        if (!f.isDirectory()) {
		            list.add(f.getName().replaceFirst("[.][^.]+$", ""));
		        }
		    }
		 
		 ret = new String[list.size()];
		 
		 for(int i=0;i<ret.length;i++)
		    {  
		        ret[i] = list.get(i);  
		    }  
		 
		 return ret;
	}
	
	/**
	 * Read Rule file to populate our plugin view
	 *
	 */
	private void readRulesFile() {
		try {
			// Read file and populate our map
			Scanner scanner = new Scanner(new File(fileLocation));
			while(scanner.hasNextLine()) {
				String[] ruleFile = scanner.nextLine().trim().split("\\s*=\\s*");
				rules.put(ruleFile[0], ruleFile[1]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
