package com.fzu.test;

import com.fzu.grammer.Grammar;
import com.fzu.grammer.LR1Parser;

public class ParserTest {
    public static void main(String[] args) {
        String s =
                "program->block\n" +
                        "block->{ decls stmts }\n" +
                        "decls->decls decl\n" +
                        "decls->epsilon\n" +
                        "decl->type id ;\n" +
                        "type->type [ num ]\n" +
                        "type->basic\n" +
                        "stmts->stmts stmt\n" +
                        "stmts->epsilon\n" +
                        "stmt->loc = bool ;\n" +
                        "stmt->if ( bool ) stmt\n" +
                        "stmt->if ( bool ) stmt else stmt\n" +
                        "stmt->while ( bool ) stmt\n" +
                        "stmt->do stmt while ( bool ) ;\n" +
                        "stmt->break ;\n" +
                        "stmt->block\n" +
                        "loc->loc [ num ]\n" +
                        "loc->id\n" +
                        "bool->bool || join\n" +
                        "bool->join\n" +
                        "join->join && equality\n" +
                        "join->equality\n" +
                        "equality->equality == rel\n" +
                        "equality->equality != rel\n" +
                        "equality->rel\n" +
                        "rel->expr < expr\n" +
                        "rel->expr <= expr\n" +
                        "rel->expr >= expr\n" +
                        "rel->expr > expr\n" +
                        "rel->expr\n" +
                        "expr->expr + term\n" +
                        "expr->expr - term\n" +
                        "expr->term\n" +
                        "term->term * unary\n" +
                        "term->term / unary\n" +
                        "term->unary\n" +
                        "unary->! unary\n" +
                        "unary->- unary\n" +
                        "unary->factor\n" +
                        "factor->( bool )\n" +
                        "factor->loc\n" +
                        "factor->num\n" +
                        "factor->real\n" +
                        "factor->true\n" +
                        "factor->false";

        Grammar grammar = new Grammar(s);
        LR1Parser lr1Parser = new LR1Parser(grammar);
        boolean b = lr1Parser.parseCLR1();
    }
}
