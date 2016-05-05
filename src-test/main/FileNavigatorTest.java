package main;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Shirobako on 4/24/2016.
 */
public class FileNavigatorTest {
    @Test
    public void testScanDirectory() {
//        FileNavigator fileNavigator = new FileNavigator(new File("D:\\P\\Fiction"));
        FileNavigator fileNavigator = new FileNavigator(new File("D:\\Downloads\\derp"));
        int count = fileNavigator.scanDirectories();
//        Assert.assertEquals(36821, count);
    }
}