package and.clasificados.com.auxiliares;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import and.clasificados.com.modelo.Mensaje;
import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class AdaptadorMensaje extends RecyclerView.Adapter<AdaptadorMensaje.ViewHolder> implements  View.OnClickListener {


    private final List<Mensaje> items;
    private View.OnClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public ImageView imagen;
        public TextView mensaje;

        public ViewHolder(View v) {
            super(v);
            mensaje = (TextView) v.findViewById(R.id.item_mensaje);
            nombre = (TextView) v.findViewById(R.id.item_usuario);
            imagen = (ImageView) v.findViewById(R.id.item_imagen);
        }
    }


    public AdaptadorMensaje(List<Mensaje> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_mensajes, viewGroup, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null)
            listener.onClick(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Mensaje item = items.get(i);
        Glide.with(viewHolder.itemView.getContext())
                .load(item.getImagen())
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.nombre.setText(item.getNombre());
        viewHolder.mensaje.setText(item.getMensaje());

    }
}