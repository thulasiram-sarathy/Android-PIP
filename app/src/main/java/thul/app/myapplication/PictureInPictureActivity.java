package thul.app.myapplication;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Rational;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class PictureInPictureActivity extends AppCompatActivity {

    private VideoView vv;
    private Toolbar tb;
    private Button pip;
    private Uri videoUri;
    private final PictureInPictureParams.Builder pictureInPictureParamsBuilder =
            new PictureInPictureParams.Builder();

    private String defaultVideo =
            "http://mirrors.standaloneinstaller.com/video-sample/metaxas-keller-Bell.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_in_picture);

        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setSubtitle("Picture in Picture");

        vv = findViewById(R.id.videoView);
        setVideoView(getIntent());

        findViewById(R.id.play).setOnClickListener(onClickListener);
        findViewById(R.id.pause).setOnClickListener(onClickListener);
        pip =  findViewById(R.id.pip);
        pip.setOnClickListener(onClickListener);
    }
    private final View.OnClickListener onClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.play: {
                            vv.start();
                            break;
                        }
                        case R.id.pause: {
                            vv.stopPlayback();
                            break;
                        }
                        case R.id.pip:
                            pictureInPictureMode();
                            break;
                    }
                }
            };
    private void setVideoView(Intent i){
        String vUrl = i.getStringExtra("videoUrl");
        if(vUrl != null && !vUrl.isEmpty()){
            videoUri =
                    Uri.parse(vUrl);
        }else{
            videoUri =
                    Uri.parse(defaultVideo);
        }
        vv.setVideoURI(videoUri);
    }
    private void pictureInPictureMode(){
        Rational aspectRatio = new Rational(vv.getWidth(), vv.getHeight());
        pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
        enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
    }
    @Override
    public void onUserLeaveHint(){
        if(!isInPictureInPictureMode()){
            Rational aspectRatio = new Rational(vv.getWidth(), vv.getHeight());
            pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
            enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
        }
    }
    @Override
    public void onPictureInPictureModeChanged (boolean isInPictureInPictureMode,
                                               Configuration newConfig) {
        if (isInPictureInPictureMode) {
            pip.setVisibility(View.GONE);
            tb.setVisibility(View.GONE);
        } else {
            pip.setVisibility(View.VISIBLE);
            tb.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onNewIntent(Intent i){
        setVideoView(i);
    }
    @Override
    public void onStop() {
        if( vv.isPlaying()){
            vv.stopPlayback();
        }
        super.onStop();
    }
}
