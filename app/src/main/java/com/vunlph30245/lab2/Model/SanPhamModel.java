package com.vunlph30245.lab2.Model;

import java.util.HashMap;

public class SanPhamModel {
    private String id;
    private String title;
    private String content;
    private String date;
    private String type;
    private int status;

    // Constructor mặc định (bắt buộc)
    public SanPhamModel() {
    }

    // Constructor đầy đủ
    public SanPhamModel(String id, String title, String content, String date, String type, int status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.type = type;
        this.status = status;
    }

    // Các getter và setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



// Chuyển đối tượng sang HashMap để lưu vào Firestore
    public HashMap<String, Object> convertToHashMap() {
        HashMap<String, Object> work = new HashMap<>();
        work.put("id", id);  // Sử dụng key là chữ thường để đồng nhất
        work.put("title", title);
        work.put("content", content);
        work.put("date", date);
        work.put("type", type);
        work.put("status", status);
        return work;
    }

    @Override
    public String toString() {
        return "SanPhamModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}
