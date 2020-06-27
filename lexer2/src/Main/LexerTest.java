package Main;

import lexer.Lexer;
import lexer.Token;

import java.io.IOException;

public class LexerTest {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer("test3.txt");
        // 需要注意测试文件的格式
        for (;;) {
            Token token = lexer.scan();
            if (token==null) break;
            System.out.println(token);
        }
    }
}
