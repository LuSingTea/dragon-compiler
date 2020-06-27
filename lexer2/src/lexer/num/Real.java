package lexer.num;

import lexer.Tag;
import lexer.Token;

/**
 * 处理浮点数
 * @author a1725
 *
 */
// 代表实数
public class Real extends Token {
	public final float value;
	public Real(float v) {
		super(Tag.REAL);
		this.value = v;
	}
	@Override
	public String toString() {
		return String.format("< %s, real >", "" + value);
	}
}
