package com.vunlph30245.lab2.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vunlph30245.lab2.DAO.ToDoDAO;
import com.vunlph30245.lab2.MainActivity;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = list.get(holder.getAdapterPosition()).getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xoa");
                    builder.setIcon(R.drawable.delete);
                    builder.setMessage("Ban co chac muon xoa");
                    builder.setPositiveButton("Xoa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean check = toDoDAO.removeTodo(id);
                            if (check) {
                                Toast.makeText(context.getApplicationContext(), "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                                list.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            } else {
                                Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                    View view = inflater.inflate(R.layout.custom_dialog, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(view);
                    EditText edtId, edtTitle, edtContent, edtDate, edtType;
                    edtId = view.findViewById(R.id.edtId);
                    edtTitle = view.findViewById(R.id.edtTitle);
                    edtContent = view.findViewById(R.id.edtContent);
                    edtDate = view.findViewById(R.id.edtDate);
                    edtType = view.findViewById(R.id.edtType);

                    //load data
                    edtId.setText(String.valueOf(list.get(position).getId()));
                    edtTitle.setText(list.get(position).getTitle());
                    edtContent.setText(list.get(position).getContent());
                    edtDate.setText(list.get(position).getDate());
                    edtType.setText(list.get(position).getType());

                    edtType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] arrType = {"De", "Trung binh", "Kho"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Vui long chon");
                            builder.setIcon(R.drawable.pencil);
                            builder.setItems(arrType, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    edtType.setText(arrType[which]);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    builder.setTitle("Cap nhat thong tin");
                    builder.setIcon(R.drawable.pencil);
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SanPhamModel sanPhamModel = new SanPhamModel();
                            sanPhamModel.setId(Integer.parseInt(edtId.getText().toString().trim()));
                            sanPhamModel.setTitle(edtTitle.getText().toString().trim());
                            sanPhamModel.setContent(edtContent.getText().toString().trim());
                            sanPhamModel.setDate(edtDate.getText().toString().trim());
                            sanPhamModel.setType(edtType.getText().toString().trim());

                            long check = toDoDAO.updateTodo(sanPhamModel);
                            if (check < 0) {
                                Toast.makeText(context, "Loi", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                            }

                            list.set(position, sanPhamModel);
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
