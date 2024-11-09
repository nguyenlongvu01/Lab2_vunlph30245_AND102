package com.vunlph30245.lab2.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vunlph30245.lab2.DAO.ToDoDAO;
import com.vunlph30245.lab2.Model.SanPhamModel;
import com.vunlph30245.lab2.R;

import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    Context context;
    ArrayList<SanPhamModel> list;
    ToDoDAO toDoDAO;

    public SanPhamAdapter(Context context, ArrayList<SanPhamModel> list) {
        this.context = context;
        this.list = list;
        toDoDAO = new ToDoDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvContent.setText(list.get(position).getContent());
        holder.tvDate.setText(list.get(position).getDate());

        if (list.get(position).getStatus()==1){
            holder.chkTask.setChecked(true);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.chkTask.setChecked(false);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }
        holder.chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = list.get(holder.getAdapterPosition()).getId();
                boolean checkRS = toDoDAO.updateStatus(id, holder.chkTask.isChecked());
                if (checkRS){
                    Toast.makeText(context, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Cap nhat that bai", Toast.LENGTH_SHORT).show();
                }
                list.clear();
                list = toDoDAO.getListSanPham();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvDate;
        CheckBox chkTask;
        ImageView imgUpdate, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
            chkTask = itemView.findViewById(R.id.chkTask);
            imgUpdate = itemView.findViewById(R.id.imgUpdate);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
