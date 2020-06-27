package lexer.num;

import lexer.Tag;
import lexer.Token;

// 代表整数
public class Num extends Token {
	public final int value;
	public Num(int v) {
		super(Tag.NUM);
		this.value = v;
	}
	
	public String toString() {
        return String.format("< %s, num >", "" + value);
    }
}
