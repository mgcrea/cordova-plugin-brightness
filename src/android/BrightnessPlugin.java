package org.apache.cordova.plugin.Brightness;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.provider.Settings;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.view.Window;

/**
 * @author Evgeniy Lukovsky
 *
 */
public class BrightnessPlugin extends CordovaPlugin {
	public enum Action{
		setBrightness,
		getBrightness,
		setKeepScreenOn
	}
	
	private class SetTask implements Runnable{
		private Activity target = null;
		private LayoutParams lp = null;
		@Override
		public void run() {
			target.getWindow().setAttributes(lp);
		}
		public void setParams(Activity act, LayoutParams params){
			this.target = act;
			this.lp = params;
		}
	}

        private class KeepOnTask implements Runnable{
                private Window win = null;
                private boolean state = false;
                @Override
                public void run() {
                        if(state){
                                win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        } else {
                                win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        }
                }
                public void setParams(Window win, boolean state){
                        this.win = win;
                        this.state = state;
                }
        }


	/* (non-Javadoc)
	 * @see org.apache.cordova.CordovaPlugin#execute(java.lang.String, org.json.JSONArray, org.apache.cordova.CallbackContext)
	 */
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		System.out.println("plugin has been started");
		boolean result = false;

		switch(Action.valueOf(action)){
		case setBrightness: result = true;
			setBrightness(args, callbackContext);
			break;
		case getBrightness: result = true;
			getBrightness(args, callbackContext);
			break;
		case setKeepScreenOn: result = true;
			setKeepScreenOn(args, callbackContext);
			break;
		}
		return result;
	}

	/**
	 * @param args
	 * @param callbackContext
	 * @return
	 */
	private boolean setBrightness(JSONArray args, CallbackContext callbackContext) {
		try {
			Activity activity = cordova.getActivity();
			WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
			String value = args.getString(0);
			double brightness = Double.parseDouble(value);
			layoutParams.screenBrightness = (float) brightness;
			SetTask task = new SetTask();
			task.setParams(activity, layoutParams);
			activity.runOnUiThread(task);
			callbackContext.success("OK");

		} catch (NullPointerException e) {
			System.out.println("Null pointer exception");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		} catch (JSONException e) {
			System.out.println("JSONException exception");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
		System.out.println("All went fine.");
		return true;
	}

	/**
	 * @param args
	 * @param callbackContext
	 * @return
	 */
	private boolean getBrightness(JSONArray args, CallbackContext callbackContext) {
		try {
			Activity activity = cordova.getActivity();
			WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
			Double brightness = (double) layoutParams.screenBrightness;
			if (brightness < 0.0) {
				int systemBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, -1);
				if (systemBrightness >= 0) {
					brightness = systemBrightness / 255.0;
				}
			}

			callbackContext.success(brightness.toString());

		} catch (NullPointerException e) {
			System.out.println("Null pointer exception");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
		System.out.println("All went fine.");
		return true;
	}
	/**
	 * @param args
	 * @param callbackContext
	 * @return
	 */
	private boolean setKeepScreenOn(JSONArray args, CallbackContext callbackContext){
		try {
			boolean value = args.getBoolean(0);
			Activity activity = cordova.getActivity();
			KeepOnTask task = new KeepOnTask();
                        task.setParams(activity.getWindow(), value);
                        activity.runOnUiThread(task);
			callbackContext.success("OK");

		} catch (NullPointerException e) {
			System.out.println("Null pointer exception");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		} catch (JSONException e) {
			System.out.println("JSONException");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
		System.out.println("All went fine.");
		return true;
	}
}
