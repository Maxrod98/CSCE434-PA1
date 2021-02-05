import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class LexicalAnalyzer {
	private String code;
	private int position = 0;
	private HashMap<String, Token> table;
	private boolean done = false;
	private int lineNum = 1;
	
	Queue<Token> queue = new LinkedList<Token>();

	public LexicalAnalyzer(String code, HashMap<String, Token> table) {
		this.code = code;
		this.table = table;
		
		//fill the queue
		while (!done) {
			Token next = getNextToken();
			if (next != null) {
				queue.add(next);
			}
		}
	}
	
	public String getLineAt(int line){
		return code.split("\n")[line - 1];
	}
	
	public Token pop() {
		return queue.poll();
	}
	
	public int getLineNum() {
		return lineNum;
	}

	private Token getNextToken() {
		Token nextToken = null;
		ignoreWhitespace();

		nextToken = tryGettingNum();
		if (nextToken != null)
			return nextToken;
		
		nextToken = tryGettingIdentifier();
		if (nextToken != null)
			return nextToken;

		nextToken = tryGettingOpSymbol();
		if (nextToken != null)
			return nextToken;
		
		withinBounds(0);
		
		return null;
	}

	private void ignoreWhitespace() {
		while (withinBounds(0) && (peek() == ' ' || peek() == '\t' || peek() == '\n' )) {
			if (String.valueOf(peek()).matches("\n")) {
				lineNum++;
			}
			if (withinBounds(1)) {
				popNextChar();
			} else { 
				break;
			}
		}
	}

	private boolean withinBounds(int offset) {
		boolean withinBound = position < code.length() - offset;
		done = !withinBound;
		
		return withinBound; 
	}
	
	private Token tryGettingOpSymbol() {
		if (peek() == ':') {
			if (popNextChar() == '=') {
				popNextChar();
				return new Token(TokenType.ASSIGNMENT, null);
			}
		}

		if (peek() == ';' || peek() == '+' || peek() == '-'
				|| peek() == '*' || peek() == '^' || peek() == '(' 
				|| peek() == ')') {
			Token operator = new Token(String.valueOf(peek()), null);
			popNextChar();
			return operator;
		}

		return null;
	}
	
	private Token tryGettingIdentifier() {
		if (peekHoldsLetter()) {
			String s = "";
			while (peekHoldsLetter() || peekHoldsDigit()) {
				s += String.valueOf(peek());
				popNextChar();
			}

			Token w = null;

			w = table.get(s);
			if (s.equals("end")) {
				done = true;
			}
			
			if (w != null) {
				return w;
			} else {
				Token newToken = new Token(TokenType.ID, s);
				table.put(s, newToken);
				return newToken;
			}
		}
		return null;
	}

	private boolean peekHoldsLetter() {
		return String.valueOf(peek()).matches("[a-zA-Z]");
	}

	private Token tryGettingNum() {
		if (peekHoldsDigit()) {
			int v = 0;
			do {
				v = v * 10 + Integer.parseInt(String.valueOf(peek()));
				popNextChar();
			} while (peekHoldsDigit());

			return new Token(TokenType.NUM, v);
		}
		return null;
	}

	private boolean peekHoldsDigit() {
		return peek() >= '0' && peek() <= '9';
	}

	private char peek() {		
		return code.charAt(position);
	}

	private char popNextChar() {
		position++;
		return code.charAt(position);
	}
}
