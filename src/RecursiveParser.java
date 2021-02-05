import java.awt.SystemColor;
import java.util.HashMap;

/*
Grammar: 

<program> -> begin <stmt_list> end
<stmt> -> <id> := <expr> | e
<stmt_list> -> <stmt> <R>
<R> -> ;<stmt> <R> | e
<expr> -> <term> <P>
<P> -> + <term> <P> | - <term> <P> | e
<term> -> <factor> <Q>
<Q> -> * <factor> <Q> | div <factor> <Q> | mod <factor> <Q> | e
<factor> -> <primary> <T>
<T> -> ^ <factor> | e
<primary> -> <id> | <num> | ( < expr > )
 */

public class RecursiveParser {
	LexicalAnalyzer la = null;
	Token lookAhead = null;
	String abstractMachineCode = "";
	Listener listener = null;

	public RecursiveParser(String code, HashMap<String, Token> table, Listener listener) {
		this.listener = listener;
		la = new LexicalAnalyzer(code, table);
	}

	public void parse() {
		lookAhead = la.pop();
		program();
	}

	private void program() {
		switch (lookAhead.getToken()) {
			case (TokenType.BEGIN): {
				match(TokenType.BEGIN); stmt_list(); exec(InstructionType.HALT); 
				listener.onFinished(abstractMachineCode);
				match(TokenType.END);
				break;
			}
			default: {
				System.out.println("Syntax error: expected keyword 'begin' at line " + la.getLineNum());
			}
		}
	}
	
	private void stmt() {
		if (lookAhead.getToken().equals(TokenType.ID)) {
			exec(InstructionType.LVALUE + " " + lookAhead.getAttribute());
			match(TokenType.ID); match(TokenType.ASSIGNMENT); expr();
		}
		exec("STO");
	}

	private void stmt_list() {
		stmt();  R();
	}

	private void R() {
		if (lookAhead.getToken().equals(TokenType.SEMICOLON)) {
			match(TokenType.SEMICOLON); stmt(); R();
		}
	}

	private void expr() {
		term(); P();
	}

	private void P() {
		switch(lookAhead.getToken()) {
		case(TokenType.SUM):{
			match(TokenType.SUM);  term(); exec(InstructionType.ADD); P(); 
			break;
		}
		
		case (TokenType.MINUS): {
			match(TokenType.MINUS);  term(); exec(InstructionType.SUB); P(); 
			break;
		}
		}
	}

	private void term() {
		factor(); Q();
	}
	
	private void Q() {
		switch(lookAhead.getToken()) {
		case(TokenType.MULTIPLY):{
			match(TokenType.MULTIPLY); factor();  exec(InstructionType.MPY); Q(); 
			break;
		}
		case (TokenType.DIVISION): {
			match(TokenType.DIVISION); factor(); exec(InstructionType.DIV); Q(); 
			break;
		}
		case (TokenType.MODULUS): {
			match(TokenType.MODULUS); factor(); exec(InstructionType.MOD); Q(); 
			break;
		}
		}
	}
	
	private void factor() {
		primary(); T();
	}
	
	private void T() {
		if (lookAhead.getToken().equals(TokenType.EXPONENT)) {
			match(TokenType.EXPONENT); factor();
			exec(InstructionType.POW);
		}
	}

	private void primary() {
		String name = String.valueOf(lookAhead.getAttribute());
		switch (lookAhead.getToken()) {
		case(TokenType.ID): {
			match(TokenType.ID);
			exec( InstructionType.RVALUE +  " " + name);
		break;
		}
		
		case(TokenType.NUM): {
			match(TokenType.NUM);
			exec(InstructionType.PUSH + " " +  name);
		break;
		}
		
		case (TokenType.START_PARENTHESES):{
			match(TokenType.START_PARENTHESES); expr(); match(TokenType.END_PARENTHESES);
		break;
		}
		
		default: {
			System.out.println("Error at line: " + la.getLineNum());
		}
		}
	}
	
	public void exec(String exec) {
		abstractMachineCode += exec + "\n";
	}

	private void match(String terminal) {
		if (this.lookAhead.getToken().equals(terminal)) {
			this.lookAhead = la.pop();
		} else {
			System.out.println(
					"Syntax error at line: " + la.getLineNum() + ", " 
					+ " Expected expression: " + terminal +  ", " +
					" Expression found: " + (lookAhead.getAttribute() == null ? lookAhead.getToken() : lookAhead.getAttribute()));
		}
	}

	public interface Listener {
		void onFinished(String abstractMachineInstr);
	}
	
}
