package net.sharksystem.ui;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.databinding.ActivityMainBinding;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

/**
 * Main Activity of the Shark messenger application
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Object for app bar configuration
     */
    private AppBarConfiguration appBarConfiguration;

    /**
     * Binding to access elements from the layout
     */
    private ActivityMainBinding binding;

    private MainAppViewModel viewModel;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //use bindings for easier use of view elements
        //  see: https://developer.android.com/topic/libraries/view-binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        this.setContentView(binding.getRoot());

        //set action bar at the top of the screen
        this.setSupportActionBar(binding.appBarMain.toolbar);

        //find the drawer layout and the navigation view (equivalent to findViewById call)
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_channels,
                R.id.nav_settings,
                R.id.nav_contacts,
                R.id.nav_radar,
                R.id.nav_network)
                .setOpenableLayout(drawer)
                .build();


        //Setting up the Navigation Host Fragment which is the part of the layout which changes
        //  through navigation. In this app, it is the main part of the visible area, below the
        //  app bar.
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_content_main);

        //The Navigation Controller manages the navigation. Mainly used to navigate to a new
        //  destination (= display new fragment based on user input).
        this.navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, this.appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        this.viewModel = new ViewModelProvider(this).get(MainAppViewModel.class);

        this.viewModel.getName().observe(this, name -> {
            //initialize SharkNetApp
            try {
                SharkNetApp.initializeSharkNetApp(this, name);
            } catch (SharkException | IOException e) {
                Log.d(this.getClass().getSimpleName(), e.getLocalizedMessage());
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //check if this start is the first one ever on this device
        try {
            //if the owner id is already present, the user already entered it, so this can't be
            //  the first start
            //if this call throws an exception, then this is in fact the first start, so in the
            //  catch block this case is treated. To be concise: this could actually be the second
            //  or further time this app was started, as the user may cancelled the app before
            //  setting his name, so the owner id is still not set. When the owner id was confirmed,
            //  any further starts aren't counting as initial first start ever again.
            String name = SharkNetApp.getOwnerID(this);
            this.viewModel.setName(name);
        } catch (SharkException e) {
            //as this is the first start, the owner id was never set before. This needs to be done
            //  now. It's done within this activity by displaying the therefor intended layout
            //navController.popBackStack();
            this.navController.navigate(R.id.nav_firstStart);
        }
    }

    /**
     * Navigating up means pressing the back button. In this case the home screen is displayed.
     * @return True if navigating up was possible.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, this.appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}