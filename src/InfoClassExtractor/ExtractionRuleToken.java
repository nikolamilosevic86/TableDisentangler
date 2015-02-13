/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package InfoClassExtractor;

// TODO: Auto-generated Javadoc
/**
 * The Class ExtractionRuleToken.
 */
public class ExtractionRuleToken {
	
	/** The token. This is from rule transformed into regular expression. This is actually regex of token */
	public String token;
	
	/** The vicinity. How far this token could be from the previous token */
	public int vicinity = 1;
	
	/**
	 * Instantiates a new extraction rule token.
	 *
	 * @param tok the tok
	 * @param vinc the vinc
	 */
	public ExtractionRuleToken(String tok, int vinc)
	{
		vicinity = vinc;
		token = tok;
	}

}
