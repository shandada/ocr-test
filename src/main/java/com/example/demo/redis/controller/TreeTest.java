package com.example.demo.redis.controller;

import ch.qos.logback.classic.joran.action.RootLoggerAction;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.beans.BeanUtils;

import org.springframework.util.CollectionUtils;
import sun.nio.cs.ext.TIS_620;

import java.io.Serializable;
import java.nio.file.NotDirectoryException;
import java.rmi.dgc.Lease;
import java.util.*;

/**
 * java构建树形结构
 * @author zls
 * @date 2019/12/26
 */
public class TreeTest {

    // 定义全局变量，统计地柜中总的循环次数
    private static int time;

    /**
     * 初始化数据，模拟从数据查询出来的数据
     * @return
     */
    public static List<Menu> initData() {
        List<Menu> menus = new ArrayList<>();

        menus.add(new Menu("0", "xxx公司", "", "1"));
        menus.add(new Menu("1", "财务部", "0", "2"));
        menus.add(new Menu("2", "人事部", "0", "2"));
        menus.add(new Menu("11", "财务部11", "1", "3"));
        menus.add(new Menu("12", "财务部12", "1", "3"));
        menus.add(new Menu("22", "财务部22", "2", "3"));
        menus.add(new Menu("21", "财务部21", "2", "3"));

        return menus;
    }

    public static void main(String[] args) {
        getTree();
    }

    /**
     * 构建树形结构
     */
    public static void getTree() {
        List<Menu> menus = initData();
        long start = System.currentTimeMillis();

        // 方法一
//        Menu root = new Menu();
//        Menu menu = buildNodes(root, menus);
//        System.out.println("JSON.toJSONString(root) = " + JSON.toJSONString(menu)); // 300s左右  递归循环了23次

        // 方法二
        Menu root1 = new Menu("0", "xxx公司", "", "1");
        // 此处假设了根节点，如果再加上你去找根节点，那么此方法花费的时间可能更多
        buildNodes1(root1, menus);
        // 750s左右, 可能更多   递归循环了49次
        System.out.println("JSON.toJSONString(root) = " + JSON.toJSONString(root1));
        System.out.println("time = " + time);
        long end = System.currentTimeMillis();
        System.out.println("花费时间 = " + (end-start) +"毫秒");
    }

    /**
     * 方法二：构建子节点
     * @param commonModel
     * @param list
     */
    public static void buildNodes1(Menu commonModel, List<Menu> list) {
        list.forEach(l -> {
            time++;
            if (commonModel.getId().equalsIgnoreCase((String) l.getParentId())) {
                Menu c = new Menu();
                BeanUtils.copyProperties(l, c);
                buildNodes1(c, list);
                commonModel.getNodes().add(c);
            }
        });
    }

    /**
     * 方法一：构建子节点
     * @param root
     * @param nodes
     * @return
     */
    public static Menu buildNodes(Menu root, List<Menu> nodes) {

        // 剩余节点（还没有找到父节点的节点）
        ArrayList<Menu> remainNodes = new ArrayList<>();
        // 当前节点下的子节点
        ArrayList<Menu> child = new ArrayList<>();

        Iterator<Menu> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            time++;
            Menu node = iterator.next();
            if (Objects.equals(node.getLevel(), "1")) {
                root = node;
                continue;
            }
            // 该节点找到了子节点
            if (Objects.equals(root.getId(), node.getParentId())) {
                child.add(node);
            } else {
                // 没有找到子节点
                remainNodes.add(node);
            }
        }
        // 根节点设置子节点
        root.setNodes(child);
        // 每一个节点再去寻找对应的子节点
        root.getNodes().stream().forEach(x -> {
            buildNodes(x, remainNodes);
        });
        return root;
    }
}

/**
 * 树形菜单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class Menu implements Serializable {
    private String id;
    // 菜单名称
    private String name;
    // 父id
    private String parentId;
    // 层级
    private String level;
    // 子节点
    private List<Menu> nodes = new ArrayList<>();

    public Menu(String id, String name, String parentId, String level) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.level = level;
    }

}