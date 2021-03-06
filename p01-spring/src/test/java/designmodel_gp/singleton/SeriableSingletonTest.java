package designmodel_gp.singleton;/**
 * Created by My on 2020-02-27.
 */

import com.panda.study.designmodel_gp.singleton.seriable.SeriableSingleton;

import java.io.*;

/**
 * @Author: Likaisheng
 * @Description:
 * @Date: Created in 11:20:38 2020-02-27
 * @Modified By:
 */
public class SeriableSingletonTest {
    public static void main(String[] args) {
        SeriableSingleton s1 = null;
        SeriableSingleton s2 = SeriableSingleton.getInstance();

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream("D:\\SeriableSingleton.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s2);
            oos.writeObject(s2);
            oos.flush();
            oos.close();

            FileInputStream fis = new FileInputStream("D:\\SeriableSingleton.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);
            s1 = (SeriableSingleton)ois.readObject();
            ois.close();

            System.out.println(s1);
            System.out.println(s2);
            System.out.println(s1 == s2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
