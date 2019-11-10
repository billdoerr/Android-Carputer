# Android-Carputer project

## Carputer project

## Goals of POC
1. **PRIMARY GOAL** -> Display streaming video from RaspberryPI running motioneye.
2. **SECONDARY GOAL** -> Display of OBD-II/CANBUS data.  RaspberryPI will connect to OBD-II/CANBUS.
3. **STRETCH GOAL** -> Play music.
4. **STRETCH GOAL** -> Location services.  Maps.
5. **STRETCH GOAL** -> Sync data.  Upload data to cloud.

### Related Repositories
    <https://github.com/billdoerr/Hardware-Carputer>  
    <https://github.com/billdoerr/Python-Carputer>  
    <https://github.com/billdoerr/Android-Carputer> 

### Features
    - Will be based on a RaspberryPi 3 Model B running motionEye.<br>
    - Android tablet will be used for the user interface.**  <br>
    - Primary goal is to have a front and rear car cameras.  RaspberryPi will archive video to USBStick and Android table will host the UI and have the ability to capture image snapshots.<br>

### Planned Features    
    - Display of OBD-II/CANBUS data.  RaspberryPI will connect to OBD-II/CANBUS.
    - Play music.
    - Location services.  Maps.
    - Sync data.  Upload data to cloud.

### Known Issues
    - Camera - mjpeg streaming video lags and choppy.

___
### HowTo
___
**Android-Carputer**<br>
Display streaming video from RaspberryPI running motionEye.<br>
<img src="images/icon.png" width="100"/>

**Navigation**<br>
App navigation is performed via the bottom navigation toolbar.<br>
<img src="images/bottom_nav.png" width="200"/><br>

And the drawer navigation which pulls out from the left.<br>
<img src="images/drawer_nav.png" width="200"/><br>


**Camera - motionEye**<br>
The home screen defaults to the **Camera - motionEye** view which streams the configured cameras.  <br>
There is one tab per configured motionEye node (not configured camera).<br>
The options menu **Snapshot** captures a screen shot of current video frame.<br>
<img src="images/motioneye_view.png" width="200"/>


**Camera - mjpeg**<br>
The **Camera - mjpeg** view streams video in mjpeg format. This view is not ideal as the streamed video is choppy.<br>
There is one tab per configured camera.<br>
The options menu **Snapshot** captures a screen shot of current video frame.<br>
<img src="images/mjpeg_view.png" width="200"/>


**Computer Management** <br>
The **Computer Management** view provides information of the connected nodes. <br>
<img src="images/mgmt_view.png" width="200"/><br>
This view contains tabs for:<br>
&nbsp;&nbsp;**SSH** - Provides simple commands and their output.<br>
&nbsp;&nbsp;**System Log** - View the system log. Contains options menu for **refresh** and **clear log**.<br>
&nbsp;&nbsp;**phpSysInfo** - Displays information about system facts like Uptime, CPU, Memory, PCI devices, SCSI devices, IDE devices, Network adapters, Disk usage, and more.<br>
<img src="images/phpsysinfo.png" width="200"/><br>


**Image Archive**<br>
This contains two tabs:<br>
&nbsp;&nbsp;**Snapshots** - Contains list of images saved using **Snapshot**. To delete an image click on the image to delete.<br>
&nbsp;&nbsp;**Image Archive** - If RaspberryPi has attached storage. motionEye is configured to archive the streamed video. This view gives access to that archive storage.<br>
<img src="images/image_archive_view.png" width="200"/>&nbsp;&nbsp;<img src="images/image_archive_storage.png" width="200"/><br>


**Settings**<br>
The settings screen allows to configure the following:.<br>
<img src="images/settings.png" width="200"/>

**Camera Details**<br>
Configure and add camera details.<br>
<img src="images/camera_details.png" width="200"/><br>

**Node Details**<br>
Configure and add node details.<br>
<img src="images/node_details.png" width="200"/><br>

**Network Settings**<br>
Configure the network settings.<br>

**Keep Device Awake** - Enabled by default. As long as this window is visible to the user, keep the device's screen turned on and bright.<br>

**Enable bottom navigation** - Always the bottom toolbar to be displayed or not.<br>

**Image Archive URL** - URL of the motionEye video archive storage.<br>


**About**<br>
Simple screen that shows the app name, version and build number. The build number is a date/time stamp when app was complied.  
An example build number is 1910300646: 19=year 10=month 03=day of month 06=hour 46=minutes<br>
<img src="images/about_view.png" width="200"/>


















