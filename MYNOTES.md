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
# https://werxltd.com/wp/2012/01/05/simple-init-d-script-template/

sudo update-rc.d listen_for_shutdown.sh defaults

#!/bin/bash
# chkconfig: 2345 20 80
# description: Description comes here....

# Source function library.
. /etc/init.d/functions

start() {
    # code to start app comes here 
    # example: daemon program_name &
}

stop() {
    # code to stop app comes here 
    # example: killproc program_name
}

case "$1" in 
    start)
       start
       ;;
    stop)
       stop
       ;;
    restart)
       stop
       start
       ;;
    status)
       # code to check status of app comes here 
       # example: status program_name
       ;;
    *)
       echo "Usage: $0 {start|stop|status|restart}"
esac

exit 0 
 	
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
# http://raspi.tv/2013/rpi-gpio-basics-3-how-to-exit-gpio-programs-cleanly-avoid-warnings-and-protect-your-pi

import RPi.GPIO as GPIO  
  
# here you would put all your code for setting up GPIO,  
# we'll cover that tomorrow  
# initial values of variables etc...  
counter = 0  
  
try:  
    # here you put your main loop or block of code  
    while counter < 9000000:  
        # count up to 9000000 - takes ~20s  
        counter += 1  
    print "Target reached: %d" % counter  
  
    except KeyboardInterrupt:  
        # here you put any code you want to run before the program   
        # exits when you press CTRL+C  
        print "\n", counter # print value of counter  
      
    except:  
        # this catches ALL other exceptions including errors.  
        # You won't get any error messages for debugging  
        # so only use it once your code is working  
        print "Other error or exception occurred!"  
      
    finally:  
        GPIO.cleanup() # this ensures a clean exit  
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

1478279949 00:c2:c6:dd:a4:e6 192.168.4.19 SKULL-CANYON 01:00:c2:c6:dd:a4:e6
1478280180 14:dd:a9:45:53:c5 192.168.4.17 android-2484d27dda64c42c 01:14:dd:a9:45:53:c5
1478280827 38:80:df:ef:49:73 192.168.4.18 android-e26387ee2616931b 01:38:80:df:ef:49:73
 
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
Run a Program On Your Raspberry Pi At Startup
https://www.dexterindustries.com/howto/run-a-program-on-your-raspberry-pi-at-startup/
===============================================================================================

===============================================================================================

apt-get install ntp <-- server

apt-get install ntpdate <-- client

You could setup a NTP server - there are tutorials describing how to do this, although this requires an understanding of NTP. 
The clients need to be configured to use the local NTP server in /etc/ntp.conf (Jessie) or  /etc/systemd/timesyncd.conf (Stretch).

A much simpler solution is to copy the date from the host (which I use when no network is available).
This can be done manually over ssh by running
ssh pi@hostname.local sudo date -s$(date -Ins)

ntpq -p shows the correct ntp server, but ntpdate -d ipaddress gives me the following error: 

Nov 13:26:00 ntpdate[11724]: no server suitable for synchronization found

 sudo ntpdate 1.ro.pool.ntp.org
 
 
 ssh pi@192.168.4.1 sudo date -s$(date -Ins)
 
 ssh-copy-id -i ~/.ssh/tatu-key-ecdsa user@host
 ssh-copy-id -i ~/.ssh/id_rsa pi@192.168.4.5
 
 date +%Y%m%d%T -s "`ssh user@server 'date "+%Y%m%d %T"'`"

------------------------------------------------ 
ssh-keygen -t rsa -b 2048
ssh-copy-id -i ~/.ssh/id_rsa pi@192.168.4.5

ssh -v pi@192.168.4.1
 
 https://www.commandlinefu.com/commands/view/9153/synchronize-date-and-time-with-a-server-over-ssh
 sudo date -s "`ssh pi@192.168.4.1 'date'`"
===============================================================================================


===============================================================================================

//  You can register a BroadcastReceiver to be notified when a WiFi connection is established (or if the connection changed).

<receiver android:name=".NetworkChangeReceiver" >
	<intent-filter>
		<action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
		<action android:name="android.net.wifi.WIFI_STATE_CHANGED"/>
		<action android:name="android.net.wifi.STATE_CHANGE"/>
	</intent-filter>
</receiver>

<intent-filter>
<action android:name="android.net.wifi.WIFI_STATE_CHANGED"/>
<action android:name="android.net.wifi.STATE_CHANGE"/>
</intent-filter>



final IntentFilter filters = new IntentFilter();
filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
filters.addAction("android.net.wifi.STATE_CHANGE");
super.registerReceiver(yourReceiver, filters);


<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

ConnectivityManager conMngr = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
android.net.NetworkInfo wifi = conMngr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
android.net.NetworkInfo mobile = conMngr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
            mobile != null && mobile.isConnectedOrConnecting();
        if (isConnected) {
            Log.d("Network Available ", "YES");
        } else {
            Log.d("Network Available ", "NO");
        }
    }
}

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() || mobile != null && mobile.isConnectedOrConnecting(); 
        if (isConnected) {
            Log.d("Network Available ", "YES");
        }else{
           Log.d("Network Available ", "NO");
        }
    }
}

=========================================================
 public class BroadCastSampleActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
            }
        }
    };
}
==============================================================
public class ConnectionChangeReceiver extends BroadcastReceiver
{
  @Override
  public void onReceive( Context context, Intent intent )
  {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
    if ( activeNetInfo != null )
    {

      Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
    }
    if( mobNetInfo != null )
    {
      Toast.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
    }
  }
}

==============================================================

^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 
 CONNECTIVITY_SERVICE ("connection")
A ConnectivityManager for handling management of network connections.
IPSEC_SERVICE ("ipsec")
A IpSecManager for managing IPSec on sockets and networks.
WIFI_SERVICE ("wifi")
A WifiManager for management of Wi-Fi connectivity. On releases before NYC, it should only be obtained from an application context, 
and not from any other derived context to avoid memory leaks within the calling process.
WIFI_AWARE_SERVICE ("wifiaware")
A WifiAwareManager for management of Wi-Fi Aware discovery and connectivity.
void	onAvailable(Network network)
Called when the framework connects and has declared a new network ready for use.

void	onBlockedStatusChanged(Network network, boolean blocked)
Called when access to the specified network is blocked or unblocked.

void	onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities)
Called when the network the framework connected to for this request changes capabilities but still satisfies the stated need.

void	onLinkPropertiesChanged(Network network, LinkProperties linkProperties)
Called when the network the framework connected to for this request changes LinkProperties.

void	onLosing(Network network, int maxMsToLive)
Called when the network is about to be disconnected.

void	onLost(Network network)
Called when the framework has a hard loss of the network or when the graceful failure ends.

void	onUnavailable()
Called if no network is found in the timeout time specified in ConnectivityManager.requestNetwork(android.net.NetworkRequest, android.net.ConnectivityManager.NetworkCallback, int) call.
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 
 ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.WIFI_SERVICE);
 
    NetworkRequest.Builder builder = new NetworkRequest.Builder();

    connectivityManager.registerNetworkCallback(
            builder.build(),
            new ConnectivityManager.NetworkCallback() {
                /**
                 * @param network
                 */
                @Override
                public void onAvailable(Network network) {

                    sendBroadcast(
                            getConnectivityIntent(false)
                    );

                }

                /**
                 * @param network
                 */
                @Override
                public void onLost(Network network) {

                    sendBroadcast(
                            getConnectivityIntent(true)
                    );

                }
            }

    );

}

 /**
 * @param noConnection
 * @return
 */
private Intent getConnectivityIntent(boolean noConnection) {

    Intent intent = new Intent();

    intent.setAction("mypackage.CONNECTIVITY_CHANGE");
    intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, noConnection);

    return intent;

}



===============================================================================================


===============================================================================================
https://stackoverflow.com/questions/36100187/how-to-start-fragment-from-an-activity
https://stackoverflow.com/questions/18634207/difference-between-add-replace-and-addtobackstack

FragmentManager fm = getFragmentManager();
FragmentTransaction ft = fm.beginTransaction();
ft.add(R.id.container,YOUR_FRAGMENT_NAME,YOUR_FRAGMENT_STRING_TAG);
ft.addToBackStack(null);
ft.commit();
And to replace fragment do this:

FragmentManager fm = getFragmentManager();
FragmentTransaction ft = fm.beginTransaction();
ft.replace(R.id.container,YOUR_FRAGMENT_NAME,YOUR_FRAGMENT_STRING_TAG);
ft.addToBackStack(null);
ft.commit();

@Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate (
                getClassLoader(),
                pref.getFragment(),
                args);
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }
	

                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                                CarputerFragmentAbout fragment =  CarputerFragmentAbout.newInstance();
                                ft.add(R.id.fragment_container, fragment);
                                ft.addToBackStack(null);
                                ft.commit();	
===============================================================================================


===============================================================================================
D/EGL_emulation: eglMakeCurrent: 0xae554600: ver 3 1 (tinfo 0xae552ae0)
E/Surface: getSlotFromBufferLocked: unknown buffer: 0xa2899460
D/AndroidRuntime: Shutting down VM
E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.billdoerr.android.carputer, PID: 8618
    java.lang.RuntimeException: Unable to stop activity {com.billdoerr.android.carputer/com.billdoerr.android.carputer.CarputerActivityMgmt}: java.lang.IllegalArgumentException: Receiver not registered: com.billdoerr.android.carputer.utils.NetworkChangeReceiver@8094503
        at android.app.ActivityThread.performStopActivityInner(ActivityThread.java:3500)
        at android.app.ActivityThread.handleStopActivity(ActivityThread.java:3550)
        at android.app.ActivityThread.-wrap20(ActivityThread.java)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1373)
        at android.os.Handler.dispatchMessage(Handler.java:102)
        at android.os.Looper.loop(Looper.java:148)
        at android.app.ActivityThread.main(ActivityThread.java:5417)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:726)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:616)
     Caused by: java.lang.IllegalArgumentException: Receiver not registered: com.billdoerr.android.carputer.utils.NetworkChangeReceiver@8094503
        at android.app.LoadedApk.forgetReceiverDispatcher(LoadedApk.java:780)
        at android.app.ContextImpl.unregisterReceiver(ContextImpl.java:1195)
        at android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:576)
        at com.billdoerr.android.carputer.SingleFragmentActivity.onStop(SingleFragmentActivity.java:148)
        at android.app.Instrumentation.callActivityOnStop(Instrumentation.java:1278)
        at android.app.Activity.performStop(Activity.java:6380)
        at android.app.ActivityThread.performStopActivityInner(ActivityThread.java:3497)
        at android.app.ActivityThread.handleStopActivity(ActivityThread.java:3550) 
        at android.app.ActivityThread.-wrap20(ActivityThread.java) 
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1373) 
        at android.os.Handler.dispatchMessage(Handler.java:102) 
        at android.os.Looper.loop(Looper.java:148) 
        at android.app.ActivityThread.main(ActivityThread.java:5417) 
        at java.lang.reflect.Method.invoke(Native Method) 
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:726) 
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:616) 		
===============================================================================================


===============================================================================================

===============================================================================================


===============================================================================================
//    /**
//     * Print pretty xml.
//     * https://stackoverflow.com/questions/25864316/pretty-print-xml-in-java-8/33541820#33541820
//     * @param xml
//     * @param indent
//     * @return
//     */
//    public static String makeXmlPretty(String xml, int indent) {
//        try {
//            // Turn xml string into a document
//            Document document = DocumentBuilderFactory.newInstance()
//                    .newDocumentBuilder()
//                    .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
//
//            // Remove whitespaces outside tags
//            document.normalize();
//            XPath xPath = XPathFactory.newInstance().newXPath();
//            NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
//                    document,
//                    XPathConstants.NODESET);
//
//            for (int i = 0; i < nodeList.getLength(); ++i) {
//                Node node = nodeList.item(i);
//                node.getParentNode().removeChild(node);
//            }
//
//            // Setup pretty print options
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            transformerFactory.setAttribute("indent-number", indent);
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//            // Return pretty print xml string
//            StringWriter stringWriter = new StringWriter();
//            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
//            return stringWriter.toString();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
===============================================================================================


===============================================================================================

===============================================================================================


===============================================================================================
FF000000

FFFFFFFF
===============================================================================================


===============================================================================================

===============================================================================================


===============================================================================================

===============================================================================================


===============================================================================================
Never EVER make API calls on the UI thread.
===============================================================================================


===============================================================================================
I/Choreographer: Skipped frames!  The application may be doing too much work on its main thread.
https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread
===============================================================================================


===============================================================================================

===============================================================================================


===============================================================================================

===============================================================================================
















 
 