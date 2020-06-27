package lexer.word;


import lexer.Tag;
import lexer.Token;

// 用户保留标识符例如
// a b aa
public class Id extends Keyword {

    public Id(String s, int tag) {
        super(s, tag);
    }

    public Id(String s) {
        super(s, Tag.ID);
    }
    public String toString() {
        return String.format("< %s, id >", lexeme);
    }
}
