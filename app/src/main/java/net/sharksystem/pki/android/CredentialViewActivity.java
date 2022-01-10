package net.sharksystem.pki.android;

import android.os.Bundle;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetActivity;

public class CredentialViewActivity extends SharkNetActivity {
    public static final String CREDENTIAL_VIEW_BEHAVIOUR_KEY =
        "CREDENTIAL_VIEW_BEHAVIOUR_KEY";

    public static final String CREDENTIAL_VIEW_BEHAVIOUR_VIEW_ONLY
            = "CREDENTIAL_VIEW_BEHAVIOUR_VIEW_ONLY";

    public static final String CREDENTIAL_VIEW_BEHAVIOUR_ADD_AND_CREATE_CERTIFICATE
            = "CREDENTIAL_VIEW_BEHAVIOUR_ADD_AND_CREATE_CERTIFICATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.credential_view_only_layout);
    }
}
