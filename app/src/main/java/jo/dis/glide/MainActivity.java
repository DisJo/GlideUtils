package jo.dis.glide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import jo.dis.glide.utils.GlideUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView coverImg = findViewById(R.id.img_cover);
        ImageView avatarImg = findViewById(R.id.img_avatar);

        String coverUrl = "http://a.hiphotos.baidu.com/image/pic/item/960a304e251f95caf1852c0bc4177f3e6709521e.jpg";
        String avatarUrl = "https://goss4.vcg.com/creative/vcg/400/version23/VCG41200439238-001.jpg";

        GlideUtils.loadImage(this, coverUrl, R.color.colorPrimary, R.color.colorPrimary, coverImg);
        GlideUtils.loadCircleImage(this, avatarUrl, R.drawable.circle_place_holder, R.drawable.circle_place_holder, avatarImg);
    }
}
