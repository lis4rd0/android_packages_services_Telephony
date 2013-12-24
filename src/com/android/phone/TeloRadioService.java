/*
 * Copyright (C) 2012 TeloKang project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.phone;

import java.util.List;
import java.util.Calendar;

import android.app.Service;
<<<<<<< HEAD
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.AlarmManager;
=======
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.UserHandle;
import android.os.IPowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.ContentResolver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.database.ContentObserver;


import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;

public class TeloRadioService extends Service
{
    private static final String TAG = "TeloRadioService";  
    
     /** DEFINES
      * 	* Thank's TheMasterBaron for ACTION_ and NETWORK_MODE_ defines
      **/
<<<<<<< HEAD
    private static final String ACTION_NETWORK_MODE_CHANGED 	= "com.android.internal.telephony.NETWORK_MODE_CHANGED";
    private static final String CHANGE_NETWORK_MODE_PERM 		= "com.android.phone.CHANGE_NETWORK_MODE";
    private static final String ACTION_TELORADIO_2G		 		= "com.android.phone.action.TELORADIO_2G";
    private static final String EXTRA_NETWORK_MODE 				= "networkMode";
    
	private static final int INTERNET_DISABLE 	= 0;
	private static final int INTERNET_WIFI 		= 1;
	private static final int INTERNET_MOBILE 	= 2;
	
	private static final int TELORADIO_PREF_ENABLE 			= 1;
	private static final int TELORADIO_PREF_LTE 			= 2;
	private static final int TELORADIO_PREF_2G_WIFI 		= 3;
	private static final int TELORADIO_PREF_2G_SCREENOFF 	= 4;
	private static final int TELORADIO_PREF_3G_UNLOCK		= 5;
	
    private TeloRadioNetworkReceiver mNetWorkReceiver;
    private TeloRadioLockReceiver mLockReceiver;
    private boolean mLockRunning = false;
    private boolean mCanTimer2G = true;
    private TeloRadioUnLockReceiver mUnLockReceiver;
    private boolean mUnLockRunning = false;
    private TeloRadio2GTimerReceiver m2GTimerReceiver;
    private boolean m2GTimerRunning = false;
    private boolean in2GScreenOff = false;
    private SettingsObserver mSettingsObserver;
    private IPowerManager mPM;
	
	private final boolean mDebug = true;
	private int mTeloRadioNetworkMode = 0;
    private long m2GScreenOffTime = 0L;
=======
    public static final String ACTION_NETWORK_MODE_CHANGED 	= "com.android.internal.telephony.NETWORK_MODE_CHANGED";
    public static final String CHANGE_NETWORK_MODE_PERM 	= "com.android.phone.CHANGE_NETWORK_MODE";
    public static final String EXTRA_NETWORK_MODE 			= "networkMode";
    
    public static final int NETWORK_MODE_GSM_ONLY 		= 1; /* GSM only */
    public static final int NETWORK_MODE_GSM_UMTS 		= 3; /* GSM/WCDMA (auto mode) */
    public static final int NETWORK_MODE_LTE_GSM_WCDMA  = 9; /* LTE,GSM,WCDMA (auto mode) */
    
	public static final int INTERNET_DISABLE 	= 0;
	public static final int INTERNET_WIFI 		= 1;
	public static final int INTERNET_MOBILE 	= 2;
	
	public static final int TELORADIO_PREF_ENABLE 		= 1;
	public static final int TELORADIO_PREF_LTE 			= 2;
	public static final int TELORADIO_PREF_2G_WIFI 		= 3;
	public static final int TELORADIO_PREF_2G_SCREENOFF = 4;
	public static final int TELORADIO_PREF_3G_UNLOCK	= 5;
	
    private TeloRadioNetworkReceiver mNetWorkReceiver;
    private TeloRadioTimerReceiver mTimerReceiver;
    private boolean mTimerRunning = false;
    private TeloRadioLockReceiver mLockReceiver;
    private boolean mLockRunning = false;
    private TeloRadioUnLockReceiver mUnLockReceiver;
    private boolean mUnLockRunning = false;
    private SettingsObserver mSettingsObserver;
    private IPowerManager mPM;
    private Context mContext;
	
	private static final boolean mDebug = true;
	private int mNetworkMode = NETWORK_MODE_GSM_UMTS;
    private long m2GScreenOffTime = 600000L;
    private long mTimeInScreenOff = 0L;
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
	
	/**
     * 	SETTINGS OBSERVER
     * 		* void observe()
     * 		* public void onChange(boolean selfChange)
     **/
     
    private final class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.TELO_RADIO_2G_WIFI), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.TELO_RADIO_LTE), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.TELO_RADIO_2G_SCREENOFF), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.TELO_RADIO_2G_SCREENOFF_TIME), false, this);
            resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.TELO_RADIO_GO3G_UNLOCK), false, this);
        }

        @Override
        public void onChange(boolean selfChange) {
			UpdatePrefs();
		}
	}

	private void UpdatePrefs() {
		ContentResolver resolver = getContentResolver();
		
<<<<<<< HEAD
		switch (isConnectedTo(this)) {
			case INTERNET_WIFI:
				if (getBooleanPrefs(TELORADIO_PREF_2G_WIFI))
					changeNetworkMode(Phone.NT_MODE_GSM_ONLY, null, false);
				else if (getBooleanPrefs(TELORADIO_PREF_LTE))
					changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA, null, false);
				else
					changeNetworkMode(Phone.NT_MODE_GSM_UMTS, null, false);
				break;
			case INTERNET_MOBILE:
				if (getBooleanPrefs(TELORADIO_PREF_LTE))
					changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA, null, false);
				else
					changeNetworkMode(Phone.NT_MODE_GSM_UMTS, null, false);
=======
		switch (isConnectedTo(mContext)) {
			case INTERNET_WIFI:
				if (getBooleanPrefs(TELORADIO_PREF_2G_WIFI))
					changeNetworkMode(Phone.NT_MODE_GSM_ONLY);
				else if (getBooleanPrefs(TELORADIO_PREF_LTE))
					changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA);
				else
					changeNetworkMode(Phone.NT_MODE_GSM_UMTS);
				break;
			case INTERNET_MOBILE:
				if (getBooleanPrefs(TELORADIO_PREF_LTE))
					changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA);
				else
					changeNetworkMode(Phone.NT_MODE_GSM_ONLY);
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
				break;
		}

		if (getBooleanPrefs(TELORADIO_PREF_2G_SCREENOFF)) {
<<<<<<< HEAD
			m2GScreenOffTime = Settings.System.getLong(resolver, Settings.System.TELO_RADIO_2G_SCREENOFF_TIME, 600000L);
=======
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
			registerLockReceiver();
			registerUnLockReceiver();
		} else {
			unregisterLockReceiver();
			unregisterUnLockReceiver();
<<<<<<< HEAD
=======
			unregisterTimerReceiver();
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
		}
	}

	/**
     * 	PREFERENCES FUNCTIONS
     * 		* boolean getBooleanPrefs(int mPref)
     **/	
	
	public boolean getBooleanPrefs(int mPref) {
		switch (mPref) {
			case TELORADIO_PREF_ENABLE:
				return Settings.System.getIntForUser(getContentResolver(), Settings.System.TELO_RADIO_ENABLED, 0, UserHandle.USER_CURRENT) == 1;
			case TELORADIO_PREF_LTE:
				return Settings.System.getIntForUser(getContentResolver(), Settings.System.TELO_RADIO_LTE, 0, UserHandle.USER_CURRENT) == 1;
			case TELORADIO_PREF_2G_WIFI:
				return Settings.System.getIntForUser(getContentResolver(), Settings.System.TELO_RADIO_2G_WIFI, 0, UserHandle.USER_CURRENT) == 1;
			case TELORADIO_PREF_2G_SCREENOFF:
				return Settings.System.getIntForUser(getContentResolver(), Settings.System.TELO_RADIO_2G_SCREENOFF, 0, UserHandle.USER_CURRENT) == 1;
			case TELORADIO_PREF_3G_UNLOCK:
				return Settings.System.getIntForUser(getContentResolver(), Settings.System.TELO_RADIO_GO3G_UNLOCK, 0, UserHandle.USER_CURRENT) == 1;
		}
		return false;
	}

	/**
     * 	GENERAL FUNCTIONS
     * 		* void TeloRadioServiceState(Context context, boolean start)
     **/
     	
	public static void TeloRadioServiceState(Context context, boolean start) {
        Intent intent = new Intent(context, TeloRadioService.class);
        
        if (start)
			context.startService(intent);
		else
			context.stopService(intent);
	}	
	
	/**
     * 	SERVICE FUNCTIONS
     * 		* void onCreate()
     * 		* void onDestroy()
     **/
        
    @Override
    public void onCreate() {
        super.onCreate(); 
         
		if (mDebug) Log.i(TAG, "TeloRadio: SERVICE START");
		
<<<<<<< HEAD
=======
		mContext = this;
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
        mPM = IPowerManager.Stub.asInterface(ServiceManager.getService(Context.POWER_SERVICE));
		
		IntentFilter mConnectivityIntent = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		mNetWorkReceiver = new TeloRadioNetworkReceiver();
		registerReceiver(mNetWorkReceiver, mConnectivityIntent);
		
		mSettingsObserver = new SettingsObserver(new Handler());
		mSettingsObserver.observe();
		UpdatePrefs();
    }    
	
	@Override
	public void onDestroy() {
		if (mDebug) Log.i(TAG, "TeloRadio: SERVICE STOP");
		unregisterReceiver(mNetWorkReceiver);
		unregisterLockReceiver();
		unregisterUnLockReceiver();
<<<<<<< HEAD
		unregister2GTimerReceiver();
=======
		unregisterTimerReceiver();	
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
		getContentResolver().unregisterContentObserver(mSettingsObserver);
	}	
	
	/**
     * 	CLASS HANDLER AND FUNCTIONS
     * 		* void handleGetPreferredNetworkTypeResponse(Message msg)
     * 		* void handleSetPreferredNetworkTypeResponse(Message msg)
     **/	
     
    private class MyHandler extends Handler {
        private static final int MESSAGE_GET_PREFERRED_NETWORK_TYPE = 0;
        private static final int MESSAGE_SET_PREFERRED_NETWORK_TYPE = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_GET_PREFERRED_NETWORK_TYPE:
                    handleGetPreferredNetworkTypeResponse(msg);
                    break;

                case MESSAGE_SET_PREFERRED_NETWORK_TYPE:
                    handleSetPreferredNetworkTypeResponse(msg);
                    break;
            }
        }
        
        private void handleGetPreferredNetworkTypeResponse(Message msg) {
			Intent intent = new Intent(ACTION_NETWORK_MODE_CHANGED);
<<<<<<< HEAD
			intent.putExtra(EXTRA_NETWORK_MODE, mTeloRadioNetworkMode);
=======
			intent.putExtra(EXTRA_NETWORK_MODE, mNetworkMode);
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
			mPhone.getContext().sendBroadcast(intent, CHANGE_NETWORK_MODE_PERM);
        }
        
		private void handleSetPreferredNetworkTypeResponse(Message msg) {
            getPhone().getPreferredNetworkType(obtainMessage(MESSAGE_GET_PREFERRED_NETWORK_TYPE));
        }        
	}
    
    /**
     * 	JOB FUNCTIONS IN CONNECTION AND NETWORK RECEIVER
     * 		* int isConnectedTo(Context context) 
<<<<<<< HEAD
     * 		* int getNetworkMode()
=======
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
     * 		* void changeNetworkMode(int modemNetworkMode)
     * 		* boolean canChangeNetworkMode()
     **/
     
    public class TeloRadioNetworkReceiver extends BroadcastReceiver {
		@Override
        public void onReceive(Context context, Intent intent) {
<<<<<<< HEAD
			if (!getBooleanPrefs(TELORADIO_PREF_ENABLE) || !canChangeNetworkMode(context))
=======
			if (!getBooleanPrefs(TELORADIO_PREF_ENABLE))
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
				return;

            switch(isConnectedTo(context)) {
				case INTERNET_WIFI:
					if (getBooleanPrefs(TELORADIO_PREF_2G_WIFI))
<<<<<<< HEAD
						changeNetworkMode(Phone.NT_MODE_GSM_ONLY, context, false);
					break;
				case INTERNET_MOBILE:
					if (getBooleanPrefs(TELORADIO_PREF_LTE))
						changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA, context, true);
					else
						changeNetworkMode(Phone.NT_MODE_GSM_UMTS, context, true);
=======
						changeNetworkMode(Phone.NT_MODE_GSM_ONLY);
					break;
				case INTERNET_MOBILE:
					if (getBooleanPrefs(TELORADIO_PREF_LTE))
						changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA);
					else
						changeNetworkMode(Phone.NT_MODE_GSM_UMTS);
					break;
				case INTERNET_DISABLE:
					if (mDebug) Log.i(TAG, "TeloRadio: NETWORK RECEIVER not wifi or data mobile");
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
					break;
			}
        }
    }
    
    private int isConnectedTo(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		if (networkInfo == null) {
			return INTERNET_DISABLE;
		} else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return INTERNET_WIFI;
		} else if (networkInfo != null) {
			return INTERNET_MOBILE;
		}
		return INTERNET_DISABLE;
	}
<<<<<<< HEAD
    
    private int getNetworkMode() {
		TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getNetworkType();
	}
    	
	private void changeNetworkMode(int modemNetworkMode, Context context, boolean restriction) {
		if (restriction && !canChangeNetworkMode(context))
			return;
			
		if (getNetworkMode() == modemNetworkMode)
			return;
		
		mTeloRadioNetworkMode = modemNetworkMode;
		
		if (mDebug) Log.i(TAG, "TeloRadio: CHANGE NETWORK MODE - New mode: " + mTeloRadioNetworkMode + ".");
=======
    	
	private void changeNetworkMode(int modemNetworkMode) {
		mNetworkMode = modemNetworkMode;
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
		getPhone().setPreferredNetworkType(modemNetworkMode, getHandler().obtainMessage(MyHandler.MESSAGE_SET_PREFERRED_NETWORK_TYPE));
    } 
    
    private boolean canChangeNetworkMode(Context context) {
<<<<<<< HEAD
		if (getNetworkMode() == Phone.NT_MODE_GSM_ONLY && isConnectedTo(context) == INTERNET_WIFI && getBooleanPrefs(TELORADIO_PREF_2G_WIFI))
			return false;
			
		if (in2GScreenOff)
=======
		if (isConnectedTo(context) == INTERNET_WIFI && getBooleanPrefs(TELORADIO_PREF_2G_WIFI))
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
			return false;
			
		return true;
	}
<<<<<<< HEAD
  
    /**
     * 	2GTIMER RECEIVER
     * 		* void onReceive(Context context, Intent intent)
     * 		* void register2GTimerReceiver()
     * 		* void unregister2GTimerReceiver()
     **/
       
	private class TeloRadio2GTimerReceiver extends BroadcastReceiver {
		
        @Override
        public void onReceive(Context context, Intent intent) {
			if (mDebug) Log.i(TAG, "TeloRadio: 2GTIMER execute");
			
			in2GScreenOff = true;
			changeNetworkMode(Phone.NT_MODE_GSM_ONLY, context, false);
        }
    };
       
    private void register2GTimerReceiver() {
		if (m2GTimerRunning)
			return;
			
		m2GTimerReceiver = new TeloRadio2GTimerReceiver();
        IntentFilter filter = new IntentFilter(ACTION_TELORADIO_2G);
        registerReceiver(m2GTimerReceiver, filter);
        m2GTimerRunning = true;
    }
    
    private void unregister2GTimerReceiver() {
		if (!m2GTimerRunning)
			return;
			
		m2GTimerRunning = false;
        unregisterReceiver(m2GTimerReceiver);
    }
             
    /**
     * 	LOCK RECEIVER
     * 		* void onReceive(Context context, Intent intent)
     * 		* void updateTimerTo2G()
=======
         
    /**
     * 	TIMER RECEIVER
     * 		* void onReceive(Context context, Intent intent)
     * 		* void registerTimerReceiver()
     * 		* void unregisterTimerReceiver()
     * 		* boolean screenOff2GFinish()
     **/
     
    public class TeloRadioTimerReceiver extends BroadcastReceiver {    
        @Override
        public void onReceive(Context context, Intent intent) {
			if (mDebug) Log.i(TAG, "TeloRadio: TIMER RECEIVER");
			if (!screenOff2GFinish())
				return;
				
			if (mDebug) Log.i(TAG, "TeloRadio: TIMER RECEIVER change 2G");
			changeNetworkMode(NETWORK_MODE_GSM_ONLY);
			unregisterTimerReceiver();
        }
    };
    
    private void registerTimerReceiver() {
		if (mTimerRunning)
			return;
			
		mTimerReceiver = new TeloRadioTimerReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		registerReceiver(mTimerReceiver, filter);
		mTimerRunning = true;
	}
    
	private void unregisterTimerReceiver() {
		if (!mTimerRunning)
			return;
			
		mTimerRunning = false;
		unregisterReceiver(mTimerReceiver);
    }
    
    private boolean screenOff2GFinish() {
		m2GScreenOffTime = Settings.System.getLong(getContentResolver(), Settings.System.TELO_RADIO_2G_SCREENOFF_TIME, 600000L);
		return mTimeInScreenOff + m2GScreenOffTime < System.currentTimeMillis();
	}
    
    /**
     * 	LOCK RECEIVER
     * 		* void onReceive(Context context, Intent intent)
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
     * 		* void registerLockReceiver()
     * 		* void unregisterLockReceiver()
     **/
    
	public class TeloRadioLockReceiver extends BroadcastReceiver {
		
        @Override
        public void onReceive(Context context, Intent intent) {
			if (mDebug) Log.i(TAG, "TeloRadio: LOCK RECEIVER execute");
			
<<<<<<< HEAD
			if (!canChangeNetworkMode(context))
				return;
			
			updateTimerTo2G();
        }
    };
    
	private void updateTimerTo2G() {
		if (!mCanTimer2G)
			return;
		register2GTimerReceiver();
		
		if (mDebug) Log.i(TAG, "TeloRadio: SCREENOFF: Change to 2G in " + m2GScreenOffTime + " ms");
        AlarmManager am = (AlarmManager)this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ACTION_TELORADIO_2G);
        PendingIntent pi = PendingIntent.getBroadcast(this.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            am.cancel(pi);
        } catch (Exception e) {
        }
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis() + m2GScreenOffTime);
        am.set(AlarmManager.RTC, time.getTimeInMillis(), pi);
        
		mCanTimer2G = false;
    }
       
=======
			if (!getBooleanPrefs(TELORADIO_PREF_2G_SCREENOFF))
				return;
				
			mTimeInScreenOff = System.currentTimeMillis();
			registerTimerReceiver();	
        }
    };
    
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
    private void registerLockReceiver() {
		if (mLockRunning)
			return;
			
		mLockReceiver = new TeloRadioLockReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mLockReceiver, filter);
        mLockRunning = true;
    }
    
    private void unregisterLockReceiver() {
		if (!mLockRunning)
			return;
			
		mLockRunning = false;
        unregisterReceiver(mLockReceiver);
    }
            
    /**
     * 	UNLOCK RECEIVER
     * 		* void onReceive(Context context, Intent intent)
<<<<<<< HEAD
     * 		* void cancelTimerTo2G()
=======
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
     * 		* void registerUnLockReceiver()
     * 		* void unregisterUnLockReceiver()
     * 		* boolean isScreenOn()
     **/
    
	public class TeloRadioUnLockReceiver extends BroadcastReceiver {
		
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!isScreenOn())
				return;
<<<<<<< HEAD
			if (mDebug) Log.i(TAG, "TeloRadio: UNLOCK RECEIVER execute");
			
			in2GScreenOff = false;
			cancelTimerTo2G();
				
			if (!getBooleanPrefs(TELORADIO_PREF_3G_UNLOCK)) {
				if (mDebug) Log.i(TAG, "TeloRadio: UNLOCK RECEIVER change 3G/LTE when screen lock");
				if (getBooleanPrefs(TELORADIO_PREF_LTE))
					changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA, context, true);
				else
					changeNetworkMode(Phone.NT_MODE_GSM_UMTS, context, true);
			} else if (action.equals(Intent.ACTION_USER_PRESENT)) {
					if (mDebug) Log.i(TAG, "TeloRadio: UNLOCK RECEIVER change 3G/LTE when screen unlock");
					if (getBooleanPrefs(TELORADIO_PREF_LTE))
						changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA, context, true);
					else
						changeNetworkMode(Phone.NT_MODE_GSM_UMTS, context, true);					
			}
        }
    };
    
    private void cancelTimerTo2G() {
		if (mCanTimer2G)
			return;
			
        AlarmManager am = (AlarmManager)this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ACTION_TELORADIO_2G);
        PendingIntent pi = PendingIntent.getBroadcast(this.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            am.cancel(pi);
        } catch (Exception e) {
        }
        unregister2GTimerReceiver();
		mCanTimer2G = true;
    }
        
=======
							
			if (mDebug) Log.i(TAG, "TeloRadio: UNLOCK RECEIVER execute");
			unregisterTimerReceiver();	
			
			if (!getBooleanPrefs(TELORADIO_PREF_2G_SCREENOFF))
				return;
				
			if (!getBooleanPrefs(TELORADIO_PREF_3G_UNLOCK) && canChangeNetworkMode(context)) {
				if (mDebug) Log.i(TAG, "TeloRadio: UNLOCK RECEIVER change 3G/LTE when screen lock");
				if (getBooleanPrefs(TELORADIO_PREF_LTE))
					changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA);
				else
					changeNetworkMode(Phone.NT_MODE_GSM_UMTS);
			}
				
			if (action.equals(Intent.ACTION_USER_PRESENT))
				if (getBooleanPrefs(TELORADIO_PREF_3G_UNLOCK) && canChangeNetworkMode(context)) {
					if (mDebug) Log.i(TAG, "TeloRadio: UNLOCK RECEIVER change 3G/LTE when screen unlock");
					if (getBooleanPrefs(TELORADIO_PREF_LTE))
						changeNetworkMode(Phone.NT_MODE_LTE_GSM_WCDMA);
					else
						changeNetworkMode(Phone.NT_MODE_GSM_UMTS);					
				}
        }
    };
    
>>>>>>> b14fcdb... [3/3]TeloRadio: packages_services_Telephony
    private void registerUnLockReceiver() {
		if (mUnLockRunning)
			return;
			
		mUnLockReceiver = new TeloRadioUnLockReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mUnLockReceiver, filter);
        mUnLockRunning = true;
    }
    
    private void unregisterUnLockReceiver() {
		if (!mUnLockRunning)
			return;
			
		mUnLockRunning = false;
        unregisterReceiver(mUnLockReceiver);
    }  
    
    private boolean isScreenOn() {
        try {
            return mPM.isScreenOn();
        } catch (RemoteException e) {
        }
        return false;
    }
                  
    /**
     * 	POINTER FUNCTIONS
     * 		* TeloRadioService getService()
     * 		* Phone getPhone()
     * 		* MyHandler getHandler()
     **/
     
	final IBinder mBinder = new LocalBinder();
	
	public class LocalBinder extends Binder {
		TeloRadioService getService() {
			return TeloRadioService.this;
		}
	}
        
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}	
			
	private Phone mPhone;
    private MyHandler mHandler;

    private Phone getPhone() {
        if (mPhone == null) {
            mPhone = PhoneFactory.getDefaultPhone();
        }
        return mPhone;
    }

    private MyHandler getHandler() {
        if (mHandler == null) {
            mHandler = new MyHandler();
        }
        return mHandler;
    }
}
