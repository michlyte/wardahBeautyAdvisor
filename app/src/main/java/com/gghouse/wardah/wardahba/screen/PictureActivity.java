package com.gghouse.wardah.wardahba.screen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAParams;
import com.gghouse.wardah.wardahba.common.WBAProperties;
import com.gghouse.wardah.wardahba.model.Notif;
import com.gghouse.wardah.wardahba.util.WBAUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PictureActivity extends AppCompatActivity {

    private SubsamplingScaleImageView mIVImage;
    private Button mBClose;

    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mIVImage.setImage(ImageSource.bitmap(bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        mIVImage = (SubsamplingScaleImageView) findViewById(R.id.iv_image);
//        mIVImage.setMaxScale(4.0F);
        mIVImage.setMinimumDpi(20);
        Intent intent = getIntent();
        if (intent != null) {
            Notif notif = (Notif) intent.getSerializableExtra(WBAParams.DATA);
            switch (WBAProperties.mode) {
                case DUMMY_DEVELOPMENT:
                    mIVImage.setImage(ImageSource.resource(notif.getDrawable()));
                    break;
                case DEVELOPMENT:
                case PRODUCTION:
                    Picasso.with(this)
                            .load(WBAUtil.constructImageUrl(notif.getFileLocation().getImgUrl()))
                            .placeholder(R.drawable.progress_animation)
                            .error(R.drawable.pic_image_not_found)
                            .into(target);
                    break;
            }
        } else {
            mIVImage.setImage(ImageSource.resource(R.drawable.pic_image_not_found));
        }

        mBClose = (Button) findViewById(R.id.b_close);
        mBClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
