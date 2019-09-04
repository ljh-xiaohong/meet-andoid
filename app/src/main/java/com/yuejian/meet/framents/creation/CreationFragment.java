package com.yuejian.meet.framents.creation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aliyun.demo.recorder.activity.AlivcSvideoRecordActivity;
import com.aliyun.svideo.base.AlivcSvideoEditParam;
import com.aliyun.svideo.editor.MediaActivity;
import com.aliyun.svideo.editor.effectmanager.MoreMVActivity;
import com.aliyun.svideo.editor.effects.control.BaseChooser;
import com.aliyun.svideo.sdk.external.struct.snap.AliyunSnapVideoParam;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.ArticleEditActivity;
import com.yuejian.meet.activities.creation.MusicSelectTemplateActivity;
import com.yuejian.meet.activities.creation.SuibiEditActivity;
import com.yuejian.meet.framents.base.BaseFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/05/11 15:48
 * @desc : 首页 - 创作Fragment
 */
public class CreationFragment extends BaseFragment {

    @Bind(R.id.cv_creation_video_btn)
    CardView mCreationVideoBtn;
    @Bind(R.id.cv_creation_suibi_btn)
    CardView mCreationSuibiBtn;
    @Bind(R.id.cv_creation_article_btn)
    CardView mCreationArticleBtn;
    @Bind(R.id.cv_creation_music_btn)
    CardView mCreationMusicBtn;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_creation, container, false);
    }

    @OnClick({R.id.cv_creation_video_btn, R.id.cv_creation_suibi_btn,
            R.id.cv_creation_article_btn, R.id.cv_creation_music_btn})
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.cv_creation_video_btn:
                AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder().build();
                AlivcSvideoRecordActivity.startRecord(getActivity(), recordParam,"creation");
                break;
            case R.id.cv_creation_suibi_btn:
                Intent toSuibiIntent = new Intent(getActivity(), SuibiEditActivity.class);
                startActivity(toSuibiIntent);

                break;
            case R.id.cv_creation_article_btn:
                Intent toArticleIntent = new Intent(getActivity(), ArticleEditActivity.class);
                startActivity(toArticleIntent);
                break;
            case R.id.cv_creation_music_btn:
                /*Intent toMusicIntent = new Intent(getActivity(), MusicSelectTemplateActivity.class);
                toMusicIntent.putExtra("index", 0);
                startActivity(toMusicIntent);*/
                Toast ts = Toast.makeText(getActivity(),"功能正在开发中!", Toast.LENGTH_LONG);
                ts.show() ;//这个是打开的意思,就是调用的意思。
//                AlivcSvideoEditParam editParam = new AlivcSvideoEditParam.Build().build();
//                MediaActivity.startImport(getActivity(), editParam);

//                Intent moreIntent = new Intent(mContext, MoreMVActivity.class);
//                moreIntent.putExtra(MoreMVActivity.SELECTD_ID, selectId);
//                startActivityForResult(moreIntent, BaseChooser.IMV_REQUEST_CODE);
                break;
            default:
                break;
        }

    }
}
