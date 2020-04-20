package com.jason.archetype.general.common.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HdfsService {
    @Autowired
    private Configuration hdfsConfig;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取HDFS文件系统(按新配置生成，供外部使用)
     * @param hdfsConfig  提供其他配置
     * @return org.apache.hadoop.fs.FileSystem
     */
    public FileSystem getFileSystem(Configuration hdfsConfig) throws IOException {
        return FileSystem.get(hdfsConfig);
    }

    /**
     * 获取HDFS文件系统(按已有配置生成，供内部使用)
     *
     * @return org.apache.hadoop.fs.FileSystem
     */
    private FileSystem getFileSystem() throws IOException {
        return FileSystem.get(hdfsConfig);
    }

    /**
     * 判断文件或者目录是否在HDFS上面存在
     *
     * @param path HDFS的相对目录路径，比如：/testDir、/testDir/a.txt
     * @return boolean
     */
    public boolean checkExists(String path) {
        try (FileSystem fileSystem = getFileSystem()) {

            //最终的HDFS文件目录
            String hdfsPath = generateHdfsPath(path);

            //创建目录
            return fileSystem.exists(new Path(hdfsPath));
        } catch (IOException e) {
            logger.error(MessageFormat.format("'判断文件或者目录是否在HDFS上面存在'失败，path:{0}", path), e);
            return false;
        }
    }

    /**
     * 创建HDFS目录
     *
     * @param path HDFS的相对目录路径，比如：/testDir
     * @return boolean 是否创建成功
     */
    public boolean mkdir(String path) {
        try (FileSystem fileSystem = getFileSystem()) {
            if (!fileSystem.exists(new Path(path))) {
                //最终的HDFS文件目录
                String hdfsPath = generateHdfsPath(path);
                //创建目录
                return fileSystem.mkdirs(new Path(hdfsPath));
            } else {
                return true;
            }

        } catch (IOException e) {
            logger.error(MessageFormat.format("创建HDFS目录失败，path:{0}", path), e);
            return false;
        }
    }


    /**
     * 上传文件至HDFS
     *
     * @param srcFile 本地文件路径，比如：D:/test.txt
     * @param dstPath HDFS的相对目录路径，比如：/testDir
     */
    public void uploadFileToHdfs(String srcFile, String dstPath) {
        this.uploadFileToHdfs(false, true, srcFile, dstPath);
    }

    /**
     * 上传文件至HDFS
     *
     * @param delSrc    是否删除本地文件
     * @param overwrite 是否覆盖HDFS上面的文件
     * @param srcFile   本地文件路径，比如：D:/test.txt
     * @param dstPath   HDFS的相对目录路径，比如：/testDir
     */
    public void uploadFileToHdfs(boolean delSrc, boolean overwrite, String srcFile, String dstPath) {
        //源文件路径
        Path localSrcPath = new Path(srcFile);
        //目标文件路径
        Path hdfsDstPath = new Path(generateHdfsPath(dstPath));

        try (FileSystem fileSystem = getFileSystem()) {
            fileSystem.copyFromLocalFile(delSrc, overwrite, localSrcPath, hdfsDstPath);
        } catch (IOException e) {
            logger.error(MessageFormat.format("上传文件至HDFS失败，srcFile:{0},dstPath:{1}", srcFile, dstPath), e);
        }
    }

    /**
     * 上传文件至HDFS
     *
     * @param bytes   上传的字节流
     * @param dstPath HDFS的相对目录文件，比如：/test.txt
     */
    public void uploadByteFileToHdfs(byte[] bytes, String dstPath) {
        //目标文件路径
        Path hdfsDstPath = new Path(generateHdfsPath(dstPath));

        try (FileSystem fileSystem = getFileSystem();
             FSDataOutputStream fs = fileSystem.create(hdfsDstPath)) {
            fs.write(bytes);
            fs.flush();
        } catch (IOException e) {
            logger.error(MessageFormat.format("上传文件至HDFS失败，dstPath:{1}", dstPath), e);
        }
    }

    /**
     * 上传文件至HDFS
     *
     * @param dstPath HDFS的相对目录文件，比如：/test.txt
     */
    public byte[] downloadByteFileFromHdfs(String dstPath) {
        //目标文件路径
        Path hdfsDstPath = new Path(generateHdfsPath(dstPath));

        try (FileSystem fileSystem = getFileSystem();
             FSDataInputStream fs = fileSystem.open(hdfsDstPath)) {
            byte[] bytes = new byte[fs.available()];
            fs.read(bytes);
            return bytes;
        } catch (IOException e) {
            logger.error(MessageFormat.format("上传文件至HDFS失败，dstPath:{1}", dstPath), e);
        }
        return null;
    }

    /**
     * 获取HDFS上面的某个路径下面的所有文件或目录（不包含子目录）信息
     *
     * @param path HDFS的相对目录路径，比如：/testDir
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     */
    public List<Map<String, Object>> listFiles(String path, PathFilter pathFilter) {
        //返回数据
        List<Map<String, Object>> result = new ArrayList<>();

        //如果目录已经存在，则继续操作

        try (FileSystem fileSystem = getFileSystem()) {
            if (fileSystem.exists(new Path(path))) {
                //最终的HDFS文件目录
                String hdfsPath = generateHdfsPath(path);

                FileStatus[] statuses;
                //根据Path过滤器查询
                if (pathFilter != null) {
                    statuses = fileSystem.listStatus(new Path(hdfsPath), pathFilter);
                } else {
                    statuses = fileSystem.listStatus(new Path(hdfsPath));
                }

                if (statuses != null) {
                    for (FileStatus status : statuses) {
                        //每个文件的属性
                        Map<String, Object> fileMap = new HashMap<>(2);
                        fileMap.put("path", status.getPath().toString());
                        fileMap.put("isDir", status.isDirectory());
                        result.add(fileMap);
                    }
                }
            }
        } catch (IOException e) {
            logger.error(MessageFormat.format("获取HDFS上面的某个路径下面的所有文件失败，path:{0}", path), e);
        }

        return result;
    }


    /**
     * 从HDFS下载文件至本地
     *
     * @param srcFile HDFS的相对目录路径，比如：/testDir/a.txt
     * @param dstFile 下载之后本地文件路径（如果本地文件目录不存在，则会自动创建），比如：D:/test.txt
     */
    public void downloadFileFromHdfs(String srcFile, String dstFile) {
        //HDFS文件路径
        Path hdfsSrcPath = new Path(generateHdfsPath(srcFile));
        //下载之后本地文件路径
        Path localDstPath = new Path(dstFile);

        try (FileSystem fileSystem = getFileSystem()) {
            fileSystem.copyToLocalFile(hdfsSrcPath, localDstPath);
        } catch (IOException e) {
            logger.error(MessageFormat.format("从HDFS下载文件至本地失败，srcFile:{0},dstFile:{1}", srcFile, dstFile), e);
        }
    }

    /**
     * 重命名
     *
     * @param srcFile 重命名之前的HDFS的相对目录路径，比如：/testDir/b.txt
     * @param dstFile 重命名之后的HDFS的相对目录路径，比如：/testDir/b_new.txt
     */
    public boolean rename(String srcFile, String dstFile) {
        //HDFS文件路径
        Path srcFilePath = new Path(generateHdfsPath(srcFile));
        //下载之后本地文件路径
        Path dstFilePath = new Path(generateHdfsPath(dstFile));

        try (FileSystem fileSystem = getFileSystem()) {
            return fileSystem.rename(srcFilePath, dstFilePath);
        } catch (IOException e) {
            logger.error(MessageFormat.format("重命名失败，srcFile:{0},dstFile:{1}", srcFile, dstFile), e);
        }
        return false;
    }

    /**
     * 删除HDFS文件或目录
     *
     * @param path HDFS的相对目录路径，比如：/testDir/c.txt
     * @return boolean
     */
    public boolean delete(String path) {
        //HDFS文件路径
        Path hdfsPath = new Path(generateHdfsPath(path));

        try (FileSystem fileSystem = getFileSystem()) {
            return fileSystem.delete(hdfsPath, true);
        } catch (IOException e) {
            logger.error(MessageFormat.format("删除HDFS文件或目录失败，path:{0}", path), e);
        }

        return false;
    }

    /**
     * 将相对路径转化为HDFS文件路径
     *
     * @param dstPath 相对路径，比如：/data
     * @return java.lang.String
     */
    private String generateHdfsPath(String dstPath) {
        String hdfsPath = hdfsConfig.get("fs.defaultFS");
        if (dstPath.startsWith("/")) {
            hdfsPath += dstPath;
        } else {
            hdfsPath = hdfsPath + "/" + dstPath;
        }

        return hdfsPath;
    }
}
