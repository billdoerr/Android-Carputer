# Changelog
All notable changes to this Android application project will be documented in this file.


## [Unreleased]
### v1.x (NOT STARTED)
#### Added
- [ ]  Implement WiFI auto connection to PINET.	
#### Changed
- [ ]  Test and validate performance using RaspberryPi with USBStick + two USB Cameras + router.
#### Removed

## [Unreleased]
### v1.3 (NOT STARTED)
#### Added
- [ ]  Automatic network connection.
#### Changed
- [ ]  SSH Fragment is a complete hack!  Replace with something more robust.
#### Removed

## [Unreleased]
### v1.2 (NOT STARTED)
#### Added
#### Changed
- [ ]  Implement material design.
	https://stackoverflow.com/questions/8855791/how-to-create-standard-borderless-buttons-like-in-the-design-guideline-mentione
- [ ]  Put a lot more effort into style/themes.
- [ ]  Review use of icons.	
  - [ ]  Need RaspberryPi svg (not color).
  - [ ]  Need motioneye svg.
#### Removed

## [Released]
### v1.1 Relese ETA:  5Mar2019
#### Added
- [x]  Add version/build number in new About menu action item.
#### Changed
- [ ] (NEEDS VALIDATION) **CarputerFragmentMgmt/SSHFragment**  Needs work regarding nodes/args.  First need to make SettingsActivity more robust.
- [x]  **SettingsActivity**  Make more robust.  Need to store Nodes in JSON and store in shared preference.  Look into nested PreferenceScreen.
		https://developer.android.com/guide/topics/ui/settings/organize-your-settings
		https://stackoverflow.com/questions/5298370/how-to-add-a-button-to-a-preferencescreen/7251575
		https://developer.android.com/reference/android/preference/PreferenceFragment
		https://google-developer-training.github.io/android-developer-fundamentals-course-concepts/en/Unit%204/91_c_shared_preferences.html
		https://stackoverflow.com/questions/4788713/nested-preferencescreens-under-a-dynamic-list-of-preferences-on-android  
		https://exceptionshub.com/is-it-possible-to-add-an-array-or-object-to-sharedpreferences-on-android.html  
		https://stackoverflow.com/questions/37744333/how-to-dynamically-add-preferences-into-preferences-screen-and-bind-their-values  
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


		
