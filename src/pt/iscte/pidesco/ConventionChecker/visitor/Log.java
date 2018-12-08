package pt.iscte.pidesco.conventionchecker.visitor;

public class Log {
	private String value;
	private int offset;
	private String message;
	private int line;
	
	public Log(String p_value, int p_offset, String message, int p_line) {
		this.value=p_value;
		this.offset = p_offset;
		this.message = message;
		this.line = p_line;
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
}
