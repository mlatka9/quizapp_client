package com.example.ble.ui.btconnection;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.ble.R;
import com.example.ble.SendDataToESP;

import java.util.ArrayList;
import java.util.UUID;
import static android.content.ContentValues.TAG;
import static java.nio.charset.Charset.defaultCharset;

public class BTConnectionFragment extends Fragment implements AdapterView.OnItemClickListener {

    /**
     *Buttons and TextView
     */
    Button discoverabilityButton;
    Button buttonSend;
    Button startCon;
    Button buttonOff;
    Button buttonDiscoverDevices;
    TextView et;
    SendDataToESP test;


    /**
     * Adapters and components
     */

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
    BluetoothDevice mBTDevice;
    ListView lvNewDevices;
    public ArrayList<BluetoothDevice> mBTDevices;
    public DeviceListAdapter mDeviceListAdapter;
    public BluetoothConnectionService mBluetoothConnection;

    /**
     * UUID for esp32
     */
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        lvNewDevices = root.findViewById(R.id.lvNewDevices);
        buttonSend = root.findViewById(R.id.sendButton);


        lvNewDevices.setOnItemClickListener(BTConnectionFragment.this);

        /**
         * receiver for Bonding
         */
        getActivity().registerReceiver(fourthReceiver, filter);
        unpairedDevices();

        /**
         * List of paired devices
         */
        mBTDevices = new ArrayList<>();


        /**
         * Sending data via socket
         */
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = "0".getBytes(defaultCharset());
                    bytes = SendDataToESP.SELECTED_ACTION.getBytes(defaultCharset());

                 Log.d(TAG, SendDataToESP.SELECTED_ACTION);
                 mBluetoothConnection.write(bytes);
            }
        });
        return root;
    }

    private void  startConnection() {
        startBTConnnection(mBTDevice, MY_UUID_INSECURE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void unpairedDevices() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices");
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling");
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(thirdReceiver, discoverDevicesIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            Log.d(TAG, "btnDiscover: Looking for unpaired devices v2");

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(thirdReceiver, discoverDevicesIntent);
        }
    }


    public void startBTConnnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "initializing");

        mBluetoothConnection.startClient(device, uuid);
    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = getActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += getActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                Log.d(TAG, "checkButton =< LOLLIPOP.");
                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version =< LOLLIPOP.");
        }
    }





    private void enableDisableBT() {
        if(mBluetoothAdapter == null){
            Log.d(TAG,"Does not have BT capabilities!");
        }
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(receiver, BTIntent);

        }
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            getActivity().registerReceiver(receiver, BTIntent);

        }


    }

    /**
     * Receiver for turn on/off BT
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "OnReceive: State OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "OnReceive: State Turning OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "OnReceive: State ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "OnReceive: State Turning ON");
                        break;
                }

            }
        }
    };

    /**
     * Receiver for scanning devices in the area
     */
    private final BroadcastReceiver secondReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                 int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "OnReceive: State OFF");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "OnReceive: State Turning OFF");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "OnReceive: State ON");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "OnReceive: State Turning ON");
                        break;
                }

            }

        }
    };


    /**
     * Adding paired devices to list
     */
        private BroadcastReceiver thirdReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                Log.d(TAG, "OnReceive: ACTION FOUND.");

                if (action.equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!mBTDevices.contains(device)) {
                        mBTDevices.add(device);
                    }
                    Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                    mDeviceListAdapter = new DeviceListAdapter(getContext(), R.layout.device_adapter_view, mBTDevices);

                    lvNewDevices.setAdapter(mDeviceListAdapter);
                }
            }
        };

    /**
     * Receiver for setting actuall connected devices
     */
    private BroadcastReceiver fourthReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "Bonded");
                    mBTDevice = device;
                }
                if(device.getBondState() == BluetoothDevice.BOND_BONDING){

                }
                if(device.getBondState() == BluetoothDevice.BOND_NONE){

                }


            }
        }
    };



    /**
     * Włączenie widoczności urządzenia na 30s
     */
    public void enableDiscoverability(){
        Intent discoverabilityIntent  = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverabilityIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverabilityIntent);

        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        getActivity().registerReceiver(secondReceiver, intentFilter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
       // getActivity().unregisterReceiver(receiver);
//        getActivity().unregisterReceiver(secondReceiver);
        getActivity().unregisterReceiver(thirdReceiver);
        getActivity().unregisterReceiver(fourthReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.cancelDiscovery();
        Log.d(TAG, "FDS" + position);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            mBTDevices.get(position).createBond();
            mBTDevice = mBTDevices.get(position);
            Log.d(TAG, String.valueOf(position));


            mBluetoothConnection = new BluetoothConnectionService(getContext());
            buttonSend.setVisibility(View.VISIBLE);
            startConnection();
        }


    }
}