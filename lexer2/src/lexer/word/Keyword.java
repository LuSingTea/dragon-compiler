package lexer.word;

import lexer.Tag;
import lexer.Token;

import java.security.Key;

/**
 * 用于保管关键字
 * @author a1725
 *
 */
public class Keyword extends Token {
	public String lexeme = "";
	public Keyword(String s, int tag) {
		super(tag);
		this.lexeme = s;
	}
	
	public String toString() {
		return String.format("< %s, key >", this.lexeme);
	}
	
	public static final Keyword
        IF = new Keyword("if", Tag.IF), ELSE = new Keyword("else", Tag.ELSE),
        DO = new Keyword("do", Tag.DO), WHILE = new Keyword("while", Tag.WHILE),
        BREAK = new Keyword("break", Tag.BREAK),
		True = new Keyword("true", Tag.TRUE),
		False = new Keyword("false", Tag.FALSE),
		temp = new Keyword("t", Tag.TEMP); // 这个好像没用
	
}
