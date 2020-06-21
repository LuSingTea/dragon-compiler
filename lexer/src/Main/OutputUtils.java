package Main;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import lexer.*;

public class OutputUtils {
    public static void outputToken(Token token) {
        if (token instanceof Word) {
            token = (Word)token;
            if (token.tag==Tag.IF || token.tag==Tag.WHILE || token.tag==Tag.ELSE ||
                    token.tag==Tag.DO || token.tag==Tag.BREAK) {
                System.out.println("<" + token + "," + " key" + ">");
            }
            else if (token == Word.and || token == Word.eq ||
                     token == Word.ne || token == Word.ge ||
                     token == Word.le || token == Word.or ) {
                System.out.println("<" + (char)token.tag + "," + " op" + ">");
            }
            else if (token == Word.True || token == Word.False){
                System.out.println("<" + token + "," + " id" + ">");
            }
            else {
                System.out.println("<" + token + "," + " id" + ">");
            }
        }
        else if ((token instanceof Real) || (token instanceof Num)) {
            System.out.println("<"+ token +","+" num>");
        }
        else {
            if ((char)token.tag=='{' || (char)token.tag=='}') {
                System.out.println("<" + (char)token.tag + "," + " 界符" + ">");
            }
            else if ((char)token.tag=='=') {
                System.out.println("<" + (char)token.tag + "," + " 赋值" + ">");
            }
            else if ((char)token.tag=='+' || (char)token.tag=='-' ||
                    (char)token.tag=='*' || (char)token.tag=='/') {
                System.out.println("<" + (char)token.tag + "," + " op" + ">");
            }
            else if ((char)token.tag=='>' || (char)token.tag=='<') {
                System.out.println("<" + (char)token.tag + "," + " relop" + ">");
            }
        }
    }
}
