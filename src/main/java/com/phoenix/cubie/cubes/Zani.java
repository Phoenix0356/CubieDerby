package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
* 赞妮
* */
public class Zani extends BaseCubie{
    boolean canSkillActivated = false;
    public Zani(Board.SpaceManager spaceManager) {
        super(Names.ZANI, spaceManager);
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

    //技能：只能投出1或者3，如果本次行动有团子叠在上面，则下一轮有40%概率多走两步
    @Override
    protected void doActivateSkill() {
        //只要投到2就重投
        while (diceRollRecord.getDiceValue() == 2){
            diceRollRecord.setDiceValue(Util.randInt());
        }
        //是否满足技能判定条件
        if(canSkillActivated && Util.probabilityCheck(40)){
            diceRollRecord.setDiceValue(diceRollRecord.getDiceValue() + 2);
        }
        //判断这轮有没有团子叠在自己上面
        canSkillActivated = this.upperCubie != null;

    }

    @Override
    public void clearState() {
        canSkillActivated = false;
    }
}
