package ExternalResourceHandlers;
import java.util.LinkedList;


public class ResourceRule {
	private String rule;
	private String caseMatch;
	LinkedList<String> commands = new LinkedList<String>();
	/**
	 * @return the rule
	 */
	public String getRule() {
		return rule;
	}
	/**
	 * @param rule the rule to set
	 */
	public void setRule(String rule) {
		this.rule = rule;
	}
	/**
	 * @return the caseMatch
	 */
	public String getCaseMatch() {
		return caseMatch;
	}
	/**
	 * @param caseMatch the caseMatch to set
	 */
	public void setCaseMatch(String caseMatch) {
		this.caseMatch = caseMatch;
	}

}
