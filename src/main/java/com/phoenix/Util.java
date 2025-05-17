package com.phoenix;

import com.phoenix.cubie.cubes.BaseCubie;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Util {
    private static final Random rand = new Random();
    public static int randInt() {
        //模拟骰子
        return rand.nextInt(3) + 1;
    }

    public static boolean probabilityCheck (int threshold) {
        if (threshold < 0 || threshold > 100) {
            throw new IllegalArgumentException("阈值必须在0到100之间");
        }
        return rand.nextInt(101) < threshold;
    }

    public static void reorderCubieList (List<BaseCubie> cubieList) {
        Collections.shuffle(cubieList);
    }
}
