package com.fzu.test;

import com.fzu.grammer.LR1Item;

import java.util.HashSet;

public class LR1ItemTest {
    public static void main(String[] args) {
        HashSet<String> set = new HashSet<>();
        set.add("a");
        LR1Item a = new LR1Item("term",new String[]{"term", "*", "unary"}, 1, set);
        HashSet<String> set2 = new HashSet<>();
        set.add("a");set2.add("b");
        LR1Item b = new LR1Item("term",new String[]{"term", "*", "unary"}, 1, set2);
        System.out.println(a.equalLR0(b));
    }
}
