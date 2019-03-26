package com.billdoerr.android.carputer.utils;

import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Raspberry Pi utilities.
 */
public class NodeUtils {

    private static final String TAG = "NodeUtils";

    private static final int TIMEOUT = 10*1000;     //  Time in milliseconds

    //  Hosts info
    private String mIP;
    private String mPort;
    private String mUser;
    private String mPwd;

    /**
     *
     * @param ip  String: Ip address or hostname of node.
     * @param port String: SSH port.
     * @param user String: SSH username.
     * @param pwd String: SSH password.
     */
    public void initialize(String ip, String port, String user, String pwd) {
        mIP = ip;
        mPort = port;
        mUser = user;
        mPwd = pwd;
    }

    /**
     *
     * @param cmd String: Remove command to be executed.
     * @return String: Result of command being executed.
     * @throws Exception Exception: Exception being thrown.
     */
    public String executeRemoteCommand(String cmd) throws JSchException {
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channelssh = null;
        ByteArrayOutputStream output = null;

        try {
            session = jsch.getSession(mUser, mIP, Integer.parseInt(mPort));
            session.setPassword(mPwd);

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");

            session.setConfig(prop);
            session.connect();
//            session.connect(TIMEOUT);

            // SSH Channel
            channelssh = (ChannelExec) session.openChannel("exec");
            output = new ByteArrayOutputStream();
            channelssh.setOutputStream(output);

            // Execute command
            channelssh.setCommand(cmd);
            channelssh.connect();

            //  Need delay
            Thread.sleep(1000);

        } catch (JSchException e) {
            Log.e(TAG, e.toString());
            throw e;
        } catch (Exception e){
            Log.e(TAG, e.toString());
        } finally{
            if (channelssh != null) {
                channelssh.disconnect();
            }
            session.disconnect();
        }

        if (output != null) {
            Log.d(TAG, "Execute Cmd Results...\n=====================\n" + output.toString());
            return output.toString();
        } else {
            Log.d(TAG, "Execute Cmd Results...\n=====================\n" + "no results!");
            return "";
        }
    }


    /**
     * Performs ping on specified Ip address or hostname.
     * @param ip String: Ip address or hostname of destination.
     * @return String: Results of operation.
     */
    public String ping(String ip) {
     /*
        Usage: ping [-LRUbdfnqrvVaA] [-c count] [-i interval] [-w deadline]
        [-p pattern] [-s packetsize] [-t ttl] [-I interface or address]
        [-M mtu discovery hint] [-S sndbuf]
        [ -T timestamp option ] [ -Q tos ] [hop1 ...] destination
     */
        String cmd = "/system/bin/ping -c 10 " + ip;
        String reply = "";
        Runtime runtime = Runtime.getRuntime();
        try
        {
            String line;
            Process process = runtime.exec(cmd);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            while ((line = br.readLine()) != null) {
                reply = reply + System.getProperty("line.separator") + line;
                Log.i(TAG, reply);
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return reply;

    }

}
