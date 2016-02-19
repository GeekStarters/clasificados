package and.clasificados.com.auxiliares;

import android.content.Context;

import and.clasificados.com.modelo.Usuario;

/**
 * Created by Gabriela Mejia on 18/2/2016.
 */

public class PrefUtils {

    public static void setCurrentUser(Usuario currentUser, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static Usuario getCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        Usuario currentUser = complexPreferences.getObject("current_user_value", Usuario.class);
        return currentUser;
    }

    public static void clearCurrentUser( Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }


}