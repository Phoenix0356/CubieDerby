package com.phoenix.cubie.cubes;

import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
* 索拉里斯最强共鸣者
* */
public class Calcharo extends BaseCubie{
    public Calcharo(Board.SpaceManager spaceManager) {
        super(Names.CALCHARO, spaceManager);
    }

    @Override
    protected void moveInTurn() {
        activateSkill();
        int forwardSteps = diceRollRecord.getDiceValue();
        while (forwardSteps-- > 0) {
            //如果有团子胜利了，直接结束
            if(spaceManager.getEndSpace().hasCubie()) return;
            oneStep();
        }
    }

    //技能：开始移动时，如果在最后则额外前进三步
    @Override
    protected void doActivateSkill() {
        //自己下面有团子，不是最后
        if (this.lowerCubie != null) return;
        //判断自己后面有没有团子
        int curPos = position;
        Board.Space preSpace = spaceManager.get(--curPos);
        while (preSpace!=null){
            if(preSpace.hasCubie()){
                return;
            }
            preSpace = spaceManager.get(--curPos);
        }

        //自己是最后,额外行动3步数
        diceRollRecord.setDiceValue(diceRollRecord.getDiceValue() + 3);

    }

    @Override
    public void clearState() {

    }
}
