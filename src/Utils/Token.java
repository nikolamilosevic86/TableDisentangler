package Utils;

public class Token {
	private String phrase;
	private String SemanticTypeShort="";
	private String SemanticType="";
	
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public String getSemanticTypeShort() {
		return SemanticTypeShort;
	}
	public void setSemanticTypeShort(String semanticTypeShort) {
		SemanticTypeShort = semanticTypeShort;
	}
	public String getSemanticType() {
		return SemanticType;
	}
	public void setSemanticType(String semanticType) {
		SemanticType = semanticType;
	}

}
