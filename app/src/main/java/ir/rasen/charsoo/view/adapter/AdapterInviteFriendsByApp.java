package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.view.interface_m.IFragInviteSelectionListener;
import ir.rasen.charsoo.view.interface_m.IInviteFriendByAppListener;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;

/**
 * Created by hossein-pc on 6/22/2015.
 */
public class AdapterInviteFriendsByApp extends BaseAdapter implements IFragInviteSelectionListener {

    private ArrayList<PackageInfoCustom> items;
    private Context context;
    private ArrayList<Boolean> isItemChecked;
    private Hashtable<Integer,String> appsTag;
    IInviteFriendByAppListener delegate;
    private ArrayList<ContactEntry> noneCharsooContactList;


    public AdapterInviteFriendsByApp(Context context, ArrayList<PackageInfoCustom> items,ArrayList<ContactEntry> noneCharsooContacts,IInviteFriendByAppListener delegate) {
        this.context = context;
        this.items = items;
        isItemChecked=new ArrayList<>();
        appsTag=new Hashtable<>();
        this.delegate=delegate;
        noneCharsooContactList=new ArrayList<>(noneCharsooContacts);
    }

    public void loadMore(ArrayList<PackageInfoCustom> newItem) {
        if (newItem!=null) {
            this.items.addAll(newItem);
            notifyDataSetChanged();
        }
    }

    public void resetItems(ArrayList<PackageInfoCustom> newItem) {
        if (newItem!=null) {
            this.items.addAll(newItem);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final Holder holder;

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_application_to_invite, viewGroup, false);


            holder.imageViewAppIcone=(RoundedSquareImageView) view.findViewById(R.id.imageView_ApplicationIcone);
            holder.textViewAppName=(TextViewFont) view.findViewById(R.id.textViewFont_ApplicationName);
//            holder.linearLayoutEmailListContainer=(LinearLayout) view.findViewById(R.id.ll_EmailListContainer);
            holder.linearLayoutAppContainer=(LinearLayout) view.findViewById(R.id.ll_AppContainer);


            view.setTag(holder);

        } else
            holder = (Holder) view.getTag();
        appsTag.put(position, items.get(position).appname);
//        if (appsTag.get(position).equals(Params.EMAIL_APP)){
//            holder.listViewNoneCharsooContacts=(ListView) view.findViewById(R.id.allContactsListView);
//            holder.allContactsListAdapter=new AdapterInviteNoneCharsooContact(context,noneCharsooContactList,AdapterInviteFriendsByApp.this);
//            holder.listViewNoneCharsooContacts.setAdapter(holder.allContactsListAdapter);
//        }

        holder.linearLayoutAppContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onItemClicked(appsTag.get(position));
                /*if (appsTag.get(position).equals(Params.EMAIL_APP)){
                    if (holder.linearLayoutEmailListContainer.getVisibility()==View.VISIBLE){
                        holder.linearLayoutEmailListContainer.setVisibility(View.GONE);
                        holder.linearLayoutAppContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                    else
                    {
                        holder.linearLayoutAppContainer.setBackgroundColor(context.getResources().getColor(R.color.dividerColor));
                        holder.linearLayoutEmailListContainer.setVisibility(View.VISIBLE);
                        holder.linearLayoutEmailListContainer.requestFocus();
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.linearLayoutEmailListContainer.getLayoutParams();
//                            layoutParams.height = (int) context.getResources().getDimension(R.dimen.profile_pic_thumbnail_container_widthheigth) * innerListView.getCount();
                        layoutParams.height=getSizeInPixelFromDp(72)*(noneCharsooContactList.size())+1;
                        holder.linearLayoutEmailListContainer.setLayoutParams(layoutParams);
//                            delegate.onItemClicked("");
                    }
                }*/
            }
        });
        holder.textViewAppName.setText(items.get(position).appname);
        holder.imageViewAppIcone.setImageBitmap(items.get(position).icon);

        holder.textViewAppName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onItemClicked(appsTag.get(position));
            }
        });
        holder.imageViewAppIcone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onItemClicked(appsTag.get(position));
            }
        });
//        try {
//
//
//        } catch (Exception e) {
//            String s = e.getMessage();
//        }
        return view;
    }

    @Override
    public void onItemCheckBoxClicked(int position) {

    }


    public class Holder{
        public RoundedSquareImageView imageViewAppIcone;
        public TextViewFont textViewAppName;
        public LinearLayout linearLayoutEmailListContainer;
        public ListView listViewNoneCharsooContacts;
        public AdapterInviteNoneCharsooContact allContactsListAdapter;
        public LinearLayout linearLayoutAppContainer;

    }

    public int getSizeInPixelFromDp(int dpToConvert){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpToConvert, r.getDisplayMetrics());
        return (int) px;
    }
}
