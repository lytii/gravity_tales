package read.gravitytales.objects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Paragraph {

   @PrimaryKey
   long id;

   long chapterId;

   int position;

   String text;

   public Paragraph(String text) {
      this.text = text;
   }

   public long getChapterId() {
      return chapterId;
   }

   public void setChapterId(long chapterId) {
      this.chapterId = chapterId;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public int getPosition() {
      return position;
   }

   public void setPosition(int position) {
      this.position = position;
   }
}
