package com.example.demo.ocr;

import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import javax.annotation.Resource;
//图片 文字识别生成文档

/**
 * @Description: 图像文字识别
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 16:19
 */
@SuppressWarnings("ALL")
@Controller
public class OCRController {
    //接口申请免费，请自行申请使用，如果学习使用可以用下
//    public static final String APP_ID = "15742445";
//    public static final String API_KEY = "LXrztEOzQxfef66DLIDQYpIG";
//    public static final String SECRET_KEY = "gbDodnochc8jYjlAHADDgyyas9mrlmkF";



    public static final String APP_ID = "22255662";
    public static final String API_KEY = "UwzUPGz47W1BayXQmE8eBLgI";
    public static final String SECRET_KEY = "wMwWP2MInzGWveotVYUm3x03PwsQiniH";

    @SuppressWarnings("AlibabaRemoveCommentedCode")
    @ResponseBody
    @PostMapping("ocrimg")
    public String ocrimg(MultipartFile file) throws IOException {

        HashMap options = new HashMap();
        options.put("language_type", "CHN_ENG");
        //高精度识别一些参数在api文档可以参考
//        options.put("detect_direction", "true");
//        options.put("detect_language", "true");
//        options.put("probability", "true");
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        byte[] bite = file.getBytes();
        //上传文件名称
        String filenameAll = file.getOriginalFilename();
        //截取文件名
        String filename = filenameAll.substring(0, filenameAll.lastIndexOf("."));
        System.out.println("上传图片名称: " + filenameAll);
        JSONObject jsonObject = client.basicGeneral(bite, options);
        String all = jsonObject.get("words_result").toString();
        // 获取主要内容,字符串处理:
        String wordsAll = "";
        JSONArray objects = JSONArray.parseArray(all);
        for (int i = 0; i < objects.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonObjectAll = objects.getJSONObject(i);
            String allString = jsonObjectAll.getString("words");
            wordsAll += allString;
        }
        System.out.println("wordsAll: " + wordsAll);
        test(wordsAll, filename);
        return "图片内容如下:- - - -   "+wordsAll;
    }

    public void test(String all, String filename) throws IOException {
        String path = "E:\\ocr图像识别\\" + filename + ".docx";
        File file = new File(path);
        //生成文件之前 先删除原有文件, 暂不支持删除中文名文件
        file.delete();


        //引入输出流
        OutputStream outPutStream;
        try {
            outPutStream = new FileOutputStream(file);
            StringBuilder stringBuilder = new StringBuilder();//使用长度可变的字符串对象；

            stringBuilder.append(all);//追加文件内容
            String context = stringBuilder.toString();//将可变字符串变为固定长度的字符串，方便下面的转码；
            byte[] bytes = context.getBytes("UTF-8");//因为中文可能会乱码，这里使用了转码，转成UTF-8；
            outPutStream.write(bytes);//开始写入内容到文件；
            outPutStream.close();//一定要关闭输出流；
        } catch (Exception e) {
            e.printStackTrace();//获取异常
        }
    }
}
