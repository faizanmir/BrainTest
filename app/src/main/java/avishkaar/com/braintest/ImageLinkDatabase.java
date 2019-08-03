package avishkaar.com.braintest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = ImageDataForDatabase.class,version = 2,exportSchema = false)
public abstract class ImageLinkDatabase extends RoomDatabase {
    public static ImageLinkDatabase INSTANCE;
    public static  RoomDatabase.Callback RoomDatabaseCallBack = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };



    public static synchronized ImageLinkDatabase getInstance(Context context)

    {
        if(INSTANCE==null)
        {
            INSTANCE =  Room.databaseBuilder(context,ImageLinkDatabase.class,"ImageDatabase")
                    .allowMainThreadQueries()
                    .addCallback(RoomDatabaseCallBack).fallbackToDestructiveMigration().allowMainThreadQueries().build();

            return INSTANCE;
        }

        return  INSTANCE;
    }
    public abstract DAO imageDatabaseDAO();



}
