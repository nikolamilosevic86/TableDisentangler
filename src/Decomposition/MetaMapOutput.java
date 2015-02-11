package Decomposition;

public class MetaMapOutput {
	private String ConceptID;
	private Object MatchedWords;
	private Object PositionalInfo;
	private String SemanticType;
	private String ConceptName;
	private String PreferedName;
	
	public MetaMapOutput()
	{
	
	}
	
	public MetaMapOutput(String conceptID, Object matchedWords,Object position,String semantic,String concept,String prefered)
	{
		setConceptID(conceptID);
		setMatchedWords(matchedWords);
		setPositionalInfo(position);
		setSemanticType(semantic);
		setConceptName(concept);
		setPreferedName(prefered);
	}

	public String getConceptID() {
		return ConceptID;
	}

	public void setConceptID(String conceptID) {
		ConceptID = conceptID;
	}

	public Object getMatchedWords() {
		return MatchedWords;
	}

	public void setMatchedWords(Object matchedWords) {
		MatchedWords = matchedWords;
	}

	public Object getPositionalInfo() {
		return PositionalInfo;
	}

	public void setPositionalInfo(Object positionalInfo) {
		PositionalInfo = positionalInfo;
	}

	public String getSemanticType() {
		return SemanticType;
	}

	public void setSemanticType(String semanticType) {
		SemanticType = semanticType;
	}

	public String getConceptName() {
		return ConceptName;
	}

	public void setConceptName(String conceptName) {
		ConceptName = conceptName;
	}

	public String getPreferedName() {
		return PreferedName;
	}

	public void setPreferedName(String preferedName) {
		PreferedName = preferedName;
	}

}
