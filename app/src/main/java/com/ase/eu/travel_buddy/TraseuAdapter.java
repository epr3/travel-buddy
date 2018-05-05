package com.ase.eu.travel_buddy;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

class TraseuAdapter extends RecyclerView.Adapter<TraseuAdapter.ViewHolder> {
    //private List<TraseuPuncte> lista;
    private List<Traseu> lista;

    public TraseuAdapter(List<Traseu> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView photoCard;
        public ImageView imageLocation;
        public TextView denumire, dataStart, dataFinal, distanta;
        public Button setPic;
        public LinearLayout layout;
        public ViewHolder(final View v) {
            super(v);
            photoCard = (CardView) v.findViewById(R.id.cardView);
            imageLocation = v.findViewById(R.id.locationPhoto);
            denumire = v.findViewById(R.id.denumire);
            dataStart = v.findViewById(R.id.dataStart);
            dataFinal = v.findViewById(R.id.dataFinal);
            distanta = v.findViewById(R.id.distanta);
            setPic = v.findViewById(R.id.setPhoto);
            layout = v.findViewById(R.id.recycler_view_row);
        }
    }



    @Override
    public TraseuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.traseu_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TraseuAdapter.ViewHolder holder, final int position) {
        Traseu traseu = lista.get(position);
        //holder.imageLocation.setImageResource(traseu.getPicture().);
        Picasso.get().load(traseu.getPicture()).fit().centerCrop().placeholder( R.drawable.placeholder).into(holder.imageLocation);
        System.out.println("The uri is: "+traseu.getPicture());
        holder.denumire.setText(traseu.getDenumire());
        holder.dataFinal.setText(traseu.getDataFinal().toString());
        holder.dataStart.setText(traseu.getDataStart().toString());
        holder.distanta.setText((traseu.getListaPuncte() != null
                ? traseu.getListaPuncte().size()
                : 0) + " puncte");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Traseu traseu = lista.get(position);
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("listaPuncte", (Serializable) traseu.getListaPuncte());
                v.getContext().startActivity(intent);
            }
        });
        holder.setPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),SetLocationPicture.class);
                i.putExtra("pic",lista.get(position).getDenumire());
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (lista != null ? lista.size(): 0);
    }
}
