package and.clasificados.com.layer.util;

import android.app.Activity;

import com.layer.sdk.LayerClient;
import com.layer.sdk.listeners.LayerAuthenticationListener;

/**
 * AuthenticationProvider implementations authenticate users with backend "Identity Providers."
 *
 * @param <Tcredentials> Session credentials for this AuthenticationProvider used to resume an
 *                       authenticated session.
 */
public interface AuthenticationProvider<Tcredentials> extends LayerAuthenticationListener.BackgroundThread.Weak {

    /**
     * Sets this AuthenticationProvider's credentials.  Credentials should be cached to handle
     * future authentication challenges.  When `credentials` is `null`, the cached credentials
     * should be cleared.
     *
     * @param credentials Credentials to cache.
     * @return This AuthenticationProvider.
     */
    AuthenticationProvider<Tcredentials> setCredentials(Tcredentials credentials);


    /**
     * Returns `true` if this AuthenticationProvider has cached credentials, or `false` otherwise.
     *
     * @return `true` if this AuthenticationProvider has cached credentials, or `false` otherwise.
     */
    boolean hasCredentials();

    /**
     * Sets the authentication callback for reporting authentication success and failure.
     *
     * @param callback Callback to receive authentication success and failure.
     * @return This AuthenticationProvider.
     */
    AuthenticationProvider<Tcredentials> setCallback(Callback callback);

    /**
     * Routes the user to a login screen if required.  If routing, return `true` and start the
     * desired login Activity.
     *
     * @param layerClient
     * @param layerAppId
     * @param from
     * @return
     */
    boolean routeLogin(LayerClient layerClient, String layerAppId, Activity from);

    /**
     * Callback for handling authentication success and failure.
     */
    interface Callback {
        void onSuccess(AuthenticationProvider provider, String userId);

        void onError(AuthenticationProvider provider, String error);
    }

    /**
     * Unified Log class used by Atlas Messenger classes that maintains similar signatures to
     * `android.util.Log`. Logs are tagged with `Atlas`.
     */
    class Log {
        public static final String TAG = "LayerAtlasMsgr";

        // Makes IDE auto-completion easy
        public static final int VERBOSE = android.util.Log.VERBOSE;
        public static final int DEBUG = android.util.Log.DEBUG;
        public static final int INFO = android.util.Log.INFO;
        public static final int WARN = android.util.Log.WARN;
        public static final int ERROR = android.util.Log.ERROR;

        private static volatile boolean sAlwaysLoggable = false;

        /**
         * Returns `true` if the provided log level is loggable either through environment options or
         * a previous call to setAlwaysLoggable().
         *
         * @param level Log level to check.
         * @return `true` if the provided log level is loggable.
         * @see #setAlwaysLoggable(boolean)
         */
        public static boolean isLoggable(int level) {
            return sAlwaysLoggable || android.util.Log.isLoggable(TAG, level);
        }

        public static void setAlwaysLoggable(boolean alwaysOn) {
            sAlwaysLoggable = alwaysOn;
        }

        public static void v(String message) {
            android.util.Log.v(TAG, message);
        }

        public static void v(String message, Throwable error) {
            android.util.Log.v(TAG, message, error);
        }

        public static void d(String message) {
            android.util.Log.d(TAG, message);
        }

        public static void d(String message, Throwable error) {
            android.util.Log.d(TAG, message, error);
        }

        public static void i(String message) {
            android.util.Log.i(TAG, message);
        }

        public static void i(String message, Throwable error) {
            android.util.Log.i(TAG, message, error);
        }

        public static void w(String message) {
            android.util.Log.w(TAG, message);
        }

        public static void w(String message, Throwable error) {
            android.util.Log.w(TAG, message, error);
        }

        public static void w(Throwable error) {
            android.util.Log.w(TAG, error);
        }

        public static void e(String message) {
            android.util.Log.e(TAG, message);
        }

        public static void e(String message, Throwable error) {
            android.util.Log.e(TAG, message, error);
        }
    }
}
