package pa.iscde.conventionchecker.ext;

public interface LogExt {
	
	/**
	 * @return the string associated with the variable that failed the convention check
	 */
	public String getValue();
	
	/**
	 * @return the offset position in the file
	 */
	public int getPosition();
	
	/**
	 * @return the error message displayed in the editor
	 */
	public String getMessage();
	
	/**
	 * @return the line in the file
	 */
	public int getLine();
	
	/**
	 * @return the complete file path of the file that has this convention error
	 */
	public String getFilePath();

}
