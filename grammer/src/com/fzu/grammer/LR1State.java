package com.fzu.grammer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * 这是一个LR1状态集例如
 * A-> .Ba, xx
 * B->.a xx
 * 里面有很多LR1项目组成
 */
public class LR1State {

    // 代表了一个LR(1)状态集所有的项目
    private LinkedHashSet<LR1Item> items;

    // 数字状态和被转换的状态集的映射
    // 例如读入字符A转入状态集1
    // 读入字符B转入状态集2
    private HashMap<String, LR1State> transition;

    // 传入文法和每个状态集的第一个项目集
    public LR1State(Grammar grammar, HashSet<LR1Item> coreItems) {
        items = new LinkedHashSet<>(coreItems);
        transition = new HashMap<>();
        closure(grammar);
    }

    // 计算当前状态集的闭包
    private void closure(Grammar grammar) {
        boolean changeFlag = false;
        do {
            changeFlag = false;
            // 对于每一个项目
            for(LR1Item item : items) {
                // 如果点还没到语法的最后一个符号之后,并且点的后一个字符是文法的非终结符
                // 那就说明可以新增项目集
                // A->aaaB. 这种情况下就不执行这个
                if (item.getDotPointer() != item.getRightSide().length) {
                    if (item.getCurrent()=="["){
                        System.out.println("当前状态为: " + item.getCurrent());
                    }
                }
                if(item.getDotPointer() != item.getRightSide().length && grammar.isVariable(item.getCurrent())) {
                    // 新建下一个项目集的向前看符号
                    HashSet<String> lookahead = new HashSet<>();
                    // 如果点已经到了语法的最后一个符号之前
                    // A->aaa.B 这种情况
                    if(item.getDotPointer() == item.getRightSide().length - 1) {
                        // 那么当前的项目集的向前看符号可以直接传给下一个项目集
                        lookahead.addAll(item.getLookahead());
                    }
                    else {
                        // 如果点还没到语法的最后一个符号之前
                        // 例如A->a.BC 就会计算C的首符集
                        // 并把其中epsilon去掉并传给下一个项目集
                        HashSet<String> firstSet = grammar.computeFirst(item.getRightSide(),item.getDotPointer()+1);

                        if(firstSet.contains("epsilon")) {
                            firstSet.remove("epsilon");
                            // 这步我感觉有问题
                            firstSet.addAll(item.getLookahead());
                        }
                        lookahead.addAll(firstSet);
                    }
                    // 将 点号 后面的以该非终结符开头的语法
                    // 找到并处理计算向前看符号
                    // 之后加入到状态集中
                    HashSet<Rule> rules = grammar.getRuledByLeftVariable(item.getCurrent());
                    for(Rule rule : rules) {
                        String[] rhs = rule.getRightSide();
                        int finished = 0;
                        // 如果该语法规则为 A -> epsilon 代表项目集为
                        // A->epsilon.
                        // 这里感觉有问题
                        if (rhs.length == 1 && rhs[0].equals("epsilon")) {
                            finished = 1;
                        }
                        // 用上一个项目集传下来的向前看初始化当前项目集的向前看
                        HashSet<String> newLA = new HashSet<>(lookahead);
                        // 初始化一个新的LR1项目集
                        LR1Item newItem = new LR1Item(rule.getLeftSide(), rhs, finished, newLA);

                        // 判断还有没有可以合并的项目集
                        boolean found = false;
                        for (LR1Item existingItem : items) {
                            // 如果新建的项目集已经存在了,那么看看能不能合并向前看操作符
                            if (newItem.equalLR0(existingItem)) {
                                HashSet<String> existLA = existingItem.getLookahead();
                                // 合并相同项目将集合的向前看操作符
                                // 例如 A->.aB , E和A->.aB , B
                                // A->.aB E,B
                                if (!existLA.containsAll(newLA)) {
                                    // 改变向前看会改变hash值
                                    // 需要先移除在添加
                                    items.remove(existingItem);
                                    existLA.addAll(newLA);
                                    items.add(existingItem);
                                    changeFlag = true;
                                }
                                found = true;
                                break;
                            }
                        }
                        // 如果还没有加入,那么将该项目集合直接加入
                        if (!found) {
                            items.add(newItem);
                            changeFlag = true;
                        }
                    }
                    // 对于所有规则,如果它后面的非终结符开头的项目已经全加进来了
                    // 那么闭包计算结束
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
        StringBuilder s = new StringBuilder();
        for(LR1Item item:items){
            s.append(item).append("\n");
        }
        return s.toString();
    }
}
