package com.phoenix.derby;

import com.phoenix.Util;
import com.phoenix.cubie.Order;
import com.phoenix.cubie.cubes.BaseCubie;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
* 比赛场地类
* */
@Getter
public class Board {
    public static Map<String, Integer> winnerRecord = new HashMap<>();
    private static final int BOARD_SIZE = 24;
    //轮次计数
    private int roundCount;
    //终点格子
    private Space end;
    //地图格子获取器
    private final SpaceManager spaceManager;
    //参赛团子列表
    List<BaseCubie> cubieList;
    //获胜团子
    BaseCubie winner;
    //是否产生获胜者
    boolean isWinnerGenerated;

    public Board() {
        this.roundCount = 1;
        spaceManager = new SpaceManager();
    }

    public void init(List<BaseCubie> cubieList) {
        this.cubieList = cubieList;
        //初始化地图
        int spaceCount = 1;
        Space start = new Space(Order.FIRST.getVal(), null, null, null);
        spaceManager.put(spaceCount,start);
        Space prev = start, cur = null;
        while (spaceCount++ < BOARD_SIZE) {
            cur = new Space(spaceCount, null, prev, null);
            spaceManager.put(spaceCount,cur);
            prev.next = cur;
            prev = cur;
        }
        end = cur;
    }

    public void initCubes(){
        //团子放上地图
        this.cubieList.forEach(cubie -> {
            cubie.setPosition(1);
        });
    }

    public void startDerby(){
        while (!isWinnerGenerated){
//            System.out.println();
//            System.out.printf("第%d轮比赛开始%n", roundCount);
            prepareRound();
            startRound();
            checkWinner();
            roundCount++;
        }

//        System.out.printf("总共比赛%d轮%n", roundCount);
        winnerRecord.put(winner.getName(), winnerRecord.getOrDefault(winner.getName(),0)+1);
        System.out.printf("%s团子获得胜利%n",winner.getName());
        System.out.println();
        clearBoard();
    }

    private void prepareRound() {
        //决定团子行动顺序
        Util.reorderCubieList(cubieList);
        //团子投骰子，并记录顺序
        int ord = Order.FIRST.getVal();
        for (int i = 0;i<cubieList.size();i++) {
            BaseCubie cubie = cubieList.get(i);
            cubie.rollDice(ord++, i == cubieList.size()-1);
        }
    }

    private void startRound(){
        cubieList.forEach(BaseCubie::move);
    }

    private void checkWinner(){
        //查看终点上是否有团子
        BaseCubie cubie = end.getFirstBaseCubie();
        if (cubie == null){
            return;
        }

        isWinnerGenerated = true;
        while (cubie.getUpperCubie() != null){
            cubie = cubie.getUpperCubie();
        }

        winner = cubie;
    }
    private void clearBoard(){
        this.roundCount = 1;
        cubieList.forEach(BaseCubie::clear);
        spaceManager.clearState();
        end.setFirstBaseCubie(null);
        isWinnerGenerated = false;
        winner = null;
    }

    /*
    * 格子类
    * */
    @Data
    public static class Space{
        int order;
        Space next;
        Space prev;

        BaseCubie firstBaseCubie;

        public Space(int order, Space next, Space prev, BaseCubie firstBaseCubie){
            this.order = order;
            this.next = next;
            this.prev = prev;
            this.firstBaseCubie = firstBaseCubie;
        }

        public boolean hasCubie(){
            return firstBaseCubie != null;
        }
    }
    /*
    * 格子管理器
    * */
    public static class SpaceManager {
        private final Map<Integer,Space> spaceMap;

        public SpaceManager() {
            this.spaceMap = new HashMap<>();
        }

        public Space get(Integer pos){
            return spaceMap.getOrDefault(pos,null);
        }

        public void put(Integer pos, Space space){
            spaceMap.put(pos, space);
        }

        public Space getEndSpace(){
            return spaceMap.get(BOARD_SIZE);
        }

        public void clearState(){
            for (Integer key: spaceMap.keySet()){
                spaceMap.get(key).setFirstBaseCubie(null);
            }
        }
    }
}

