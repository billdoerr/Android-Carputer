# Android-Carputer

## Carputer project

** Will be based on a RaspberryPi 3 Model B running motioneye. **  <br />
** Android tablet will be used for the user interface. **  <br />
** Using and configuring the RaspberryPi as router has not been tested and validated.

## Goals of POC
1.  PRIMARY GOAL -> Display streaming video from RaspberryPI running motioneye.
2.  SECONDARY GOAL -> Display of OBD-II/CANBUS data.  RaspberryPI will connect to OBD-II/CANBUS.
3.  STRETCH GOAL -> Play music.
4.  STRETCH GOAL -> Location services.  Maps.

##  Tasks - Android
- []
- []
- []
- []
- []
- []
- []
- []
- []
- []

##  Tasks - RaspberryPi 3 Model B
- [x]  Install Rasbian.
- [x]  Enable SSH.
- [x]  Enable VNC.
- [x]  Install MotionEye.
- []  Setup RaspberryPi as Router.  https://www.instructables.com/id/Use-Raspberry-Pi-3-As-Router/
- []  Test and validate using RaspberryPi as router.  If this does not work out as planned then plan B would be to buy small router.
- []  USBStick (16/32GB) for image archive.
- []  Determine power usage with RaspberryPi + USBStick + two USB Cameras.
- []
- []
- []
- []

## Camera Module Issues from proof of concept project 'Android-CarputerPOC'.
1. PERFORMANCE:  Snapshot fragment slows everything down. Probably since it is capturing every frame, but there is no control over this.
	- []  Disabled displaying fps.  This improved performance a bit.
	- []  Snapshot view is the performance hog and do not have any control over this.

2. PROBLEM:  Layout with MJPEG widget.  Very frustrating getting other widgets to display correcting along with MJPEG widget.

3. PROBLEM:  Would like to figure a way to capture a frame with a click so I don't have to use the mode of capturing every frame. 
	- []  MJPEG uses SurfaceView and there isn't any way to capture a bitmap from this Widget.
	
4. DESIGN:  Implement DrawerView as home interface.  CarputerActivity will launch CameraActivity as default view.
	- []  Partially implmented.
	- []  (RESOLVED) PROBLEM:  How to have DrawView enabled for all activities/fragments?  
		- []  (SOLUTION): Implemented DrawView in SingleFragmentActivity and which other activities are subclassed.
	- []  (PARTIALLY RESOLVED) PROBLEM:  Difficulites getting ActionBar to display in SettingsActivity.
		- []  (SOLUTION): DrawView not posible in SettingsActivity.  Implemented toolbar which just supports onBackPressed(). 

5. (RESOLVED) PROBLEM:  Unable to use Android WebView to display MotionEye UI.  Current using Web Browser common content.
	- []  (SOLUTION):  User error.

6. PROBLEM:  Unable to send Authentication Mode -> Basic in MotionEye but not able to pass url with credentials.  Still prompts for user/password.
Url should have this syntax:  http://username:password@host or http://username:password@IP:PORT


 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 