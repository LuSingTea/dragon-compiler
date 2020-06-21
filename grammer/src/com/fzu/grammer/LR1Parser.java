package com.fzu.grammer;

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
        // 用户保存项目集规范族
        canonicalCollection = new ArrayList<>();
        // 存放LR1项目
        HashSet<LR1Item> start = new HashSet<>();

        // 得到第一条语法规则
        Rule startRule = grammar.getRules().get(0);

        // S'的向前看符号
        HashSet<String> startLockahead = new HashSet<>();
        // 文法开始放入 $ 符号
        startLockahead.add("$");

        // 对于每一个状态集初始化第一个项目集
        start.add(new LR1Item(startRule.getLeftSide(),startRule.getRightSide(),0,startLockahead));

        LR1State startState = new LR1State(grammar, start);
        // 此时第一个状态集已经计算完毕
        canonicalCollection.add(startState);

        // 遍历所有的状态集
        for (int i = 0; i < canonicalCollection.size(); i++) {

            HashSet<String> stringWithDot = new HashSet<>();
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                if (item.getCurrent() != null) {
                    stringWithDot.add(item.getCurrent());
                }
            }
            for (String str : stringWithDot) {
                HashSet<LR1Item> nextStateItems = new HashSet<>();
                for (LR1Item item : canonicalCollection.get(i).getItems()) {

                    if (item.getCurrent() != null && item.getCurrent().equals(str)) {
                        LR1Item temp = new LR1Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()+1,item.getLookahead());
                        nextStateItems.add(temp);
                    }
                }
                LR1State nextState = new LR1State(grammar, nextStateItems);
                boolean isExist = false;
                for (int j = 0; j < canonicalCollection.size(); j++) {
                    if (canonicalCollection.get(j).getItems().containsAll(nextState.getItems())
                            && nextState.getItems().containsAll(canonicalCollection.get(j).getItems())) {
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
        System.out.println(canonicalCollectionStr());
        // 创建goto表
        createGoToTable();
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

    // 解析LRLR(1)
//    public boolean parseLALR1(){
//        createStatesForLALR1();
//        createGoToTable();
//        return createActionTable();
//    }

//    public void createStatesForLALR1(){
//        createStatesForCLR1();
//        ArrayList<LR1State> temp = new ArrayList<>();
//        for (int i = 0; i < canonicalCollection.size(); i++) {
//            HashSet<String> lookahead = new HashSet<>();
//            HashSet<LR0Item> itemsi = new HashSet<>();
//            for(LR1Item item:canonicalCollection.get(i).getItems()){
//                itemsi.add(new LR0Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()));
//            }
//            for (int j = i+1; j < canonicalCollection.size(); j++) {
//                HashSet<LR0Item> itemsj = new HashSet<>();
//                for(LR1Item item:canonicalCollection.get(j).getItems()){
//                    itemsj.add(new LR0Item(item.getLeftSide(),item.getRightSide(),item.getDotPointer()));
//                }
//                if(itemsi.containsAll(itemsj) && itemsj.containsAll(itemsi)){
//                    for(LR1Item itemi : canonicalCollection.get(i).getItems()){
//                        for(LR1Item itemj : canonicalCollection.get(j).getItems()){
//                            if(itemi.equalLR0(itemj)){
//                                itemi.getLookahead().addAll(itemj.getLookahead());
//                                break;
//                            }
//                        }
//                    }
//                    for (int k = 0; k < canonicalCollection.size(); k++) {
//                        for(String s : canonicalCollection.get(k).getTransition().keySet()){
//                            if(canonicalCollection.get(k).getTransition().get(s).getItems().containsAll(canonicalCollection.get(j).getItems()) &&
//                                    canonicalCollection.get(j).getItems().containsAll(canonicalCollection.get(k).getTransition().get(s).getItems())){
//                                canonicalCollection.get(k).getTransition().put(s,canonicalCollection.get(i));
//                            }
//                        }
//                    }
//                    canonicalCollection.remove(j);
//                    j--;
//
//                }
//            }
//            temp.add(canonicalCollection.get(i));
//        }
//        canonicalCollection = temp;
//    }
    // 构建goto表
    protected void createGoToTable() {
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

    // 寻找状态集合
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
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                if (grammar.getTerminals().contains(s)) {
                    actionTable[i].put(s, new Action(ActionType.SHIFT, findStateIndex(canonicalCollection.get(i).getTransition().get(s))));
                }
            }
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (LR1Item item : canonicalCollection.get(i).getItems()) {
                if (item.getDotPointer() == item.getRightSide().length) {
                    if (item.getLeftSide().equals("S'")) {
                        actionTable[i].put("$", new Action(ActionType.ACCEPT, 0));
                    } else {
                        Rule rule = new Rule(item.getLeftSide(), item.getRightSide().clone());
                        int index = grammar.findRuleIndex(rule);
                        Action action = new Action(ActionType.REDUCE, index);
                        for (String str : item.getLookahead()) {
                            if (actionTable[i].get(str) != null) {
                                System.out.println(actionTable[i]);
                                System.out.println("it has a REDUCE-" + actionTable[i].get(str).getType() + " confilct in state " + i);
                                return false;
                            } else {
                                actionTable[i].put(str, action);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
