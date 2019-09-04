package com.yuejian.meet.utils;

import com.netease.nim.uikit.common.util.storage.StorageType;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;

/**
 * Created by zh03 on 2017/8/9.
 */

public class ImgUtils {

    public static final String JPG = ".jpg";

    public static String imgTempFile() {
        String filename = StringUtil.get32UUID() + JPG;
        return StorageUtil.getWritePath(filename, StorageType.TYPE_TEMP);
    }
}
