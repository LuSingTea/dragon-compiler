package com.fzu.grammer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * 一个LR(1)项目
 * A->aBC, a 这个是一个LR1项目
 */
public class LR1Item {

    // 向前看符号
    private HashSet<String> lookahead;
    // 右部和左部
    private String leftSide;
    private String[] rightSide;
    // .的位置
    // 如果产生式右部有两个字符,点的取值有3中
    // A->.ab A->a.b A->ab.
    private int dotPointer;

    /**
     * 用四个参数构造一个LR1项目集
     * @param leftSide
     * @param rightSide
     * @param dotPointer
     * @param lookahead
     */
    public LR1Item(String leftSide, String[] rightSide, int dotPointer, HashSet<String> lookahead) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.dotPointer = dotPointer;
        this.lookahead = lookahead;
    }

    // 得到当前点的下一个符号
    public String getCurrent(){
        if(dotPointer == rightSide.length){
            return null;
        }
        return rightSide[dotPointer];
    }

    // 点前进
    boolean goTo() {
        if (dotPointer >= rightSide.length) {
            return false;
        }
        dotPointer++;
        return true;
    }

    public int getDotPointer() {
        return dotPointer;
    }

    public String[] getRightSide() {
        return rightSide;
    }

    public HashSet<String> getLookahead() {
        return lookahead;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public void setLookahead(HashSet<String> lookahead) {
        this.lookahead = lookahead;
    }

    public void setRightSide(String[] rightSide) {
        this.rightSide = rightSide;
    }

    // 比较LR(1)项目集是否相等
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LR1Item lr1Item = (LR1Item) o;
        return dotPointer == lr1Item.dotPointer &&
                Objects.equals(lookahead, lr1Item.lookahead) &&
                Objects.equals(leftSide, lr1Item.leftSide) &&
                Arrays.equals(rightSide, lr1Item.rightSide);
    }

    // 比较LR(0)项目集是否相等
    // 就是不包括向前看符号
    public boolean equalLR0(LR1Item item){
        return leftSide.equals(item.getLeftSide()) && Arrays.equals(rightSide,item.getRightSide()) && dotPointer == item.getDotPointer();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.dotPointer;
        hash = 31 * hash + Objects.hashCode(this.leftSide);
        hash = 31 * hash + Arrays.deepHashCode(this.rightSide);
        hash = 31 * hash + Objects.hashCode(this.lookahead);
        return hash;
    }

    @Override
    public String toString() {
        String str = leftSide + " -> ";
        for (int i = 0; i < rightSide.length; i++) {
            if (i == dotPointer) {
                str += ".";
            }
            str += rightSide[i];
            if(i != rightSide.length - 1){
                str+= " ";
            }
        }
        if (rightSide.length == dotPointer) {
            str += ".";
        }
        str += " , " + lookahead;
        return str;
    }

}
