package read.gravitytales;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import read.gravitytales.objects.Paragraph;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

   private static final int PARAGRAPH = 603;
   private static final int FOOTER = 365;
   private List<Paragraph> paragraphList;

   public static class ViewHolder extends RecyclerView.ViewHolder {

      @Nullable
      @BindView(R.id.chapter_item_text_view)
      public TextView textView;

      public ViewHolder(View itemView) {
         super(itemView);
         ButterKnife.bind(this, itemView);
      }
   }

   public ChapterAdapter(List<Paragraph> paragraphList) {
      this.paragraphList = paragraphList;
   }

   @Override
   public ChapterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      int layout = R.layout.chapter_item_layout;

      if (viewType == FOOTER) {
         layout = R.layout.chapter_footer_layout;
      }
      LinearLayout tv = (LinearLayout) LayoutInflater.from(parent.getContext())
                                                     .inflate(layout, parent, false);

      return new ViewHolder(tv);
   }

   @Override
   public int getItemViewType(int position) {
      return position == paragraphList.size() ? FOOTER : PARAGRAPH;
   }


   @Override
   @SuppressWarnings("deprecation")
   public void onBindViewHolder(ViewHolder holder, int position) {
      if(position != paragraphList.size()) {
         String textItem = paragraphList.get(position).getParagraphText();
         holder.textView.setText(Html.fromHtml(textItem));
      }
   }

   @Override
   public int getItemCount() {
      return paragraphList.size()+1;
   }
}
