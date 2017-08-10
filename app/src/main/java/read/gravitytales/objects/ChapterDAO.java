package read.gravitytales.objects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ChapterDAO {

   @Query("SELECT * FROM Chapter where url LIKE :url")
   Chapter getParagraph(String url);

   @Query("SELECT * FROM Chapter where bookTitle LIKE :title")
   List<Chapter> getBook(String title);

   @Query("SELECT * FROM Chapter where number = :number" )
   Single<ChapterListingParagraphs> getChapter(int number);

   @Insert
   void addChapter(Chapter chapter);

   @Insert
   void addParagraph(Paragraph paragraph);
}
