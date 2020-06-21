package com.fzu.grammer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class LR1State {

    // 代表了一个LR(1)状态集所有的项目
    private LinkedHashSet<LR1Item> items;

    // 状态和所有LR(1)状态集的映射
    private HashMap<String, LR1State> transition;

    // 传入文法和每个状态集的第一个项目集
    public LR1State(Grammar grammar, HashSet<LR1Item> coreItems) {
        items = new LinkedHashSet<>(coreItems);
        transition = new HashMap<>();
        closure(grammar);
    }

    // 计算每一个状态集的闭包
    private void closure(Grammar grammar) {
        boolean changeFlag = false;
        do {
            changeFlag = false;
            // 对于每一个项目
            for(LR1Item item : items) {
                // A->aaaB. 这种情况下就不执行这个
                // 如果点还没到语法的最后一个符号之后,并且点的后一个字符是文法的非终结符
                if(item.getDotPointer() != item.getRightSide().length && grammar.isVariable(item.getCurrent())) {
                    // 新建向前看符号
                    HashSet<String> lookahead = new HashSet<>();
                    // 如果点已经到了语法的最后一个符号之前
                    if(item.getDotPointer() == item.getRightSide().length - 1) {
                        // 这里感觉有问题
                        lookahead.addAll(item.getLookahead());
                    }
                    // 如果点还没到语法的最后一个符号之前
                    // 例如A->a.BC 就会计算C的首符集
                    else {
                        // 计算点后两个的的首付集合
                        // A->a.BC 就会计算C的首符集
                        // 这个lookahead是给下一个文法准备的
                        HashSet<String> firstSet = grammar.computeFirst(item.getRightSide(),item.getDotPointer()+1);

                        if(firstSet.contains("epsilon")) {
                            firstSet.remove("epsilon");
                            firstSet.addAll(item.getLookahead());
                        }
                        lookahead.addAll(firstSet);
                    }
                    // 将 点号后面的以非终结符开头的语法规则加入到 当前的状态集
                    HashSet<Rule> rules = grammar.getRuledByLeftVariable(item.getCurrent());
                    for(Rule rule : rules){
                        String[] rhs = rule.getRightSide();
                        int finished = 0;
                        // 如果该语法规则为 A -> epsilon 代表可以直接结束
                        // 这里感觉有问题
                        if (rhs.length == 1 && rhs[0].equals("epsilon")) {
                            finished = 1;
                        }

                        HashSet<String> newLA = new HashSet<>(lookahead);
                        LR1Item newItem = new LR1Item(rule.getLeftSide(), rhs, finished, newLA);
                        // 合并向前看操作符
                        boolean found = false;
                        for (LR1Item existingItem : items) {
                            if (newItem.equalLR0(existingItem)) {
                                HashSet<String> existLA = existingItem.getLookahead();
                                if (!existLA.containsAll(newLA)) {
                                    // changing the lookahead will change the hash code
                                    // of the item, which means it must be re-added.
                                    items.remove(existingItem);
                                    existLA.addAll(newLA);
                                    items.add(existingItem);
                                    changeFlag = true;
                                }
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            items.add(newItem);
                            changeFlag = true;
                        }
                    }
                    if (changeFlag) {
                        break;
                    }
                }
            }
        } while (changeFlag);

    }

    public HashMap<String, LR1State> getTransition() {
        return transition;
    }

    public LinkedHashSet<LR1Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String s = "";
        for(LR1Item item:items){
            s += item + "\n";
        }
        return s;
    }
}
