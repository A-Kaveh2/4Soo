package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.friend.RequestCancelFriendship;
import ir.rasen.charsoo.view.interface_m.ICancelFriendship;
import ir.rasen.charsoo.view.widgets.MaterialProgressBar;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.buttons.NoShadowFloatButton;
import ir.rasen.charsoo.view.widgets.imageviews.ImageViewCircle;

/**
 * Created by android on 3/7/2015.
 * Edited by Sina KH on 23/6/2015 : Changing view to new layout (item_user.xml) and optimizations
 */
public class AdapterUsersFromBAItems extends BaseAdapter implements ICancelFriendship {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    SimpleLoader simpleLoader;
    ICancelFriendship iCancelFriendship;
    //IWebserviceResponse iWebserviceResponse;
    //WaitDialog progressDialog;
    int visitedId;
    Mode mode;

    public enum Mode {
        USERS, OWN_FOLLOWERS
    }

    public AdapterUsersFromBAItems(Context context, int visitedId, ArrayList<BaseAdapterItem> items, Mode mode) {
        this.context = context;
        this.items = items;
        simpleLoader = new SimpleLoader(context);
        iCancelFriendship = this;
        //this.iWebserviceResponse = iWebserviceResponse;
        //this.progressDialog = progressDialog;
        this.visitedId = visitedId;
        this.mode = mode;
    }

    public void loadMore(ArrayList<BaseAdapterItem> newItem){
        this.items.addAll(newItem);
        notifyDataSetChanged();
    }

    public void notifyRemoveAllItems(){
        items.clear();
        notifyDataSetChanged();
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
        Holder holder;

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false);
            holder.imageView = (ImageViewCircle) view.findViewById(R.id.user_img);
            holder.name = (TextViewFont) view.findViewById(R.id.user_name);
            holder.id = (TextViewFont) view.findViewById(R.id.user_id);
            holder.progressBar = (MaterialProgressBar) view.findViewById(R.id.user_pb);
            holder.request = (NoShadowFloatButton) view.findViewById(R.id.user_send_request);
            holder.remove = (NoShadowFloatButton) view.findViewById(R.id.user_remove);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context, items.get(position).getId());
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RequestCancelFriendship(context, LoginInfo.getUserId(context), items.get(position).getId(), iCancelFriendship).execute();
                items.remove(position);
                notifyDataSetChanged();
            }
        });

        // TODO:: WE SHOULD GET ID AND TITLE SEPERATELY TO USE HERE...
        //holder.id.setText(items.get(position).getTitle());
        holder.id.setVisibility(View.GONE);

        //download image with customized class via imageId
        simpleLoader.loadImage(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.USER, holder.imageView, holder.progressBar);
        holder.name.setText(items.get(position).getTitle());

        switch (mode) {
            case USERS:
                // TODO:: we should get friendship status to know when 'request' button should be available and...
                holder.request.setVisibility(View.GONE);
                if (visitedId == LoginInfo.getUserId(context)) {
                    holder.remove.setVisibility(View.VISIBLE);
                }
                break;
            case OWN_FOLLOWERS:
                holder.request.setVisibility(View.GONE);
                holder.remove.setVisibility(View.GONE);
                break;
        }

        return view;
    }

    @Override
    public void notifyDeleteFriend(int userId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == userId)
                items.remove(i);
        }
        notifyDataSetChanged();
    }
    //Each item in this adapter has a picture and a title

    private class Holder {
        ImageViewCircle imageView;
        TextViewFont name, id;
        MaterialProgressBar progressBar;
        NoShadowFloatButton request, remove;
    }
}
