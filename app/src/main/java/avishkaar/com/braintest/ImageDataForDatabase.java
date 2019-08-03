package avishkaar.com.braintest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ImageDataForDatabaseTable")
public class ImageDataForDatabase {

    public ImageDataForDatabase(String albumId, String thumbNailUri, String iconUri, String title) {
        this.albumId = albumId;
        this.thumbNailUri = thumbNailUri;
        this.iconUri = iconUri;
        this.title = title;
    }

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo
    String albumId;

    @ColumnInfo
    String thumbNailUri;

    @ColumnInfo
    String iconUri;

    @ColumnInfo
    String title;

}
