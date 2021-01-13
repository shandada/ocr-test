package com.example.demo.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:
 * @Auther: Shan PengKun
 * @Date: 2021/1/8 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode {
    private String id;
    private String pid;

    private String name;
        private List<TreeNode> children;

}
