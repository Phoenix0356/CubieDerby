package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.cubie.Names;
import com.phoenix.cubie.SkillActivateState;
import com.phoenix.derby.Board;

import java.util.List;

/*
* 长离
* */
//TODO长离有bug，但是不想改了
public class Changli extends BaseCubie{
    List<BaseCubie> cubieList;
    boolean canSkillActivated = false;
    private int skillActivatedState;
    private int savedDiceVal;

    public Changli(Board.SpaceManager spaceManager, List<BaseCubie> cubieList) {
        super(Names.CHANGLI, spaceManager);
        this.cubieList = cubieList;
        this.skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
        this.canSkillActivated = false;
        this.savedDiceVal = 0;
    }

    @Override
    protected void moveInTurn() {
        activateSkill();
        int forwardSteps = diceRollRecord.getDiceValue();
        //如果技能正在触发，说明是最后一个行动
        if (skillActivatedState == SkillActivateState.ACTIVATING.getState()){
            forwardSteps = savedDiceVal;
            skillActivatedState = SkillActivateState.ACTIVATED.getState();
        }
        while (forwardSteps-- > 0) {
            //如果有团子胜利了，直接结束
            if(spaceManager.getEndSpace().hasCubie()) return;
            oneStep();
        }
        //最后重置技能状态
        skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
    }

    //技能：如果下方堆叠其他团子，下一轮有65%概率最后行动
    @Override
    protected void doActivateSkill() {
        //如果触发技能，把自己行动顺序移到最后，本轮什么都不做
        if (skillActivatedState == SkillActivateState.NOT_ACTIVATED.getState() &&
                canSkillActivated && Util.probabilityCheck(65)){
            //把自己放到行动列表最后
            cubieList.add(this);
            //记录投到的数值，最后行动时恢复
            savedDiceVal = diceRollRecord.getDiceValue();
            skillActivatedState = SkillActivateState.ACTIVATING.getState();
            //本轮不走
            diceRollRecord.setDiceValue(0);
        }
        //判断自己上面是否有团子
        canSkillActivated = this.upperCubie != null;
    }

    @Override
    public void clearState() {
        this.cubieList.remove(cubieList.size()-1);
        this.skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
        this.canSkillActivated = false;
        this.savedDiceVal = 0;
    }
}
