package lexer.operator;

import lexer.Tag;
import lexer.Token;

// 用于保留位运算
// & |
public class BitOp extends Token {
    public String lexeme = "";
    public BitOp(String s, int t) {
        super(t);
        lexeme = s;
    }
    @Override
    public String toString() {
        return String.format("< %s, 位运算符 >", "" + lexeme);
    }
    public static final BitOp
            logicAnd = new BitOp("&", Tag.LOGICAND), logicOr = new BitOp("|", Tag.LOGICOR);
}
