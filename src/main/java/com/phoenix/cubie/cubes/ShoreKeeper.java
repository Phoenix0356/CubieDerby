package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
* 守岸人
* */
public class ShoreKeeper extends BaseCubie{
    public ShoreKeeper(Board.SpaceManager spaceManager) {
        super(Names.SHORE_KEEPER, spaceManager);
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

    //技能：只能投出2或者3
    @Override
    protected void doActivateSkill() {
        //只要投到1就重投
        while (diceRollRecord.getDiceValue() == 1){
            diceRollRecord.setDiceValue(Util.randInt());
        }


    }

    @Override
    public void clearState() {

    }
}
