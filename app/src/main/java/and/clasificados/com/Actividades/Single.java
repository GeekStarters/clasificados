package and.clasificados.com.Actividades;

import android.view.Menu;

import com.blunderer.materialdesignlibrary.handlers.ActionBarDefaultHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;

import and.clasificados.com.R;

public class Single extends com.blunderer.materialdesignlibrary.activities.Activity{

    @Override
    protected int getContentView() {
        return R.layout.activity_single;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarDefaultHandler(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single, menu);
        return true;
    }

    @Override
    protected boolean enableActionBarShadow() {
        return false;
    }

}
