package read.gravitytales.BookObjects;

import android.app.Activity;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * wrapper class to use objectbox
 */
public class ObjectBox {
   BoxStore boxStore;

   public ObjectBox(Activity activity) {
      boxStore = MyObjectBox.builder().androidContext(activity).build();
      Box<Book> bookBox = boxStore.boxFor(Book.class);

      Book book = new Book();
      book.setId(0);
      book.setTitle("Evil Lich");
      book.setCurrentChapter(111);
      bookBox.put(book);
   }

   /**
    * Adds new chapter to chapter box/paragraph box
    */
   public void putChapter(Elements chapterItems, int chapterNumber) {
      // XXX Remove hardcode
      Box<Book> bookBox = boxStore.boxFor(Book.class);
      Book book = bookBox.query().equal(Book_.id, 1).build().find().get(0);

      List<Chapter> bookChapters = book.getChapters();

      // Make new chapter
      Chapter chapter = new Chapter();
      chapter.setChapterNumber(chapterNumber);
      boxStore.boxFor(Chapter.class).put(chapter);
      bookChapters.add(chapter);

      // Make new paragraph list
      List<Paragraph> chapterParagraphs = chapter.getParagraphs();
      Box<Paragraph> paragraphBox = boxStore.boxFor(Paragraph.class);

      for (int i = 0; i < 2; i++) {
         chapterItems.remove(0);
      }
      // trim chapter items
      for (int i = 0; i < 16; i++) {
         chapterItems.remove(chapterItems.size() - 1);
      }

      // add paragraphs to box
      for (Element item : chapterItems) {
         Paragraph newParagraph = new Paragraph();
         newParagraph.setParagraphText(item.text());
         newParagraph.setChapterId(chapter.getChapterId());

         paragraphBox.put(newParagraph);
         chapterParagraphs.add(newParagraph);
      }

   }

   /**
    * Get chapter from chapter box
    * returns null if not cached
    */
   public Chapter queryChapter(int chapterNumber) {
      Box<Chapter> chapterBox = boxStore.boxFor(Chapter.class);
      List<Chapter> chapterList = chapterBox.query()
                                            .equal(Chapter_.bookId, 0) // XXX Remove hardcode
                                            .equal(Chapter_.chapterNumber, chapterNumber)
                                            .build()
                                            .find();
      if (chapterList.size() == 0)
         return null;
      return chapterList.get(0);
   }
}
