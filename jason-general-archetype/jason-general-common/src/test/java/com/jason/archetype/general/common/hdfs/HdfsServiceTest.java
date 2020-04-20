package com.jason.archetype.general.common.hdfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author jasonCQ
 * @version 1.0
 * @date 2020/4/20 10:19
 */
@SpringBootTest(classes = {HdfsConfig.class, HdfsService.class})
public class HdfsServiceTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private HdfsService hdfsService;

    @BeforeClass
    public void setUp() {
        hdfsService.mkdir("/data/test");
        Assert.assertTrue(hdfsService.checkExists("/data/test"));

        try {
            File file = new File("./test.txt");
            file.createNewFile();
            Assert.assertTrue(file.exists());
        } catch (Exception e) {
            System.out.println("Exception Occurred:");
            e.printStackTrace();
        } finally {

        }
    }

    @AfterClass
    public void destory() {
        hdfsService.delete("/data/test");
        Assert.assertTrue(!hdfsService.checkExists("/data/test"));
        File file = new File("./test.txt");
        file.delete();
    }

    @Test
    public void testListFiles() {
        hdfsService.uploadFileToHdfs("./test.txt", "/data/test");
        Assert.assertTrue(hdfsService.checkExists("/data/test/test.txt"));
        List<Map<String, Object>> result = hdfsService.listFiles("/data/test", null);

        final boolean[] containFlag = {false};
        result.forEach(fileMap -> {
            fileMap.forEach((key, value) -> {
                if (value instanceof String) {
                    if (((String) value).contains("/data/test/test.txt")) {
                        containFlag[0] = true;
                    }
                }
            });
        });
        Assert.assertTrue(containFlag[0]);
        hdfsService.delete("/data/test/test.txt");
    }

    @Test
    public void testUploadByteFileToHdfs() {
        String str = new String("test");
        hdfsService.uploadByteFileToHdfs(str.getBytes(), "/data/test/test.txt");
        Assert.assertTrue(hdfsService.checkExists("/data/test/test.txt"));
        byte[] bytes = hdfsService.downloadByteFileFromHdfs("/data/test/test.txt");
        Assert.assertTrue(new String(bytes).equals(str));
        hdfsService.delete("/data/test/test.txt");
    }

    @Test
    public void testDownloadFileFromHdfs() {
        hdfsService.uploadFileToHdfs("./test.txt", "/data/test");
        Assert.assertTrue(hdfsService.checkExists("/data/test/test.txt"));
        hdfsService.downloadFileFromHdfs("/data/test/test.txt", "./textDownload.txt");
        File file = new File("./textDownload.txt");
        file.delete();
        file = new File("./.textDownload.txt.crc");
        file.delete();
        hdfsService.delete("/data/test/test.txt");
    }

    @Test
    public void testRename() {
        hdfsService.uploadFileToHdfs("./test.txt", "/data/test");
        Assert.assertTrue(hdfsService.checkExists("/data/test/test.txt"));
        hdfsService.rename("/data/test/test.txt", "/data/test/testRename.txt");
        Assert.assertTrue(hdfsService.checkExists("/data/test/testRename.txt"));
        hdfsService.delete("/data/test/testRename.txt");
    }
}