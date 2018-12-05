package ASTVisitor;

public class Log {
	private String value;
	private int offset;
	private String message;
	
	public Log(String p_value, int p_offset, String message) {
		this.value=p_value;
		this.offset = p_offset;
		this.message = message;
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
}
