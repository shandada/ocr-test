//package com.example.demo.duan4;
//
///**
// * @author pengkun shan
// * @Description:
// * @date 2021/1/18 14:15
// */
//
//import com.amazonaws.ClientConfiguration;
//import com.amazonaws.Protocol;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.*;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Title:        S3Manager
// * Description:  Ceph储存的s3接口实现，参考文档：
// * https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/dev/RetrievingObjectUsingJava.html
// * http://docs.ceph.org.cn/radosgw/s3/
// * author:       xu jun
// * date:         2018/10/22
// */
//@Slf4j
//@Service
//public class S3Manager extends StorageManagerBase implements StorageManager {
//    private final UKID ukid;
//    private final S3ClientConfig s3ClientConfig;
//    private final RedisManage redisManage;
//    private AmazonS3 amazonClient;
//
//    @Autowired
//    public S3Manager(UKID ukid, S3ClientConfig s3ClientConfig, RedisManage redisManage) {
//        this.ukid = ukid;
//        this.s3ClientConfig = s3ClientConfig;
//        this.redisManage = redisManage;
//    }
//
//    private AmazonS3 getAmazonClient() {
//        if (amazonClient == null) {
//            String accessKey = s3ClientConfig.getAccessKey();
//            String secretKey = s3ClientConfig.getSecretKey();
//            String endpoint = s3ClientConfig.getEndPoint();
//
//            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//            ClientConfiguration clientConfig = new ClientConfiguration();
//            clientConfig.setProtocol(Protocol.HTTP);
//
//            AmazonS3 conn = AmazonS3ClientBuilder.standard()
//                    .withClientConfiguration(clientConfig)
//                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, ""))
//                    .withPathStyleAccessEnabled(true)
//                    .build();
//
//            //检查储存空间是否创建
//            checkBucket(conn);
//            amazonClient = conn;
//        }
//        return amazonClient;
//    }
//
//    @Override
//    public String uploadFile(byte[] fileData, String extension) {
//        log.info("Storage s3 api, upload file start");
//
//        //生成上传文件的随机序号
//        long fileId = ukid.getGeneratorID();
//        String fileName = Long.toString(fileId);
//        //储存空间名
//        String bucketName = s3ClientConfig.getBucketName();
//        AmazonS3 conn = getAmazonClient();
//
//        PutObjectResult result = conn.putObject(bucketName, fileName, new ByteArrayInputStream(fileData), null);
//        log.info("Storage s3 api, put object result :{}", result);
//
//        log.info("Storage s3 api, upload file end, file name:" + fileName);
//        return fileName;
//    }
//
//    @Override
//    public String uploadAppenderFile(byte[] fileData, String extension) {
//        log.info("Storage s3 api, upload appender file start");
//
//        //生成上传文件的随机序号
//        long ukId = ukid.getGeneratorID();
//        String fileName = Long.toString(ukId);
//        //储存空间名
//        String bucketName = s3ClientConfig.getBucketName();
//        AmazonS3 conn = getAmazonClient();
//        List<PartETag> partETags = new ArrayList<>();
//        //初始化分片上传
//        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, fileName);
//        InitiateMultipartUploadResult initResponse = conn.initiateMultipartUpload(initRequest);
//        String uploadId = initResponse.getUploadId();
//
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
//        Integer contentLength = fileData.length;
//        // 文件上传
//        UploadPartRequest uploadPartRequest = new UploadPartRequest()
//                .withBucketName(bucketName)
//                .withKey(fileName)
//                .withUploadId(uploadId)
//                .withPartNumber(1)
//                .withPartSize(contentLength)
//                .withInputStream(byteArrayInputStream);
//        UploadPartResult uploadPartResult = conn.uploadPart(uploadPartRequest);
//
//        try {
//            byteArrayInputStream.close();
//        } catch (IOException e) {
//            throw FileCenterExceptionConstants.INTERNAL_IO_EXCEPTION;
//        }
//        partETags.add(uploadPartResult.getPartETag());
//        Integer partNumber = uploadPartResult.getPartNumber();
//
//        S3CacheMode cacheMode = new S3CacheMode();
//        cacheMode.setPartETags(partETags);
//        cacheMode.setPartNumber(partNumber);
//        cacheMode.setUploadId(uploadId);
//        redisManage.set(fileName, cacheMode);
//
//        log.info("Storage s3 api, upload appender file end, fileName: {}", fileName);
//        return fileName;
//    }
//
//    @Override
//    public void uploadChunkFile(ChunkFileSaveParams chunkFileSaveParams) {
//        log.info("Storage s3 api, upload chunk file start");
//
//        String fileName = chunkFileSaveParams.getFileAddress();
//        Result result = redisManage.get(fileName);
//        JSONObject jsonObject = (JSONObject) result.getData();
//        if (jsonObject == null) {
//            throw FileCenterExceptionConstants.CACHE_DATA_NOT_EXIST;
//        }
//        S3CacheMode cacheMode = jsonObject.toJavaObject(S3CacheMode.class);
//        Integer partNumber = cacheMode.partNumber;
//        String uploadId = cacheMode.getUploadId();
//        List<PartETag> partETags = cacheMode.partETags;
//
//        //储存空间名
//        String bucketName = s3ClientConfig.getBucketName();
//        AmazonS3 conn = getAmazonClient();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(chunkFileSaveParams.getBytes());
//        Integer contentLength = chunkFileSaveParams.getBytes().length;
//
//        UploadPartRequest uploadPartRequest = new UploadPartRequest()
//                .withBucketName(bucketName)
//                .withKey(fileName)
//                .withUploadId(uploadId)
//                .withPartNumber(partNumber + 1)
//                .withPartSize(contentLength)
//                .withInputStream(byteArrayInputStream);
//
//        UploadPartResult uploadPartResult = conn.uploadPart(uploadPartRequest);
//        partETags.add(uploadPartResult.getPartETag());
//        partNumber = uploadPartResult.getPartNumber();
//
//        try {
//            byteArrayInputStream.close();
//        } catch (IOException e) {
//            throw FileCenterExceptionConstants.INTERNAL_IO_EXCEPTION;
//        }
//
//        S3CacheMode cacheModeUpdate = new S3CacheMode();
//        cacheModeUpdate.setPartETags(partETags);
//        cacheModeUpdate.setPartNumber(partNumber);
//        cacheModeUpdate.setUploadId(uploadId);
//        redisManage.set(fileName, cacheModeUpdate);
//
//        if (chunkFileSaveParams.getChunk().equals(chunkFileSaveParams.getChunks() - 1)) {
//            //完成分片上传，生成储存对象
//            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, fileName,
//                    uploadId, partETags);
//            conn.completeMultipartUpload(compRequest);
//        }
//
//        log.info("Storage s3 api, upload chunk file end");
//    }
//
//    @Override
//    public byte[] downloadFile(String fileName) {
//        log.info("Storage s3 api, download file start");
//        //储存空间名
//        String bucketName = s3ClientConfig.getBucketName();
//        AmazonS3 conn = getAmazonClient();
//        S3Object object;
//        if (conn.doesObjectExist(bucketName, fileName)) {
//            object = conn.getObject(bucketName, fileName);
//        } else {
//            throw FileCenterExceptionConstants.OBJECT_NOT_EXIST;
//        }
//        log.debug("Storage s3 api, get object result :{}", object);
//
//        byte[] fileByte;
//        InputStream inputStream;
//        inputStream = object.getObjectContent();
//        try {
//            fileByte = IOUtils.toByteArray(inputStream);
//            inputStream.close();
//        } catch (IOException e) {
//            throw FileCenterExceptionConstants.INTERNAL_IO_EXCEPTION;
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage());
//                }
//            }
//        }
//        log.info("Storage s3 api, download file end");
//        return fileByte;
//    }
//
//    @Override
//    public byte[] downloadFile(String fileName, long fileOffset, long fileSize) {
//        log.info("Storage s3 api, download file by block start");
//        //储存空间名
//        String bucketName = s3ClientConfig.getBucketName();
//        AmazonS3 conn = getAmazonClient();
//        S3Object object;
//        if (conn.doesObjectExist(bucketName, fileName)) {
//            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName)
//                    .withRange(fileOffset, fileOffset + fileSize);
//            //范围下载。
//            object = conn.getObject(getObjectRequest);
//        } else {
//            throw FileCenterExceptionConstants.OBJECT_NOT_EXIST;
//        }
//        log.info("Storage s3 api, get object result :{}", object);
//
//        // 读取数据。
//        byte[] buf;
//        InputStream in = object.getObjectContent();
//        try {
//            buf = inputToByte(in, (int) fileSize);
//        } catch (IOException e) {
//            throw FileCenterExceptionConstants.INTERNAL_IO_EXCEPTION;
//        } finally {
//            try {
//                in.close();
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        }
//        log.info("Storage s3 api, download file by block end");
//        return buf;
//    }
//
//    @Override
//    public String fileSecret(String filePath) {
//        return null;
//    }
//
//    @Override
//    public String fileDecrypt(String filePath) {
//        return null;
//    }
//
//    @Override
//    public String getDomain() {
//        return null;
//    }
//
//
//    /**
//     * 检查储存空间是否已创建
//     *
//     * @param conn 客户端连接
//     */
//    private void checkBucket(AmazonS3 conn) {
//        //储存空间名
//        String bucketName = s3ClientConfig.getBucketName();
//        if (conn.doesBucketExist(bucketName)) {
//            log.debug("Storage s3 api, bucketName is found: " + bucketName);
//        } else {
//            log.warn("Storage s3 api, bucketName is not exist, create it: " + bucketName);
//            conn.createBucket(bucketName);
//        }
//    }
//
//    /**
//     * inputStream转byte[]
//     *
//     * @param inStream 输入
//     * @param fileSize 文件大小
//     * @return 输出
//     * @throws IOException 异常
//     */
//    private static byte[] inputToByte(InputStream inStream, int fileSize) throws IOException {
//        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//        byte[] buff = new byte[fileSize];
//        int rc;
//        while ((rc = inStream.read(buff, 0, fileSize)) > 0) {
//            swapStream.write(buff, 0, rc);
//        }
//        return swapStream.toByteArray();
//    }
//
//    /**
//     * 调试用的方法，可以在控制台看到io的数据
//     *
//     * @param input 输入
//     * @throws IOException 异常
//    private static void displayTextInputStream(InputStream input) throws IOException {
//    // Read the text input stream one line at a time and display each line.
//    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//    String line;
//    while ((line = reader.readLine()) != null) {
//    log.info(line);
//    }
//    }
//     */
//}
