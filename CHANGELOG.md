# Changelog
All notable changes to this Android application project will be documented in this file.

##  NO ADDITIONAL RELEASES PLANNED AT THIS TIME.

## [Unreleased]
### v1.x  (NOT STARTED)
##  Feature Creep - not assigned to any release
- [ ]  Android:  View images/videos archived on the RaspberryPi USBStick.
- [ ]  Android:  cron job to delete old snapshots.  Would also need new shared preferences and fragment added to SettingsActivity.
- [ ]  Android:  SSH Commands.  Create history of commands.  UI to also delete commands.  Expand list of 'stock' commands.
- [ ]
- [ ]

## [Unreleased]
### v1.x (NOT STARTED)
#### Added
- [ ]  Update/Add javadocs comments.  Will be ongoing process to better document code.  
	<https://developer.android.com/training/monitoring-device-state/connectivity-monitoring>  <https://stackoverflow.com/questions/5888502/how-to-detect-when-wifi-connection-has-been-established-in-android>id<https://www.journaldev.com/10356/android-broadcastreceiver-example-tutorial>rial  
#### Changed
- [ ]  Test and validate performance using RaspberryPi with USBStick + two USB Cameras + router.
- [ ]  I/Choreographer: Skipped frames!  The application may be doing too much work on its main thread.  
		First guess it is caused by CameraFragmentMjpegSnapshot grabbing frames.  If so, nothing I can do if I want to keep this functional<https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread>thread
- [ ] 
#### Removed


## [Released]
### v1.5.3 (9Nov2019)
#### Added
- [x] Bottom drawer navigation. Preference settings added:  Misc -> Enable bottom navigation.
- [x]  **List dhcp leases**
    sudo cat /var/lib/misc/dnsmasq.leases 
#### Changed    
- [x] Corrected launch icon. Was using default Android icon.
- [x]  Migrate to a single activity implementation.  This would be CarputerActivity which extends the BaseActivity.   (Note:  Settings will remain as a one-off activity).
- [ ] ~~Resolve issue where app is not able to connect with PINET where device has cellular capabilities.~~
    **UPDATE:** This is not doable.  To resolve this issue requires the disabling of **Cellular Data**. With BUILD > 21, this can only be performed in a system app. 
- [ ] Updated README.md with app screen shots.    
#### Removed


## [Released]
### v1.5.2 (25Apr2019)
#### Added
- [x]  RTC (real time clock) has been added to master node.  Slave nodes will sync date/time upon boot-up with master.  Carputer app has two log entries added with starting of SSHFragment.
These include outputting the date on the nodes to verify date/time functionality is working as expected and also logs the firmware version of the nodes, because, why not.
#### Changed
#### Removed

## [Released]
### v1.5.1 (20Apr2019)
#### Added
#### Changed
- [x]  Added 'sudo hwclock -w' following by 'sudo hwclock -r' to list of hard coded commands.  These will write/read the date set in the real time clock module.  
       This has been added to the 'Sync Date' feature.  
       Only the master RaspberryPi has an RTC implemented.
#### Removed
- [x]  Removed the auto syncing of the Android date/time with the connected nodes.  Implemented real time clock on master RaspberryPi so need to sync date when app starts.  
       Will not be concerned with date on nodes other than master.
       Still keeping the functionality to where a button is coded to set the date.

       
## [Released]
### v1.5 (8Apr2019)
#### Added
- [x] Ability to view archived video from the RaspberryPi.  Create new fragment that hosts web view.  On RaspberryPi quick and easy implementation by creating 
		symbolic link to motioneye video archive.  Apache web server is configured to display index of directory.
#### Changed
#### Removed

## [Released]
### v1.4.1 (4Apr2019)
#### Added 
- [x]  Connectivity monitoring. Logs Wifi connection change to system log.  
#### Changed
#### Removed


## [Released]
### v1.4 (31Mar2019)
#### Added
- [x]  Update/Add javadocs comments.  Will be ongoing process to better document code.  
- [x]  Create syslog and make it viewable.  The SSHFragment->txtReply is a poor man's version of syslog. 
- [x]  Added GlobalVariables extends Application class.  This will contain shared preferences and other objects that are TBD.
    - [x]  Change code to utilize GlobalVariables.
- [ ]  **(STRETCH GOAL)**  System Status dialog.   Feature creep!  
	- [x]  Infrastructure added.
	- [ ]  Add content.  ON HOLD. PROBABLY WILL NOT GO FORWARD WITH THIS.
- [x]  System Logging.  Feature creep!
	- [x]  Logging in SSH Fragment needs improvement.
	- [x] Use across application.
#### Changed
- [x]  SSH Fragment is a complete hack!  Replace or improve.
	- [x] Add feature to display progress when executing comm<https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a>as<https://stackoverflow.com/questions/3781751/android-anything-similar-to-the-iphone-sdk-delegate-callbacks>-callbacks  
- [x]  Address TODO's relating to EventBus->sendMessage().  
#### Removed
- [x]  Remove fragment:  CameraFragmentMjpegView

## [Released]
### v1.3.1 (15Mar2019)
#### Added
- [x]  Not working across fragments.  Keep the dev<https://developer.android.com/training/scheduling/wakelock>ing/wakelock  
	Added code to the SingleFragmentActivity.
- [x]  Update/Add javadocs comments.  Will be ongoing process to better document code.    
#### Changed
#### Removed

## [Released]
### v1.3 (14Mar2019)
#### Added
- [x]  Keep the device awake. <https://developer.android.com/training/scheduling/wakelock> 
  - [x]  Add preference fro this choice.  Create new category 'Misc' and put it under there for now.
- [x]  Automatic network connection.
	- [x]  (ADDED IN v1.1)  Added network preferences for SSID and Passphrase for WPA network connection.  WiFI connection not yet implementation in the Carputer application.	
- [x]  Update/Add javadocs comments.  Will be ongoing process to better document code.  	
#### Changed
#### Removed

## [Released]
### v1.2 (12Mar2019)
#### Added
- [x]  Add javadocs comments.  Will be ongoing process to better document code.  
#### Changed
- [x]  Implement material design.  Didn't really do anything extra.  I did implement the material design icons from the start. <https://stackoverflow.com/questions/8855791/how-to-create-standard-borderless-buttons-like-in-the-design-guideline-mentione>
	- C:\Users\bdoerr\.gradle\caches\transforms-1\files-1.1\preference-1.1.0-alpha03.aar\50f8bf0a051ec5e694dfaeeb38097e7e\res\layout
- [x]  Put a lot more effort into style/themes.  Applied to settings detail fragments.
- [x]  Review use of icons.	
  - [ ]  ~~Need RaspberryPi svg (not color).~~  Couldn't find a solution plus I like the color.
  - [x]  Need motioneye svg.  Current eye con (pun intended) works.
#### Removed

## [Released]
### v1.1 (5Mar2019)
#### Added
- [x]  Add version/build number in new About menu action item.
#### Changed
- [x] (NEEDS VALIDATION) **CarputerFragmentMgmt/SSHFragment**  Needs work regarding nodes/args.  First need to make SettingsActivity more robust.
- [x]  **SettingsActivity**  Make more robust.  Need to store Nodes in JSON and store in shared preference.  Look into nested Preferences. 
    <https://developer.android.com/guide/topics/ui/settings/organize-your-settings><br>
    <https://stackoverflow.com/questions/5298370/how-to-add-a-button-to-a-preferencescreen/7251575>
    <https://developer.android.com/reference/android/preference/PreferenceFragment>
    <https://google-developer-training.github.io/android-developer-fundamentals-course-concepts/en/Unit%204/91_c_shared_preferences.html>
    <https://stackoverflow.com/questions/4788713/nested-preferencescreens-under-a-dynamic-list-of-preferences-on-android>
    <https://exceptionshub.com/is-it-possible-to-add-an-array-or-object-to-sharedpreferences-on-android.html>
    <https://stackoverflow.com/questions/37744333/how-to-dynamically-add-preferences-into-preferences-screen-and-bind-their-values> 
- [x]  Address TO-DO's.	
- [x]  Added network preferences for SSID and Passphrase for WPA network connection.  WiFI connection not yet implementation in the Carputer application.	
- [x]  Switched to using Android Jetpack.
#### Removed
- [x] Dual-pane view removed.


## [Released]
### v1.0 28Jan2019
- [x]  Migrate code from Android-CarputerPOC (the proof of concept project).
- [x]  Design change from CarputerPOC
	- [x]  Use MotionEye to display streaming video.  Mjpeg has poor performance when implementing the snapshot feature.  Able to implement on-click event to capture image of screen.
			<br/>**NOTE:**  Performance improved once enabling RaspberryPi as an access point.
		- [x]  Create activity/fragment to host tab layout to host MotionEye.  Currently only one tab is needed.
		- [x]  ~~Use TabLayout to also host WebChromeClient for viewing MotionEye admin console.~~
	- [x]  Create new activity and menu item for viewing snapshots rather than as a tab in the Camera view.  Use TabLayout for future expansion.
	- [x]  Draw menu:  Camera - mjpeg, Camera MotionEye, Image Archive, Settings.
	- [x]  Disable settings for features: flip image, rotate image and possibly authentication settings.
	- [ ]
- [x]  Improve image capture.  Currently senses FINGER_ACTION_UP so sensitive and creates unnecessary snapshots.	
- [x]  **(NEED SD CARDS)**  Create image of sd card.  Then use three cars: <https://lifehacker.com/how-to-clone-your-raspberry-pi-sd-card-for-super-easy-r-1261113524>
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
- [x]  Implement rear camera using a Pi Zero w/motioneye. <https://github.com/ccrisan/motioneye/issues/970>


		

