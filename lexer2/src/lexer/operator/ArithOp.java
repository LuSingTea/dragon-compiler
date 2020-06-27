package lexer.operator;

import lexer.Tag;
import lexer.Token;

// 用于保留关系运算符
//  + - * /
public class ArithOp extends Token {
    public String lexeme = "";
    public ArithOp(String s, int t) {
        super(t);
        lexeme = s;
    }
    @Override
    public String toString() {
        return String.format("< %s, 算术运算符 >", "" + lexeme);
    }
    public static final ArithOp
        minus = new ArithOp("-", Tag.MINUS), plus = new ArithOp("+", Tag.PLUS),
        multiply = new ArithOp("*", Tag.MULTIPLY), divide = new ArithOp("/", Tag.DIVIDE);
}
