package pa.iscde.conventionchecker.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pa.iscde.conventionchecker.service.ConventionCheckerListener;
import pa.iscde.conventionchecker.service.LogExt;

public class listener implements ConventionCheckerListener {

	@Override
	public void checkingFinished(ArrayList<LogExt> conventionErrors) {
		Map<String, Integer> errMap = new HashMap();

		for(LogExt l : conventionErrors) {
			if (!errMap.containsKey(l.getFilePath()))
				errMap.put(l.getFilePath(), 0);
			
			errMap.put(l.getFilePath(), errMap.get(l.getFilePath()) + 1);
		}
		
		for(Map.Entry<String, Integer> entry : errMap.entrySet()) {
			System.out.println("File " + entry.getKey() + " with " + entry.getValue() + " errors");
		}
	}

}
