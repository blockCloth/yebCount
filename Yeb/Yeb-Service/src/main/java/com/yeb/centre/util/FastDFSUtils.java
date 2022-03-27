package com.yeb.centre.util;

import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author dai
 * @create 2022-02-2022/2/13  16-45-46
 * 文件上传工具类
 */
public class FastDFSUtils {
    //定义日志打印
    private static final Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    //ClientGlobal.init方法回初始化配置 ，并初始化对应的配置
    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            logger.error("FastDFS初始化失败！", e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file) {
        String name = file.getOriginalFilename();
        logger.info("文件名: " + name);
        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        StorageClient storageClient = null;
        try {
            //获取storage客户端
            storageClient = getStorageClient();
            //上传文件
            uploadResults = storageClient.upload_file(file.getBytes(),
                    name.substring(name.lastIndexOf(".") + 1), null);
        } catch (Exception e) {
            logger.error("文件上传异常：",e.getMessage());
        }
        //验 证 上 传 结 果
        if (uploadResults == null && storageClient != null) {
            logger.error("文件上传失败，错误码：" +
                    storageClient.getErrorCode());
        }
        //上 传 文 件 成 功 会 返 回 groupName。
        return uploadResults;
    }
    /**
     * 获 取 文 件 信 息
     *
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            logger.error("获取文件信息失败", e);
        }
        return null;
    }
    /**
     * 下 载 文 件
     *
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName,
                    remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (Exception e) {
            logger.error("文件下载失败！", e.getMessage());
        }
        return null;
    }
    /**
     * 删 除 文 件
     *
     * @param groupName
     * @param remoteFileName
     * @tadminows Exception
     */
    public static void deleteFile(String groupName, String remoteFileName)
            throws Exception {
        StorageClient storageClient = getStorageClient();
        int i = storageClient.delete_file(groupName, remoteFileName);
        logger.info("文件删除成功！" + i);
    }
    /**
     * 生 成 Storage客 户 端
     *
     * @return
     * @tadminows IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer,
                null);
        return storageClient;
    }
    /**
     * 生 成 Tracker服 务 器 端
     *
     * @return
     * @tadminows IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        return trackerServer;
    }
    /**
     * 获 取 文 件 路 径
     *
     * @return
     */
    public static String getTrackerUrl() {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        StorageServer storeStorage = null;
        try {
            trackerServer = trackerClient.getTrackerServer();
            storeStorage = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://" + storeStorage.getInetSocketAddress().getHostString() + ":8888/";
    }

}
