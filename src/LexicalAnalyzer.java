import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class LexicalAnalyzer {
	private String code;
	private int position = 0;
	private HashMap<String, Token> table;
	private boolean done = false;
	private int lineNum = 1;

	public LexicalAnalyzer(String code, HashMap<String, Token> table) {
		this.code = code;
		this.table = table;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public String getLineAt(int line){
		return code.split("\n")[line - 1];
	}
	
	public int getLineNum() {
		return lineNum;
	}

	public Token getNextToken() {

		Token nextToken = null;
		this.ignoreWhitespace();

		nextToken = this.tryGettingNum();
		if (nextToken != null)
			return nextToken;
		
		nextToken = this.tryGettingIdentifier();
		if (nextToken != null)
			return nextToken;

		nextToken = this.tryGettingOpSymbol();
		if (nextToken != null)
			return nextToken;

		//if still has an error
		if (nextToken == null) {
			System.out.println("Error at line : " + lineNum + " Ignoring symbol :" + String.valueOf(peek()));
			this.getNextChar();
			return this.getNextToken();
		}
		return null;
	}

	private void ignoreWhitespace() {
		while ((peek() <= ' ')) {
			if (String.valueOf(peek()).matches("\n")) {
				lineNum++;
			}
			this.getNextChar();
		}
		
	}

	private Token tryGettingOpSymbol() {
		if (peek() == ':') {
			if (this.getNextChar() == '=') {
				this.getNextChar();
				return new Token(":=", null);
			}
		}

		if (peek() == ';' || peek() == '+' 
				|| peek() == '-' || peek() == '*' 
				|| peek() == '^' || peek() == '(' || peek() == ')') {
			Token operator = new Token(String.valueOf(peek()), null);
			this.getNextChar();
			return operator;
		}

		return null;
	}

	// ********ID PARSING

	private Token tryGettingIdentifier() {
		if (peekHoldsLetter()) {
			String s = "";
			while (this.peekHoldsLetter() || this.peekHoldsDigit()) {
				s += String.valueOf(peek());
				this.getNextChar();
			}

			Token w = null;

			w = table.get(s);

			if (s.equals("end")) {
				done = true;
			}
			
			if (w != null) {
				return w;
			} else {
				Token newToken = new Token(Type.ID, s);
				table.put(s, newToken);
				return newToken;
			}
		}

		return null;
	}

	private boolean peekHoldsLetter() {
		return String.valueOf(peek()).matches("[a-zA-Z]");
	}

	// ******NUMERIC PARSING
	private Token tryGettingNum() {
		if (peekHoldsDigit()) {
			int v = 0;
			do {
				v = v * 10 + Integer.parseInt(String.valueOf(peek()));
				this.getNextChar();
			} while (peekHoldsDigit());

			return new Token(Type.NUM, v);
		}
		return null;
	}

	private boolean peekHoldsDigit() {
		return peek() >= '0' && peek() <= '9';
	}

	private char peek() {
		if (position >= code.length() && !done) {
			throw new RuntimeException("No end identifier at the end of the code!");
		}
		
		return code.charAt(position);
	}

	private char getNextChar() {
		position++;
		return code.charAt(position);
	}
}
