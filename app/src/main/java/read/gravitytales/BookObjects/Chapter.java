package read.gravitytales.BookObjects;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Relation;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.exception.DbDetachedException;
import io.objectbox.exception.DbException;
import io.objectbox.relation.ToOne;

@Entity
public class Chapter {

   @Id
   private long chapterId;

   @Index
   private int chapterNumber;

   private long bookId;

   @Relation(idProperty = "chapterId")
   private List<Paragraph> paragraphs;

   @Relation
   private Book book;

   /** Used to resolve relations */
   @Internal
   @Generated(hash = 1307364262)
   transient BoxStore __boxStore;

@Internal
@Generated(hash = 274585948)
private transient ToOne<Chapter, Book> book__toOne;

   @Generated(hash = 260141279)
   public Chapter(long chapterId, int chapterNumber, long bookId) {
       this.chapterId = chapterId;
       this.chapterNumber = chapterNumber;
       this.bookId = bookId;
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

   public long getBookId() {
       return bookId;
   }

   public void setBookId(long bookId) {
       this.bookId = bookId;
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

/** See {@link io.objectbox.relation.ToOne} for details. */
@Generated(hash = 1047404409)
public synchronized ToOne<Chapter, Book> getBook__toOne() {
    if (book__toOne == null) {
        book__toOne = new ToOne<>(this, Chapter_.bookId, Book.class);
    }
    return book__toOne;
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 1325058034)
public Book getBook() {
    book = getBook__toOne().getTarget(this.bookId);
    return book;
}

/** Set the to-one relation including its ID property. */
@Generated(hash = 126347696)
public void setBook(Book book) {
    getBook__toOne().setTarget(book);
    this.book = book;
}

}

