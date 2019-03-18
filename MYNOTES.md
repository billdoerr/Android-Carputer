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


===============================================================================================

===============================================================================================
&lt;%pri%&gt;%protocol-version% %timestamp:::date-rfc3339% %HOSTNAME% %app-name% %procid% %msgid% %msg%n

Aug 30 10:06:12 newfish kernel:   3 SCB_CONTROL[0xe0] SCB_SCSIID[0x7] SCB_LUN[0x0] SCB_TAG[0xff]
Aug 30 10:06:12 newfish kernel:   4 SCB_CONTROL[0xe0] SCB_SCSIID[0x7] SCB_LUN[0x0] SCB_TAG[0xff]
Aug 30 10:06:12 newfish kernel:   5 SCB_CONTROL[0xe0] SCB_SCSIID[0x17] SCB_LUN[0x0] SCB_TAG[0xff]
Aug 30 10:06:12 newfish kernel:   6 SCB_CONTROL[0x0] SCB_SCSIID[0x17] SCB_LUN[0x0] SCB_TAG[0xff]
Aug 30 10:06:12 newfish kernel:   7 SCB_CONTROL[0xe0] SCB_SCSIID[0x17] SCB_LUN[0x0] SCB_TAG[0xff]
Aug 30 10:06:12 newfish kernel:   8 SCB_CONTROL[0x0] SCB_SCSIID[0xff] SCB_LUN[0xff] SCB_TAG[0xff]
Aug 30 10:06:12 newfish kernel:   9 SCB_CONTROL[0x0] SCB_SCSIID[0xff] SCB_LUN[0xff] SCB_TAG[0xff]
Aug 30 10:06:12 newfish kernel:  10 SCB_CONTROL[0x0] SCB_SCSIID[0xff] SCB_LUN[0xff] SCB_TAG[0xff]

2019-03-16T00:08:46		SingleFragmentActivity Keep device awake enabled.

2019-03-16T00:10:34		SingleFragmentActivity:  Application starting.

2019-03-16T00:10:34		SingleFragmentActivitySingleFragmentActivity :Shared preferences
com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_CAMERAS->	
[Name:  Front
Name:  http://192.168.4.1:8081
Name:  false
Name:  null
Name:  null
]
com.billdoerr.android.carputer.settings.SettingsActivity.PREF_KEY_NODES->	


		
===============================================================================================

===============================================================================================
public class subActivity extends Activity {

private TextView textView;
private StringBuilder text = new StringBuilder();

protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.text);
    BufferedReader reader = null;

    try {
        reader = new BufferedReader(
            new InputStreamReader(getAssets().open("inputNews.txt")));

        // do reading, usually loop until end of file reading  
        String mLine;
        while ((mLine = reader.readLine()) != null) {
            text.append(mLine);
            text.append('\n');
        }
    } catch (IOException e) {
        Toast.makeText(getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
        e.printStackTrace();
    } finally {
        if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            //log the exception
        }
    }

    TextView output= (TextView) findViewById(R.id.summtext);
    output.setText((CharSequence) text);

 }
}
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
// Calling Application class (see application tag in AndroidManifest.xml)
private GlobalVariables mGlobalVariables;

// Calling Application class (see application tag in AndroidManifest.xml)
mGlobalVariables = (GlobalVariables) getApplicationContext();



//  System logging
FileStorageUtils mSystemLog = new FileStorageUtils();


@Override
protected void startUp() {

	// Calling Application class (see application tag in AndroidManifest.xml)
	mGlobalVariables = (GlobalClass) getApplicationContext();

	//  System logging
	mSystemLog.initializeSystemLog(getApplicationContext(), mGlobalVariables.SYS_LOG);

	mSystemLog.writeSystemLog(TAG + ": Application starting.");
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
//    //  TODO:  Modify to use globalVariable.
//    /**
//     * Retrieve list of camera's that are stored in SharedPreferences as a JSON string.
//     * @param context Context:  Application context.
//     * @return List<Camera>:  Returns List<Camera> of configured cameras.
//     */
//    private static List<Camera> getCamerasFromSharedPrefs(Context context) {
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        Gson gson = new Gson();
//        String json = appSharedPrefs.getString(Camera.PrefKey.PREF_KEY_CAMERAS, "");
//        mCameras = gson.fromJson(json, new TypeToken<ArrayList<Camera>>(){}.getType());
//        if (mCameras == null) {
//            mCameras = new ArrayList<Camera>();
//        }
//        return mCameras;
//    }
//
//    //  TODO:  Modify to use globalVariable.
//    /**
//     * Save list of camera's that are stored in SharedPreferences as a JSON string.
//     * @param context Context:  Application context.
//     */
//    private static void saveCamerasToSharedPrefs(Context context) {
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(mCameras); //tasks is an ArrayList instance variable
//        prefsEditor.putString(Camera.PrefKey.PREF_KEY_CAMERAS, json);
//        prefsEditor.apply();
//    }
//
//    //  TODO:  Modify to use globalVariable.
//    /**
//     * Retrieve list of node's that are stored in SharedPreferences as a JSON string.
//     * @param context Context:  Application context.
//     * @return List<Node>:  Returns List<Node> of configured nodes.
//     */
//    private static List<Node> getNodesFromSharedPrefs(Context context) {
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        Gson gson = new Gson();
//        String json = appSharedPrefs.getString(Node.PrefKey.PREF_KEY_NODES, "");
//        mNodes = gson.fromJson(json, new TypeToken<ArrayList<Node>>(){}.getType());
//        if (mNodes == null) {
//            mNodes = new ArrayList<Node>();
//        }
//        return mNodes;
//    }
//
//    //  TODO:  Modify to use globalVariable.
//    /**
//     * Save list of node's that are stored in SharedPreferences as a JSON string.
//     * @param context Context:  Application context.
//     */
//    private static void saveNodesToSharedPrefs(Context context) {
//        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
//        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(mNodes); //tasks is an ArrayList instance variable
//        prefsEditor.putString(Node.PrefKey.PREF_KEY_NODES, json);
//        prefsEditor.apply();
//    }

===============================================================================================
















 
 