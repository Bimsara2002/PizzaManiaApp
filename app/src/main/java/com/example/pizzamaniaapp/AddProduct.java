package com.example.pizzamaniaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AddProduct extends AppCompatActivity {

    EditText etName, etDescription, etPrice, etCategory;
    Spinner etBranch;
    Button btnAdd, btnSelectImage;
    ImageView ivPreview;
    DBHelper DB;
    Uri selectedImageUri;
    String savedImagePath; //will hold copied safe path

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etBranch=findViewById(R.id.spinnerBranch);
        btnAdd = findViewById(R.id.btnAdd);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivPreview = findViewById(R.id.ivPreview);

        DB = new DBHelper(this);

        //set location to spinner

        String[] locations = {"Colombo", "Galle", "Kandy", "Jaffna"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etBranch.setAdapter(adapter);

        //Add Product button
        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String priceStr = etPrice.getText().toString();
            String category = etCategory.getText().toString();
            String Branch = etBranch.getSelectedItem().toString();


            if (name.isEmpty() || priceStr.isEmpty() || savedImagePath == null) {
                Toast.makeText(this, "Name, Price and Image are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            //Save file path, not content:// URI
            boolean success = DB.addProduct(name, description, price, savedImagePath, category,Branch);

            if (success) {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });

        //Image picker launcher
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivPreview.setImageURI(selectedImageUri);

                        //Copy image  app storage
                        savedImagePath = copyImageToAppStorage(selectedImageUri);
                    }
                });

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
    }
    //Save selected gallery image to app's private storage
    private String copyImageToAppStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), "img_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            inputStream.close();
            outputStream.close();

            return file.getAbsolutePath(); //return file path
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
