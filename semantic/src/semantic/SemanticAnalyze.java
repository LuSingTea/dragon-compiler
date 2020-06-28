package semantic;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SemanticAnalyze {

    //每条分析语句的地址
    private int address = 100;
    // 表示过程执行到相应位置的地址符号
    private int LID = 0;
    // 用于替换表达式的标识符
    private int tID = 0;
    private int ip = 0;
    private String inputs[] = new String[1000];
    private int maxsize = 0;

    // 字符串和数字的连接
    public String link(String a, int b) {
        return a + b;
    }
    // 获取表达式中的元素对象
    // 如果缺少元素就会输出错误并继续下一行
    public String element() {
        if (inputs[ip].equals("expr") || inputs[ip].equals("num")) {
            ip++;
            return inputs[ip - 1];
        }
        else if (inputs[ip].equals("(")) {
            ip++;
            String result = tranformFourAddress();
            if (inputs[ip].equals(")"))
                ip++;
            else
                System.out.println("缺少)");
            return result;
        }
        else
            System.out.println("error");
        return "";
    }
    //处理表达式,带有运算符
    public String expressionWithOp(String op) {

        if (inputs[ip].equals("*") || inputs[ip].equals("/")) {
            op = inputs[ip];
            ip++;
            String arg1 = element();
            String op_1 = "", result = link("t", tID++);
            String arg2 = expressionWithOp(op_1);
            if (op_1.equals(""))
                op_1 = "=";
            if (arg2.equals("")) {
                System.out.printf("%d: %s = %s\n",address++, result, arg1);
            }
            else {
                System.out.printf("%d: %s = %s %s %s\n", address++, result, arg1, op_1, arg2);
            }
            return result;
        }
        return "";
    }
    //处理表达式
    public String expression() {
        String op = "", result = link("t", tID++);
        String arg1 = element();
        String arg2 = expressionWithOp(op);
        if (op.equals("")) {
            op = "=";
        }
        if (arg2.equals("")) {
            System.out.printf("%d: %s = %s\n", address++, result, arg1);
        }
        else {
            System.out.printf("%d: %s = %s %s %s\n", address++, result, arg1, op, arg2);
        }
        return result;
    }
    //递归 ---处理表达式，转为四地址输出
    public String tranformFourAddressWithOp(String op) {
        String result = "";
        if (inputs[ip].equals("+") || inputs[ip].equals("-")) {
            op = inputs[ip];
            ip++;
            String arg1 = expression();
            String op_1 = "";
            String arg2 = tranformFourAddressWithOp(op_1);
            result = link("t", tID++);
            if (op_1.equals("")) {
                op_1 = "=";
            }
            if (arg2.equals("")) {
                System.out.printf("%s: %s = %s\n", address++, result, arg1);
            }
            else {
                System.out.printf("%d: %s = %s %s %s\n", address++, result, arg1, op_1, arg2);
            }
        }
        return result;
    }
    //处理表达式，转为四地址输出
    public String tranformFourAddress() {
        String arg1 = "", op = "";
        if (inputs[ip].equals("+") || inputs[ip].equals("-"))
        {
            arg1 = inputs[ip];
            ip++;
        }
        arg1 += expression();
        String arg2 = tranformFourAddressWithOp(op);
        String result = link("t", tID++);
        if (op.equals("")) {
            op = "=";
        }
        if (arg2.equals("")) {
            System.out.printf("%s: %s = %s\n", address++, result, arg1);
        }
        else {
            System.out.printf("%d: %s = %s %s %s\n", address++, result, arg1, op, arg2);
        }
        return result;
    }
    //判断并获取运算符
    public String getOperator()
    {
        if (inputs[ip].equals("=") || inputs[ip].equals("<>") || inputs[ip].equals("<") || inputs[ip].equals(">") ||
                inputs[ip].equals("<=") || inputs[ip].equals(">=")) {
            ip++;
            return inputs[ip - 1];
        }
        else {
            System.out.println("error");
        }
        return "";
    }
    //输出 if 语句的条件的三地址代码
    //L1,L2 分别为 if 条件为 true 和 false 时候的跳转地址
    public void condition(int L1, int L2) {
        String result = link("t", tID++);
        String arg1 = tranformFourAddress(); //获得表达式的运算符的左边内容
        String op = getOperator(); //获得表达式的运算符
        String arg2 = tranformFourAddress(); //获得表达式的运算符的右边内容
        if (arg2.equals("")) {
            System.out.printf(" %s = %s", result, arg1);
        }
        else {
            System.out.printf("%d: %s = %s %s %s\n", address++, result, arg1, op, arg2);
        }
        System.out.printf("%d: if true %s goto L %d\n",address++, result, L1);
        System.out.printf("%d: if false %s goto L %d\n",address++, result, L2);
    }
    //判断关键字,调用相应的产生式分析
    public void processByKeyword(int next, int flag)
    {
        if (ip >= maxsize) return;
        if (inputs[ip].equals("expr")) {
            ip++;
            if (inputs[ip].equals("=")) //赋值语句 转化为四元式
            {
                ip++;
                String arg1 = tranformFourAddress();
                String arg2 = "";
                if (arg2.equals("")) {
                    System.out.printf("%d: expr = %s\n", address++, arg1);
                }
            }
            else {
                System.out.println("error");

            }
        }
        //if 的语义子程序
        else if (inputs[ip].equals("if")) {
            ip++;
            int L1 = LID++;
            int L2 = LID++;
            if (inputs[ip].equals("(")) {
                ip++;
                condition(L1, L2);
            }
            else {
                System.out.println("缺少(");
                return;
            }
            if (inputs[ip].equals(")"))
                ip++;
            else {
                System.out.println("缺少(");
                return;
            }
            System.out.printf("L%d:\n", L1);
            processByKeyword(next, flag);
            ip++;
            if (inputs[ip].equals("else")) {
                System.out.printf("L%d:\n", L2);
                ip++;
                processByKeyword(next, flag);
            }
        }
        //while 的语义子程序
        else if (inputs[ip].equals("while")) {
            ip++;
            int L1 = LID++;
            int L2 = LID++;
            if (inputs[ip].equals("(")) {
                ip++;
                System.out.printf("L%d:\n", L1);
                condition(L2, next);
            }
            else {
                System.out.println("缺少(");
                return;
            }
            if (inputs[ip].equals(")"))
                ip++;
            else {
                System.out.println("缺少(");
                return;
            }
            System.out.printf("L%d:\n", L2);
            // 这里flag原本为引用
            processByKeyword(next, flag);
            System.out.printf("goto L%d\n", L1);
            flag = 1;
        }
    }
    //递归 ---生成并输出条件返回地址
    public void processByKeywordAnalyzeList1() {
        if (ip >= maxsize) return;
        if (inputs[ip].equals(";")) {
            ip++;
            int next = LID++;
            int flag = 0;
            processByKeyword(next, flag);
            if (flag!=0)
                System.out.printf("L%d:\n", next);
            processByKeywordAnalyzeList1();
        }
    }
    //生成并输出条件返回地址
    public void processByKeywordAnalyzeList()
    {
        int next = LID++;
        int flag = 0;
        processByKeyword(next, flag);
        if (flag != 0)
            System.out.printf("L%d:\n", next);
        processByKeywordAnalyzeList1();
    }
    public void startAnalyze()
    {
        int next = LID++;
        int flag = 0;
        processByKeywordAnalyzeList();
        if (flag != 0)
            System.out.printf("L%d:\n", next);
    }
    //文件读入
    public void readFile(String filename)
    {
        // 使用ArrayList来存储每行读取到的字符串
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                System.out.println(str);
                String[] s = str.split(" ");
                for (String tmp : s) {
                    inputs[maxsize++] = tmp;
                }
            }
            bf.close();
            fr.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < maxsize; i++) {
            System.out.println(inputs[i]);
        }
    }
    public static void main(String[] args) {
        SemanticAnalyze semanticAnalyze = new SemanticAnalyze();
        semanticAnalyze.readFile("input3.txt");
        semanticAnalyze.startAnalyze();
    }
}
