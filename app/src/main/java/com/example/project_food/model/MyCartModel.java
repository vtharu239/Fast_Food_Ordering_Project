package com.example.project_food.model;

import java.io.Serializable;

public class MyCartModel implements Serializable
{
    String productName;
    int productPrice;
    int totalQuantity;
    int totalPrice;
    String documentId;

    public MyCartModel()
    {
    }

    public MyCartModel(String productName, int productPrice, int totalQuantity, int totalPrice)
    {
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public String getDocumentId()
    {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

//    public void setProductPrice(int productPrice) {
//        this.productPrice = productPrice;
//    }

    public void setProductPrice(Object productPrice) {
        if (productPrice instanceof String) {
            try {
                // Loại bỏ khoảng trắng và chuyển đổi thành số nguyên
                this.productPrice = Integer.parseInt(((String) productPrice).trim());
            } catch (NumberFormatException e) {
                this.productPrice = 0; // Giá trị mặc định nếu không chuyển đổi được
            }
        } else if (productPrice instanceof Number) {
            this.productPrice = ((Number) productPrice).intValue();
        }
    }


    public int getTotalQuantity() {
        return totalQuantity;
    }

//    public void setTotalQuantity(int totalQuantity) {
//        this.totalQuantity = totalQuantity;
//    }

    // Cập nhật setter để xử lý cả String và int
    public void setTotalQuantity(Object totalQuantity) {
        if (totalQuantity instanceof String) {
            try {
                // Loại bỏ khoảng trắng và chuyển đổi thành số nguyên
                this.totalQuantity = Integer.parseInt(((String) totalQuantity).trim());
            } catch (NumberFormatException e) {
                this.totalQuantity = 0; // Giá trị mặc định nếu không chuyển đổi được
            }
        } else if (totalQuantity instanceof Number) {
            this.totalQuantity = ((Number) totalQuantity).intValue();
        }
    }


    public int getTotalPrice() {
        return totalPrice;
    }

//    public void setTotalPrice(int totalPrice) {
//        this.totalPrice = totalPrice;
//    }

    // Cập nhật setter để xử lý cả String và int
    public void setTotalPrice(Object totalPrice) {
        if (totalPrice instanceof String) {
            try {
                // Loại bỏ khoảng trắng và chuyển đổi thành số nguyên
                this.totalPrice = Integer.parseInt(((String) totalPrice).trim());
            } catch (NumberFormatException e) {
                this.totalPrice = 0; // Giá trị mặc định nếu không chuyển đổi được
            }
        } else if (totalPrice instanceof Number) {
            this.totalPrice = ((Number) totalPrice).intValue();
        }
    }
}