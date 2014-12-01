package json.gson;

import java.util.List;

public class RetrievalResult {

	private String questionId;
	private String questionBody;
	private String exact_answer;//
	private String ideal_answer;//
	private String type;//
	private List<String> documents;
	public String getExact_answer() {
		return exact_answer;
	}

	public void setExact_answer(String exact_answer) {
		this.exact_answer = exact_answer;
	}

	public String getIdeal_answer() {
		return ideal_answer;
	}

	public void setIdeal_answer(String ideal_answer) {
		this.ideal_answer = ideal_answer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Snippet> getSnippets() {
		return snippets;
	}

	public void setSnippets(List<Snippet> snippets) {
		this.snippets = snippets;
	}

	public void setQuestionBody(String questionBody) {
		this.questionBody = questionBody;
	}

	private List<String> concepts;
	private List<Triple> triples;
	private List<Snippet> snippets;

	public RetrievalResult(String questionId, String questionBody,
			List<String> concepts, List<String> documents, List<Triple> triples,List<Snippet> snippets,String exact_anwer,String ideal_answer) {
		super();
		this.questionId = questionId;
		this.questionBody = questionBody;
		this.concepts = concepts;
		this.documents = documents;
		this.triples = triples;
		this.snippets=snippets;
		this.exact_answer=exact_anwer;
		this.ideal_answer=ideal_answer;
		this.type="yesno";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((questionId == null) ? 0 : questionId.hashCode());
		result = prime * result
				+ ((questionBody == null) ? 0 : questionBody.hashCode());
		result = prime * result
				+ ((concepts == null) ? 0 : concepts.hashCode());
		result = prime * result
				+ ((documents == null) ? 0 : documents.hashCode());
		result = prime * result + ((triples == null) ? 0 : triples.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetrievalResult other = (RetrievalResult) obj;
		if (this.questionId == null) {
			if (other.questionId != null)
				return false;
		} else if (!questionId.equals(other.questionId))
			return false;
		if (questionBody == null) {
			if (other.questionBody != null)
				return false;
		} else if (!questionBody.equals(other.questionBody))
			return false;
		if (concepts == null) {
			if (other.concepts != null)
				return false;
		} else if (!concepts.equals(other.concepts))
			return false;
		if (documents == null) {
			if (other.documents != null)
				return false;
		} else if (!documents.equals(other.documents))
			return false;
		if (triples == null) {
			if (other.triples != null)
				return false;
		} else if (!triples.equals(other.triples))
			return false;
		return true;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String id) {
		this.questionId = id;
	}

	public String getQuestionBody() {
		return questionBody;
	}

	public void setBody(String body) {
		this.questionBody = body;
	}

	public List<String> getDocuments() {
		return documents;
	}

	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

	public List<String> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<String> concepts) {
		this.concepts = concepts;
	}

	public List<Triple> getTriples() {
		return triples;
	}

	public void setTriples(List<Triple> triples) {
		this.triples = triples;
	}
}