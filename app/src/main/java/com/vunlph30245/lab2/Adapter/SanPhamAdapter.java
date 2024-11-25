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

import com.google.firebase.firestore.FirebaseFirestore;
import com.vunlph30245.lab2.Model.SanPhamModel;
import com.vunlph30245.lab2.R;

import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    Context context;
    ArrayList<SanPhamModel> list;
    FirebaseFirestore db;

    public SanPhamAdapter(Context context, ArrayList<SanPhamModel> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SanPhamModel item = list.get(position);
        holder.tvContent.setText(item.getContent());
        holder.tvDate.setText(item.getDate());

        // Hiển thị trạng thái hoàn thành
        if (item.getStatus() == 1) {
            holder.chkTask.setChecked(true);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.chkTask.setChecked(false);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Cập nhật trạng thái công việc
        holder.chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setStatus(isChecked ? 1 : 0);
                db.collection("SanPham").document(item.getId())
                        .update("status", item.getStatus())
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Xóa công việc
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa công việc")
                        .setIcon(R.drawable.delete)
                        .setMessage("Bạn có chắc chắn muốn xóa?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("SanPham").document(item.getId())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                            list.remove(position);
                                            notifyItemRemoved(position);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .create()
                        .show();
            }
        });

        // Cập nhật công việc
        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                View view = inflater.inflate(R.layout.custom_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);

                EditText edtId = view.findViewById(R.id.edtId);
                EditText edtTitle = view.findViewById(R.id.edtTitle);
                EditText edtContent = view.findViewById(R.id.edtContent);
                EditText edtDate = view.findViewById(R.id.edtDate);
                EditText edtType = view.findViewById(R.id.edtType);

                // Load dữ liệu vào dialog
                edtId.setText(item.getId());
                edtTitle.setText(item.getTitle());
                edtContent.setText(item.getContent());
                edtDate.setText(item.getDate());
                edtType.setText(item.getType());

                // Xử lý chọn loại công việc
                edtType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] arrType = {"Dễ", "Trung bình", "Khó"};
                        AlertDialog.Builder typeBuilder = new AlertDialog.Builder(context);
                        typeBuilder.setTitle("Vui lòng chọn")
                                .setItems(arrType, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        edtType.setText(arrType[which]);
                                    }
                                })
                                .create()
                                .show();
                    }
                });

                builder.setTitle("Cập nhật công việc")
                        .setIcon(R.drawable.pencil)
                        .setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Lấy dữ liệu mới
                                item.setTitle(edtTitle.getText().toString().trim());
                                item.setContent(edtContent.getText().toString().trim());
                                item.setDate(edtDate.getText().toString().trim());
                                item.setType(edtType.getText().toString().trim());

                                // Cập nhật vào Firestore
                                db.collection("SanPham").document(item.getId())
                                        .set(item)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                            notifyItemChanged(position);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
