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
<factor> -> <primary> <S>
<S> -> ^ <factor> <S> | e
<primary> -> <id> | <num> | ( < expr > )

 */

public class RecursiveParser {
	LexicalAnalyzer la = null;
	Token lookAhead = null;

	public RecursiveParser(String code, HashMap<String, Token> table) {
		la = new LexicalAnalyzer(code, table);
	}

	public void parse() {
		lookAhead = la.getNextToken();
	}

	private void program() {
		switch (lookAhead.getToken()) {
			case ("begin"): {
				match("begin"); stmt_list(); match("end");
				break;
			}
			default: {
				System.out.println("Syntax error: expected keyword 'begin' at line " + la.getLineNum());
			}
		}
	}
	
	private void stmt() {
		if (lookAhead.getToken() == "id") {
			match("id"); match(":="); expr();
		}
	}

	private void stmt_list() {
		stmt(); R();
	}

	private void R() {
		if (lookAhead.getToken() == ";") {
			match(";"); stmt(); R();
		}
	}

	private void expr() {
		term(); P();
	}

	private void P() {
		switch(lookAhead.getToken()) {
		case("+"):{
			match("+"); term(); P();
		}
		case ("-"): {
			match("-"); term(); P();
		}
		}
	}

	private void term() {
		factor(); Q();
	}
	
	private void Q() {
		switch(lookAhead.getToken()) {
		case("*"):{
			match("*"); factor(); Q();
		}
		case ("div"): {
			match("div"); factor(); Q();
		}
		case ("mod"): {
			match("mod"); factor(); Q();
		}
		}
	}
	
	private void factor() {
		
	}

	private void match(String terminal) {
		if (this.lookAhead.getToken() == terminal) {
			this.lookAhead = la.getNextToken();
		} else {
			System.out.println("Syntax error at line: " + la.getLineNum());
		}
	}

}
