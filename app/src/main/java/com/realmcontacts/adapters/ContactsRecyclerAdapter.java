package com.realmcontacts.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.realmcontacts.R;
import com.realmcontacts.model.Contacts;
import com.realmcontacts.utils.CropCircleTransformation;
import com.realmcontacts.widgets.ContactImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by Yuriy on 2016-03-28 RealmContacts.
 */
public class ContactsRecyclerAdapter
        extends RealmBasedRecyclerViewAdapter<Contacts, ContactsRecyclerAdapter.ViewHolder> {

    private Context mContext;

    public ContactsRecyclerAdapter(Context context, RealmResults<Contacts> realmResults, boolean automaticUpdate, boolean animateResults, String animateExtraColumnName) {
        super(context, realmResults, automaticUpdate, animateResults, animateExtraColumnName);
        mContext = context;
    }

    public ContactsRecyclerAdapter(Context context, RealmResults<Contacts> realmResults, boolean automaticUpdate, boolean animateResults, boolean addSectionHeaders, String headerColumnName) {
        super(context, realmResults, automaticUpdate, animateResults, addSectionHeaders, headerColumnName);
        mContext = context;
    }

    @Override
    public String createHeaderFromColumnValue(String columnValue) {
        return super.createHeaderFromColumnValue(columnValue);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_item_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final Contacts contacts = realmResults.get(position);
        viewHolder.contactName.setText(contacts.getName());
        viewHolder.contactType.setText("Mobile");

        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);

        picasso
                .load(contacts.getThumbnail())
                .fit()
                .centerCrop()
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.ic_contact)
                .tag(mContext)
                .into(viewHolder.contactImage);
    }

    public class ViewHolder extends RealmViewHolder {

        @Bind(R.id.contact_image)
        ContactImageView contactImage;
        @Bind(R.id.contact_text_view)
        TextView contactName;
        @Bind(R.id.contact_type)
        TextView contactType;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
