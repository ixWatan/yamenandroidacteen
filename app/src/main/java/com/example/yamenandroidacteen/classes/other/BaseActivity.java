package com.example.yamenandroidacteen.classes.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerNetworkChangeReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChangeReceiver();
    }

    // Method to register the BroadcastReceiver
    private void registerNetworkChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeReceiver, filter);
    }

    // Method to unregister the BroadcastReceiver
    private void unregisterNetworkChangeReceiver() {
        unregisterReceiver(networkChangeReceiver);
    }

    // Inner class to handle network change events
    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isNetworkConnected(context)) {
                            NoConnectionDialog.create(context, "No Internet Connection").show();
                             Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                // You can start the NoConnectionActivity or show a fragment here
                // startActivity(new Intent(context, NoConnectionActivity.class));
                // Or replace the current fragment with NoConnectionFragment
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NoConnectionFragment()).commit();
            }
        }
    }
}
