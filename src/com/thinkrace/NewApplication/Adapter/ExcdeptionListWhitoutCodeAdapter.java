package com.thinkrace.NewApplication.Adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.thingrace.newapplication.R;
import com.thinkrace.NewApplication.Model.ExcdeptionListWhitoutCodeModel;
import com.thinkrace.NewApplication.Model.ToolsClass;
import com.thinkrace.NewApplication.Uti.CircularImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExcdeptionListWhitoutCodeAdapter extends BaseAdapter {
	private List<ExcdeptionListWhitoutCodeModel> list = null;
	private Context mContext;
	private FinalBitmap finalBitmap;

	public ExcdeptionListWhitoutCodeAdapter(Context mContext,
			List<ExcdeptionListWhitoutCodeModel> list) {
		this.mContext = mContext;
		this.list = list;
		finalBitmap = finalBitmap.create(mContext);
		finalBitmap.configLoadingImage(R.drawable.app_defult_headimage);
		finalBitmap.configLoadfailImage(R.drawable.app_defult_headimage);
	}

	public void updateListView(List<ExcdeptionListWhitoutCodeModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ItemView itemView = null;
		if (view == null) {
			itemView = new ItemView();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.excdeption_list_whitout_code_item_view, null);

			itemView.CreateDate_TextView = (TextView) view
					.findViewById(R.id.CreateDate_TextView);
			itemView.Head_ImageView = (CircularImage) view
					.findViewById(R.id.Head_ImageView);
			itemView.NickName_TextView = (TextView) view
					.findViewById(R.id.NickName_TextView);
			itemView.Message_TextView = (TextView) view
					.findViewById(R.id.Message_TextView);

			view.setTag(itemView);
		} else {
			itemView = (ItemView) view.getTag();
		}
		finalBitmap.display(itemView.Head_ImageView, list.get(position).Avatar);
		itemView.CreateDate_TextView.setText(new ToolsClass().getStringToCal(
				list.get(position).CreateDate).subSequence(5, 16));
		itemView.NickName_TextView.setText(list.get(position).Nickname);
		itemView.Message_TextView.setText(list.get(position).Message);

		return view;

	}

	final static class ItemView {
		TextView CreateDate_TextView;
		CircularImage Head_ImageView;
		TextView NickName_TextView;
		TextView Message_TextView;
	}

}
