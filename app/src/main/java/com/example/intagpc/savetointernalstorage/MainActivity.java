package com.example.intagpc.savetointernalstorage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private Button btnSaveImage, btnGetImage;
    File directory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        buttonListener();

    }

    private void buttonListener() {

        btnGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromStorage(directory.toString());
            }
        });
        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = null;
                Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher_background);
                bitmap = drawableToBitmap(drawable);
                saveToInternalStorage(bitmap);
                Toast.makeText(MainActivity.this, "path is" + directory, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String saveToInternalStorage(Bitmap bitmap) {

        ContextWrapper contextWrapper = new ContextWrapper(MainActivity.this);
        directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory + "/myImage.jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private void initWidgets() {

        btnSaveImage = (Button) findViewById(R.id.btnSaveImage);
        btnGetImage = (Button) findViewById(R.id.btnGetImage);

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void getImageFromStorage(String path) {

        try {
            File file = new File(path, "myImage.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            ImageView img = (ImageView) findViewById(R.id.imageUser);
            img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
