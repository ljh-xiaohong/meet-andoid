package com.yuejian.meet.widgets.letterList;

import com.yuejian.meet.bean.NewFriendBean;

import java.util.Comparator;

/**
 * 专用于按首字母排序
 * @author : ljh
 * @time : 2019/9/29 16:20
 * @desc :
 */
public class LetterComparator implements Comparator<NewFriendBean.DataBean>{

    @Override
    public int compare(NewFriendBean.DataBean contactModel, NewFriendBean.DataBean t1) {
        if (contactModel == null || t1 == null){
            return 0;
        }
        String lhsSortLetters = contactModel.getName().substring(0, 1).toUpperCase();
        String rhsSortLetters = t1.getName().substring(0, 1).toUpperCase();
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
