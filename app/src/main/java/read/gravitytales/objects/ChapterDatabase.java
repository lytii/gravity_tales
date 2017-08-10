package read.gravitytales.objects;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Chapter.class, Paragraph.class}, version = 1)
public abstract class ChapterDatabase extends RoomDatabase {

   private static ChapterDatabase INSTANCE;

   public abstract ChapterDAO chapterDAO();

   public static ChapterDatabase getINSTANCE(Context context) {
      if (INSTANCE == null) {
         INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                              ChapterDatabase.class,
                              "chapter-database")
                        .build();
      }
      return INSTANCE;
   }

   public static void destroyInstance() {
      INSTANCE = null;
   }

}
