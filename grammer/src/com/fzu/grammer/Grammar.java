package com.fzu.grammer;

import java.util.*;

/**
 * 文法
 */
public class Grammar {

    private ArrayList<Rule> rules; // 文法规则
    private String startVariable; // 文法开始符号

    private HashSet<String> terminals; //终结符
    private HashSet<String> variables; // 非终结符

    private HashMap<String, HashSet<String>> firstSets; // 所有的first集
    private HashMap<String, HashSet<String>> followSets; // 所有的follow集

    public Grammar(String s) {

        rules = new ArrayList<>();
        terminals = new HashSet<>(); // 终结符
        variables = new HashSet<>(); // 非终结符

        int line = 0;
        for(String st : s.split("\n")) {

            String[] sides = st.split("->");
            String leftSide = sides[0].trim();
            // 左边必定是非终结符
            variables.add(leftSide);
            String[] rulesRightSide = sides[1].trim().split(" ");
            // 找到右边的终结符
            for (String word : rulesRightSide) {
                if (isTerminal(word)) {
                    terminals.add(word);
                }
            }

            // 手动扩展文法
            if (line == 0) {
                // startVariable = leftSide;
                startVariable = "S'";
                // 不太确定需不要这个
                variables.add("S'");
                rules.add(new Rule("S'", new String[]{leftSide}));
            }
            rules.add(new Rule(leftSide, rulesRightSide));
            line++;
        }
        outputGrammer();
        computeFirstSets();
        outputFirstsets();
        computeFollowSet();
        outputFollowsets();
        outputTerminals();
        outputVariables();
    }

    // 打印所有文法
    public void outputGrammer() {
        System.out.println("Rules: ");
        for (int i = 0;i < rules.size();i++) {
            System.out.println(i + " : " + rules.get(i));
        }
    }
    // 判断是否是终结符
    public boolean isTerminal(String word) {
        if (word.equals("S'") || word.equals("program") || word.equals("block") || word.equals("decls") || word.equals("decl") ||
            word.equals("type") || word.equals("stmts") || word.equals("stmt") || word.equals("loc") || word.equals("bool") ||
            word.equals("join") || word.equals("equality") || word.equals("rel") || word.equals("expr") || word.equals("term") ||
            word.equals("unary") || word.equals("factor")) {
            return false;
        }
        // if (word.equals("S'") || word.equals("S") || word.equals("B")) {
        //     return false;
        // }
        return true;
    }
    /**
     * 输出全部的first集合
     */
    public void outputFirstsets() {
        Set<String> lefts = firstSets.keySet();
        System.out.println("first集合为:");
        for (String left : lefts) {
            HashSet<String> right = firstSets.get(left);
            System.out.printf("%-10s : %s\n",left ,right);
        }
        System.out.println();
    }

    /**
     * 输出全部的follow集合
     */
    public void outputFollowsets() {
        Set<String> stringSet = followSets.keySet();
        System.out.println("follow集合为:");
        for (String string : stringSet) {
            HashSet<String> strings1 = followSets.get(string);
            System.out.printf("%-10s : %s\n",string ,strings1);
        }
        System.out.println();
    }
    public ArrayList<Rule> getRules() {
        return rules;
    }

    /**
     * 找到规则的下标
     * @param rule
     * @return
     */
    public int findRuleIndex(Rule rule){
        for(int i=0 ; i<rules.size();i++){
            if(rules.get(i).equals(rule)){
                return i;
            }
        }
        return -1;
    }


    public HashSet<String> getVariables() {
        return variables;
    }

    public String getStartVariable() {
        return startVariable;
    }

    // 计算所有的first集合
    private void computeFirstSets() {
        firstSets = new HashMap<>();

        for (String s : variables) {
            HashSet<String> temp = new HashSet<>();
            firstSets.put(s, temp);
        }
        while (true) {
            // 判断首符集是否变化
            boolean isChanged = false;
            // 对于所有的非终结符
            for (String variable : variables) {
                HashSet<String> firstSet = new HashSet<>();
                for (Rule rule : rules) {
                    if (rule.getLeftSide().equals(variable)) {
                        HashSet<String> addAll = computeFirst(rule.getRightSide(), 0);
                        firstSet.addAll(addAll);
                    }
                }
                if (!firstSets.get(variable).containsAll(firstSet)) {
                    isChanged = true;
                    firstSets.get(variable).addAll(firstSet);
                }

            }
            if (!isChanged) {
                break;
            }
        }
        // 原本是put S的
        firstSets.put("S'", firstSets.get(startVariable));
    }

    // 计算所有的follow集合
    private void computeFollowSet() {
        followSets = new HashMap<>();
        for (String s : variables) {
            HashSet<String> temp = new HashSet<>();
            followSets.put(s, temp);
        }
        HashSet<String> start = new HashSet<>();
        start.add("$");
        followSets.put("S'", start);

        while (true) {
            boolean isChange = false;
            for (String variable : variables) {
                for (Rule rule : rules) {
                    for (int i = 0; i < rule.getRightSide().length; i++) {
                        // 如果第i个为非终结符
                        if (rule.getRightSide()[i].equals(variable)) {
                            HashSet<String> first;
                            if (i == rule.getRightSide().length - 1) {
                                first = followSets.get(rule.leftSide);
                            }
                            else {
                                first = computeFirst(rule.getRightSide(), i + 1);
                                if (first.contains("epsilon")) {
                                    first.remove("epsilon");
                                    first.addAll(followSets.get(rule.leftSide));
                                }
                            }
                            if (!followSets.get(variable).containsAll(first)) {
                                isChange = true;
                                followSets.get(variable).addAll(first);
                            }
                        }
                    }
                }
            }
            if (!isChange) {
                break;
            }
        }
    }

    // 计算给定右部的first集合
    public HashSet<String> computeFirst(String[] string, int index) {
        HashSet<String> first = new HashSet<>();
        if (index == string.length) {
            return first;
        }

        // 如果当前的字符为终结符或者说是epsilon,就添加到first集合并直接返回
        if (terminals.contains(string[index]) || string[index].equals("epsilon")) {
            first.add(string[index]);
            return first;
        }
        // 如果当前的字符是非终结符
        if (variables.contains(string[index])) {
            // 那就找到非终结符对应的首符集和,把它加入到当前的字符的首付集合
            for (String str : firstSets.get(string[index])) {
                first.add(str);
            }
        }

        if (first.contains("epsilon")) {
            if (index != string.length - 1) {
                first.remove("epsilon");
                first.addAll(computeFirst(string, index + 1));
            }
        }
        return first;
    }

    // 根据左部找到对应的文法规则
    public HashSet<Rule> getRuledByLeftVariable(String variable) {
        HashSet<Rule> variableRules = new HashSet<>();
        for (Rule rule : rules) {
            if (rule.getLeftSide().equals(variable)) {
                variableRules.add(rule);
            }
        }
        return variableRules;
    }

    // 输出终结符集合
    public void outputTerminals() {
        System.out.println("终结符为: ");
        for (String terminal : terminals) {
            System.out.print(terminal + " ");
        }
        System.out.println();
    }

    // 输出非终结符
    public void outputVariables() {
        System.out.println("非终结符为: ");
        for (String variabe : variables) {
            System.out.print(variabe + " ");
        }
        System.out.println();
    }
    // 判断是否是非终结符
    public boolean isVariable(String s) {
        return variables.contains(s);
    }

    public HashMap<String, HashSet<String>> getFirstSets() {
        return firstSets;
    }

    public HashMap<String, HashSet<String>> getfollowSets() {
        return followSets;
    }

    public HashSet<String> getTerminals() {
        return terminals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.rules);
        hash = 37 * hash + Objects.hashCode(this.terminals);
        hash = 37 * hash + Objects.hashCode(this.variables);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Grammar other = (Grammar) obj;
        if (!Objects.equals(this.rules, other.rules)) {
            return false;
        }
        if (!Objects.equals(this.terminals, other.terminals)) {
            return false;
        }
        if (!Objects.equals(this.variables, other.variables)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(Rule rule: rules){
            str.append(rule).append("\n");
        }
        return str.toString();
    }
}
