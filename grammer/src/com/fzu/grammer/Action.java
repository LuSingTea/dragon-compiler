package com.fzu.grammer;

public class Action {
    private ActionType type;
    // 如果是移进项目,那么这项就是状态
    // 如果是规约项目,那么这项就是第几号产生式
    private int operand;

    public Action(ActionType type, int operand) {
        this.type = type;
        this.operand = operand;
    }

    @Override
    public String toString() {
        return type + " " + (type == ActionType.ACCEPT ? "":operand);
    }

    public ActionType getType() {
        return type;
    }

    public int getOperand() {
        return operand;
    }

}