package com.phoenix;

import com.phoenix.cubie.cubes.*;
import com.phoenix.derby.Board;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int ROUNDS = 1_000_000;
        Board board = new Board();
        Board.SpaceManager spaceManager = board.getSpaceManager();
        List<BaseCubie> cubieList = new ArrayList<>();
        //B组
        cubieList.add(new Cantarella(spaceManager));
        cubieList.add(new Roccia(spaceManager));
        cubieList.add(new Cartethyia(spaceManager));
        cubieList.add(new Brant(spaceManager));
        cubieList.add(new Zani(spaceManager));
        cubieList.add(new Phoebe(spaceManager));
        //TODO A组一堆bug懒得改
//        cubieList.add(new Calcharo(spaceManager));
//        cubieList.add(new Carlotta(spaceManager));
//        cubieList.add(new Camellya(spaceManager));
//        cubieList.add(new ShoreKeeper(spaceManager));
//        cubieList.add(new Jingxi(spaceManager));
//        cubieList.add(new Changli(spaceManager,cubieList));

        board.init(cubieList);

        for (int i = 1; i <= ROUNDS; i++) {
            System.out.printf("第%d轮比赛开始！！！", i);
            board.initCubes();
            board.startDerby();
        }
        var winnerRecord = Board.winnerRecord;
        System.out.printf("共比赛%d轮%n",ROUNDS);
        for (String key: winnerRecord.keySet()){
            System.out.println();
            System.out.printf("%s团子获得了%d次胜利，胜率为%.2f%%",key,winnerRecord.get(key),(double)winnerRecord.get(key)/ROUNDS*100);
        }
    }
}