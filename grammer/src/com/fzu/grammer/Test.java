package com.fzu.grammer;

public class Test {
    public static void main(String[] args) {
        /*
        program→block
        block→{ decls stmts }
        decls→decls decl
        decls->epsilon
        decl→type id ;
        type→type [ num ]
        type->basic
        stmts→stmts stmt
        stmts->epsilon
        stmt→loc = bool ;
        stmt->if ( bool ) stmt
        stmt->if ( bool ) stmt else stmt
        stmt->while ( bool ) stmt
        stmt->do stmt while ( bool ) ;
        stmt->break ;
        stmt->block
        loc→loc [ num ]
        loc->id
        bool→bool || join
        bool->join
        join→join && equality
        join->equality
        equality→equality == rel
        equality->equality != rel
        equality->rel
        rel→expr < expr
        rel->expr <= expr
        rel->expr >= expr
        rel->expr > expr
        rel->expr
        expr→expr + term
        expr->expr - term
        expr->term
        term→term * unary
        term->term / unary
        term->unary
        unary→! unary
        unary->- unary
        unary->factor
        factor→( bool )
        factor->loc
        factor->num
        factor->real
        factor->true
        factor->false
        */
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
