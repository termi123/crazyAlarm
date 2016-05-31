package dungpt.crazyalarm.vietitpro.crazyalarm.utils.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by te on 5/30/2016.
 */
public class BaseDialog extends DialogFragment {

    private static final String TAG = DialogFragment.class.getSimpleName();

    protected String mTag;

    private boolean mDismiss;

    @Override
    public void onResume() {
        try {
            if (mDismiss) {
                if (TextUtils.isEmpty(mTag)) {
                    Log.w(TAG, "mTag empty");
                    return;
                }
                Fragment fragment = getFragmentManager().findFragmentByTag(mTag);
                Log.i(TAG, "fragment " + fragment);
                Log.i(TAG, "mTag " + mTag);
                if (fragment instanceof DialogFragment) {
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                    mDismiss = false;
                }
            }
        } finally {
            super.onResume();
        }
    }

    @Override
    public final void show(FragmentManager manager, String tag) {
        mTag = tag;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, mTag);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void dismiss() {
        if (isResumed()) {
            Log.i(TAG, "dismiss");
            super.dismiss();
        } else {
            Log.i(TAG, "not dismiss isResumed() " + isResumed());
            mDismiss = true;
        }
    }
}
