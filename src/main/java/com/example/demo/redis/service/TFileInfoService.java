package com.example.demo.redis.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.redis.mapper.TFileInfoMapper;
import com.example.demo.redis.pojo.TFileInfo;
import com.example.demo.redis.vo.OneChapter;
import com.example.demo.redis.vo.TwoFile;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2021/1/6 15:31
 */
@Service
@Transactional
public class TFileInfoService extends ServiceImpl<TFileInfoMapper, TFileInfo>{

    @Resource
    private TFileInfoMapper tFileInfoMapper;


    /**
     * 目录树状
     */
    public List<OneChapter> queryChapterAndVideoList() {
        long start = System.currentTimeMillis();
        //定义一个目录集合
        List<OneChapter> oneChapterList = new ArrayList<>();
        //先查询目录列表集合
        QueryWrapper<TFileInfo> queryWrapper = new QueryWrapper<>();
        //根据供应商id+name 去重 结果作为父节点
        queryWrapper.select("distinct supplier_id,supplier_name");
        List<TFileInfo> fileNames = baseMapper.selectList(queryWrapper);
        for (TFileInfo fileName : fileNames) {
            String supplierId = fileName.getSupplierId();
            //去重目录
            queryWrapper
                    .eq("supplier_id", supplierId);
            System.out.println("supplierName:supplierId " + fileName.getSupplierName() + supplierId);
        }
        //再遍历章节集合，获取每个节点ID(版本号)
        for (TFileInfo eduChapter : fileNames) {
            OneChapter oneChapter = new OneChapter();
            BeanUtils.copyProperties(eduChapter, oneChapter);
            //再根据每个目录的ID查询节点的列表
            QueryWrapper<TFileInfo> videoWrapper = new QueryWrapper<>();
            videoWrapper.eq("supplier_id", oneChapter.getSupplierId());
            List<TFileInfo> eduVideoList = tFileInfoMapper.selectList(videoWrapper);
            System.out.println("eduVideoList = " + eduVideoList);
            //把小节的列表添加目录中
            for (TFileInfo thisFile : eduVideoList) {
                TwoFile twoFile = new TwoFile();
                BeanUtils.copyProperties(thisFile, twoFile);
                //数据添加到二级目录
                oneChapter.getChildren().add(twoFile);
            }
            //所有数据添加自定义list集合中,返回
            oneChapterList.add(oneChapter);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        return oneChapterList;
    }

    /**
     * 目录树状2
     */

    /**
     * 多个文件删除
     *
     * @param ids
     */
    public void delMany(String[] ids) {
        //遍历字符串,
        for (String id : ids) {
            baseMapper.deleteById(id);
        }
    }
}

