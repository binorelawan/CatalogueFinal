package relawan.moviecatalogue.model.moviemodel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieCatalogue moCa);


    @Delete
    void deleteMovie(MovieCatalogue moCa);

    @Query("SELECT * FROM movie_table")
    LiveData<List<MovieCatalogue>> getFavoriteMovies();


    @Query("SELECT * FROM movie_table WHERE id = :id")
    LiveData<List<MovieCatalogue>> getIdMovie(int id);


}
