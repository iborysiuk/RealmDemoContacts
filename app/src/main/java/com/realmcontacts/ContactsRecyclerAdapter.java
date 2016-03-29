package com.realmcontacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.realmcontacts.realm.model.Contacts;

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

    public ContactsRecyclerAdapter(Context context, RealmResults<Contacts> realmResults,
                                   boolean automaticUpdate, boolean animateResults,
                                   String animateExtraColumnName) {
        super(context, realmResults, automaticUpdate, animateResults, animateExtraColumnName);
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
    }

    public class ViewHolder extends RealmViewHolder {

        @Bind(R.id.contact_text_view)
        TextView contactName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
