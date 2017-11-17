package com.example.ls.myquxian;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ColuliActivity extends Activity {
    private CurveChartColuli line_curvechart;
    //这里的x和y轴自己可以添加100000条，只要你想要多少就可以。去试试吧。
    float[] xValues = new float[]{1, 2, 3, 4, 5, 6, 7};
    float[] yValues = new float[]{66, 11, 44, 67,44, 99, 12};
    private TextView caluli_tv, shouh_xq_caluli, tv_max_calilu, tv_zuidazhi;
    private String UserID;
    private List<Integer> arryList;
    private float MaxColilu;
    private float density;
    private int densityDPI;
    private int screenWidth;
    private int screenHeight;
    private static CurveChartColuli.CurveChartBuilder chartBuilder;
    private LinearLayout caluli_line_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coluli);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //初始化时候你们不要buidle.show()。没封装好调用会异常，你们有时间自己可以好好优化一下。
        line_curvechart = (CurveChartColuli) findViewById(R.id.line_curvechart_colu);
        chartBuilder = CurveChartColuli.CurveChartBuilder.createBuilder(line_curvechart);
        chartBuilder.setXYCoordinate((float)1.06, (float) 8.5, 0,1);//第一个参数决定了位置第一个起始位置显示的数字。
        chartBuilder.setFillDownColor(Color.parseColor("#8c8b8b"));
        chartBuilder.setXYValues(xValues, yValues);
        //这个方法里面为了屏幕适配没别的。你们可以不用管
        setDimager();
        initView();

    }

    private void setDimager() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
        screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
        line_curvechart.mHeight = (int) (screenHeight * 0.31);
        line_curvechart.mWidth = (int) (screenWidth * 0.83);
        line_curvechart.setFillDownLineColor(true);
    }

    private void initView() {
        tv_zuidazhi = (TextView) findViewById(R.id.tv_zuidazhi);
        caluli_tv = (TextView) findViewById(R.id.caluli_tv_connected);
        tv_max_calilu = (TextView) findViewById(R.id.tv_max_calilu);
        shouh_xq_caluli = (TextView) findViewById(R.id.shouh_xq_caluli);
        caluli_line_back = (LinearLayout) findViewById(R.id.caluli_line_back);
        chartBuilder.setXYValues(xValues, yValues);
    }

}
