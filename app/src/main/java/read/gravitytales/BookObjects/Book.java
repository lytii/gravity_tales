package read.gravitytales.BookObjects;

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
public class Book {

   @Id @Index
   private long id;

   private String title;

   private int currentChapter;

   @Relation(idProperty = "bookId")
   private List<Chapter> chapters;

   /** Used to resolve relations */
   @Internal
   @Generated(hash = 1307364262)
   transient BoxStore __boxStore;

   @Generated(hash = 2115557891)
   public Book(long id, String title, int currentChapter) {
       this.id = id;
       this.title = title;
       this.currentChapter = currentChapter;
   }

   @Generated(hash = 1839243756)
   public Book() {
   }

   public long getId() {
       return id;
   }

   public void setId(long id) {
       this.id = id;
   }

   public String getTitle() {
       return title;
   }

   public void setTitle(String title) {
       this.title = title;
   }

   public int getCurrentChapter() {
       return currentChapter;
   }

   public void setCurrentChapter(int currentChapter) {
       this.currentChapter = currentChapter;
   }

   /**
    * To-many relationship, resolved on first access (and after reset).
    * Changes to to-many relations are not persisted, make changes to the target entity.
    */
   @Generated(hash = 1748720437)
   public List<Chapter> getChapters() {
       if (chapters == null) {
           final BoxStore boxStore = this.__boxStore;
           if (boxStore == null) {
               throw new DbDetachedException();
           }
           Box<Chapter> box = boxStore.boxFor(Chapter.class);
           int targetTypeId = boxStore.getEntityTypeIdOrThrow(Chapter.class);
           List<Chapter> chaptersNew = box.getBacklinkEntities(targetTypeId,
                   Chapter_.bookId, id);
           synchronized (this) {
               if (chapters == null) {
                   chapters = chaptersNew;
               }
           }
       }
       return chapters;
   }

   /** Resets a to-many relationship, making the next get call to query for a fresh result. */
   @Generated(hash = 936914273)
   public synchronized void resetChapters() {
       chapters = null;
   }

   /**
    * Removes entity from its object box. Entity must attached to an entity context.
    */
   @Generated(hash = 1327349168)
   public void remove() {
       if (__boxStore == null) {
           throw new DbDetachedException();
       }
       __boxStore.boxFor(Book.class).remove(this);
   }

   /**
    * Puts the entity in its object box.
    * Entity must attached to an entity context.
    */
   @Generated(hash = 1742122961)
   public void put() {
       if (__boxStore == null) {
           throw new DbDetachedException();
       }
       __boxStore.boxFor(Book.class).put(this);
   }
}
