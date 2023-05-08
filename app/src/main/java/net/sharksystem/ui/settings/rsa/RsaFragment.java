package net.sharksystem.ui.settings.rsa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.utils.DateTimeHelper;
import net.sharksystem.databinding.FragmentRsaBinding;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This fragment allows you to generate a new RSA key and see when your key was last generated.
 */
public class RsaFragment extends Fragment {

    /**
     * Binding for easy access to layout elements
     */
    private FragmentRsaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentRsaBinding.inflate(this.getLayoutInflater());

        //Display key creation time
        try {
            String creationTime = DateTimeHelper.long2DateString(SharkNetApp.getSharkNetApp().
                    getSharkPKI().getKeysCreationTime());

            this.binding.fragmentRsaDate.setText(creationTime);

        } catch (ASAPSecurityException e) {
            Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage());
        }

         //Generate new key pair in separate thread
        this.binding.fragmentRsaGenerateButton.setOnClickListener(view -> {
            //Using Java ScheduledExecutor. Thread would be possible as well
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                try {
                    SharkNetApp.getSharkNetApp().getSharkPKI().generateKeyPair();

                    String creationTime = DateTimeHelper.long2DateString(SharkNetApp.
                            getSharkNetApp().getSharkPKI().getKeysCreationTime());

                    this.binding.fragmentRsaDate.setText(creationTime);

                } catch (ASAPSecurityException e) {
                    Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage());
                }
            }, 0, TimeUnit.MILLISECONDS);

            Navigation.findNavController(view).navigate(R.id.action_nav_rsa_to_nav_settings);
        });


        return this.binding.getRoot();
    }
}