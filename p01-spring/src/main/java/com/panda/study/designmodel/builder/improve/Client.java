package com.panda.study.designmodel.builder.improve;

/**
 * Created by Administrator on 2019/7/29.
 */
public class Client {
    public static void main(String[] args) {
        //盖普通房子
        CommonHouse commonHouse = new CommonHouse();
        //准备创建房子的指挥者
        HouseDirector houseDirector = new HouseDirector(commonHouse);
        //完成盖房子，返回产品(房子)
        houseDirector.constructHouse();
        System.out.println("---------------------------------");
        //盖高楼
        HighBuilding highBuilding = new HighBuilding();
        houseDirector.setHouseBuilder(highBuilding);
        houseDirector.constructHouse();
    }
}
