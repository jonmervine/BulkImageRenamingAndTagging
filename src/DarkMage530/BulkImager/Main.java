package DarkMage530.BulkImager;

import DarkMage530.BulkImager.Csv.AllCsvDatabase;
import DarkMage530.BulkImager.Csv.WallpaperCsvDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by DarkMage530 on 5/21/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Configuration
@ComponentScan
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        BirtConfiguration config = context.getBean(BirtConfiguration.class);
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            log.error("Issue loading config.properties", ex);
            throw new RuntimeException(ex);
        }
        config.build(prop);

//        AllCsvDatabase allCsv = context.getBean(AllCsvDatabase.class);
//        allCsv.build();
//        WallpaperCsvDatabase wallpaperCsv = context.getBean(WallpaperCsvDatabase.class);
//        wallpaperCsv.build();
        Driver driver = context.getBean(Driver.class);

        try {
            driver.drive();
        } catch (Exception e) {
            log.error("Failure", e);
            throw new RuntimeException(e);
        }
    }
}

    /*    Current (maybe) issues:
        Need Multi threading slow <- limited due to the hits to iqdb i can make "on iqdb it's 60 requests per floating 5 minute window"
        maybe connection pool c3po
        Not doing anything with indivudal boorus yet
     */

/*
flow (Creating wallpaper structure)

!! Read in config settings
!! import local csv DB <- All Known
!! Import Wallpaper csvDB <- just wallpapers
!! start scanning from 'need to sort' bucket
!! filter any images of a certain resolution
!! check md5 to wallpaper DB
    if found
        ?? other potential workflows
        for purpose of wallpapers though this file is done (maybe duplicate/move to a 'to delete folder')
check md5 to local DB
    if found
!!      copy file to wallpaper directory
        tag jpgs, rename with tags
!!      update wallpaper dbs
? Take md5 to check boorus directly
    ? parse web info
    ? copy files to wallpaper directory
    ? tag jpgs rename with tags
    ? update local and wallpaper dbs
search iqdb to get rating and tags
    copy files to wallpaper directory
    tag jpgs, rename with tags
    update local and wallpaper dbs

 For Creating Wallpaper Structure With Wallpapers
* Read in Config Settings
* Import Local CSV DB (All Known)
* Import Wallpaper CSV DB (Just Wallpapers)
* Start Scanning from 'Need to Sort'
* Filter any images that match the wallpaper resolutions I want
* Check Md5 to Wallpaper DB
*
* Check Md5 to Local DB
*
* Search IQDB to get rating and tags
*   Copy files to wallpaper directory, tag jpgs, rename with tags, update local & wallpaper DB's

 */