package com.gghouse.wardah.wardahba.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.gghouse.wardah.wardahba.R;
import com.gghouse.wardah.wardahba.common.WBAIntent;
import com.gghouse.wardah.wardahba.enumeration.ViewMode;

public class PelangganInputActivity extends WardahInputActivity {

    private EditText metName;
    private EditText metPhoneNumber;
    private EditText metEmail;
    private EditText metAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan_input);

        metName = (EditText) findViewById(R.id.etName);
        metPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        metEmail = (EditText) findViewById(R.id.etEmail);
        metAddress = (EditText) findViewById(R.id.etAddress);

        Intent prevIntent = getIntent();
        ViewMode viewMode = (ViewMode) prevIntent.getSerializableExtra(WBAIntent.PELANGGAN);

        switch (viewMode) {
            case VIEW:
                metName.setEnabled(false);
                metPhoneNumber.setEnabled(false);
                metEmail.setEnabled(false);
                metAddress.setEnabled(false);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pelanggan_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                attemptDone();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void validate() {
        // Reset errors
        metName.setError(null);
        metPhoneNumber.setError(null);
        metEmail.setError(null);
        metAddress.setError(null);

        String name = metName.getText().toString();
        String phoneNumber = metPhoneNumber.getText().toString();
        String email = metEmail.getText().toString();
        String address = metAddress.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            metName.setError(getString(R.string.error_field_required));
            focusView = metName;
            cancel = true;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            metPhoneNumber.setError(getString(R.string.error_field_required));
            focusView = metPhoneNumber;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            metEmail.setError(getString(R.string.error_field_required));
            focusView = metEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            metAddress.setError(getString(R.string.error_field_required));
            focusView = metAddress;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            attemptDone();
        }
    }

    @Override
    protected void attemptDone() {

    }
}
