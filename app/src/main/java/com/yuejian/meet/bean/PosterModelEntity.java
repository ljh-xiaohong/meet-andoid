package com.yuejian.meet.bean;

import java.io.Serializable;
import java.util.List;

public class PosterModelEntity implements Serializable {


    /**
     * id : 19
     * labelName : ["国庆节"]
     * postersTitle : 八月秋
     * previewUrl : http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/201909061157355735752097.jpg
     */

    private int id;
    private String postersTitle;
    private String previewUrl;
    private List<String> labelName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostersTitle() {
        return postersTitle;
    }

    public void setPostersTitle(String postersTitle) {
        this.postersTitle = postersTitle;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public List<String> getLabelName() {
        return labelName;
    }

    public void setLabelName(List<String> labelName) {
        this.labelName = labelName;
    }
}
