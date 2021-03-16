package com.biyoex.app.common.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by xxx on 2018/10/20.
 */

public class MulitpleItem<T> implements MultiItemEntity {
    /**
     * 第一种布局的ItemType
     */
    public static final int FIRST_KIND_SYTLE = 1;
    /**
     * 第二种布局的ItemType
     */
    public static final int SECOND_KINDS_SYTLE = 2;
    /**
     * 第三种布局的ItemType
     */
    public static final int THIRD_KINDS_SYTLE = 3;
    /**
     * 第四种布局的ItemType
     */
    public static final int FOURTH_KINDS_SYTLE = 4;

    private int itemType;

    /**
     * 用于展示的数据
     */
    private T data;

    public MulitpleItem(int itemType, T data) {
        this.itemType = itemType;
        this.data = data;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public T getData() {
        return data;
    }
}
