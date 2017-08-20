package read.gravitytales;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static read.gravitytales.R.style.noTitleDialog;

public class ReadActivity extends AppCompatActivity {

   @BindView(R.id.chapter_recycler_view)
   RecyclerView chapterRecyclerView;

   LastItemDetector lastItemDetector;
   LinearLayoutManager chapterLayoutManager;
   Menu globalMenu;
   ReadPresenter presenter;

   @BindView(R.id.toolbar)
   Toolbar toolbar;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_read);
      ButterKnife.bind(this);
      setupChapterView();
      presenter = new ReadPresenter(this);
      setSupportActionBar(toolbar);
   }

   private void setupChapterView() {
      chapterLayoutManager = new LinearLayoutManager(this);
      chapterRecyclerView.setLayoutManager(chapterLayoutManager);
      lastItemDetector = new LastItemDetector();
      chapterRecyclerView.addOnScrollListener(lastItemDetector);
      chapterRecyclerView.setOnTouchListener(new View.OnTouchListener() {
         float downX = 0;
         float downY = 0;

         @Override
         public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
               case MotionEvent.ACTION_UP:
                  float deltaY = motionEvent.getY() - downY;
                  float deltaX = motionEvent.getX() - downX;
                  Log.d(TAG, "onTouch: delta X " + deltaX + " Y " + deltaY);
                  // right to left (NEXT)
                  if (deltaX <= -200 && Math.abs(deltaY) <= 50) {
                     Log.d(TAG, "onTouch: swipe left");
                     presenter.showNextChapter();
                  }
                  // left to right (PREV)
                  if (deltaX >= 200 && Math.abs(deltaY) <= 50) {
                     Log.d(TAG, "onTouch: swipe right");
                     presenter.showPrevChapter();
                  }
               case MotionEvent.ACTION_DOWN:
                  Log.d(TAG, "onTouch: down");
                  downX = motionEvent.getX();
                  downY = motionEvent.getY();
            }

            return false;
         }
      });
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.chapter_menu, menu);
      globalMenu = menu;
      return true;
   }

   public void prevChapter(View view) {
      presenter.showPrevChapter();
   }

   public void nextChapter(View view) {
      presenter.showNextChapter();
   }


   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.menu_jump:
            jump(toolbar);
            break;
         case R.id.menu_book:
            changeBook();
            break;
         default:
            break;
      }
      return true;
   }

   @Override
   protected void onStop() {
      super.onStop();
      int pos = chapterLayoutManager.findFirstVisibleItemPosition();
      presenter.markScroll(pos);
   }

   public void setTitle(String title) {
      toolbar.setTitle(title.replace("Chapter ", ""));
   }

   public void setSroll(int scrollPosition) {
      chapterLayoutManager.scrollToPosition(scrollPosition);
   }

   public void changeBook() {
      final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      AlertDialog.Builder alert = new AlertDialog.Builder(this, noTitleDialog);
      final EditText input = new EditText(this);
      input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
      alert.setTitle("Enter Book Address");
      alert.setView(input);
      alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {
            presenter.changeBook(input.getText().toString());
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

   /**
    * wrapper method for view to enter chapter to jump to
    */
   public void jump(View view) {
      jump();
   }

   /**
    * Opens a dialog to input Chapter Number
    * Loads from network or cache to display Chapter
    */
   private void jump() {
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

   public void showLoading() {
      ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_bar);
      progressBar.setVisibility(View.VISIBLE);
   }

   public void doneLoading() {
      ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_bar);
      progressBar.setVisibility(View.INVISIBLE);
   }

   /**
    * Displays chapter
    * Resets scrollListener for end of page preLoading
    */
   public void displayChapter(ChapterAdapter chapterAdapter) {
      chapterRecyclerView.removeOnScrollListener(lastItemDetector);
      chapterRecyclerView.setAdapter(chapterAdapter);
      chapterRecyclerView.addOnScrollListener(lastItemDetector);
      ;
   }

   private class LastItemDetector extends RecyclerView.OnScrollListener {

      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
         if (isAtBottom(recyclerView)) {
            presenter.preLoadNextChapter();
            chapterRecyclerView.removeOnScrollListener(lastItemDetector);
         }
      }

      private boolean isAtBottom(RecyclerView recyclerView) {
         return !recyclerView.canScrollVertically(0);
      }

   }
}
