/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package InfoClassExtractor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;

import Utils.Utilities;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class InfoClassExtractionRule {

	public LinkedList<ExtractionRuleToken> Rule = new LinkedList<ExtractionRuleToken>();

	/**
	 * Instantiates a new info class extraction rule which transforms string rule into list of regex tokens with vicinity measures
	 *
	 * @param rule the rule
	 */
	public InfoClassExtractionRule(String rule) {
		InputStream is;
		try {
			is = new FileInputStream("my-token.bin");

			TokenizerModel model = new TokenizerModel(is);
			Tokenizer tokenizer = new TokenizerME(model);
			
			String[] tokens = tokenizer.tokenize(rule);
			for (int i = 0;i<tokens.length;i++) {
				int vicinity = 1;
				String token = tokens[i];
				if(i+3<tokens.length && tokens[i+1].equals("[")&& tokens[i+3]!=null && tokens[i+2]!=null && tokens[i+3].equals("]")&&Utilities.isNumeric(tokens[i+2]))
				{
					vicinity = Integer.parseInt(tokens[i+2]);
					i=i+3;
				}
				if (token.equals("%d")) {
					ExtractionRuleToken et = new ExtractionRuleToken(
							"\\b\\d*\\b", vicinity);
					Rule.add(et);
				} else if (token.equals("%f")) {
					ExtractionRuleToken et = new ExtractionRuleToken(
							"\\b\\d*[\\.,·]\\d*\\b", vicinity);
					Rule.add(et);
				}
				else
				{
					ExtractionRuleToken et = new ExtractionRuleToken(
							token, vicinity);
					Rule.add(et);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
