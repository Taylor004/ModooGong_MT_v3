package kr.co.bravecompany.modoogong.android.stdapp.activity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import kr.co.bravecompany.modoogong.android.stdapp.R;
import kr.co.bravecompany.modoogong.android.stdapp.config.Tags;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {

    private Toolbar mToolbar;
    PhotoView photoView;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemBar(true);

        setContentView(R.layout.activity_photo_view);

        mPath = getIntent().getStringExtra(Tags.TAG_IMAGE);

        initLayout();
        initListener();
        initData();
    }

    private void initLayout() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        photoView = (PhotoView)findViewById(R.id.photoView);

    }

    private void initListener() {
    }

    private void initData(){
        if(mPath != null) {
            Glide.with(getApplicationContext())
                    .load(mPath)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .thumbnail(0.1f)
                    .into(photoView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
