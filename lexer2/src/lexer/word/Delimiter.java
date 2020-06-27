package lexer.word;

import lexer.Tag;
import lexer.Token;

/**
 * 用于保管分隔符号分别为
 * {} [] () ;
 * @author a1725
 */
public class Delimiter extends Token {
	public String lexeme = "";
	public Delimiter(String s, int tag) {
		super(tag);
		this.lexeme = s;
	}
	
	public String toString() {
		return String.format("< %s, 分隔符 >", this.lexeme);
	}
	
	public static final Delimiter
		leftBrace = new Delimiter("{", Tag.Delimiter),rightBrace = new Delimiter("}", Tag.Delimiter),
		leftSquare = new Delimiter("[", Tag.Delimiter),rightSquare = new Delimiter("]", Tag.Delimiter),
        leftRound = new Delimiter("(", Tag.Delimiter),rightROund = new Delimiter(")", Tag.Delimiter),
        semicolon = new Delimiter(";", Tag.Delimiter);
}
