package read.gravitytales;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

   private Elements chapterItems;

   public static class ViewHolder extends RecyclerView.ViewHolder {

      @BindView(R.id.chapter_item_text_view)
      public TextView textView;

      public ViewHolder(View itemView) {
         super(itemView);
         ButterKnife.bind(this, itemView);
      }
   }

   public ChapterAdapter(Elements chapterItems) {
      this.chapterItems = chapterItems;
   }

   @Override
   public ChapterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LinearLayout tv = (LinearLayout) LayoutInflater.from(parent.getContext())
                                                     .inflate(R.layout.chapter_item_layout, parent, false);
      return new ViewHolder(tv);
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      String textItem = chapterItems.get(position).text();
      holder.textView.setText(textItem);
   }

   @Override
   public int getItemCount() {
      return chapterItems.size();
   }
}
