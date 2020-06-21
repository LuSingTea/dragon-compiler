package lexer;

/**
 * 处理浮点数
 * @author a1725
 *
 */
public class Real extends Token {
	public final float value;
	public Real(float v) {
		super(Tag.REAL);
		this.value = v;
	}
	@Override
	public String toString() {
		return "" + this.value;
	}
}
