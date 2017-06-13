package DarkMage530.BulkImager;

import DarkMage530.BulkImager.Iqdb.SearchIqdb;
import DarkMage530.BulkImager.Output.PictureOutput;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by DarkMage530 on 4/23/2016. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
@Component
public class Driver implements Runnable {

    /*    Current issues:
        Need Multi threading slow
        Updating Wallpapers will process a lot of duplicates because we are doing a copy and not a move
        Not doing anything with indivudal boorus yet, just scraping iqdb
        maybe connection pool c3po

        pre scan using md5 lookup to boorus before iqdb. "on iqdb it's 60 requests per floating 5 minute window"
     */

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    @Autowired
    private BirtConfiguration config;

    @Autowired
    private SearchIqdb searcher;

    @Autowired
    private PictureOutput outputResult;

    private final Object queueLock = new Object();

//    private List<PictureFile> queue = Lists.newLinkedList();

    private Queue<PictureFile> concurrentQueue = new ConcurrentLinkedQueue();

    public void drive() {
        if (config.isFindWallpaper()) {
            File moveRoot;
            if (config.isDefaultLocation()) {
                moveRoot = config.getEndLocation();
            } else {
                moveRoot = config.getWallpaperLocationOverride();
            }
            recursivelyScanDirectories(config.getScanLocation(), moveRoot);
        }

        boolean endApp = false;
        while (!endApp) {
            log.info("Waiting for threads to finish before ending App, queue size: " + concurrentQueue.size());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
//            synchronized (queueLock) {
                if (concurrentQueue.isEmpty()) {
                    endApp = true;
                    stopThreads();
                }
//            }
        }
        while (continueRunning) {
            log.info("Waiting for Threads to Stop");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
            }
        }
    }

    private List<Thread> threads = Lists.newArrayList();
    public void startThreads() {
        for (int i = 0; i < 10; i++) {
            Thread myThread = new Thread(this);
            myThread.start();
            threads.add(myThread);
        }
    }

    public void stopThreads() {
        continueRunning = false;
    }

    private void recursivelyScanDirectories(File recusiveRoot, File moveRoot) {
        for (File file : recusiveRoot.listFiles()) {
            if (file.isDirectory()) {
                recursivelyScanDirectories(file, moveRoot);
            } else if (file.getName().endsWith(".jpeg") ||
                    file.getName().endsWith(".jpg") ||
                    file.getName().endsWith(".png") ||
                    file.getName().endsWith(".gif")) {
                processFile(file, moveRoot);
            } else if (file.getName().endsWith("picasa.ini")) {
                file.delete();
            }
        }
    }

    private void processFile(File foundPicture, File moveRoot) {
        log.info("Found file " + foundPicture.getPath());
        final PictureFile pictureFile = new PictureFile(foundPicture, moveRoot);

        if (pictureFile.isWallpaper() && config.isFindWallpaper()) {
            log.info("pictureFile is wallpaper");

            pictureFile.addNextAction(() -> searcher.searchBoorus(pictureFile));
            pictureFile.addNextAction(() -> outputResult.copy(pictureFile));
//            synchronized (queueLock) {
                concurrentQueue.add(pictureFile);
//            }
        }
    }

    private void foundPicture(File file) {
    /*    log.info("Processing file " + file.getPath());

        Image image = null;
        try {
            image = searcher.searchBoorus(file);
        } catch (IqdbException e) {
            log.error("IqdbException", e);
        }

        if (image != null) {
            File outputFile = null;//outputResult.getOutputFile(image);

            XMPManager xmp = new XMPManager();
            try {
                if (xmp.hasTags(image.getLocation())) {
                    xmp.addXmlTags(image.getLocation(), outputFile, image.getTags());
                } else {
                    xmp.writeNewXmlTags(image.getLocation(), outputFile, image.getTags());
                }
                log.info("Wrote tags and moved to " + outputFile.getPath());
                image.getLocation().delete();
            } catch (MetadataExecption e) {
                log.error("Exception trying to write and move new tags to images");
            }
        } else {
//            outputResult.moveErroredFile(file);
            log.info("Image was null, moved to Manual sort");
        }*/
    }

    private volatile boolean continueRunning = true;

    @Override
    public void run() {
        log.info("Current Thread is: " + Thread.currentThread().getName() + " ID: " + Thread.currentThread().getId() + " To String " + Thread.currentThread().toString());

        boolean isEmpty = true;
        while (continueRunning) {
            if (isEmpty) {
                log.info("Queue is Empty");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                isEmpty = false;
            }
            PictureFile pictureFile = null;
//            synchronized (queueLock) {
                if (!concurrentQueue.isEmpty()) {
                    pictureFile = concurrentQueue.poll();
                }
//            }
            if (pictureFile != null) {
                log.info("Wallpaper to process");
                pictureFile.execute();
            } else {
                isEmpty = true;
            }
        }
    }
}
