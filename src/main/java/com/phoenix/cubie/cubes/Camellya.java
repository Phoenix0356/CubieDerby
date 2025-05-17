package com.phoenix.cubie.cubes;

import com.phoenix.Util;
import com.phoenix.cubie.Names;
import com.phoenix.cubie.SkillActivateState;
import com.phoenix.derby.Board;

/*
* 椿
* */
public class Camellya extends BaseCubie{
    private int skillActivatedState;
    public Camellya(Board.SpaceManager spaceManager) {
        super(Names.CAMELLYA, spaceManager);
        this.skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
    }

    @Override
    protected void moveInTurn() {
        activateSkill();
        int forwardSteps = diceRollRecord.getDiceValue();
        while (forwardSteps-- > 0) {
            //如果有团子胜利了，直接结束
            if(spaceManager.getEndSpace().hasCubie()) return;
            if (skillActivatedState == SkillActivateState.ACTIVATING.getState()){
                this.oneStep();
            }else{
                super.oneStep();
            }
        }
        skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
    }

    //技能：自身行动时，50%概率触发。当前格子除了自身每有一个团子，自身多行动一步，且不会带走其他团子
    @Override
    protected void doActivateSkill() {
        if(Util.probabilityCheck(50)){
            skillActivatedState = SkillActivateState.ACTIVATING.getState();
            diceRollRecord.setDiceValue(diceRollRecord.getDiceValue() + countOtherCubes());
        }
    }

    private int countOtherCubes(){
        int cnt = 0;
        BaseCubie cubie = this;
        while (cubie.upperCubie != null){
            cnt++;
            cubie = cubie.getUpperCubie();
        }
        while (cubie.lowerCubie != null){
            cnt++;
            cubie = cubie.lowerCubie;
        }
        return cnt;
    }

    @Override
    protected void oneStep(){
        //删除下面团子对自己的引用
        BaseCubie lowerCubie = this.lowerCubie;
        if (lowerCubie != null) {
            lowerCubie.upperCubie = null;
            this.lowerCubie = null;
        }else {
            //如果自己就是第一个团子，把格子上的第一个团子删掉
            Board.Space curSpace = spaceManager.get(position);
            curSpace.setFirstBaseCubie(null);
        }
        //自己往前走一步,不带其他团子
        position++;
        if (this.upperCubie!=null) {
            this.upperCubie.lowerCubie = null;
            this.upperCubie = null;
        }

        //拿到下一个格子
        Board.Space destSpace = spaceManager.get(position);
        //判断是否到达终点
        if (destSpace == spaceManager.getEndSpace()){
            //尝试声称自己胜利
            tryClaimWinner(destSpace);
        }else {
            //如果没到终点
            //拿到下一个格子上的团子
            BaseCubie cubieOnDest = destSpace.getFirstBaseCubie();
            //如果已经有团子，把自己叠上去
            if (cubieOnDest != null) {
                cubieOnDest = getTop(cubieOnDest);
                cubieOnDest.upperCubie = this;
                this.lowerCubie = cubieOnDest;
            } else {
                //没有团子，直接把自己放在格子上
                destSpace.setFirstBaseCubie(this);
            }
        }
    }

    @Override
    public void clearState() {
        skillActivatedState = SkillActivateState.NOT_ACTIVATED.getState();
    }
}
