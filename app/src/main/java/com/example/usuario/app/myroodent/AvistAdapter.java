package com.example.usuario.app.myroodent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class AvistAdapter extends RecyclerView.Adapter<AvistAdapter.ViewHolder>{

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
        holder.TxtDetalle2.setText(reporteEspecie.get(position).getFecha());
        holder.TxtDetalle3.setText(reporteEspecie.get(position).getHora());

        /*if(holder.TxtTitulo.getText().equals("Mamifero")){
            holder.ImgPhoto.setImageResource(R.drawable.mamifero_activo);
        }else{
            if(holder.TxtTitulo.getText().equals("Ave")){
                holder.ImgPhoto.setImageResource(R.drawable.aves_activo);
            }else{
                if(holder.TxtTitulo.getText().equals("Anfibio")){
                    holder.ImgPhoto.setImageResource(R.drawable.anfibio_activo);
                }else{
                    if (holder.TxtTitulo.getText().equals("Reptil")){
                        holder.ImgPhoto.setImageResource(R.drawable.reptil_activo);
                    }
                }
            }

        }*/


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
        public TextView TxtDetalle3;
        public ImageView ImgPhoto;

        public ViewHolder(View itemView) {

            super(itemView);

            mView = itemView;

            TxtTitulo = (TextView) mView.findViewById(R.id.txtTitulo);
            TxtDetalle = (TextView) mView.findViewById(R.id.txtDetalle);
            TxtDetalle2 = (TextView) mView.findViewById(R.id.txtDetalle2);
            TxtDetalle3 = (TextView) mView.findViewById(R.id.txtDetalle3);
            ImgPhoto = (ImageView) mView.findViewById(R.id.imgPhoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getContext(), RegistrosActivity.class);
                    intent.putExtra("dac", reporteEspecie.get(getAdapterPosition()).getDoc());
                    mView.getContext().startActivity(intent);
                }
            });
        }
    }
}
