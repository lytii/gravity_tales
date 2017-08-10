package read.gravitytales.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Chapter {

   private String bookTitle;

   @PrimaryKey
   private long id;

   private int number;

   private String url;

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getUrl() {
      return url;
   }

   public String getBookTitle() {
      return bookTitle;
   }

   public void setBookTitle(String bookTitle) {
      this.bookTitle = bookTitle;
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
}