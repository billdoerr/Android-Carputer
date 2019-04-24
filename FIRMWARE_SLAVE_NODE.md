# Firmware
All notable changes to the nodes required for the Android application project will be documented in this file.


## [Unreleased]
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

## [Unreleased]
### v1.X (NOT STARTED)
#### Added
- [ ]  Add power off switch  
	https://github.com/TonyLHansen/raspberry-pi-safe-off-switch/
#### Changed
#### Removed


## [Released]
### v1.1 (24Apr2019)
#### Added
- [ ]  Auto sync dates of nodes that dont' have RTC with that of the master.  
        Use SSH command to retrieve date from master.  
        https://superuser.com/questions/685471/how-can-i-run-a-command-after-boot  
        
        -------------------------------------------
        1.  PERFORM THE FOLLOWING ON THE SLAVE NODE
        -------------------------------------------
        
crontab -e
@reboot /bin/timesync.sh

sudo vi /bin/timesync.sh
#! /bin/sh

# Need delay
sleep 20

# Get date from master node and set date
sudo date -s "`ssh pi@192.168.4.1 'date'`" >> /tmp/timesync.log  

sudo chmod 755  /bin/timesync.sh

-------------------------------------------
2. PERFORM THE FOLLOWING ON THE SLAVE NODE
-------------------------------------------

ssh-keygen -t rsa -b 2048
ssh-copy-id -i ~/.ssh/id_rsa pi@192.168.4.1

- [x]  Version OS images independent of Android app versions

cd /bin
sudo vi carputer
#! /bin/sh
cat /etc/carputer/version

sudo chmod +x carputer


cd /etc
sudo mkdir carput
cd carputer
sudo vi version
v1.1
Released 24Apr2019

- [x]  Add Real Time Clock (RTC)  
    https://pimylifeup.com/raspberry-pi-rtc/  
    https://learn.adafruit.com/adding-a-real-time-clock-to-raspberry-pi/overview  
    https://www.sparkfun.com/products/12708  
#### Changed
#### Removed


## [Released]
### v1.0 (28Feb2019)
- [x]  Install Raspbian.
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
- [x]  Create final Raspbian image for Carputer-Rear.
	- [x]  Need to configure motioneye to use PiCamera rather than USB camera.
- [x]  Create final Raspbian image for Carputer.
    - [x]  Need to redo mounting of usb flash drive.
	- [x]  Need to configure motioneye to use PiCamera from Carputer-Rear rather than the USB camera from Carputer-Rear.  
	



  
