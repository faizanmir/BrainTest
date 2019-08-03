package avishkaar.com.braintest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO  {

    @Update
    void updateimage(ImageDataForDatabase imageDataTable);

    @Delete
    void deleteImage(ImageDataForDatabase imageData);

    @Insert
    void insetIntoDatabase(ImageDataForDatabase imageData);

    @Query("SELECT * FROM ImageDataForDatabaseTable ")
    List<ImageDataForDatabase> getAllData();

    @Query("SELECT * FROM ImageDataForDatabaseTable WHERE id = :idNumber")
    List<ImageDataForDatabase> filterByid(int idNumber);

    @Query("DELETE FROM ImageDataForDatabaseTable")
    void deleteAll();






}
