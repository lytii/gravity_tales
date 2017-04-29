package read.gravitytales;

import android.support.v7.widget.RecyclerView;
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

   private List<Paragraph> paragraphList;

   public static class ViewHolder extends RecyclerView.ViewHolder {

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
      LinearLayout tv = (LinearLayout) LayoutInflater.from(parent.getContext())
                                                     .inflate(R.layout.chapter_item_layout, parent, false);
      return new ViewHolder(tv);
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      String textItem = paragraphList.get(position).getParagraphText();
      holder.textView.setText(textItem);
   }

   @Override
   public int getItemCount() {
      return paragraphList.size();
   }
}
