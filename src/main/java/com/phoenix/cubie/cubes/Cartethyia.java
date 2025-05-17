package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
* 卡提
* */
public class Cartethyia extends BaseCubie{
    int extraPad;
    public Cartethyia(Board.SpaceManager spaceManager) {
        super(Names.CARTETHYIA, spaceManager);
        extraPad = 0;
    }

    @Override
    protected void moveInTurn() {
        int forwardSteps = diceRollRecord.getDiceValue() + extraPad;
        while (forwardSteps-- > 0) {
            //如果有团子胜利了，直接结束
            if(spaceManager.getEndSpace().hasCubie()) return;
            oneStep();
        }
        activateSkill();
    }

    //技能：如果行动结束后是最后，有60%概率后续所有行动都额外前进2步
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

        //自己是最后,之后额外行动2步数
        if (Util.probabilityCheck(60)){
            this.extraPad = 2;
        }
    }

    @Override
    public void clearState() {
        extraPad = 0;
    }
}
