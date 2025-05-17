package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
* 柯莱塔
* */
public class Carlotta extends BaseCubie{
    public Carlotta(Board.SpaceManager spaceManager) {
        super(Names.CARLOTTA, spaceManager);
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

    //技能：有28%几率行动两次;
    @Override
    protected void doActivateSkill() {
        if(Util.probabilityCheck(28)){
            diceRollRecord.setDiceValue(diceRollRecord.getDiceValue()<<1);
        }
    }

    @Override
    public void clearState() {

    }
}
