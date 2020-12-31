package com.example.demo.test;

/**
 * @Description:
 * @Auther: logo丶西亚卡姆
 * @Date: 2020/10/13 09:35
 */
public class sys {
    public static void main(String[] args) {

        int[] arr1 = {1, 2, 3, 4, 5};
        int[] arr2 = new int[10];
        System.arraycopy(arr1,3,arr2,8,2);
        for (int i = 0; i < arr2.length; i++) {
            System.out.println(arr2[i]);
        }
    }
}
