package com.woohfresh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.activity.MainActivity;

public class MyAdapterSettings extends BaseAdapter {
    Context context;
    String Item[];
    String SubItem[];
    LayoutInflater inflter;

    public MyAdapterSettings(Context applicationContext, String[] Item, String[] SubItem) {
        this.context = applicationContext;
        this.Item = Item;
        this.SubItem = SubItem;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Item.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        view = inflter.inflate(R.layout.my_row_settings, null);
//        TextView item = (TextView) view.findViewById(R.id.tvSettingTitle);
//        TextView subitem = (TextView) view.findViewById(R.id.tvSettingDesc);
//        item.setText(Item[i]);
//        subitem.setText(SubItem[i]);
//        return view;
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ListContent holder;
        View v = convertView;
        if (v == null) {
            v = inflter.inflate(R.layout.my_row_settings, null);
            holder = new ListContent();
            holder.l = (LinearLayout) v.findViewById(R.id.lvSettings);
            holder.t = (TextView) v.findViewById(R.id.tvSettingTitle);
            holder.s = (TextView) v.findViewById(R.id.tvSettingDesc);

            v.setTag(holder);
            holder.l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            App.showDialogLang((MainActivity) context);
                            break;
                        case 1:
                            App.appExit((MainActivity) context);
                            break;
                    }
                }
            });
        } else {
            holder = (ListContent) v.getTag();
        }

        holder.t.setText(Item[position]);
        holder.s.setText(SubItem[position]);

        return v;
    }
}

class ListContent {

    TextView t;
    TextView s;
    LinearLayout l;

}
