package com.phoenix.cubie;

import lombok.Getter;

@Getter
public enum Order {
    FIRST(1),
    SECOND(2);

    final int val;

    Order(int state){
        this.val = state;
    }
}
