package com.example.contactsapp;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
//    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<User> arrayList=new ArrayList<>();
    MyAdapter adapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_view);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
            else
            {
                inflateLayout();
            }
        }
        else
            inflateLayout();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       MenuInflater inflater=getMenuInflater();
       inflater.inflate(R.menu.search_menu,menu);
       MenuItem menuItem=menu.findItem(R.id.search);
       SearchView searchView=(SearchView)menuItem.getActionView();

       searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {

               adapter.getFilter().filter(newText);
           return false;
           }
       });


        return true;
    }

    private void inflateLayout() {


                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                ContentResolver contentResolver=getContentResolver();
                Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                String[] projection={ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor cursor=contentResolver.query(uri,projection,null,null,null);

                while(cursor.moveToNext())
                {
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    user=new User(name,phone);
                    arrayList.add(user);

                }

                Collections.sort(arrayList, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapter=new MyAdapter(arrayList,this);
                recyclerView.setAdapter(adapter);
            }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED )
            {

                Toast.makeText(this, "permission granted successfully", Toast.LENGTH_SHORT).show();
                inflateLayout();
            }
            else
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
        }

    }
}
