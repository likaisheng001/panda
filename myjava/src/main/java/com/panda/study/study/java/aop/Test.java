package com.panda.study.study.java.aop;

/**
 * Created by Administrator on 2019/6/30.
 */
public class Test {
    private static int i = 1;
    public int getNext(){
        return i++;
    }
    public static void main(String[] args){
        Test test = new Test();
        Test testObject = new Test();
        test.getNext();
        testObject.getNext();
        System.out.println(testObject.getNext());
    }
}