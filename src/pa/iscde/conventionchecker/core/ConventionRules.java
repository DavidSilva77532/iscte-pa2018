package pa.iscde.conventionchecker.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import pa.iscde.conventionchecker.view.ConventionCheckerView;


public class ConventionRules {
	private Map<String, String> rules = new HashMap<String, String>();;
	private String FOLDERLOCATION = ConventionCheckerView.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/rules";
	private String currentProfile = "default";
	private String fileLocation = FOLDERLOCATION + "/" + currentProfile + ".file";

	/**
	 * Creates the rule object reading the default rules from the existing file
	 * @return ConventionRules object that manages the rules
	 */
	public ConventionRules() {
		readRulesFile(); 
	}
	
	/**
	 * Creates the rule object, reading the default rules from the existing file
	 * @param p_folderLocation the location of the folder with the rule files stored
	 * @return ConventionRules object that manages the rules
	 */
	public ConventionRules(String p_folderLocation) {
		FOLDERLOCATION = p_folderLocation;
		fileLocation = FOLDERLOCATION + "/" + currentProfile + ".file";
		readRulesFile(); 
	}
	
	
	/**
	 * changes profile reading the rules for the new profile
	 * 
	 * @param p_profileName
	 */
	public void changeProfile(String p_profileName) {
		if (!currentProfile.equals(p_profileName)) {
			currentProfile = p_profileName;
			fileLocation = FOLDERLOCATION + "/" + currentProfile + ".file";
			readRulesFile(); 
		}
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
			String regex = "";
			
			while(scanner.hasNextLine()) {
				String[] ruleFile = scanner.nextLine().trim().split("\\s*=\\s*");
				
				regex = ""; // to avoid nulls in the file
				if(ruleFile.length>1)
					regex = ruleFile[1];
				
				rules.put(ruleFile[0], regex);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
