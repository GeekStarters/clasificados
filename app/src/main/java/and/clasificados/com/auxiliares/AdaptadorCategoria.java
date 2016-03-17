package and.clasificados.com.auxiliares;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import and.clasificados.com.R;
import and.clasificados.com.common.RoundedTransformation;
import and.clasificados.com.modelo.Clasificado;

/**
 * Created by Gabriela Mejia on 16/3/2016.
 */
public class AdaptadorCategoria extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
private List<Clasificado> data;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

private static final int TYPE_CLASIFICADO = 0;
private static final int TYPE_FOOTER = 1;
    public AdaptadorCategoria(@NonNull List<Clasificado> data, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

@Override
public int getItemViewType(int position) {
        if (data.get(position) instanceof Clasificado) {
        return TYPE_CLASIFICADO;
        } else if (data.get(position) instanceof Footer) {
        return TYPE_FOOTER;
        } else {
        throw new RuntimeException("ItemViewType unknown");
        }
        }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CLASIFICADO) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_categorias, parent, false);
            ViewHolder pvh = new ViewHolder(row, recyclerViewOnItemClickListener);
            return pvh;
        } else {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(row);
            return vh;
        }
    }




@Override
public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof ViewHolder) {
        Clasificado item = (Clasificado) data.get(position);
        ViewHolder paletteViewHolder = (ViewHolder) viewHolder;
        ImageView aux = (ImageView) paletteViewHolder.getImagen();
        if(item.getUrl()==null){
            Picasso.with(viewHolder.itemView.getContext())
                    .load(R.mipmap.ic_launcher)
                    .transform(new RoundedTransformation(15, 0))
                    .fit()
                    .into(aux);
        }else{
            Picasso.with(viewHolder.itemView.getContext())
                    .load(item.getUrl())
                    .transform(new RoundedTransformation(15, 0))
                    .fit()
                    .into(aux);
        }
        paletteViewHolder.getTituloTextView().setText(item.getTextoAnuncio());
        paletteViewHolder.getPrecioTextView().setText(item.getPrecio());
        paletteViewHolder.getCategoriaTextView().setText(item.getCategoria());
        }
        //FOOTER: nothing to do

        }

@Override
public int getItemCount() {
        return data.size();
        }


public static class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    public TextView nombre;
    public TextView precio;
    public ImageView imagen;
    public TextView categoria;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public ViewHolder(View v, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        super(v);
        categoria = (TextView) v.findViewById(R.id.texto_categoria);
        nombre = (TextView) v.findViewById(R.id.texto_anuncio);
        precio = (TextView) v.findViewById(R.id.precio_anuncio);
        imagen = (ImageView) v.findViewById(R.id.item_imagen);
        v.setOnClickListener(this);
    }

    public TextView getTituloTextView() {
        return nombre;
    }

    public TextView getCategoriaTextView() {
        return categoria;
    }

    public TextView getPrecioTextView() {
        return precio;
    }

    public View getImagen() {
        return imagen;
    }


    @Override
    public void onClick(View v) {
        recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
    }
}

public static class FooterViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    private ProgressBar progressBar;

    public FooterViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progress_footer);
    }
}

}