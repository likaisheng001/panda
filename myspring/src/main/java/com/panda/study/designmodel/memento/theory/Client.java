package com.panda.study.designmodel.memento.theory;

/**
 * Created by Administrator on 2019/8/6.
 */
public class Client {
    public static void main(String[] args) {
        Originator originator = new Originator();
        Caretaker caretaker = new Caretaker();

        originator.setState(" 状态#1 攻击力100 ");
        //保持了当前的状态
        caretaker.add(originator.saveStateMemento());
        originator.setState(" 状态#2 攻击力80 ");

        caretaker.add(originator.saveStateMemento());
        originator.setState(" 状态#3 攻击力50 ");
        //希望回复到状态1
        System.out.println("当前的状态是=" + originator.getState());
        //希望得到状态1,将originator恢复到状态1
        originator.getStateFromMemento(caretaker.get(0));
        System.out.println("恢复到状态1，当前的状态是=" + originator.getState());
    }
}