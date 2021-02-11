package com.games.tictocgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;


public class SeriesResult extends AppCompatActivity {
    Button paytm;
    String str;
    TelephonyManager telecomManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_result);
        telecomManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O)
        {    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        }
        else
        {
            Log.d("hh","dhhd");
        }

        str = telecomManager.getDeviceId();
        paytm=findViewById(R.id.paytm);
        Intent intent = getIntent();
        String player1Wins = String.valueOf(intent.getExtras().getInt("Player 1 Wins"));
        String player2Wins = String.valueOf(intent.getExtras().getInt("Player 2 Wins"));
        String draws = String.valueOf(intent.getExtras().getInt("Draws"));
        String player1Name = intent.getExtras().getString("Player 1 Name");
        String player2Name = intent.getExtras().getString("Player 2 Name");
        TextView player1NameView = (TextView) findViewById(R.id.p1name);
        TextView player2NameView = (TextView) findViewById(R.id.p2name);
        TextView player1WinsView = (TextView) findViewById(R.id.p1wins);
        TextView player2WinsView = (TextView) findViewById(R.id.p2wins);
        TextView drawsView = (TextView) findViewById(R.id.draws);

        FirebaseDatabase.getInstance().getReference(str+"(name)").setValue(player1Name).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        if(Integer.parseInt(player1Wins)>Integer.parseInt(player2Wins)){
            player1NameView.setTextColor(Color.GREEN);
            player2NameView.setTextColor(Color.RED);
        }
        else if(Integer.parseInt(player1Wins)<Integer.parseInt(player2Wins)){
            player2NameView.setTextColor(Color.GREEN);
            player1NameView.setTextColor(Color.RED);
        }
        else
        {
            player2NameView.setTextColor(Color.YELLOW);
            player1NameView.setTextColor(Color.YELLOW);
        }
        player1NameView.setText(player1Name);
        player2NameView.setText(player2Name);

        player1WinsView.setText(player1Wins);
        player2WinsView.setText(player2Wins);
        drawsView.setText(draws);
        if(Integer.parseInt(player1Wins)>Integer.parseInt(player2Wins))
        {
            paytm.setEnabled(true);
            paytm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(SeriesResult.this);
                    dialog.setTitle("Congratulation");
                    dialog.setMessage("Payment reverted into your paytm number soon");
                    LayoutInflater inflater = LayoutInflater.from(SeriesResult.this);
                    View register_layout = inflater.inflate(R.layout.tenlottery, null);
                    final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtnumber);

                    dialog.setView(register_layout);
                    dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference("winners").child(str).setValue(edtPhone.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   Intent ab=new Intent(SeriesResult.this,MainActivity.class);
                                   startActivity(ab);
                                   finish();
                                }
                            });
                        }
                    });
                    dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }

    }

    public void onClickContinue(View view){
        Intent intent = new Intent(this,MainActivity.class);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
            finish();
        }
    }
}