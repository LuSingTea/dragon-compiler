package com.fzu.test;

import com.fzu.grammer.Grammar;
import com.fzu.grammer.LR1Parser;

import java.util.ArrayList;

public class GrammerTest {
    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer("S->B B\nB->a B\nB->b");
        Grammar grammar = new Grammar(stringBuffer.toString());
        LR1Parser lr1Parser = new LR1Parser(grammar);
        ArrayList<String> inputs = new ArrayList<>();
        inputs.add("a");inputs.add("b"); inputs.add("a"); inputs.add("b");
        lr1Parser.parseCLR1();
        System.out.println(lr1Parser.accept2(inputs));
        System.out.println(lr1Parser.actionTableStr());
    }
}
