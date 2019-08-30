package relawan.moviecatalogue.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import relawan.moviecatalogue.R;
import relawan.moviecatalogue.adapter.ViewPagerAdapter;
import relawan.moviecatalogue.fragment.MovieDiscoverFragment;
import relawan.moviecatalogue.fragment.TvDiscoverFragment;
import relawan.moviecatalogue.settings.SettingsActivity;
import relawan.moviecatalogue.view.favorite.FavoriteMovieActivity;
import relawan.moviecatalogue.view.favorite.FavoriteTvActivity;
import relawan.moviecatalogue.view.search.SearchMovieActivity;
import relawan.moviecatalogue.view.search.SearchTvActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPagerAdapter adapter;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPagerAdapter with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    public void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieDiscoverFragment(), getResources().getString(R.string.movies));
        adapter.addFragment(new TvDiscoverFragment(), getResources().getString(R.string.tv));
        viewPager.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_now_playing) {

            Intent intent = new Intent(MainActivity.this, NowPlayingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_popular) {

            Intent intent = new Intent(MainActivity.this, PopularActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_release) {

            Intent intent = new Intent(MainActivity.this, ReleaseActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_search_movie) {

            Intent intent = new Intent(MainActivity.this, SearchMovieActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_search_tv) {

            Intent intent = new Intent(MainActivity.this, SearchTvActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_favorite_movie) {

            Intent favoriteMovieIntent = new Intent(getApplicationContext(), FavoriteMovieActivity.class);
            startActivity(favoriteMovieIntent);

        } else if (id == R.id.nav_favorite_tv) {

            Intent favoriteTvIntent = new Intent(getApplicationContext(), FavoriteTvActivity.class);
            startActivity(favoriteTvIntent);
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
