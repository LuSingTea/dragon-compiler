package symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word {

	public int width = 0;
	public Type(String s, int tag, int w) {
		super(s, tag);
		this.width = w;
	}
	
	public static final Type
		Int = new Type("int", Tag.BASIC, 4),
		Float = new Type("float", Tag.BASIC, 8),
		Char = new Type("char", Tag.BASIC, 1),
		Bool = new Type("bool", Tag.BASIC, 1);
	
	public static boolean numeric(Type p) {
		if (p == Type.Char || p == Type.Int || p == Type.Float) {
			return true;
		}
		return false;
	}
	
	public static Type max(Type ty1, Type type2) {
		if (!numeric(ty1) || !numeric(type2)) {
			return null;
		}
		else if (ty1 == Type.Float || type2 == Type.Float) {
			return Type.Float;
		}
		else if (ty1 == Type.Int || type2 == Type.Int) {
			return Type.Int;
		}
		else return Type.Char;
				
	}
}
