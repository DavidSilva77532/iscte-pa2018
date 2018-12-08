package pa.iscde.conventionchecker.core;

import java.util.ArrayList;

import pa.iscde.conventionchecker.service.ConventionCheckerService;
import pa.iscde.conventionchecker.visitor.Log;

public class ConventionCheckerServiceImpl implements ConventionCheckerService{
	private ArrayList<Log> errors;
	
	public ConventionCheckerServiceImpl () {
		
	}

	@Override
	public ArrayList<Log> getConventionErrors() {
		// TO-DO
		return errors;
	}

}
