package pa.iscde.conventionchecker.visitor;

import pa.iscde.conventionchecker.ext.LogExt;

public class Log implements LogExt {
	private String value;
	private int offset;
	private String message;
	private int line;
	private String fileName;
	
	public Log(String p_value, int p_offset, String message, int p_line, String p_filename) {
		this.value=p_value;
		this.offset = p_offset;
		this.message = message;
		this.line = p_line;
		this.fileName = p_filename;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public int getPosition() {
		return this.offset;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getLine() {
		return this.line;
	}
	
	public String getFilePath() {
		return this.fileName;
	}
}
