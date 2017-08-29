package read.gravitytales.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Book {

   @PrimaryKey
   int id;

   String bookUrl;

   String bookTitle;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getBookUrl() {
      return bookUrl;
   }

   public void setBookUrl(String bookUrl) {
      this.bookUrl = bookUrl;
   }

   public String getBookTitle() {
      return bookTitle;
   }

   public void setBookTitle(String bookTitle) {
      this.bookTitle = bookTitle;
   }


}
