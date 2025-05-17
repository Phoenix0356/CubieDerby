package com.phoenix.cubie;

import lombok.Getter;

@Getter
public enum SkillActivateState{
    NOT_ACTIVATED(0),
    ACTIVATING(1),
    ACTIVATED(2);

    final int state;

    SkillActivateState(int state){
        this.state = state;
    }
}
