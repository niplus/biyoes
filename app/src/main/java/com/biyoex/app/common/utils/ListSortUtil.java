package com.biyoex.app.common.utils;

import com.biyoex.app.my.bean.PersonalAssetsBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by LG on 2017/7/17.
 */

public class ListSortUtil {
    /**
     *
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public  void sort(List<PersonalAssetsBean.DataBean.VWalletsBean> targetList) {
            Collections.sort(targetList, new Comparator(){
                @Override
                public int compare(Object o1, Object o2) {
                    try {
                        PersonalAssetsBean.DataBean.VWalletsBean stu1=(PersonalAssetsBean.DataBean.VWalletsBean)o1;
                        PersonalAssetsBean.DataBean.VWalletsBean stu2=(PersonalAssetsBean.DataBean.VWalletsBean)o2;
                        if(stu2.getDoubleTotal()<stu1.getDoubleTotal()){
                            return -1;
                        }else{
                            return 1;
                        }
                        //return stu2.getDoubleTotal().compare(stu1.getDoubleTotal());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }

}
