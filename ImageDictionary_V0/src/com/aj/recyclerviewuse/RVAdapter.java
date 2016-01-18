package com.aj.recyclerviewuse;

import java.util.List;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aj.imagedictionary.R;
import com.koushikdutta.ion.Ion;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.WordViewHolder> {

	public static class WordViewHolder extends RecyclerView.ViewHolder {
		CardView cv;
		TextView word;
		TextView meaning;
		ImageView photo;

		WordViewHolder(View itemView) {
			super(itemView);
			cv = (CardView) itemView.findViewById(R.id.cv);
			word = (TextView) itemView.findViewById(R.id.word);
			meaning = (TextView) itemView.findViewById(R.id.meaning);
			photo = (ImageView) itemView.findViewById(R.id.photo);
		}
	}

	List<WordObject> words;

	RVAdapter(List<WordObject> words) {
		this.words = words;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return words.size();
	}

	@Override
	public WordViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.item, viewGroup, false);
		WordViewHolder pvh = new WordViewHolder(v);
		return pvh;
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}

	@Override
	public void onBindViewHolder(WordViewHolder wordView, int pos) {
		// TODO Auto-generated method stub
		Log.e("check", "" + pos);
		wordView.word.setText(words.get(pos).word);
		wordView.meaning.setText(words.get(pos).meaning);

		Ion.with(wordView.photo).error(R.drawable.ic_launcher)
				.load(words.get(pos).photo);

	}

}
