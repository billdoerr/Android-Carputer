# Android-Carputer project

## Carputer project

- **Will be based on a RaspberryPi 3 Model B running motioneye.**  <br />
- **Android tablet will be used for the user interface.**  <br />
- **Primary goal is to have a front and rear car cameras.  RaspberryPi will archive video to USBStick and Android table will host the UI and have the ability to capture image snapshots.**  <br />
-

## Goals of POC
1. **PRIMARY GOAL** -> Display streaming video from RaspberryPI running motioneye.
2. **SECONDARY GOAL** -> Display of OBD-II/CANBUS data.  RaspberryPI will connect to OBD-II/CANBUS.
3. **STRETCH GOAL** -> Play music.
4. **STRETCH GOAL** -> Location services.  Maps.
5. **STRETCH GOAL** -> Sync data.  Upload data to cloud.

##  Tasks - Android

### v1.0 (RELEASED)
- [x]  Migrate code from Android-CarputerPOC (the proof of concept project).
- [x]  Design change from CarputerPOC
	- [x]  Use MotionEye to display streaming video.  Mjpeg has poor performance when implementing the snapshot feature.  Able to implment on-click event to capture image of screen.
			<br/>**NOTE:**  Performance improved once enabling RaspberryPi as an access point.
		- [x]  Create activity/fragment to host tab layout to host MotionEye.  Currently only one tab is needed.
		- [x]  ~~Use TabLayout to also host WebChromeClient for viewing MotionEye admin console.~~
	- [x]  Create new activity and menu item for viewing snapshots rather than as a tab in the Camera view.  Use TabLayout for future expansion.
	- [x]  Draw menu:  Camera - mjpeg, Camera MotionEye, Image Archive, Settings.
	- [x]  Disable settings for features: flip image, rotate image and possibly authentication settings.
	- [ ]
- [x]  Improve image capture.  Currently senses FINGER_ACTION_UP so sensitive and creates unnecessary snapshots.	
- [x]  **(NEED SD CARDS)**  Create image of sd card.  Then use three cars:  prod/dev/release.  (https://lifehacker.com/how-to-clone-your-raspberry-pi-sd-card-for-super-easy-r-1261113524)
- [x]  Code review.  Code cleanup.
	- [x]  CameraActivityMjpeg.java
	- [x]  CameraActivityMotionEye.java
	- [x]  CameraFragment.java
	- [x]  CameraFragmentMotionEye.java
	- [x]  CameraFragmentSnapshot.java
	- [x]  CameraFragmentSnapshotViewer.java		**Will address when creating new activity to launch this fragment.**
	- [x]  CameraFragmentTwoPane.java
	- [x]  CameraFragmentMjpegView.java
	- [x]  CarputerActivity.java
	- [x]  CarputerFragment.java
	- [x]  SingleFragmentActivity.java
	- [x]  SettingsActivity.java
- [x]  **(NOT POSSIBLE w/SURFACEVIEW WIDGET)**  ~~Dual-pane view.  Try to enable scrolling.~~  
- [x]  MotionEye view.  Enable button to take snapshot rather than touching screen.
- [x]  SSH Commands - Basic implementation.
	- [x]  txtReply.  Use non-proportional font (try courier).
	- [x]  Trouble why some are not working.
	- [x]  Hide keyboard after click "Execute Command".
- [x]  Implement rear camera using a Pi Zero w/motioneye.  This would connect to PINET.  Ref:  https://github.com/ccrisan/motioneye/issues/970

### v1.1 (IN PROGRESS)
- [x]  Add version/build number in new About menu action item.
- [ ]  **SettingsActivity**  Make more robust.  Need to store Nodes in JSON and store in shared preference.  Look into nested PreferneceScreen.
		https://developer.android.com/guide/topics/ui/settings/organize-your-settings
		https://stackoverflow.com/questions/5298370/how-to-add-a-button-to-a-preferencescreen/7251575
		https://developer.android.com/reference/android/preference/PreferenceFragment
		https://google-developer-training.github.io/android-developer-fundamentals-course-concepts/en/Unit%204/91_c_shared_preferences.html
		https://stackoverflow.com/questions/4788713/nested-preferencescreens-under-a-dynamic-list-of-preferences-on-android  
		https://exceptionshub.com/is-it-possible-to-add-an-array-or-object-to-sharedpreferences-on-android.html  
		https://stackoverflow.com/questions/37744333/how-to-dynamically-add-preferences-into-preferences-screen-and-bind-their-values  
		
### v1.2 (NOT STARTED)
- [ ] **CarputerFragmentMgmt/SSHFragment**  Needs work regarding nodes/args.  First need to make SettingsActivity more robust.

### v1.x (NOT STARTED)
- [ ]  Review use of icons.	
  - [ ]  Need RaspberryPi svg (not color).
  - [ ]  Need motioneye svg.
- [ ]  Address TODO's.	
- [ ]  Test and validate performance using RaspberryPi with USBStick + two USB Cameras + router.
- [ ]  Put a lot more effort into style/themes.

##  Tasks - Raspberry Pi 3 Model B+

### v1.0 (RELEASED)
- [x]  Install Rasbian.
- [x]  Enable SSH.
- [x]  Enable VNC.
- [x]  Install MotionEye.
- [x]  Setup RaspberryPi as Router.  https://www.instructables.com/id/Use-Raspberry-Pi-3-As-Router/
  - [x]  Access point.
- [x]  **Successfully deployed RaspberryPi as access point.**  Test and validate using RaspberryPi as router.  If this does not work out as planned then plan B would be to buy small router.
- [x]  **(ON ORDER)**  USBStick (16/32GB) for image archive.
- [x]  **Use Pi Zero as rear camera.**
    - [x] Install motioneye
	- [x] Configure main RaspberryPi motioneye Rear camera with streaming URL from Pi Zero.
	- [x] **(PI Camera cable on order)** Use PiCam rather than USB camera.  Validated USB camera works.  
- [x]  Create final Rasbian image for Carputer-Rear.
	- [x]  Need to configure motioneye to use PiCamera rather than USB camera.
- [x]  Create final Raspbian image for Carputer.
    - [x]  Need to redo mouting of usb flash drive.
	- [x]  Need to configure motioneye to use PiCamera from Carputer-Rear rather than the USB camera from Carputer-Rear.  
	
### v1.1  (NOT STARTED)
- [ ]  Version OS images independent of Android app versions
- [ ]  Add power off switch  
	https://github.com/TonyLHansen/raspberry-pi-safe-off-switch/
- [ ]  Add Real Time Clock (RTC)  
    https://learn.adafruit.com/adding-a-real-time-clock-to-raspberry-pi/overview  
    https://www.sparkfun.com/products/12708  

### v1.x  (NOT STARTED)
- [ ]  Determine power usage with RaspberryPi + USBStick + two USB Cameras + router.
- [ ]  Setup RaspberryPi as Router.  https://www.instructables.com/id/Use-Raspberry-Pi-3-As-Router/
  - [ ]  IP Bridge.  **NOTE:**  Once enabling the bridge it no longer provices an IP address for a client connection.
  
##  Feature Creep - not assigned to any release
- [ ]  Android:  View images/videos archived on the RaspberryPi USBStick.
- [ ]  Android:  cron job to delete old snapshots.  Would also need new shared preferences and fragment added to SettingsActivity.
- [ ]  RaspberryPi:  Create REST API to obtain files (images, videos, data, etc).  Example using Python -> https://codeburst.io/this-is-how-easy-it-is-to-create-a-rest-api-8a25122ab1f3.
- [x]  Android:  Simple SSH connect with JSch to RaspberryPi. http://eridem.net/android-tip-021-ssh-execute-remote-commands-with-android
- [ ]  Android:  SSH Commands.  Create history of commands.  UI to also delete commands.  Expand list of 'stock' commands.
- [ ]
- [ ]


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
 
 