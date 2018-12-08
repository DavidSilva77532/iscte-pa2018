package pt.iscte.pidesco.conventionchecker;

import java.util.ArrayList;

import pt.iscte.pidesco.conventionchecker.service.ConventionCheckerService;
import pt.iscte.pidesco.conventionchecker.visitor.Log;

public class ConventionCheckerServiceImpl implements ConventionCheckerService{
	private ArrayList<Log> errors;
	
	public ConventionCheckerServiceImpl () {
		
	}

	@Override
	public ArrayList<Log> getConventionErrors() {
		return errors;
	}

}
