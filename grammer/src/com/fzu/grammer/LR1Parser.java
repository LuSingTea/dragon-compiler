package com.fzu.grammer;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LR1Parser extends LRParser {

    // 项目集规范族
    private ArrayList<LR1State> canonicalCollection;

    public LR1Parser(Grammar grammar){
        super(grammar);
    }

    // 创建LR(1)项目集规范族
    protected void createStatesForCLR1() {
        // 用于保存项目集规范族
        canonicalCollection = new ArrayList<>();
        // 存放一个LR1状态集的LR1项目
        HashSet<LR1Item> start = new HashSet<>();

        // 得到第一条语法规则
        Rule startRule = grammar.getRules().get(0);

        // S'的向前看符号
        HashSet<String> startLockahead = new HashSet<>();
        // 对于S' -> program向前看放入 $符号
        startLockahead.add("$");

        // 对于每一个状态集初始化第一个项目集
        start.add(new LR1Item(startRule.getLeftSide(),startRule.getRightSide(),0,startLockahead));

        // 之后用初始化的项目集计算闭包,同时计算向前看符号
        LR1State startState = new LR1State(grammar, start);

        // 此时第一个状态集已经计算完毕
        canonicalCollection.add(startState);

        // 遍历所有的状态集,构造项目集规范族
        for (int i = 0; i < canonicalCollection.size(); i++) {
            // 0 = {LR1Item@513} "S' -> .program , [$]"
            // 1 = {LR1Item@550} "program -> .block , [$]"
            // 2 = {LR1Item@983} "block -> .{ decls stmts } , [$]"
            HashSet<String> stringWithDot = new HashSet<>();
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                if (item.getCurrent() != null) {
                    stringWithDot.add(item.getCurrent());
                }
            }
            for (String str : stringWithDot) {
                // 构造下一个状态集
                HashSet<LR1Item> nextStateItems = new HashSet<>();
                for (LR1Item item : canonicalCollection.get(i).getItems()) {

                    if (item.getCurrent() != null && item.getCurrent().equals(str)) {
                        LR1Item temp = new LR1Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()+1,item.getLookahead());
                        nextStateItems.add(temp);
                    }
                }

                // 用一个状态集的第一行初始化一个项目集合
                LR1State nextState = new LR1State(grammar, nextStateItems);

                // 判断刚刚创建的项目集在不在原来的项目集中
                boolean isExist = false;
                for (int j = 0; j < canonicalCollection.size(); j++) {
                    if (canonicalCollection.get(j).getItems().containsAll(nextState.getItems()) &&
                        nextState.getItems().containsAll(canonicalCollection.get(j).getItems())) {
                        isExist = true;
                        canonicalCollection.get(i).getTransition().put(str, canonicalCollection.get(j));
                    }
                }
                if (!isExist) {
                    canonicalCollection.add(nextState);
                    canonicalCollection.get(i).getTransition().put(str, nextState);
                }
            }
        }

    }

    // 分析CLR(1)
    public boolean parseCLR1(){
        // 创建LR(1)项目集规范组
        createStatesForCLR1();
        // System.out.println(canonicalCollectionStr());
        // 创建goto表
        createGoToTable();
        System.out.println(goToTableStr());
        // 创建分析表
        return createActionTable();
    }

    /* 打印所有的状态集 */
    public String canonicalCollectionStr() {
        String str = "Canonical Collection : \n";
        for (int i = 0; i < 10; i++) {
            str += "State " + i + " : \n";
            str += canonicalCollection.get(i)+"\n";
        }
        return str;
    }

    // 构建goto表
    public void createGoToTable() {
        goToTable = new HashMap[canonicalCollection.size()];
        // 每个状态对应一行
        for (int i = 0; i < goToTable.length; i++) {
            goToTable[i] = new HashMap<>();
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                if (grammar.isVariable(s)) {
                    goToTable[i].put(s, findStateIndex(canonicalCollection.get(i).getTransition().get(s)));
                }
            }
        }
    }

    // 根据状态集招待对应的状态
    private int findStateIndex(LR1State state) {
        for (int i = 0; i < canonicalCollection.size(); i++) {
            if (canonicalCollection.get(i).equals(state)) {
                return i;
            }
        }
        return -1;
    }

    // 构造动作表
    private boolean createActionTable() {
        actionTable = new HashMap[canonicalCollection.size()];
        for (int i = 0; i < goToTable.length; i++) {
            actionTable[i] = new HashMap<>();
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            // 得到该状态集可以跳转的所有的状态集
            // 键为读入某个token之后会跳转到的状态集,读入
            //　例如读入A之后会跳转到状态,B之后会跳转到一个新的状态集
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                // 如果要读入的字符是一个终结符那么就放在动作表中一个
                // 移进项目
                if (grammar.getTerminals().contains(s)) {
                    actionTable[i].put(s, new Action(ActionType.SHIFT, findStateIndex(canonicalCollection.get(i).getTransition().get(s))));
                }
            }
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                // 如果点已经到了最后一个字符之后了
                // A->aaaaB.
                if (item.getDotPointer() == item.getRightSide().length) {
                    // 如果是接受项目
                    if (item.getLeftSide().equals("S'")) {
                        // 在$那一列放上接受动作
                        actionTable[i].put("$", new Action(ActionType.ACCEPT, 0));
                    }
                    else {
                        //
                        Rule rule = new Rule(item.getLeftSide(), item.getRightSide().clone());
                        // 找到文法的编号
                        int index = grammar.findRuleIndex(rule);

                        Action action = new Action(ActionType.REDUCE, index);

                        // 找到向前看操作符,在对应的位置填上Reduce state
                        for (String str : item.getLookahead()) {
                            // 如果该格子中已经有了规约或者移进项目那么就会有
                            // 规约规约冲突或者是移进规约冲突 直接报错
                           if (actionTable[i].get(str) != null) {
                               System.out.println(actionTable[i]);
                               System.out.println("it has a REDUCE-" + actionTable[i].get(str).getType() + " confilct in state " + i);
                               return false;
                           } else {
                               actionTable[i].put(str, action);
                           }
                            // 如果是移进项目直接替换
                            // 如果是规约不做替换
                            // Action originAction = actionTable[i].get(str);
                            // if (originAction != null) {
                            //     if (originAction.getType()==ActionType.SHIFT) {
                            //         actionTable[i].put(str, action);
                            //     }
                            //     else if (originAction.getType() == ActionType.REDUCE) {
                            //         continue;
                            //     }
                            // }
                            // else {
                            //     actionTable[i].put(str, action);
                            // }
                            // 直接替换
                            // actionTable[i].put(str, action);
                        }
                    }
                }
            }
        }
        return true;
    }
}
