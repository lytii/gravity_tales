package read.gravitytales;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import read.gravitytales.objects.Chapter;

import static read.gravitytales.R.style.noTitleDialog;

public class ReadActivity extends Activity {

   @BindView(R.id.chapter_recycler_view)
   RecyclerView chapterRecyclerView;

   ReadPresenter presenter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_read);

      ButterKnife.bind(this);

      LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
      chapterRecyclerView.setLayoutManager(LayoutManager);
//      chapterRecyclerView.addOnScrollListener(new LastItemDetector());

      presenter = new ReadPresenter(this);

   }

   @OnClick(R.id.next_button)
   public void next() {
         presenter.showNextChapter();
   }

   @OnClick(R.id.prev_button)
   public void prev() {
         presenter.showPrevChapter();
   }

   @OnClick(R.id.jump_button)
   public void jump() {
      final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      AlertDialog.Builder alert = new AlertDialog.Builder(this, noTitleDialog);

      final EditText input = new EditText(this);
      input.setInputType(InputType.TYPE_CLASS_NUMBER);
      alert.setTitle("Enter Chapter Number");
      alert.setView(input);
      alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {
            int chapter = Integer.parseInt(input.getText().toString());
            presenter.jumpToChapter(chapter);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
         }
      });
      alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
         }
      });
      alert.show();

      input.requestFocus();
      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
   }

   public void displayChapter(Chapter chapter) {
      chapterRecyclerView.setAdapter(new ChapterAdapter(chapter.getParagraphs()));
   }

   public void displayChapter(ChapterAdapter chapterAdapter) {
      chapterRecyclerView.setAdapter(chapterAdapter);
   }

   public class LastItemDetector extends RecyclerView.OnScrollListener   {

      public void onScrollStateChanged(RecyclerView recyclerView, int newState){
         if(isAtBottom(recyclerView)) {
            presenter.preLoadNextChapter();
         }
      }

      public boolean isAtBottom(RecyclerView recyclerView) {
         int lastItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
         return lastItem > recyclerView.getChildCount();
      }

   }
}
