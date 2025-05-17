package com.phoenix.cubie.cubes;

import com.phoenix.cubie.Names;
import com.phoenix.derby.Board;

/*
 * 今汐
 * */
public class Jingxi extends BaseCubie{
    public Jingxi(Board.SpaceManager spaceManager) {
        super(Names.JINGXI, spaceManager);
    }

    @Override
    protected void moveInTurn() {
        int forwardSteps = diceRollRecord.getDiceValue();
        while (forwardSteps-- > 0) {
            //如果有团子胜利了，直接结束
            if(spaceManager.getEndSpace().hasCubie()) return;
            oneStep();
        }
        activateSkill();
    }

    //技能：如果上方有团子堆叠，则有40%概率移到最上面
    @Override
    protected void doActivateSkill() {
        BaseCubie upperCubie = this.upperCubie;
        if (upperCubie == null) return;
        //移动自己到最上面
        BaseCubie lowerCubie = this.lowerCubie;
        BaseCubie topCubie = upperCubie;
        while (topCubie.upperCubie != null){
            topCubie = topCubie.upperCubie;
        }

        topCubie.upperCubie = this;

        this.lowerCubie = topCubie;
        this.upperCubie = null;
        //如果自己不在最下面，连接自己下面的团子和上面的团子
        upperCubie.lowerCubie = lowerCubie;
        if (lowerCubie != null){
            lowerCubie.upperCubie = upperCubie;
        }
    }

    @Override
    public void clearState() {

    }
}
