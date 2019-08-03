package avishkaar.com.braintest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "ImageTable")
public class ImageData {


    public ImageData() {
    }
    @SerializedName("albumId")
    String albumId;
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    int id;

    @SerializedName("title")
    String title;

    @SerializedName("url")
    String url ;

    @SerializedName("thumbnailUrl")
    String thumbnailUrl;



    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
