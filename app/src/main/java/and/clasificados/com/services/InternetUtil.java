package and.clasificados.com.services;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;

public class InternetUtil {

	// CHECK FOR INTERNET METHOD

	public static boolean isInternetOn(Context context) {

		// Log.d(TAG, "In isInternetOn method");

		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// ARE WE CONNECTED TO THE NET

		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED

		||

		connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING

		||

		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING

		||

		connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

			return true;

		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED

				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

			return false;
		}
		return false;
	}

	/**
	 * Display error dialog
	 * 
	 * @param context
	 * @param isGoBack
	 *            - if true go to the previous screen when clicked on OK
	 */
	@SuppressWarnings("deprecation")
	public static void showErrorDialog(final Context context,
			final boolean isGoBack) {

		String alertMessage = "Compruebe su conexión a Internet.";

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setTitle("Error de red");
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				if (isGoBack) {
					dialog.cancel();
				}
				return;
			}
		});

		alertDialog.show();
	}

}
