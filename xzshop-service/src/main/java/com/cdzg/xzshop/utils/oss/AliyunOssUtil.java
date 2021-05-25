package com.cdzg.xzshop.utils.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.cdzg.xzshop.mapper.OrderMapper;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.utils.pay.PayClientUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 阿里云OSS相关java API
 * @author xiaoxia
 *øø
 */
@Component
@Slf4j
public class AliyunOssUtil {


    /**
     *  删除一个Bucket和其中的Objects
     * @param client
     * @param bucketName
     * @throws OSSException
     */

    private static OSSClient client;

    @Autowired
    public AliyunOssUtil(OSSClient client) {
       AliyunOssUtil.client = client;
    }

    public static void deleteBucket(String bucketName)
            throws OSSException, ClientException {

        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing
                .getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }

    /**
     *  把Bucket设置为所有人可读
     * @param client
     * @param bucketName
     * @throws OSSException
     * @throws ClientException
     */
    public static void setBucketPublicReadable(String bucketName)
            throws OSSException, ClientException {
        //创建bucket
        client.createBucket(bucketName);

        //设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    /**
     *  上传文件
     * @param client
     * @param bucketName
     * @param key
     * @param filename
     * @param contentType default "image/gif"
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public static String uploadFile(String bucketName, String key, String filename,String contentType)
            throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filename);
        contentType = contentType == null ? "image/gif" : contentType;
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        objectMeta.setContentType(contentType);
        InputStream input = new FileInputStream(file);
        PutObjectResult result =  client.putObject(bucketName, key, input, objectMeta);
        return result.getETag();
    }

    /**
     * 下载文件
     * @param bucketName
     * @param key
     * @param filename
     * @throws OSSException
     * @throws ClientException
     */
    public static void downloadFile( String bucketName, String key, String filename)
            throws OSSException, ClientException {
        client.getObject(new GetObjectRequest(bucketName, key),
                new File(filename));
    }

    public static InputStream downloadFile(String url)
            throws OSSException, ClientException, MalformedURLException {
        OSSObject object = client.getObject(new URL(url), Maps.newHashMap());
        return object.getObjectContent();
    }



    /**
     * 创建一个文件夹
     * @param client
     * @param bucketName
     * @param folderPah
     */
    public static void createFolder(String bucketName,String folderPah){
        ObjectMetadata objectMeta = new ObjectMetadata();
        byte[] buffer = new byte[0];
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        objectMeta.setContentLength(0);
        try {
            client.putObject(bucketName, folderPah, in, objectMeta);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除一个OSS文件对象
     * @param client
     * @param bucketName
     * @param key
     */
    public static void deleteObject( String bucketName, String key){
        client.deleteObject(bucketName, key);
    }
}

