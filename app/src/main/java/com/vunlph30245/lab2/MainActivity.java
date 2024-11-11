package com.vunlph30245.lab2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vunlph30245.lab2.Adapter.SanPhamAdapter;
import com.vunlph30245.lab2.DAO.ToDoDAO;
import com.vunlph30245.lab2.Model.SanPhamModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String TAG = "//====";
    ToDoDAO dao;
    ArrayList<SanPhamModel> listSanPham;
    RecyclerView rcvSanPham;
    SanPhamAdapter adapter;
    EditText edtId, edtTitle, edtContent, edtDate, edtType;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initView();

        dao = new ToDoDAO(this);
        listSanPham = dao.getListSanPham();
        Log.d(TAG, "onCreate: " + listSanPham.size());

        adapter = new SanPhamAdapter(MainActivity.this, listSanPham);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvSanPham.setLayoutManager(linearLayoutManager);
        rcvSanPham.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();
                String date = edtDate.getText().toString().trim();
                String type = edtType.getText().toString().trim();
                if (title.isEmpty() || content.isEmpty() || date.isEmpty()|| type.isEmpty()){
                    Toast.makeText(MainActivity.this, "Vui long them du lieu", Toast.LENGTH_SHORT).show();
                    if (title.isEmpty()){
                        edtTitle.setError("Vui long nhap title");
                    }
                    if (content.isEmpty()){
                        edtContent.setError("Vui long nhap content");
                    }
                    if (date.isEmpty()){
                        edtDate.setError("Vui long nhap date");
                    }
                    if (type.isEmpty()){
                        edtType.setError("Vui long nhap type");
                    }
                }else {
                    SanPhamModel sanPhamModel = new SanPhamModel(1, title, content, date, type, 0);
                    long check = dao.addTodo(sanPhamModel);
                    if (check <0){
                        Toast.makeText(MainActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                    }
                    listSanPham = dao.getListSanPham();
                    adapter = new SanPhamAdapter(MainActivity.this, listSanPham);
                    rcvSanPham.setLayoutManager(linearLayoutManager);
                    rcvSanPham.setAdapter(adapter);
                    reset();
                }
            }
        });
        edtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arrType = {"De", "Trung binh", "Kho"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    }

    private void initView() {
        rcvSanPham = findViewById(R.id.rcvSanPham);
        edtId= findViewById(R.id.edtId);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        edtDate = findViewById(R.id.edtDate);
        edtType = findViewById(R.id.edtType);
        btnAdd = findViewById(R.id.btnAdd);
    }
    public void reset(){
        edtId.setText("");
        edtTitle.setText("");
        edtContent.setText("");
        edtDate.setText("");
        edtType.setText("");

    }


}
