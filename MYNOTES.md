# MyNotes
Just some internal notes around this project.


## Camera Module Issues from proof of concept project 'Android-CarputerPOC'.
1. **PERFORMANCE:**  Snapshot fragment slows everything down. Probably since it is capturing every frame, but there is no control over this.
<br/>**NOTE:**  Performance improved once enabling RaspberryPi as an access point.
	- Disabled displaying fps.  This improved performance a bit.
	- Snapshot view is the performance hog and do not have any control over this.

2. **PROBLEM:**  Layout with MJPEG widget.  Very frustrating getting other widgets to display correcting along with MJPEG widget.

3. **PROBLEM:**  Would like to figure a way to capture a frame with a click so I don't have to use the mode of capturing every frame. 
	- MJPEG uses SurfaceView and there isn't any way to capture a bitmap from this Widget.
	
4. **DESIGN:**  Implement DrawerView as home interface.  CarputerActivity will launch CameraActivity as default view.
	- Partially implmented.
	- (RESOLVED) PROBLEM:  How to have DrawView enabled for all activities/fragments?  
		- (SOLUTION): Implemented DrawView in SingleFragmentActivity and which other activities are subclassed.
	- (PARTIALLY RESOLVED) PROBLEM:  Difficulites getting ActionBar to display in SettingsActivity.
		- (SOLUTION): DrawView not posible in SettingsActivity.  Implemented toolbar which just supports onBackPressed(). 

5. **(RESOLVED)** **PROBLEM:**  Unable to use Android WebView to display MotionEye UI.  Current using Web Browser common content.
	- **(SOLUTION)**:  User error.

6. **PROBLEM:**  Unable to send Authentication Mode -> Basic in MotionEye but not able to pass url with credentials.  Still prompts for user/password.
Url should have this syntax:  http://username:password@host or http://username:password@IP:PORT

Preference Themes/Styles
https://chromium.googlesource.com/android_tools/+/2fd98442e4e0ced6bb30bd57abfd8a34f8142d09/sdk/extras/android/support/v14/preference/res/values/styles.xml
https://stackoverflow.com/questions/10228482/how-to-apply-theme-to-preferencescreen-elements-of-a-preferencecategory
 
###  Save for later use
sudo date -s "Thu Jan  17 14:37:00 PST 2019"
"EEE MMM dd hh:mm:ss z yyyy"
shutdown -h 0


**OBD-II/CANBUS**
Python parser:  https://github.com/brendan-w/python-OBD
Python timer:  https://stackoverflow.com/questions/474528/what-is-the-best-way-to-repeatedly-execute-a-function-every-x-seconds-in-python
Android app:  https://github.com/fr3ts0n/AndrOBD

2018-11-13-raspbian-stretch-motioneye.img

//  Android Web Browser common intent
Intent i = new Intent(Intent.ACTION_VIEW, uri);
startActivity(i); 


    <View android:id="@+id/divider"
        style="@style/Divider" />

https://medium.com/exploring-android/exploring-the-new-android-constraintlayout-eed37fe8d8f1

phpSysInfo 
http://192.168.4.1/phpsysinfo 
http://192.168.1.104/phpsysinfo/xml.php?plugin=complete&json

Linux perf commands
https://medium.com/@chrishantha/linux-performance-observability-tools-19ae2328f87f
https://opensource.com/article/17/7/20-sysadmin-commands
 
	

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
startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // Code goes here            
        return true;
    }
};

EditTextPreference pref = (EditTextPreference)findPreference(getString(R.string.pref1));
pref1.setOnPreferenceChangeListener(changeListener);

EditTextPreference pref2 = (EditTextPreference)findPreference(getString(R.string.pref2));
pref2.setOnPreferenceChangeListener(changeListener);


  
     <Preference
        app:title="@string/pref_category_cameras" >
    </Preference>

    <Preference
        app:title="@string/pref_title_add_camera"
        app:icon="@drawable/ic_baseline_add_24px" >
    </Preference>

Preference userButton = (Preference) findPreference("user");
userButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
    @Override
    public boolean onPreferenceClick(Preference arg0) {
        Intent intent = new Intent(getActivity(), YourTargetActivity.class);
        intent.putExtra(EXTRA, mUser);
        startActivity(intent);
        return true;
    }
});	


RecyclerView listView = getListView();
listView.getAdapter().notifyDataSetChanged();

public class CustomPreference extends DialogPreference {

    EditText first;
    EditText second;
    EditText third;

    protected View onCreateDialogView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.three_edit_texts, null);
        first = v.findViewById(...);
        second = v.findViewById(...);
        third = v.findViewById(...);
        return v;
    }

}

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
 
 