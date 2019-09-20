package com.yuejian.meet.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.widgets.CustomToast;


//import com.rablive.jwrablive.app.FolkApplication;

/**
 * 侵入式View的调用工具类
 * <p/>
 * <b>创建时间</b> 2016/4/7
 *
 * @author zhouwenjun
 */
public class ViewInject {

    private static CustomToast sCustomToast;

    private static Toast myToast;

    private ViewInject() {
    }

    private static class ClassHolder {
        private static final ViewInject instance = new ViewInject();
    }

    /**
     * 类对象创建方法
     *
     * @return 本类的对象
     */
    public static ViewInject create() {
        return ClassHolder.instance;
    }

    public static void CollectionToast(Context context, String message) {
        if (null == myToast) {
            myToast = new Toast(context);
            myToast.setView(LayoutInflater.from(context).inflate(R.layout.toast_collection, null));
        }
        TextView tv = myToast.getView().findViewById(R.id.toast_tv);
        tv.setText(message);
        myToast.setDuration(Toast.LENGTH_LONG);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();
    }

    /**
     * 显示一个toast
     *
     * @param msg
     */
    public static void shortToast(Context context, String msg) {
        showToast(context, msg, true);
    }

    public static void shortToast(Context context, int valuestringid) {
        String msg = context.getResources().getString(valuestringid);
        showToast(context, msg, true);
    }

    public static void toast(Context context, int valuestringid) {
        String msg = context.getResources().getString(valuestringid);
        toast(context, msg);
    }

    public static void toast(String msg, Context context) {
        toast(context, msg);
    }

    public static void toast(Context context, String msg) {
        showToast(context, msg, false);
    }

    /**
     * 显示一个toast
     *
     * @param msg
     */
    private static void showToast(Context context, String msg, boolean isShort) {
        try {
            if (context != null) {
                if (sCustomToast == null) {
                    sCustomToast = new CustomToast(context);
                }
                sCustomToast.show(msg, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
            }
        } catch (Exception e) {

        }
    }


    public static void cancelToast() {
        if (sCustomToast != null) {
            sCustomToast.hide();
        }
    }

    /**
     * 显示一个toast
     *
     * @param valuestringid ValueString Id
     */


    /**
     * 返回一个退出确认对话框
     */
    public void getExitDialog(final Context context, String title,
                              OnClickListener l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(title);
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", l);
        builder.create();
        builder.show();
        builder = null;
    }

    /**
     * 返回一个自定义View对话框
     */
    public AlertDialog getDialogView(Context cxt, String title, View view) {
        AlertDialog dialog = new AlertDialog.Builder(cxt).create();
        dialog.setMessage(title);
        dialog.setView(view);
        dialog.show();
        return dialog;
    }

    /**
     * 用于创建PopupWindow封装一些公用属性
     */
    // private PopupWindow createWindow(View view, int w, int h, int argb) {
    // PopupWindow popupView = new PopupWindow(view, w, h);
    // popupView.setFocusable(true);
    // popupView.setBackgroundDrawable(new ColorDrawable(argb));
    // popupView.setOutsideTouchable(true);
    // return popupView;
    // }

    /**
     * 返回一个日期对话框
     */
//    public void getDateDialog(String title, final TextView textView) {
//        final String[] time = SystemTool.getDataTime("yyyy-MM-dd").split("-");
//        final int year = StringUtils.toInt(time[0], 0);
//        final int month = StringUtils.toInt(time[1], 1);
//        final int day = StringUtils.toInt(time[2], 0);
//        DatePickerDialog dialog = new DatePickerDialog(textView.getContext(),
//                new OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        textView.setText(year + "-" + (monthOfYear + 1) + "-"
//                                + dayOfMonth);
//                    }
//                }, year, month - 1, day);
//        dialog.setTitle(title);
//        dialog.show();
//    }

    /**
     * 返回一个等待信息弹窗
     *
     * @param aty    要显示弹出窗的Activity
     * @param msg    弹出窗上要显示的文字
     * @param cancel dialog是否可以被取消
     */
    public static ProgressDialog getprogress(Activity aty, String msg,
                                             boolean cancel) {
        // 实例化一个ProgressBarDialog
        ProgressDialog progressDialog = new ProgressDialog(aty);
        progressDialog.setMessage(msg);
        progressDialog.getWindow().setLayout(DensityUtils.getScreenW(aty),
                DensityUtils.getScreenH(aty));
        progressDialog.setCancelable(cancel);
        // 设置ProgressBarDialog的显示样式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        return progressDialog;
    }

//    public static Context getAppContext() {
//        return FolkApplication.mAppContext;
//    }


}
