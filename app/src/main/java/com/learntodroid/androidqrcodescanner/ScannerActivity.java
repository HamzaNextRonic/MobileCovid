package com.learntodroid.androidqrcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ScannerActivity extends AppCompatActivity {

    public final static  int REQUEST_ACCESS_COARSE_LOCATION = 1;
    public final static  int REQUEST_ENABLE_BLUETOOTH = 11;
    private ListView deviceList;

    private ArrayAdapter<String> listArrayAdapter;
    private Button startScan;
    final BluetoothManager bluetoothManager =  (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    private BluetoothAdapter bluetoothAdapter ;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean mScanning;
    private Handler handler;
   // private LeDeviceListAdapter leDeviceListAdapter;
    private BluetoothAdapter.LeScanCallback leScanCallback;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported", Toast.LENGTH_SHORT).show();
            finish();

        }

        bluetoothLeScanner =
                BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        handler = new Handler();


        //get bluetooth adapter
        //bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter = bluetoothManager.getAdapter();
        deviceList = findViewById(R.id.deviceList);
        startScan = findViewById(R.id.startScan);

        // create a simple array to display devices detected
        listArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        deviceList.setAdapter(listArrayAdapter);

        //check the Bluetooth state
        checkBluetoothState();


        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter != null && bluetoothAdapter.isEnabled() ){
                    //check if the coarse location must be asked
                    if(checkCoarseLocationPermission()){
                        listArrayAdapter.clear();
                        bluetoothAdapter.startDiscovery();
                    }else{
                        checkBluetoothState();
                    }
                }
            }
        });

        // check permission of start the app
        checkCoarseLocationPermission();

    }
    private void scanLeDevice() {
        if (!mScanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan((ScanCallback) leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothLeScanner.startScan((ScanCallback) leScanCallback);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan((ScanCallback) leScanCallback);
        }
    }
   /* public  class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return null;
        }
    }*/

    boolean isBleSupported(Context context){
        return BluetoothAdapter.getDefaultAdapter()!= null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //register a dedicated receiver for some bluetooth actions
        registerReceiver(deviceFoundReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(deviceFoundReceiver,new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(deviceFoundReceiver,new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(deviceFoundReceiver);
    }

    private boolean checkCoarseLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
            return false;
        }else {
            return  true;
        }
    }

    private void checkBluetoothState(){
        if(bluetoothAdapter == null){
            Toast.makeText(this,"BLUETOOTH is not supported in your device",Toast.LENGTH_SHORT).show();
        }else {
            if(bluetoothAdapter.isEnabled()){
                if(bluetoothAdapter.isDiscovering()){
                    Toast.makeText(this,"Device discovering progress ...",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this,"Bluetooth is enabled", Toast.LENGTH_SHORT).show();
                        startScan.setEnabled(true);

                }

            }else {
                Toast.makeText(this,"you need to enable bluetooth",Toast.LENGTH_SHORT).show();
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            checkBluetoothState();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_ACCESS_COARSE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    Toast.makeText(this,"Access coarse location allowed. You can scan Bluetooth devices",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"Access coarse location forbidden. You can't scan Bluetooth devices",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //implement our received to get devices detected
    private final BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listArrayAdapter.add("Name : "+device.getName()+"\n"+"Address : "+device.getAddress()+"\n"+"UUID"+
                        device.getUuids()+"\n"+"type : "+device.getType());
                listArrayAdapter.notifyDataSetChanged();
            }else {
                if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    startScan.setText("Scanning Bluetooth Devices");
                }else {
                    if( BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                        startScan.setText("Scanning in progress ...");

                    }
                }
            }
        }
    };
    //
    public static void toast(Context context, String string) {

        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
