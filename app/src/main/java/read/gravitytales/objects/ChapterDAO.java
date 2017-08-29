package read.gravitytales.objects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface ChapterDAO {

   @Query("SELECT * FROM Book")
   Single<List<Book>> getBooks();

   @Query("SELECT * FROM Book where bookUrl LIKE :bookUrl")
   Single<Book> getBookByUrl(String bookUrl);

   @Query("SELECT * FROM Book where id = :bookId")
   Single<Book> getBook(int bookId);

   @Query("SELECT * FROM Chapter where url LIKE :url")
   Chapter getParagraph(String url);

   @Query("SELECT * FROM Chapter where number = :number AND bookId = :bookId" )
   Maybe<ChapterListingParagraphs> getChapter(int number, int bookId);

   @Insert
   void addBook(Book book);

   @Insert
   void addChapter(Chapter chapter);

   @Insert
   void addParagraph(Paragraph paragraph);
}