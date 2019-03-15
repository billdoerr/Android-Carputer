===============================================================================================
# MyNotes
Just some internal notes around this project.
===============================================================================================

===============================================================================================
## Camera Module Issues from proof of concept project 'Android-CarputerPOC'.
1. **PERFORMANCE:**  Snapshot fragment slows everything down. Probably since it is capturing every frame, but there is no control over this.
<br/>**NOTE:**  Performance improved once enabling RaspberryPi as an access point.
	- Disabled displaying fps.  This improved performance a bit.
	- Snapshot view is the performance hog and do not have any control over this.

2. **PROBLEM:**  Layout with MJPEG widget.  Very frustrating getting other widgets to display correcting along with MJPEG widget.

3. **PROBLEM:**  Would like to figure a way to capture a frame with a click so I don't have to use the mode of capturing every frame. 
	- MJPEG uses SurfaceView and there isn't any way to capture a bitmap from this Widget.
	
4. **DESIGN:**  Implement DrawerView as home interface.  CarputerActivity will launch CameraActivity as default view.
	- Partially implemented.
	- (RESOLVED) PROBLEM:  How to have DrawView enabled for all activities/fragments?  
		- (SOLUTION): Implemented DrawView in SingleFragmentActivity and which other activities are subclassed.
	- (PARTIALLY RESOLVED) PROBLEM:  Difficulites getting ActionBar to display in SettingsActivity.
		- (SOLUTION): DrawView not possible in SettingsActivity.  Implemented toolbar which just supports onBackPressed(). 

5. **(RESOLVED)** **PROBLEM:**  Unable to use Android WebView to display MotionEye UI.  Current using Web Browser common content.
	- **(SOLUTION)**:  User error.

6. **PROBLEM:**  Unable to send Authentication Mode -> Basic in MotionEye but not able to pass url with credentials.  Still prompts for user/password.
Url should have this syntax:  http://username:password@host or http://username:password@IP:PORT
===============================================================================================

===============================================================================================
###  Save for later use
sudo date -s "Thu Jan  17 14:37:00 PST 2019"
"EEE MMM dd hh:mm:ss z yyyy"
shutdown -h 0
===============================================================================================

===============================================================================================
**OBD-II/CANBUS**
Python parser:  https://github.com/brendan-w/python-OBD
Python timer:  https://stackoverflow.com/questions/474528/what-is-the-best-way-to-repeatedly-execute-a-function-every-x-seconds-in-python
Android app:  https://github.com/fr3ts0n/AndrOBD
===============================================================================================

===============================================================================================
phpSysInfo 
http://192.168.4.1/phpsysinfo 
http://192.168.1.104/phpsysinfo/xml.php?plugin=complete&json

Linux perf commands
https://medium.com/@chrishantha/linux-performance-observability-tools-19ae2328f87f
https://opensource.com/article/17/7/20-sysadmin-commands
 	
===============================================================================================

===============================================================================================

editor.putString("jsondata", jobj.toString());
And to get it back:

String strJson = sharedPref.getString("jsondata","0");//second parameter is necessary ie.,Value to return if this preference does not exist. 

if (strJson != null) {
           try {
               JSONObject response = new JSONObject(strJson);

         } catch (JSONException e) {

         }
  }
===============================================================================================

===============================================================================================
startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
===============================================================================================

===============================================================================================
Preference Themes/Styles
https://chromium.googlesource.com/android_tools/+/2fd98442e4e0ced6bb30bd57abfd8a34f8142d09/sdk/extras/android/support/v14/preference/res/values/styles.xml
https://stackoverflow.com/questions/10228482/how-to-apply-theme-to-preferencescreen-elements-of-a-preferencecategory
 

tools:ignore="Autofill"
android:importantForAutofill="no"


2131427407

2131427385
2131427407

getResources().getResourceEntryName(2131427407);



        android:id="@android:id/list_container"
		
		
		
		        final LayoutInflater themedInflater = inflater.cloneInContext(getContext());

        final View view = themedInflater.inflate(mLayoutResId, container, false);

        final View rawListContainer = view.findViewById(AndroidResources.ANDROID_R_LIST_CONTAINER);
        if (!(rawListContainer instanceof ViewGroup)) {
            throw new RuntimeException("Content has view with id attribute "
                    + "'android.R.id.list_container' that is not a ViewGroup class");
					
					getActivity().setTheme(R.style.mystyle);
					
					
					
	    <style name="PreferenceCategoryTitleTextStyle">
        <item name="android:textAppearance">?attr/preferenceCategoryTitleTextAppearance</item>
        <item name="android:textColor">@color/preference_fallback_accent_color</item>	


        <TextView
            android:id="@android:id/title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textAlignment="viewStart"
            style="@style/PreferenceCategoryTitleTextStyle"/>


    <style name="Base.TextAppearance.AppCompat.Body2">
        <item name="android:textSize">@dimen/abc_text_size_body_2_material</item>
        <item name="android:textColor">?android:textColorPrimary</item>
    </style>

    <style name="PreferenceThemeOverlay">
        <item name="android:scrollbars">vertical</item>
        <item name="checkBoxPreferenceStyle">@style/Preference.CheckBoxPreference.Material</item>
        <item name="dialogPreferenceStyle">@style/Preference.DialogPreference.Material</item>
        <item name="dropdownPreferenceStyle">@style/Preference.DropDown.Material</item>
        <item name="editTextPreferenceStyle">@style/Preference.DialogPreference.EditTextPreference.Material</item>
        <item name="preferenceCategoryStyle">@style/Preference.Category.Material</item>
        <item name="preferenceFragmentCompatStyle">@style/PreferenceFragment.Material</item>
        <item name="preferenceFragmentListStyle">@style/PreferenceFragmentList.Material</item>
        <item name="preferenceFragmentStyle">@style/PreferenceFragment.Material</item>
        <item name="preferenceScreenStyle">@style/Preference.PreferenceScreen.Material</item>
        <item name="preferenceStyle">@style/Preference.Material</item>
        <item name="seekBarPreferenceStyle">@style/Preference.SeekBarPreference.Material</item>
        <item name="switchPreferenceCompatStyle">@style/Preference.SwitchPreferenceCompat.Material</item>
        <item name="switchPreferenceStyle">@style/Preference.SwitchPreference.Material</item>
        <item name="preferenceCategoryTitleTextAppearance">@style/TextAppearance.AppCompat.Body2</item>


EditText
TextView
Switch
Button


<item name="editTextPreferenceStyle">@style/Preference.DialogPreference.EditTextPreference.Material</item>
<item name="preferenceCategoryStyle">@style/Preference.Category.Material</item>
<item name="preferenceFragmentStyle">@style/PreferenceFragment.Material</item>
<item name="preferenceScreenStyle">@style/Preference.PreferenceScreen.Material</item>
<item name="preferenceStyle">@style/Preference.Material</item>
<item name="switchPreferenceCompatStyle">@style/Preference.SwitchPreferenceCompat.Material</item>
<item name="switchPreferenceStyle">@style/Preference.SwitchPreference.Material</item>
<item name="preferenceCategoryTitleTextAppearance">@style/TextAppearance.AppCompat.Body2</item>		
					

===============================================================================================

===============================================================================================
   /**
     * {@inheritDoc}
     */
	
	
	 
/**
 * The Dialog for the {@link TimePreference}.
 *
 * @author Jakob Ulbrich
 */



   /**
     * Creates a new Instance of the TimePreferenceDialogFragment and stores the key of the
     * related Preference
     *
     * @param key The key of the related Preference
     * @return A new Instance of the TimePreferenceDialogFragment
     */
	 
===============================================================================================
 
===============================================================================================
	
 sudo shutdown -h now
 
**List dhcp leases**
sudo cat /var/lib/misc/dnsmasq.leases 
 
# =======================================
sudo nano /etc/dhcpcd.conf

interface eth0
static ip_address=192.168.4.6/24
static routers=192.168.4.1
static domain_name_servers=192.168.4.1

interface wlan0
static ip_address=192.168.4.5/24
static routers=192.168.4.1
static domain_name_servers=192.168.4.1
===============================================================================================

===============================================================================================
//  https://gist.github.com/jaydeepw/4201419


// create a class member variable.
WifiManager.WifiLock mWifiLock = null;


/***
* Calling this method will aquire the lock on wifi. This is avoid wifi
* from going to sleep as long as <code>releaseWifiLock</code> method is called.
**/
private void holdWifiLock() {
	WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		
	if( mWifiLock == null )
		mWifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
		
	mWifiLock.setReferenceCounted(false);

	if( !mWifiLock.isHeld() )
		mWifiLock.acquire();
}
	
/***
* Calling this method will release if the lock is already help. After this method is called,
* the Wifi on the device can goto sleep.
**/
private void releaseWifiLock() {
		
	if( mWifiLock == null )
		Log.w(TAG, "#releaseWifiLock mWifiLock was not created previously");
		
	if( mWifiLock != null && mWifiLock.isHeld() ){
		mWifiLock.release();
		//mWifiLock = null;
	}
		
}
===============================================================================================

===============================================================================================

//  You can register a BroadcastReceiver to be notified when a WiFi connection is established (or if the connection changed).

//  Register the BroadcastReceiver:

IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
registerReceiver(broadcastReceiver, intentFilter);

//  And then in your BroadcastReceiver do something like this:

@Override
public void onReceive(Context context, Intent intent) {
    final String action = intent.getAction();
    if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
        if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
            //do stuff
        } else {
            // wifi connection was lost
        }
    }
}

// For more info, see the documentation for BroadcastReceiver and WifiManager

// Of course you should check whether the device is already connected to WiFi before this.

//  EDIT: Thanks to ban-geoengineering, here's a method to check whether the device is already connected:

private boolean isConnectedViaWifi() {
     ConnectivityManager connectivityManager = (ConnectivityManager) appObj.getSystemService(Context.CONNECTIVITY_SERVICE);
     NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);     
     return mWifi.isConnected();
}
===============================================================================================

===============================================================================================
https://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android


//  Write File:
private void writeToFile(String data,Context context) {
    try {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }
    catch (IOException e) {
        Log.e("Exception", "File write failed: " + e.toString());
    } 
}

//  Read File:
private String readFromFile(Context context) {

    String ret = "";

    try {
        InputStream inputStream = context.openFileInput("config.txt");

        if ( inputStream != null ) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        }
    }
    catch (FileNotFoundException e) {
        Log.e("login activity", "File not found: " + e.toString());
    } catch (IOException e) {
        Log.e("login activity", "Can not read file: " + e.toString());
    }

    return ret;
}

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================

===============================================================================================
















 
 