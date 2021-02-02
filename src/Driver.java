import java.util.HashMap;

public class Driver {
	//TODO: create lexeme class 
	//TODO: create LexicalAnalyzer
	//TODO: more on will be discussed later
	
	
	public static void main(String[] args) {
		String code = FileUtils.readFile("C:\\Users\\Admin\\Documents\\TestFolder\\test.txt");
		
		
		
		Token current = null;
		
		HashMap<String, Token> table = new HashMap<String, Token>();
		table.put("begin", new Token("begin", null));
		table.put("end", new Token("end", null));
		table.put("div", new Token("div", null));
		table.put("mod", new Token("mod", null));
		
		LexicalAnalyzer la = new LexicalAnalyzer(code, table);
		
		while (!la.isDone()) {
			System.out.println(la.getNextToken());
		}
		
		System.out.println(la.lineNum);
	}

}
