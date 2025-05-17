package com.phoenix.cubie.cubes;

import com.phoenix.cubie.Names;
import com.phoenix.cubie.Order;
import com.phoenix.derby.Board;

/*
* 布兰特
* */
public class Brant extends BaseCubie{
    public Brant(Board.SpaceManager spaceManager) {
        super(Names.BRANT, spaceManager);
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

    //技能：如果第一个行动，可以额外行动两步
    @Override
    protected void doActivateSkill() {;
        if (diceRollRecord.getOrder() == Order.FIRST.getVal()){
            diceRollRecord.setDiceValue(diceRollRecord.getDiceValue()+2);
        }
    }

    @Override
    public void clearState() {

    }
}
