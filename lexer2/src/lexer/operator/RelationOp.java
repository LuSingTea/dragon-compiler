package lexer.operator;

import lexer.Tag;
import lexer.Token;

// 用于保留关系运算符
// <、>、<=、>=、==、!=
public class RelationOp extends Token {
    public String lexeme = "";
    public RelationOp(String s, int t) {
        super(t);
        lexeme = s;
    }

    @Override
    public String toString() {
        return String.format("< %s, 关系运算符 >", "" + lexeme);
    }

    public static final RelationOp
        less = new RelationOp("<", Tag.LESS),greater = new RelationOp(">", Tag.GREATER),
        le = new RelationOp("<=", Tag.LE),ge = new RelationOp(">=", Tag.GE),
        eq = new RelationOp("==", Tag.EQ),ne = new RelationOp("!=", Tag.NE);
}
