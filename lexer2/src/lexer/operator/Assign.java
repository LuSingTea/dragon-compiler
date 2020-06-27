package lexer.operator;

import lexer.Tag;
import lexer.Token;

/**
 * 用于保管赋值运算符
 * @author a1725
 *
 */
public class Assign extends Token {
	public String lexeme = "";
	public Assign(String s, int tag) {
		super(tag);
		this.lexeme = s;
	}
	
	public String toString() {
		return String.format("< %s, 赋值 >", this.lexeme);
	}
	
	public static final Assign
		assign = new Assign("=", Tag.ASSIGN);
	
}
