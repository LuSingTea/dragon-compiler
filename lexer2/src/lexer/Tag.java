package lexer;

public class Tag {
	public final static int
        // 关键字
        BREAK = 258,DO = 259,ELSE = 260,FALSE = 262,IF = 265,TRUE = 274,WHILE = 275,
        // 基本数据类型
        BASIC = 257,
        // 数字
        NUM = 270,REAL = 272,
        // id
        ID = 264,
        // 赋值
        ASSIGN = 278,
        // 分隔符
        Delimiter = 276,
        // 关系运算符
        EQ = 261,GE = 263,LE = 267,NE = 269,LESS = 279,GREATER = 280,
        // 算术运算符
        MINUS = 268,PLUS = 281, MULTIPLY = 282, DIVIDE = 283,
        // 逻辑运算符
        AND = 256,OR = 271,NOT = 284,
        // 位运算
        LOGICAND = 285, LOGICOR = 286,
        // 暂时没用的
        TEMP = 273,INDEX = 266
    ;
	
}
