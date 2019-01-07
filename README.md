# Android-Carputer project

## Carputer project

- **Will be based on a RaspberryPi 3 Model B running motioneye.**  <br />
- **Android tablet will be used for the user interface.**  <br />
- **Primary goal is to have a front and rear car cameras.  RaspberryPi will archive video to USBStick and Android table will host the UI and have the ability to capture image snapshots.**  <br />

## Goals of POC
1. **PRIMARY GOAL** -> Display streaming video from RaspberryPI running motioneye.
2. **SECONDARY GOAL** -> Display of OBD-II/CANBUS data.  RaspberryPI will connect to OBD-II/CANBUS.
3. **STRETCH GOAL** -> Play music.
4. **STRETCH GOAL** -> Location services.  Maps.
5. **STRETCH GOAL** -> Sync data.  Upload data to cloud.

##  Tasks - Android
- [x]  Migrate code from Android-CarputerPOC (the proof of concept project).
- [ ]  Design change from CarputerPOC
	- [x]  Use MotionEye to display streaming video.  Mjpeg has poor performance when implementing the snapshot feature.  Able to implment on-click event to capture image of screen.
			<br/>**NOTE:**  Performance improved once enabling RaspberryPi as an access point.
		- [x]  Create activity/fragment to host tab layout to host MotionEye.  Currently only one tab is needed.
		- [x]  ~~Use TabLayout to also host WebChromeClient for viewing MotionEye admin console.~~
	- [x]  Create new activity and menu item for viewing snapshots rather than as a tab in the Camera view.  Use TabLayout for future expansion.
	- [x]  Draw menu:  Camera - mjpeg, Camera MotionEye, Image Archive, Settings.
	- [x]  Disable settings for features: flip image, rotate image and possibly authentication settings.
	- [ ]
- [ ]  Improve image capture.  Currently senses FINGER_ACTION_UP so sensitive and creates unnecessary snapshots.	
- [ ]  **(NEED SD CARDS)**  Create image of sd card.  Then use three cars:  prod/dev/release.
- [ ]
- [ ]  Code review.  Code cleanup.
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

- [x]  Review use of icons.	
- [ ]  Address TODO's.	
- [ ]  Test and validate performance using RaspberryPi with USBStick + two USB Cameras + router.
- [ ]
- [ ]  Put some effort into style/themes.
- [ ]
- [ ]
- [ ]

##  Tasks - RaspberryPi 3 Model B
- [x]  Install Rasbian.
- [x]  Enable SSH.
- [x]  Enable VNC.
- [x]  Install MotionEye.
- [ ]  Setup RaspberryPi as Router.  https://www.instructables.com/id/Use-Raspberry-Pi-3-As-Router/
  - [x]  Access point.
  - [ ]  IP Bridge.  **NOTE:**  Once enabling the bridge it no longer provices an IP address for a client connection.
- [ ]  Test and validate using RaspberryPi as router.  If this does not work out as planned then plan B would be to buy small router.
- [ ]  USBStick (16/32GB) for image archive.
- [ ]  Determine power usage with RaspberryPi + USBStick + two USB Cameras + router.
- [ ]
- [ ]
- [ ]
- [ ]

##  Feature Creep
- [ ]  Android:  View images/videos archived on the RaspberryPi USBStick.
- [ ]  Android:  cron job to delete old snapshots.  Would also need new shared preferences and fragment added to SettingsActivity.
- [ ]  RaspberryPi:  Create REST API to obtain files (images, videos, data, etc).  Example using Python -> https://codeburst.io/this-is-how-easy-it-is-to-create-a-rest-api-8a25122ab1f3.
- [ ]  (**IN PROGESS**) Android:  Simple SSH connect with JSch to RaspberryPi. http://eridem.net/android-tip-021-ssh-execute-remote-commands-with-android
- [ ]
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

//  Android Web Browser common intent
Intent i = new Intent(Intent.ACTION_VIEW, uri);
startActivity(i); 


    <View android:id="@+id/divider"
        style="@style/Divider" />

https://medium.com/exploring-android/exploring-the-new-android-constraintlayout-eed37fe8d8f1

phpSysInfo 
http://192.168.4.1/phpsysinfo 
http://192.168.1.104/phpsysinfo/xml.php?plugin=complete&json
 
 
        <TextView
            android:id="@+id/txtReply"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:gravity="top|start"
            android:enabled="false"
            android:inputType="textMultiLine"
            android:lines="30"
            android:maxLines="100"
            android:minLines="30"
            android:scrollbars="vertical"
            android:text="@string/txt_carputer_mgmt_ssh_results"
            android:background="@android:color/black"
            android:textColor="@android:color/holo_red_dark" />
		

editor.putString("jsondata", jobj.toString());
And to get it back:

String strJson = sharedPref.getString("jsondata","0");//second parameter is necessary ie.,Value to return if this preference does not exist. 

if (strJson != null) {
           try {
               JSONObject response = new JSONObject(strJson);

         } catch (JSONException e) {

         }
  }
	
 
//  Play video HTML5
<video class="motionpicture" width="720" height="480" autobuffer
controls preload="auto">

<source src="/path/to/vid.webm" />
Your browser does not appear to support HTML5 media. Try updating
your browser or (if you are not already) using an open source
browser like Firefox." </video>
 
 
 <View style="@style/Divider"/>
 
 
 
 
 
 