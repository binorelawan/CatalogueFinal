package relawan.moviecatalogue.model.tvmodel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTv(TvCatalogue tsCa);


    @Delete
    void deleteTv(TvCatalogue tsCa);

    @Query("SELECT * FROM tvshow_table")
    LiveData<List<TvCatalogue>> getFavoriteTvs();

    @Query("SELECT * FROM tvshow_table WHERE id = :id")
    LiveData<List<TvCatalogue>> getIdTv(int id);
}
