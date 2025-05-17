package com.phoenix.cubie.cubes;

import com.phoenix.cubie.Names;
import com.phoenix.cubie.SkillActivateState;
import com.phoenix.derby.Board;

/*
* 坎特蕾拉
* */
public class Cantarella extends BaseCubie {
    private int skillActivatedState;
    //技能触发时格子上第一个团子
    BaseCubie firstBaseCubieDuringSkill;
    //技能触发时自己下面的团子
    BaseCubie secondLastBaseCubieDuringSkill;

    public Cantarella(Board.SpaceManager spaceManager) {
        super(Names.CANTARELLA, spaceManager);
        this.skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
    }


    @Override
    protected void moveInTurn() {
        int forwardSteps = diceRollRecord.diceValue;
        while (forwardSteps-- > 0) {
            //如果有团子胜利了，直接结束
            if(spaceManager.getEndSpace().hasCubie()) return;
            //向前走一步
            oneStep();
            //拿到目前的格子
            Board.Space space = spaceManager.get(position);
            BaseCubie firstBaseCubie = space.getFirstBaseCubie();
            //如果格子上第一个团子不是自己，且技能还没有触发过，说明触发了技能
            if(firstBaseCubie != null && firstBaseCubie != this && skillActivatedState == 0) {
                activateSkill();
            }
        }
        //如果技能触发中，结束技能
        if(skillActivatedState == SkillActivateState.ACTIVATING.getState()) {
            skillActivatedState = SkillActivateState.ACTIVATED.getState();
            deActivateSkill();
        }
    }

    //技能：第一次遇到其他团子时带着他们前进
    @Override
    protected void doActivateSkill() {
        this.skillActivatedState = SkillActivateState.ACTIVATING.getState();
        //拿到当前格子上第一个团子
        firstBaseCubieDuringSkill = spaceManager.get(position).getFirstBaseCubie();
        //拿到自己下面的团子
        secondLastBaseCubieDuringSkill = this.lowerCubie;
        //把自己移到最下面，等价于带着所有团子一起动
        firstBaseCubieDuringSkill.lowerCubie = this;
        lowerCubie.upperCubie = null;
        this.lowerCubie = null;
        this.upperCubie = firstBaseCubieDuringSkill;

        //把自己设置为格子上第一个团子
        Board.Space space = spaceManager.get(position);
        space.setFirstBaseCubie(this);
    }

    private void deActivateSkill() {
        //拿到当前位置上自己下面的团子
        BaseCubie lowerBaseCubie = this.lowerCubie;

        //把之前受影响团子都移到原来的位置
        firstBaseCubieDuringSkill.lowerCubie = null;
        secondLastBaseCubieDuringSkill.upperCubie = this;
        this.lowerCubie = secondLastBaseCubieDuringSkill;
        this.upperCubie = null;

        if(lowerBaseCubie !=null){
            //把移完的团子重新叠到其他团子上
            lowerBaseCubie.upperCubie = firstBaseCubieDuringSkill;
            firstBaseCubieDuringSkill.lowerCubie = lowerBaseCubie;
        }else{
            //把移完的第一个团子设置到格子上
            Board.Space space = spaceManager.get(position);
            space.setFirstBaseCubie(firstBaseCubieDuringSkill);
        }
    }

    @Override
    public void clearState() {
        skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
        firstBaseCubieDuringSkill = null;
        secondLastBaseCubieDuringSkill = null;
    }
}
