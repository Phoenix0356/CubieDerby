package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
* 菲比
* */
public class Phoebe extends BaseCubie{
    public Phoebe(Board.SpaceManager spaceManager) {
        super(Names.PHOEBE, spaceManager);
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

    //技能：有50%概率多走两步
    @Override
    protected void doActivateSkill() {
        if(Util.probabilityCheck(50)){
            diceRollRecord.setDiceValue(diceRollRecord.getDiceValue() + 1);
        }
    }

    @Override
    public void clearState() {

    }
}
