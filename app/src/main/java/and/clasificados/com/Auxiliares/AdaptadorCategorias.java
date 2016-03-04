package and.clasificados.com.auxiliares;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import and.clasificados.com.R;
import and.clasificados.com.common.RoundedTransformation;
import and.clasificados.com.modelo.Clasificado;

/**
 * Created by Gabriela Mejia on 1/2/2016.
 */
public class AdaptadorCategorias extends RecyclerView.Adapter<AdaptadorCategorias.ViewHolder>{


    private final List<Clasificado> items;
    OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Clasificado item = items.get(i);
        if(item.getUrl()==null){
            Picasso.with(viewHolder.itemView.getContext())
                    .load(R.mipmap.ic_launcher)
                    .transform(new RoundedTransformation(15, 0))
                    .fit()
                    .into(viewHolder.imagen);
        }else{
        Picasso.with(viewHolder.itemView.getContext())
                .load(item.getUrl())
                .transform(new RoundedTransformation(15, 0))
                .fit()
                .into(viewHolder.imagen);
        }
        viewHolder.categoria.setText(item.getCategoria());
        viewHolder.nombre.setText(item.getTextoAnuncio());
        viewHolder.precio.setText(item.getPrecio());
    }
}