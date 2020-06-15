package net.sharksystem.eID;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.util.Log;

class ForegroundDispatcher {
    private final Activity mActivity;
    private final NfcAdapter mAdapter;
    private final PendingIntent mPendingIntent;
    private final IntentFilter[] mFilters;
    private final String[][] mTechLists;

    ForegroundDispatcher(Activity pActivity) {
        mActivity = pActivity;
        mAdapter = NfcAdapter.getDefaultAdapter(mActivity);
        Intent intent = new Intent(mActivity, mActivity.getClass()).
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(mActivity, 0, intent, 0);

        mFilters = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        };
        mTechLists = new String[][]{new String[]{
                IsoDep.class.getName()
        }};
    }

    void enable() {
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(mActivity,
                    mPendingIntent,
                    mFilters,
                    mTechLists);
            Log.i("nfc", "enabled");
        }
    }

    void disable() {
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(mActivity);
            Log.i("nfc", "disabled");
        }
    }
}