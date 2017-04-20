package read.gravitytales;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Relation;
import io.objectbox.annotation.Generated;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.exception.DbDetachedException;
import io.objectbox.exception.DbException;

@Entity
public class Chapter {

   @Id
   private long chapterId;

   @Index
   private int chapterNumber;

   @Relation(idProperty = "chapterId")
   private List<Paragraph> paragraphs;

   /** Used to resolve relations */
   @Internal
   @Generated(hash = 1307364262)
   transient BoxStore __boxStore;

   @Generated(hash = 1835204239)
   public Chapter(long chapterId, int chapterNumber) {
       this.chapterId = chapterId;
       this.chapterNumber = chapterNumber;
   }

   @Generated(hash = 393170288)
   public Chapter() {
   }

   public long getChapterId() {
       return chapterId;
   }

   public void setChapterId(long chapterId) {
       this.chapterId = chapterId;
   }

   public int getChapterNumber() {
       return chapterNumber;
   }

   public void setChapterNumber(int chapterNumber) {
       this.chapterNumber = chapterNumber;
   }

   /**
    * To-many relationship, resolved on first access (and after reset).
    * Changes to to-many relations are not persisted, make changes to the target entity.
    */
   @Generated(hash = 753852040)
   public List<Paragraph> getParagraphs() {
       if (paragraphs == null) {
           final BoxStore boxStore = this.__boxStore;
           if (boxStore == null) {
               throw new DbDetachedException();
           }
           Box<Paragraph> box = boxStore.boxFor(Paragraph.class);
           int targetTypeId = boxStore.getEntityTypeIdOrThrow(Paragraph.class);
           List<Paragraph> paragraphsNew = box.getBacklinkEntities(targetTypeId,
                   Paragraph_.chapterId, chapterId);
           synchronized (this) {
               if (paragraphs == null) {
                   paragraphs = paragraphsNew;
               }
           }
       }
       return paragraphs;
   }

   /** Resets a to-many relationship, making the next get call to query for a fresh result. */
   @Generated(hash = 447589010)
   public synchronized void resetParagraphs() {
       paragraphs = null;
   }

   /**
    * Removes entity from its object box. Entity must attached to an entity context.
    */
   @Generated(hash = 678604146)
   public void remove() {
       if (__boxStore == null) {
           throw new DbDetachedException();
       }
       __boxStore.boxFor(Chapter.class).remove(this);
   }

   /**
    * Puts the entity in its object box.
    * Entity must attached to an entity context.
    */
   @Generated(hash = 1041176412)
   public void put() {
       if (__boxStore == null) {
           throw new DbDetachedException();
       }
       __boxStore.boxFor(Chapter.class).put(this);
   }
}

