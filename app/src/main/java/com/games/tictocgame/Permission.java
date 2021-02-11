package com.games.tictocgame;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Permission extends AppCompatActivity {

TextView terms1;

    CheckBox First;
    public int count ;
    private Button btnGrant;
TextView txtview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        txtview=findViewById(R.id.rules);

        terms1=findViewById(R.id.terms1);
        terms1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        First = (CheckBox)findViewById(R.id.chkbox);
        FirebaseDatabase.getInstance().getReference("Permission").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.getValue().toString();
                txtview.setText(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        terms1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://tictactoewinner.000webhostapp.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(Permission.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Permission.this, MainActivity.class));

            finish();
            return;
        }
        if (ContextCompat.checkSelfPermission(Permission.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Permission.this, MainActivity.class));

            finish();
            return;
        }
        if (ContextCompat.checkSelfPermission(Permission.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(Permission.this, MainActivity.class));

            finish();
            return;
        }

        btnGrant = findViewById(R.id.btn_grant);

        btnGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(Permission.this)
                        .withPermissions(Manifest.permission.READ_CONTACTS
                                , Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    if(First.isChecked())
                                    startActivity(new Intent(Permission.this, MainActivity.class));
                                    else
                                    {

                                                startActivity(new Intent(Permission.this, Permission.class));

                                       finish();


                                    }

                                    finish();

                                }
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Permission.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permission to access device . you need to go to setting to allow the permission.")
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                }
                                            })
                                            .show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }


                        })
                        .check();
            }
        });
    }





}
