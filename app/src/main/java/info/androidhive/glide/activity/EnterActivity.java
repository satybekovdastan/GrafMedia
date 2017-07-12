package info.androidhive.glide.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import info.androidhive.glide.R;
import info.androidhive.glide.model.Image;

public class EnterActivity extends AppCompatActivity {
Button enterBtn;
    EditText password;
    DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        password = (EditText) findViewById(R.id.password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Войти");
        dataBase = new DataBase(this);
        dataBase.readData();
        Cursor cursor = dataBase.getData();
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            String pass = cursor.getString(cursor.getColumnIndex(DataBase.PASSWORD_COLUMN));
            Log.e("pass",pass);
        }

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.logo_glav).into(imageView);

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(GlavActivity.this,FolderActivity.class);
//                startActivity(i);
                String pass = password.getText().toString();
                new EnterActivity.ParseTask(pass).execute();
            }
        });
    }
    public class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = "";

        String pass;
        public ParseTask(String pass){
            this.pass = pass;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://176.31.209.182/get_password?password="+pass);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                jsonResult = builder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("JSON", jsonResult);
            return jsonResult;
        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            Log.e("TAG", json);

            JSONObject dataJsonObject;
            String password;
            int ItemId;
            try {
                dataJsonObject = new JSONObject(json);
                // JSONArray menus = dataJsonObject.getJSONArray("objects");

                // JSONObject secondObject = menus.getJSONObject(0);
                password = dataJsonObject.getString("result");
                Log.e("RESULT", password);
                if (password.equals("Something went wrong")){
                    Toast.makeText(EnterActivity.this, "Неправб", Toast.LENGTH_SHORT).show();
                    EnterActivity.this.password.setError("неправильно");
                }else{
                    dataBase.deleteData();
                    dataBase.addPassword(password);
                    Intent i = new Intent(EnterActivity.this,FolderActivity.class);
                    i.putExtra("id",password);
                    startActivity(i);
                    finish();
                }
//                Folder folder = new Folder();
//                folder.setName(secondName);
//                list.add(folder);

//                Log.d(TAG, "Второе имя: " + secondName);
//
//                for (int i = 0; i < menus.length(); i++) {
//                    JSONObject menu = menus.getJSONObject(i);
//
//                    secondName = menu.getString("name");
//                    ItemId = menu.getInt("id");
//                    Folder folder = new Folder();
//                    folder.setName(secondName);
//                    folder.setID(ItemId);
//                    list.add(folder);
//
//                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
