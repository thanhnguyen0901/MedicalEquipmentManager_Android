package com.example.k23dtcn436_nguyenvietthanh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.k23dtcn436_nguyenvietthanh.R;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialCardView cardCategories = findViewById(R.id.cardCategories);
        MaterialCardView cardEquipment = findViewById(R.id.cardEquipment);
        MaterialCardView cardReports = findViewById(R.id.cardReports);

        cardCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CategoryListActivity.class);
                startActivity(intent);
            }
        });

        cardEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EquipmentListActivity.class);
                startActivity(intent);
            }
        });

        cardReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });
    }
}
