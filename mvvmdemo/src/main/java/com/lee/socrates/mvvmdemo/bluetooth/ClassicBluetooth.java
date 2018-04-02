package com.lee.socrates.mvvmdemo.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by lee on 2018/3/28.
 */

public class ClassicBluetooth extends Activity {

    private static final int ENABLE_BT_REQUEST_CODE = 1;

    private void use() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //Display a toast notifying the user that their device doesn’t support Bluetooth//
            Toast.makeText(getApplicationContext(), "This device doesn’t support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        //判断蓝牙是否可用
        if (!bluetoothAdapter.isEnabled()) {
            //Create an intent with the ACTION_REQUEST_ENABLE action, which we’ll use to display our system Activity//
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //Pass this intent to startActivityForResult(). ENABLE_BT_REQUEST_CODE is a locally defined integer that must be greater than 0,
            //for example private static final int ENABLE_BT_REQUEST_CODE = 1//
            startActivityForResult(enableIntent, ENABLE_BT_REQUEST_CODE);
            Toast.makeText(getApplicationContext(), "Enabling Bluetooth!", Toast.LENGTH_LONG).show();
        }

        //获取已经保存的配对过的设备
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there’s 1 or more paired devices...//
        if (pairedDevices.size() > 0) {
            //...then loop through these devices//
            for (BluetoothDevice device : pairedDevices) {
                //Retrieve each device’s public identifier and MAC address. Add each device’s name and address to an ArrayAdapter, ready to incorporate into a
                //ListView
            }
        }

        //发现新蓝牙设备
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //Specify how long the device will be discoverable for, in seconds.//
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 400);
        startActivity(discoveryIntent);

        if (bluetoothAdapter.startDiscovery()) {
            //If discovery has started, then display the following toast....//
            Toast.makeText(getApplicationContext(), "Discovering other bluetooth devices...",
                    Toast.LENGTH_SHORT).show();
        } else {
            //If discovery hasn’t started, then display this alternative toast//
            Toast.makeText(getApplicationContext(), "Something went wrong! Discovery has failed to start.",
                    Toast.LENGTH_SHORT).show();
        }


        //Register for the ACTION_FOUND broadcast//
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);

        try {
            BluetoothServerSocket bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("lee", UUID.randomUUID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Create a BroadcastReceiver for ACTION_FOUND//
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //Whenever a remote Bluetooth device is found...//
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                //….retrieve the BluetoothDevice object and its EXTRA_DEVICE field, which contains information about the device’s characteristics and capabilities//
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //You’ll usually want to display information about any devices you discover, so here I’m adding each device’s name and address to an ArrayAdapter,
                //which I’d eventually incorporate into a ListView//
                Log.e("Classic", device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Cut down on unnecessary system overhead, by unregistering the ACTION_FOUND receiver//
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check what request we’re responding to//
        if (requestCode == ENABLE_BT_REQUEST_CODE) {
            //If the request was successful…//
            if (resultCode == Activity.RESULT_OK) {
                //...then display the following toast.//
                Toast.makeText(getApplicationContext(), "Bluetooth has been enabled",
                        Toast.LENGTH_SHORT).show();
            }

            //If the request was unsuccessful...//
            if (resultCode == RESULT_CANCELED) {
                //...then display this alternative toast.//
                Toast.makeText(getApplicationContext(), "An error occurred while attempting to enable Bluetooth",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
