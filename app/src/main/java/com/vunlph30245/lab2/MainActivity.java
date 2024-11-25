package com.vunlph30245.lab2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.vunlph30245.lab2.Adapter.SanPhamAdapter;
import com.vunlph30245.lab2.Model.SanPhamModel;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String TAG = "//====";
    FirebaseFirestore database; // Biến toàn cục Firestore
    ArrayList<SanPhamModel> listSanPham = new ArrayList<>();
    RecyclerView rcvSanPham;
    SanPhamAdapter adapter;
    EditText edtId, edtTitle, edtContent, edtDate, edtType;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo view
        initView();

        // Khởi tạo Firebase Firestore
        database = FirebaseFirestore.getInstance();

        // Lấy danh sách dữ liệu từ Firestore
        loadDataFromFirestore();

        listenFirebaseFirestore();

        // Thêm sự kiện cho nút thêm mới
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();
                String date = edtDate.getText().toString().trim();
                String type = edtType.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty() || date.isEmpty() || type.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng thêm dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo ID ngẫu nhiên
                String id = UUID.randomUUID().toString();
                SanPhamModel sanPham = new SanPhamModel(id, title, content, date, type, 0);

                // Ghi vào Firestore
                database.collection("SanPham") // Tên collection
                        .document(id) // Sử dụng ID làm document
                        .set(sanPham.convertToHashMap())
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            reset();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Lỗi khi thêm dữ liệu vào Firestore: ", e);
                            Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        });
            }
        });


        // Hiển thị danh sách loại (popup dialog)
        edtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arrType = {"Dễ", "Trung bình", "Khó"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Vui lòng chọn");
                builder.setItems(arrType, (dialog, which) -> edtType.setText(arrType[which]));
                builder.create().show();
            }
        });
    }

    private void initView() {
        rcvSanPham = findViewById(R.id.rcvSanPham);
        edtId = findViewById(R.id.edtId);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        edtDate = findViewById(R.id.edtDate);
        edtType = findViewById(R.id.edtType);
        btnAdd = findViewById(R.id.btnAdd);

        // Setup RecyclerView
        adapter = new SanPhamAdapter(this, listSanPham);
        rcvSanPham.setLayoutManager(new LinearLayoutManager(this));
        rcvSanPham.setAdapter(adapter);
    }

    private void reset() {
        edtId.setText("");
        edtTitle.setText("");
        edtContent.setText("");
        edtDate.setText("");
        edtType.setText("");
    }

    private void loadDataFromFirestore() {
        database.collection("SanPham")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<SanPhamModel> listSanPham = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        SanPhamModel sanPham = document.toObject(SanPhamModel.class); // Sử dụng constructor mặc định
                        listSanPham.add(sanPham);
                    }
                    adapter = new SanPhamAdapter(MainActivity.this, listSanPham);
                    rcvSanPham.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void listenFirebaseFirestore() {
        // Lắng nghe sự thay đổi trong collection "SanPham"
        database.collection("SanPham")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        // Log lỗi nếu có vấn đề trong việc lắng nghe
                        Log.e(TAG, "Lỗi khi lắng nghe Firestore: ", error);
                        return;
                    }

                    if (querySnapshot != null) {
                        // Xóa dữ liệu cũ và cập nhật dữ liệu mới
                        listSanPham.clear();
                        listSanPham.addAll(querySnapshot.toObjects(SanPhamModel.class));

                        // Cập nhật RecyclerView
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "Dữ liệu đã thay đổi. Số lượng mới: " + listSanPham.size());
                    }
                });
    }

}
