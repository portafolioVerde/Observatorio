package com.example.usuario.app.myroodent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import static android.app.PendingIntent.getActivity;
import static android.support.v4.content.ContextCompat.startActivity;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AvistAdapter extends RecyclerView.Adapter<AvistAdapter.ViewHolder>{

    private Context contexto;
    private int LEFT_CELL = 1;
    private int RIGHT_CELL = 0;
    private List<ReporteEspecie> reporteEspecie;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{

        void OnItemClick (int position);
    }
    public void setOnItemClickListener (OnItemClickListener listener){

        mListener = listener;

    }

    public AvistAdapter(List<ReporteEspecie> reporteEspecie){
        super();

        this.reporteEspecie = reporteEspecie;

    }
    //
    @Override
    public int getItemViewType(int position) {
        if(position % 2 == 0){

            return RIGHT_CELL;

        }
        else {

            return LEFT_CELL;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if(LEFT_CELL == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_right,parent,false);
        } else {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_left,parent,false);
        }

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.TxtTitulo.setText(reporteEspecie.get(position).getEspecie());
        holder.TxtDetalle.setText(reporteEspecie.get(position).getDireccion());
        holder.TxtDetalle2.setText(reporteEspecie.get(position).getFechaYhora().toString());
        //holder.ImgPhoto.setImageResource(reporteEspecie.get(position).getImage());

    }

    @Override
    public int getItemCount() {

        return reporteEspecie.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TextView TxtTitulo;
        public TextView TxtDetalle;
        public TextView TxtDetalle2;
        //public ImageView ImgPhoto;

        public ViewHolder(View itemView) {

            super(itemView);

            mView = itemView;

            TxtTitulo = (TextView) mView.findViewById(R.id.txtTitulo);
            TxtDetalle = (TextView) mView.findViewById(R.id.txtDetalle);
            TxtDetalle2 = (TextView) mView.findViewById(R.id.txtDetalle2);
            //ImgPhoto = (ImageView) mView.findViewById(R.id.imgPhoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("Mensaje",reporteEspecie.get(getAdapterPosition()).getDoc());

                    Intent intent = new Intent(mView.getContext(), CompleteActivity.class);
                    intent.putExtra("doc", reporteEspecie.get(getAdapterPosition()).getDoc());
                    mView.getContext().startActivity(intent);

                }
            });
        }
    }
}
