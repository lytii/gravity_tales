package read.gravitytales;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Relation;
import io.objectbox.annotation.Generated;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.exception.DbDetachedException;
import io.objectbox.exception.DbException;
import io.objectbox.relation.ToOne;

@Entity
public class Paragraph {

   @Id
   private long paragraphId;

   long chapterId;

   @Relation
   Chapter chapter;

   String paragraphText;

   /** Used to resolve relations */
   @Internal
   @Generated(hash = 1307364262)
   transient BoxStore __boxStore;

   @Generated(hash = 1388636159)
   public Paragraph(long paragraphId, long chapterId, String paragraphText) {
       this.paragraphId = paragraphId;
       this.chapterId = chapterId;
       this.paragraphText = paragraphText;
   }

   @Generated(hash = 1886769140)
   public Paragraph() {
   }

   public long getParagraphId() {
       return paragraphId;
   }

   public void setParagraphId(long paragraphId) {
       this.paragraphId = paragraphId;
   }

   public long getChapterId() {
       return chapterId;
   }

   public void setChapterId(long chapterId) {
       this.chapterId = chapterId;
   }

   public String getParagraphText() {
       return paragraphText;
   }

   public void setParagraphText(String paragraphText) {
       this.paragraphText = paragraphText;
   }

   @Internal
   @Generated(hash = 1880128277)
   private transient ToOne<Paragraph, Chapter> chapter__toOne;

   /** See {@link io.objectbox.relation.ToOne} for details. */
   @Generated(hash = 151103615)
   public synchronized ToOne<Paragraph, Chapter> getChapter__toOne() {
       if (chapter__toOne == null) {
           chapter__toOne = new ToOne<>(this, Paragraph_.chapterId, Chapter.class);
       }
       return chapter__toOne;
   }

   /** To-one relationship, resolved on first access. */
   @Generated(hash = 288427868)
   public Chapter getChapter() {
       chapter = getChapter__toOne().getTarget(this.chapterId);
       return chapter;
   }

   /** Set the to-one relation including its ID property. */
   @Generated(hash = 468304211)
   public void setChapter(Chapter chapter) {
       getChapter__toOne().setTarget(chapter);
       this.chapter = chapter;
   }

   /**
    * Removes entity from its object box. Entity must attached to an entity context.
    */
   @Generated(hash = 1086912993)
   public void remove() {
       if (__boxStore == null) {
           throw new DbDetachedException();
       }
       __boxStore.boxFor(Paragraph.class).remove(this);
   }

   /**
    * Puts the entity in its object box.
    * Entity must attached to an entity context.
    */
   @Generated(hash = 1206185148)
   public void put() {
       if (__boxStore == null) {
           throw new DbDetachedException();
       }
       __boxStore.boxFor(Paragraph.class).put(this);
   }
}
