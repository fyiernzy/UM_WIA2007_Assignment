package com.example.betterher;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.betterher.SafetyHome.SafetyHomeFragment;
import com.example.betterher.GetSupport.GetSupportIntroFragment;
import com.example.betterher.ReportCases.ReportIntroFragment;
import com.example.betterher.TrackCases.TrackCasesFragment;
import com.example.betterher.UrgentHelp.UrgentHelpFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new SafetyHomeFragment());

        //Firebase
        FirebaseApp.initializeApp(this);


        //tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navigation drawer menu
        drawerLayout = findViewById(R.id.dl_safety);
        navigationView = findViewById(R.id.side_nav);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.dest_urgent_help) {
                    replaceFragment(new UrgentHelpFragment());
                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.dest_report) {
                    replaceFragment(new ReportIntroFragment());
                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.dest_track_cases) {
                    replaceFragment(new TrackCasesFragment());
                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.dest_get_support) {
                    replaceFragment(new GetSupportIntroFragment());
                    drawerLayout.closeDrawers();
                }
                return false;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, fragment, null)
                .addToBackStack("name")
                .commit();
    }

}