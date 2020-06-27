package lexer.operator;

import lexer.Tag;
import lexer.Token;

// 用于保留关系运算符
//  + - * /
public class LogicOp extends Token {
    public String lexeme = "";
    public LogicOp(String s, int t) {
        super(t);
        lexeme = s;
    }
    @Override
    public String toString() {
        return String.format("< %s, 逻辑运算符 >", "" + lexeme);
    }
    public static final LogicOp
        and = new LogicOp("&&", Tag.AND), or = new LogicOp("||", Tag.OR),
        not = new LogicOp("!", Tag.NOT);
}
