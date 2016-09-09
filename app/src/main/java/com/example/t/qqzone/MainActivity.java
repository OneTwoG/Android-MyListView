package com.example.t.qqzone;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_header;
    private ParallaxListView lv;
    private SimpleDraweeView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        View header = View.inflate(this,R.layout.listview_header, null);
        iv_header = (ImageView) header.findViewById(R.id.layout_header_image);
        iv_icon = (SimpleDraweeView) header.findViewById(R.id.iv_icon);
        lv = (ParallaxListView) findViewById(R.id.parallax_lv);

        /*设置头像*/
        Uri uri = Uri.parse("res://" + getPackageName() + File.separator + R.drawable.icon);
        final Uri failureUri = Uri.parse("res://" + getPackageName() + "/" + R.drawable.failu);

        ControllerListener controllerListener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                iv_icon.setImageURI(failureUri);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setOldController(iv_icon.getController())
                .setControllerListener(controllerListener)
                .build();
        iv_icon.setController(controller);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R .layout.simple_list_item_1, new String[]{
                "星期一 写代码",
                "星期二 改bug",
                "星期三 改需求",
                "星期四 删代码",
                "星期五 写代码",
                "星期六 写代码",
                "星期日 可以休息了"
        });
        lv.addHeaderView(header);
        lv.setZoomImageView(iv_header);
        lv.setIconImageView(iv_icon);
        lv.setAdapter(adapter);
    }
}
