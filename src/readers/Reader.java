/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package readers;
import tablInEx.*;

public interface Reader {
	
	/**
	 * Inits the class with source which have to be read. Most often file path
	 *
	 * @param file_path the file_path
	 */
	public void init(String file_path);
	
	
	/**
	 * Read one article, which was initiated with path in init method.
	 *
	 * @return the article
	 */
	public Article Read();

}
