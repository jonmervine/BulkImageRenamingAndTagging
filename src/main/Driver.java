package main;

import java.io.File;

/**
 * Created by Shirobako on 4/23/2016.
 */
public class Driver {

    public static void main(String[] args) {
        File root = new File("D:\\P\\Fiction");
        FileNavigator fileNavigator = new FileNavigator(root);
        fileNavigator.scanDirectories();
    }
}
