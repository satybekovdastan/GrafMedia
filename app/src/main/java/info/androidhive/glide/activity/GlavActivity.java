package info.androidhive.glide.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import info.androidhive.glide.R;

public class GlavActivity extends AppCompatActivity {
    ImageView portfolioBtn, oNasBtn;
    ImageView download;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glav);
        portfolioBtn = (ImageView) findViewById(R.id.portfolioBtn);
        oNasBtn = (ImageView) findViewById(R.id.oNasBtn);
        download = (ImageView) findViewById(R.id.download);
        Glide.with(this).load(R.drawable.portfel).into(portfolioBtn);
        Glide.with(this).load(R.drawable.logo_glav).into(oNasBtn);
        Glide.with(this).load(R.drawable.upload).into(download);

        dataBase = new DataBase(this);

        portfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlavActivity.this, MainActivity.class);
                intent.putExtra("id", 1);
                intent.putExtra("name", "Портфолио");
                startActivity(intent);
            }
        });
        oNasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GlavActivity.this, AboutActivity.class);

                startActivity(i);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBase.readData();
                Cursor cursor = dataBase.getData();
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String pass = cursor.getString(cursor.getColumnIndex(DataBase.PASSWORD_COLUMN));
                    Log.e("pass", pass);
                    Intent i = new Intent(GlavActivity.this, FolderActivity.class);
                    i.putExtra("id", pass);

                    startActivity(i);
                } else {
                    Intent i = new Intent(GlavActivity.this, EnterActivity.class);

                    startActivity(i);
                }

            }
        });


    }


    public void onClickCall(View view) {
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:0123456789"));
//        startActivity(intent);
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+996706887721", null)));
    }
}
