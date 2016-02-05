package and.clasificados.com.auxiliares;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import and.clasificados.com.modelo.Clasificado;
import and.clasificados.com.R;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class AdaptadorCategorias extends RecyclerView.Adapter<AdaptadorCategorias.ViewHolder> implements  View.OnClickListener {


    private final List<Clasificado> items;
    private View.OnClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView precio;
        public ImageView imagen;
        public TextView categoria;

        public ViewHolder(View v) {
            super(v);
            categoria = (TextView) v.findViewById(R.id.texto_categoria);
            nombre = (TextView) v.findViewById(R.id.texto_anuncio);
            precio = (TextView) v.findViewById(R.id.precio_anuncio);
            imagen = (ImageView) v.findViewById(R.id.item_imagen);
        }
    }


    public AdaptadorCategorias(List<Clasificado> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista_categorias, viewGroup, false);
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
        Clasificado item = items.get(i);
        Glide.with(viewHolder.itemView.getContext())
                .load(item.getIdDrawable())
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.categoria.setText(item.getCategoria());
        viewHolder.nombre.setText(item.getTextoAnuncio());
        viewHolder.precio.setText("Q" + item.getPrecio());


    }
}