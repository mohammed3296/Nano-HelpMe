package com.example.mohammedabdullah3296.helpme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mohammedabdullah3296.helpme.adapters.CategoryAdapter;
import com.example.mohammedabdullah3296.helpme.utils.Sesstion;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth auth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if (!Sesstion.getInstance(this).isLoggedIn() && auth.getCurrentUser() == null) {
            logoutUser();
            return;
        }
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        de.hdodenhof.circleimageview.CircleImageView drawerImage = (de.hdodenhof.circleimageview.CircleImageView) headerView.findViewById(R.id.draw_imageView);
        TextView drawerUsername = (TextView) headerView.findViewById(R.id.draw_name);
        drawerUsername.setText(Sesstion.getInstance(this).getUser().getFirstName() + " " + Sesstion.getInstance(this).getUser().getSecondName());
        TextView drawerAccount = (TextView) headerView.findViewById(R.id.draw_email);
        drawerAccount.setText(Sesstion.getInstance(this).getUser().getEmail());
        if (Sesstion.getInstance(this).getUser().getProfileImage() != "") {
            Uri builtUri = Uri.parse(Sesstion.getInstance(this).getUser().getProfileImage()).buildUpon().build();
            Picasso.with(this).load(builtUri).placeholder(R.drawable.profile).into(drawerImage);
        }
        drawerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void logoutUser() {
        Intent startPageIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPageIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homepage, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.general) {
            Bundle bundle = new Bundle();
            bundle.putString("newsURL1", getString(R.string.general_url1));
            bundle.putString("newsURL2", getString(R.string.general_url2));
            bundle.putString("type", getString(R.string.sdhfjsla546));
            Intent newsActivity = new Intent(this, NewsActivity.class);
            newsActivity.putExtras(bundle);
            startActivity(newsActivity);
        } else if (id == R.id.business) {
            Bundle bundle = new Bundle();
            bundle.putString("newsURL1", getString(R.string.business_url1));
            bundle.putString("newsURL2", getString(R.string.business_url2));
            bundle.putString("type", getString(R.string.isuefkdjs65));
            Intent newsActivity = new Intent(this, NewsActivity.class);
            newsActivity.putExtras(bundle);
            startActivity(newsActivity);
        } else if (id == R.id.technology) {
            Bundle bundle = new Bundle();
            bundle.putString("newsURL1", getString(R.string.technology_url1));
            bundle.putString("newsURL2", getString(R.string.technology_url2));
            bundle.putString("type", getString(R.string.jxbdfk878));
            Intent newsActivity = new Intent(this, NewsActivity.class);
            newsActivity.putExtras(bundle);
            startActivity(newsActivity);
        } else if (id == R.id.science) {
            Bundle bundle = new Bundle();
            bundle.putString("newsURL1", getString(R.string.science_url1));
            bundle.putString("newsURL2", getString(R.string.science_url2));
            bundle.putString("type", getString(R.string.khxbdfvxm687));
            Intent newsActivity = new Intent(this, NewsActivity.class);
            newsActivity.putExtras(bundle);
            startActivity(newsActivity);
        } else if (id == R.id.sports) {
            Bundle bundle = new Bundle();
            bundle.putString("newsURL1", getString(R.string.sports_url1));
            bundle.putString("newsURL2", getString(R.string.sports_url2));
            bundle.putString("type", getString(R.string.jkxbvjkn65));
            Intent newsActivity = new Intent(this, NewsActivity.class);
            newsActivity.putExtras(bundle);
            startActivity(newsActivity);
        } else if (id == R.id.money) {
            Bundle bundle = new Bundle();
            bundle.putString("newsURL1", getString(R.string.money_url1));
            bundle.putString("newsURL2", getString(R.string.money_url2));
            bundle.putString("type", getString(R.string.kjxdhfk3548));
            Intent newsActivity = new Intent(this, NewsActivity.class);
            newsActivity.putExtras(bundle);
            startActivity(newsActivity);
        } else if (id == R.id.travel) {
            Bundle bundle = new Bundle();
            bundle.putString("newsURL1", getString(R.string.travel_url1));
            bundle.putString("newsURL2", getString(R.string.travel_url2));
            bundle.putString("type", getString(R.string.skjfhdkjs878));
            Intent newsActivity = new Intent(this, NewsActivity.class);
            newsActivity.putExtras(bundle);
            startActivity(newsActivity);
        } else if (id == R.id.nav_manage) {
            auth.signOut();
            Sesstion.getInstance(this).logout();
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_link_message));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.exit) {
            finish();
            System.exit(0);
        } else if (id == R.id.nav_feedback) {


            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.myEmailForFeedback)}); // recipients
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.kjcfhxgvkjdfx65));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.kjxhjv54));
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(getString(R.string.dfkjgnkdf54)));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
