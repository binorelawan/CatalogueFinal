package relawan.moviecatalogue.view;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.ViewPagerAdapter;
import relawan.moviecatalogue.fragment.MovieNowFragment;
import relawan.moviecatalogue.fragment.TvNowFragment;

public class NowPlayingActivity extends AppCompatActivity {

    private ViewPagerAdapter adapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.now_playing));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieNowFragment(), getResources().getString(R.string.movies));
        adapter.addFragment(new TvNowFragment(), getResources().getString(R.string.tv));
        viewPager.setAdapter(adapter);
    }


}
