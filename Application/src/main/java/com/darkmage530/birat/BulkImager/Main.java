package com.darkmage530.birat.BulkImager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by DarkMage530 on 5/21/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@ComponentScan
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Driver driver = context.getBean(Driver.class);
        try {
//            driver.drive();
        } catch (Exception e) {
            log.error("Failure", e);
        }
    }
}

    /*
        Run program to find all wallpapers, put into list with md5, cross reference with my initial md5 list of ALL images,
            and the iqdb irc returned DB list and then my exported version to verify nothing being found.
        iqdb limited due to the hits to iqdb i can make "on iqdb it's 60 requests per floating 5 minute window"
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
 */