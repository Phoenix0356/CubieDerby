package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.derby.Board;
import lombok.Data;

/*
* 团子基类
* */
@Data
public abstract class BaseCubie {
    protected String name;
    //当前位置
    protected int position;
    //上面的团子，下面的团子
    protected BaseCubie upperCubie;
    protected BaseCubie lowerCubie;
    //拿到棋盘格子
    protected Board.SpaceManager spaceManager;
    //这轮掷骰子的结果记录
    protected DiceRollRecord diceRollRecord;

    public BaseCubie(String name, Board.SpaceManager spaceManager) {
        this.name = name;
        this.spaceManager = spaceManager;
    }

    //掷骰子
    public void rollDice(int order, boolean isLast) {
        int diceValue = Util.randInt();
//        System.out.printf("%s团子掷出了%d点%n", this.name, diceValue);
        //记录这轮掷骰子的结果
        this.diceRollRecord = new DiceRollRecord(order, diceValue, isLast);
    }

    //团子移动模板方法
    public void move(){
//        System.out.printf("%s团子开始行动%n", this.name);
        moveInTurn();
    }

    abstract protected void moveInTurn();

    //触发技能模板方法
    protected void activateSkill(){
        doActivateSkill();
    }

    abstract protected void doActivateSkill();

    //比赛结束后清理自己的状态
    public void clear(){
        clearState();
        this.lowerCubie = null;
        this.upperCubie = null;
        this.diceRollRecord = null;
    }
    public abstract void clearState();

    //前进一步
    protected void oneStep() {
        //删除下面团子对自己的引用
        BaseCubie lowerCubie = this.lowerCubie;
        if (lowerCubie != null) {
            lowerCubie.upperCubie = null;
            this.lowerCubie = null;
        }else {
            //如果自己就是第一个团子，把格子上的第一个团子删掉
            Board.Space curSpace = spaceManager.get(position);
            curSpace.setFirstBaseCubie(null);
        }
        //自己往前走一步
        position++;
        //叠在上面的团子一起前进
        BaseCubie cur = this.upperCubie;
        while (cur != null) {
            cur.position +=1 ;
            cur = cur.upperCubie;
        }

        //拿到下一个格子
        Board.Space destSpace = spaceManager.get(position);
        //判断是否到达终点
        if (destSpace == spaceManager.getEndSpace()){
            //尝试声称自己胜利
            tryClaimWinner(destSpace);
        }else {
            //如果没到终点
            //拿到下一个格子上的团子
            BaseCubie cubieOnDest = destSpace.getFirstBaseCubie();
            //如果已经有团子，把自己叠上去
            if (cubieOnDest != null) {
                cubieOnDest = getTop(cubieOnDest);
                cubieOnDest.upperCubie = this;
                this.lowerCubie = cubieOnDest;
            } else {
                //没有团子，直接把自己放在格子上
                destSpace.setFirstBaseCubie(this);
            }
        }
    }

    protected void tryClaimWinner(Board.Space end){
        //如果已经有胜利者，直接失败
        if(end.hasCubie()){
            return;
        }
        //如果头上有团子，把胜者设为它
        BaseCubie winner = getTop(this);
        //如果终点上还未宣称胜利者，宣称胜利
        end.setFirstBaseCubie(winner);
    }

    protected BaseCubie getTop(BaseCubie cubie){
        while (cubie.upperCubie != null) {
            cubie = cubie.upperCubie;
        }
        return cubie;
    }
    //团子的投骰子记录
    @Data
    public static class DiceRollRecord{
        //行动顺序
        int order;
        //投出的值
        int diceValue;
        //是否最后一个行动
        boolean isLast;

        public DiceRollRecord(int order, int diceValue, boolean isLast) {
            this.order = order;
            this.diceValue = diceValue;
            this.isLast = isLast;
        }
    }
}
