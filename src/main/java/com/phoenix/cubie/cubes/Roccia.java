package com.phoenix.cubie.cubes;

import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
* 洛可可
* */
public class Roccia extends BaseCubie{
    public Roccia(Board.SpaceManager spaceManager) {
        super(Names.ROCCIA, spaceManager);
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

    //技能：如果在最后行动，可以额外行动2步
    @Override
    protected void doActivateSkill() {
        if(diceRollRecord.isLast){
            diceRollRecord.setDiceValue(diceRollRecord.getDiceValue()+2);
        }
    }

    @Override
    public void clearState() {

    }
}
