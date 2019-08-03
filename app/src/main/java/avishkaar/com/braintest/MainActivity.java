package avishkaar.com.braintest;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ServiceConfigurationError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NetworkState.NetworkStateReceiverListener{
    private static final String TAG = "MainActivity";
    NetworkState networkStateReceiver;
    Retrofit retrofit;
    RetrofitApi retrofitApi;
    Call<List<ImageData>>photosCall;
    ArrayList<String> urlArrayList;
    Bitmap bitmap;
    Uri uriToReturn;
   static ImageLinkDatabase db;
   ImageView imageView;
   Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkStateReceiver = new NetworkState();
        networkStateReceiver.addListener(MainActivity.this);
        Button deleteButton = findViewById(R.id.deleteButton);
        imageView = findViewById(R.id.changeImageView);
        handler = new Handler();


        db = ImageLinkDatabase.getInstance(this);


        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
         retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(RetrofitApi.BASE_URL).build();
         retrofitApi = retrofit.create(RetrofitApi.class);
         photosCall= retrofitApi.getImages();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.imageDatabaseDAO().deleteAll();
                    }
                }).start();

            }
        });




    }

    @Override
    public void networkAvailable() {

    }

    @Override
    public void networkUnavailable() {
        Log.e(TAG, "networkUnavailable: " + "Network unavailable" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }


    void sendCallToServer(){
        photosCall.enqueue(new Callback<List<ImageData>>() {
            @Override
            public void onResponse(Call<List<ImageData>> call, Response<List<ImageData>> response) {

               if(response.body()!=null) {
                   List<ImageData> imageDataList = response.body();
                   for(ImageData imageData:imageDataList) {
                       new GetImagesFromUrls(MainActivity.this,imageData).execute();
                   }

               }
            }


            @Override
            public void onFailure(Call<List<ImageData>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage() );
            }
        });
    }

    public void startData(View view) {
        sendCallToServer();
    }

    public void printData(View view) {
        Intent intent = new Intent(MainActivity.this,Activity2.class);
        startActivity(intent);
    }

    public void cancelCall(View view) {
        photosCall.cancel();
    }


    static class GetImagesFromUrls extends AsyncTask<Void,Void,Void>{
        Bitmap bitmap;
        WeakReference<MainActivity> weakReference;
        ImageData imageData;

        public GetImagesFromUrls(MainActivity mainActivity,ImageData imageData) {
            this.weakReference = new WeakReference<>(mainActivity);
            this.imageData = imageData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                weakReference.get();
               Uri uriForUrl =  weakReference.get().returnFormedUri(new URL(imageData.getUrl()),weakReference);
               Uri uriForThumbnail =  weakReference.get().returnFormedUri(new URL(imageData.getThumbnailUrl()),weakReference);
                Log.i(TAG, "doInBackground: uri:"+ uriForUrl + "do in bg " + uriForThumbnail);
              try {
                  db.imageDatabaseDAO().insetIntoDatabase(new ImageDataForDatabase(imageData.getAlbumId(), uriForThumbnail.toString(), uriForUrl.toString(), imageData.getTitle()));
              }catch (NullPointerException e)
              {
                  e.printStackTrace();
              }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
            }

        }

    public Uri returnFormedUri(URL url,WeakReference<MainActivity> weakReference) throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        InputStream inputStream =  urlConnection.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        bufferedInputStream.close();
        inputStream.close();
        File filePath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        filePath.mkdirs();
        File imageFile = new File(filePath + "Imagedata"+new Random().nextInt() +".png");
        FileOutputStream out = new FileOutputStream(imageFile, true);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
        out.flush();
        out.close();
        MediaScannerConnection.scanFile(weakReference.get().getApplicationContext(), new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(final String path, final Uri uri) {
               uriToReturn = uri;
            }
        });

        return uriToReturn;
    }

    static class SetImageView extends AsyncTask<Void,Void,Void>
    { Handler handler;
        WeakReference<MainActivity> mainActivityWeakReference;
        public SetImageView(Handler handler,MainActivity mainActivity) {
            this.handler = handler;
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (final ImageDataForDatabase imageData:db.imageDatabaseDAO().getAllData()
                 ) {
               mainActivityWeakReference.get().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Log.e(TAG, "run: " + imageData.iconUri);
                      ImageView imageView = new ImageView(mainActivityWeakReference.get());
                      imageView.setImageDrawable(Drawable.createFromPath(Uri.parse(imageData.iconUri).getPath()));
                      imageView.setImageURI(Uri.parse(imageData.iconUri));
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       Picasso.get().load(imageData.iconUri).into(mainActivityWeakReference.get().imageView);
                   }
               });


            }
            return null;
        }
    }


}

