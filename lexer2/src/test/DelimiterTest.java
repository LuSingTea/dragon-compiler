package test;

import lexer.word.Delimiter;
import lexer.Tag;

public class DelimiterTest {
    public static void main(String[] args) {
        Delimiter delimiter1 = new Delimiter("{", Tag.Delimiter);
        Delimiter delimiter2 = new Delimiter("{", Tag.Delimiter);
        Delimiter delimiter3 = new Delimiter("[", Tag.Delimiter);
        Delimiter delimiter4 = new Delimiter("]", Tag.Delimiter);
        Delimiter delimiter5 = new Delimiter("(", Tag.Delimiter);
        Delimiter delimiter6 = new Delimiter(")", Tag.Delimiter);
        Delimiter delimiter7 = new Delimiter(";", Tag.Delimiter);

        System.out.println(delimiter1);
        System.out.println(delimiter2);
        System.out.println(delimiter3);
        System.out.println(delimiter4);
        System.out.println(delimiter5);
        System.out.println(delimiter6);
        System.out.println(delimiter7);
        System.out.println(Delimiter.leftBrace);
    }
}
