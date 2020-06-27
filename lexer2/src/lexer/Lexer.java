package lexer;

import lexer.num.Num;
import lexer.num.Real;
import lexer.operator.Assign;
import lexer.operator.BitOp;
import lexer.operator.LogicOp;
import lexer.operator.RelationOp;
import lexer.word.Delimiter;
import lexer.word.Id;
import lexer.word.Keyword;
import sun.awt.windows.WBufferStrategy;
import symbols.Type;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Lexer {
    public static int line = 1;
    char peek = ' ';
    Map words = new HashMap();
    public void reserve(Keyword w) {
        words.put(w.lexeme, w);
    }
    File file;
    Reader reader = null;
    public Lexer(String filename) {
        try {
            file = new File(filename);
            reader = new InputStreamReader(new FileInputStream(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        reserve(Keyword.IF);
        reserve(Keyword.ELSE);
        reserve(Keyword.DO);
        reserve(Keyword.WHILE);
        reserve(Keyword.BREAK);
        reserve(Keyword.True);
        reserve(Keyword.False);
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);

    }

    public Lexer() {
        try {
            File file = new File("test1.txt");
            reader = new InputStreamReader(new FileInputStream(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        reserve(Keyword.IF);
        reserve(Keyword.ELSE);
        reserve(Keyword.DO);
        reserve(Keyword.WHILE);
        reserve(Keyword.BREAK);
        reserve(Keyword.True);
        reserve(Keyword.False);
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);

    }
    void readch() throws IOException {
        // peek = (char)System.in.read();
        try {
            int temp;
            if ((temp = reader.read()) == -1)
                peek = '#';
            peek = (char) temp;
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    boolean readch(char c) throws IOException {
        readch();
        if (peek!=c) return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        // 掉过空格,制表符,换行
        // for (;;readch()) {
        //     if (peek==' ' || peek=='\t') continue;
        //     else if (peek=='\n') line = line+1;
        //     else break;
        // }
        while(true) {
            readch();
            if (peek==' ' || peek=='\t') continue;
            // 回车和换行
            else if (peek=='\n' || peek=='\r') line = line+1;
            else break;
        }

        switch (peek) {
            case '&':
                if (readch('&')) return LogicOp.and;
                else return BitOp.logicAnd;
            case '|':
                if (readch('|')) return LogicOp.or;
                else return BitOp.logicOr;
            case '=':
                if (readch('=')) return RelationOp.eq;
                else return Assign.assign;
            case '!':
                if (readch('=')) return RelationOp.ne;
                else return LogicOp.not;
            case '<':
                if (readch('=')) return RelationOp.le;
                else return RelationOp.less;
            case '>':
                if (readch('=')) return RelationOp.ge;
                else return RelationOp.greater;
            case '{':
                return Delimiter.leftBrace;
            case '}':
                return Delimiter.rightBrace;
            case '(':
                return Delimiter.leftRound;
            case ')':
                return Delimiter.rightROund;
            case '[':
                return Delimiter.leftSquare;
            case ']':
                return Delimiter.rightSquare;
            case ';':
                return Delimiter.semicolon;
        }
        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                readch();
            }while (Character.isDigit(peek));
            if (peek!='.') return new Num(v);
            float x = v;
            float d = 10;
            for (;;) {
                readch();
                if (!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10)/d;
                d = d*10;
            }
            return new Real(x);
        }

        // 这里就是识别关键字和id
        if (Character.isLetter(peek)) {
            StringBuilder b = new StringBuilder();
            do {
                b.append(peek);
                readch();
            }while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            // 这里有问题
            Keyword w = (Keyword)words.get(s);

            // 识别有没有已经存过的id或者说关键字了
            if (w!=null) return w;

            // 如果没有重新存一个,并且重新存得一定是id
            w = new Id(s);
            words.put(s, w);
            return w;
        }
        return null;
    }
}
