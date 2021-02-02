
public class Token {
	private String token;
	private Object attribute; //it is very general to accept strings, integers, whatever
	
	Token(String _token, Object _attribute){
		token = _token;
		attribute = _attribute;
	}
	
	@Override
	public String toString() {
		return "{ Token: " + token + ", attribute: " + attribute + "}";
	}
	

}

