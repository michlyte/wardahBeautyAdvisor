package com.gghouse.wardah.wardahba.screen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gghouse.wardah.wardahba.R;

public class WardahInputActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                attemptCancel();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void attemptCancel() {
        new MaterialDialog.Builder(this)
                .title(R.string.prompt_konfirmasi)
                .content(R.string.message_common_cancel)
                .positiveText(R.string.action_iya)
                .positiveColorRes(R.color.colorPrimary)
                .negativeText(R.string.action_tidak)
                .negativeColorRes(R.color.colorAccent)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

    protected void validate() {

    }

    protected void attemptDone() {

    }
}
