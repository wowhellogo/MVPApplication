package com.hao.common.main;

import java.io.Serializable;

/**
 * @Package com.haomvp.main
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2016年12月29日  16:07
 */


public class User implements Serializable {
    public String name;
    public String age;
    public String sex;

    public User(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
