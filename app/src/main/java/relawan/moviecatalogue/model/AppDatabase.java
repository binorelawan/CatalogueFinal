package relawan.moviecatalogue.model;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import relawan.moviecatalogue.model.moviemodel.MovieCatalogue;
import relawan.moviecatalogue.model.moviemodel.MovieDao;
import relawan.moviecatalogue.model.tvmodel.TvCatalogue;
import relawan.moviecatalogue.model.tvmodel.TvDao;

@Database(entities = {MovieCatalogue.class, TvCatalogue.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "database";

    public abstract MovieDao movieDao();
    public abstract TvDao tvDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context)  {
        if (instance == null)   {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    // mereset/menghapus semua database ketika database update.
                    .fallbackToDestructiveMigration()
                    .build();

        }
        Log.d(TAG, "database instance");
        return instance;
    }

}
