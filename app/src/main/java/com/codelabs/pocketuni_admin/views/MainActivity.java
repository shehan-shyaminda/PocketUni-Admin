package com.codelabs.pocketuni_admin.views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codelabs.pocketuni_admin.R;
import com.codelabs.pocketuni_admin.services.SharedPreferencesManager;
import com.codelabs.pocketuni_admin.ui.AnnouncementsFragment;
import com.codelabs.pocketuni_admin.ui.HomeFragment;
import com.codelabs.pocketuni_admin.ui.NoticesFragment;
import com.codelabs.pocketuni_admin.ui.ProfileFragment;
import com.codelabs.pocketuni_admin.utils.CustomAlertDialog;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.material.navigation.NavigationView;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView nvDrawer;
    private AnimatedBottomBar bottomBar;
    private ImageView icMenu, btnCalender;
    private TextView fragmentTitle;
    private SharedPreferencesManager sharedPreferencesManager;
    private CustomAlertDialog customAlertDialog;
    private ConstraintLayout appbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = findViewById(R.id.bottomMenu);
        fragmentTitle = findViewById(R.id.fragmentTitle);
        icMenu = findViewById(R.id.ic_menu);
        appbarLayout = findViewById(R.id.layout_appbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
        bottomBar.selectTabById(R.id.home,true);
        fragmentTitle.setText("My Events");
        sharedPreferencesManager = new SharedPreferencesManager(MainActivity.this);
        customAlertDialog = new CustomAlertDialog();

        SideDrawerInitializing();

        icMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDrawerPosition();
            }
        });

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                Fragment fragment = null;
                String title = "";
                int color = getResources().getColor(R.color.white);
                switch (tab1.getId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        title = "My Events";
                        color = getResources().getColor(R.color.white);
                        break;
                    case R.id.notices:
                        fragment = new NoticesFragment();
                        title = "Batch Notices";
                        color = getResources().getColor(R.color.white);
                        break;
                    case R.id.announcements:
                        fragment = new AnnouncementsFragment();
                        title = "Public Notices";
                        color = getResources().getColor(R.color.white);
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        title = "My Profile";
                        color = getResources().getColor(R.color.secondary_blue);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                fragmentTitle.setText(title);
                appbarLayout.setBackgroundColor(color);
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    //-----------side drawer implementation---------------
    private void SideDrawerInitializing() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        mToggle.syncState();
        setupDrawerContent(nvDrawer);
        View hView = nvDrawer.getHeaderView(0);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.studReset:
                        startActivity(new Intent(MainActivity.this, ResetStudentActivity.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Log.e(TAG, "onNavigationItemSelected: " + item.getTitle());
                        break;
                    case R.id.adminReset:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        if (sharedPreferencesManager.getBooleanPreferences(SharedPreferencesManager.PREF_IS_SUPER_ADMIN)){
                            startActivity(new Intent(MainActivity.this, ResetAdminActivity.class));
                        }else{
                            customAlertDialog.negativeDismissAlert(MainActivity.this, "Oops!", "Sign-in with super admin account to access this feature!",  CFAlertDialog.CFAlertStyle.NOTIFICATION);
                        }
                        break;
                    case R.id.aboutUs:
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.helpSupport:
                        startActivity(new Intent(MainActivity.this, HelpSupportActivity.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.newAdmin:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        if (sharedPreferencesManager.getBooleanPreferences(SharedPreferencesManager.PREF_IS_SUPER_ADMIN)){
                            startActivity(new Intent(MainActivity.this, AddAdminActivity.class));
                        }else{
                            customAlertDialog.negativeDismissAlert(MainActivity.this, "Oops!", "Sign-in with super admin account to access this feature!",  CFAlertDialog.CFAlertStyle.NOTIFICATION);
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void setDrawerPosition() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.setVisibility(View.VISIBLE);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    public void sideDrawerOpen(View v) {
        setDrawerPosition();
    }
}