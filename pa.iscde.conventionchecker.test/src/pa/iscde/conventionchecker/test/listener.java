package pa.iscde.conventionchecker.test;

import java.util.ArrayList;

import pa.iscde.conventionchecker.service.ConventionCheckerListener;
import pa.iscde.conventionchecker.service.LogExt;

public class listener implements ConventionCheckerListener {

	@Override
	public void checkingFinished(ArrayList<LogExt> conventionErrors) {
		for(LogExt l : conventionErrors) {
			System.out.println(l.getValue() + " file: " + l.getFilePath());
		}
		
	}

}
