package read.gravitytales.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Chapter {

   private int bookId;

   @PrimaryKey
   private long id;

   private int number;

   private String url;

   private String title;

   private String nextChapterUrl;

   private String prevChapterUrl;


   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getUrl() {
      return url;
   }

   public int getBookId() {
      return bookId;
   }

   public void setBookId(int bookId) {
      this.bookId = bookId;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public int getNumber() {
      return number;
   }

   public void setNumber(int number) {
      this.number = number;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getNextChapterUrl() {
      return nextChapterUrl;
   }

   public void setNextChapterUrl(String nextChapterUrl) {
      this.nextChapterUrl = nextChapterUrl;
   }

   public String getPrevChapterUrl() {
      return prevChapterUrl;
   }

   public void setPrevChapterUrl(String prevChapterUrl) {
      this.prevChapterUrl = prevChapterUrl;
   }
}