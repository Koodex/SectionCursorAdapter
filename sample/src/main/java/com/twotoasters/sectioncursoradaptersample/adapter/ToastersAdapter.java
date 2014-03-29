package com.twotoasters.sectioncursoradaptersample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;
import com.twotoasters.sectioncursoradapter.SectionCursorAdapter;
import com.twotoasters.sectioncursoradaptersample.R;
import com.twotoasters.sectioncursoradaptersample.database.ToasterModel;
import com.twotoasters.sectioncursoradaptersample.transformation.SquareTransformation;

import java.util.SortedMap;
import java.util.TreeMap;

public class ToastersAdapter extends SectionCursorAdapter {

    private final SquareTransformation mToasterTrans;
    private final SquareTransformation mHumanTrans;

    public ToastersAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);

        mToasterTrans = new SquareTransformation(true);
        mHumanTrans = new SquareTransformation(false);
    }

    @Override
    protected SortedMap<Integer, Object> buildSections(Cursor cursor) {
        TreeMap<Integer, Object> sectionMap = new TreeMap<Integer, Object>();
        // It is safe to have just one model because I know i have data in ever cell.
        final ToasterModel toaster = new ToasterModel();
        int position = 0;
        while (cursor.moveToNext() == true) {
            toaster.loadFromCursor(cursor);
            if (!sectionMap.containsValue(toaster.shortJob)) {
                sectionMap.put(position, toaster.shortJob);
                position++;
            }
            position++;
        }
        return sectionMap;
    }

    @Override
    protected View newSectionView(Context context, Object item, ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.item_section, parent, false);
    }

    @Override
    protected void bindSectionView(View convertView, Context context, int position, Object item) {
        ((TextView) convertView).setText((String) item);
    }

    @Override
    protected View newItemView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = getLayoutInflater().inflate(R.layout.item_toaster, parent, false);
        ViewHolder holder = new ViewHolder(convertView);
        convertView.setTag(holder);

        return convertView;
    }
    static String url;
    @Override
    protected void bindItemView(View convertView, Context context, Cursor cursor) {
        final ToasterModel toaster = new ToasterModel();
        toaster.loadFromCursor(cursor);

        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.txtName.setText(toaster.name);
        holder.txtJob.setText(toaster.jobDescription);


        Picasso.with(context).load(toaster.imageUrl).transform(mToasterTrans).into(holder.imgToaster);
        Picasso.with(context).load(toaster.imageUrl).transform(mHumanTrans).into(holder.imgHuman);

        if (holder.switcher.getDisplayedChild() != 0) {
            holder.switcher.showPrevious();
        };
        holder.switcher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewSwitcher) v).showNext();
            }
        });
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtJob;
        ImageView imgToaster;
        ImageView imgHuman;
        ViewSwitcher switcher;

        private ViewHolder(View convertView) {
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtJob = (TextView) convertView.findViewById(R.id.txtJob);
            imgToaster = (ImageView) convertView.findViewById(R.id.imgToaster);
            imgHuman = (ImageView) convertView.findViewById(R.id.imgHuman);
            switcher = (ViewSwitcher) convertView.findViewById(R.id.switcherImg);
        }
    }
}
