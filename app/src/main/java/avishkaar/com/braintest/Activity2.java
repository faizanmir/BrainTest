package avishkaar.com.braintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class Activity2 extends AppCompatActivity {
    List<ImageDataForDatabase> imageList;
    private static final String TAG = "Activity2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        final ImageLinkDatabase db = ImageLinkDatabase.getInstance(this);



        RecyclerView recyclerView = findViewById(R.id.recyclerViewDatabase);
        DatabaseRecyclerViewAdapter adapter = new DatabaseRecyclerViewAdapter(db.imageDatabaseDAO().getAllData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));



    }
}
