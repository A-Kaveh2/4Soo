package ir.rasen.charsoo.view.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.BaseAdapterItem;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.friend.AnswerRequestFriendship;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.ImageViewCircle;


/**
 * Created by android on 3/7/2015.
 */
public class AdapterUserFriendshipRequest extends BaseAdapter {

    private ArrayList<BaseAdapterItem> items;
    private Context context;
    SimpleLoader simpleLoader;
    ListView listView;
    ArrayList<BaseAdapterItem> acceptedUsers;



    public ArrayList<BaseAdapterItem> getRemainingFriendRequests(){
        return items;
    }

    public AdapterUserFriendshipRequest(Context context, ArrayList<BaseAdapterItem> items) {
        this.context = context;
        this.items = items;
        simpleLoader = new SimpleLoader(context);
        acceptedUsers = new ArrayList<>();

    }

    public void loadMore(ArrayList<BaseAdapterItem> newItem){
        this.items.addAll(newItem);
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
        final Holder holder;

        if (listView == null) {
            listView = (ListView) viewGroup;
            listView.setSelector(new ColorDrawable(0x00ffffff));
        }

        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.item_friendship_requests, viewGroup, false);
            holder.imageViewImage = (ImageViewCircle) view.findViewById(R.id.imageView_base_adapter_item_image);
            holder.textViewUserIdentifier = (TextViewFont) view.findViewById(R.id.textView_base_adapter_item_title);
            holder.imageViewYes = (ImageView) view.findViewById(R.id.imageView_yes);
            holder.imageViewNo = (ImageView) view.findViewById(R.id.imageView_no);
            view.setTag(holder);
        } else
            holder = (Holder) view.getTag();

        holder.textViewUserIdentifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.goUserHomeInfoPage(context,items.get(position).getId());
            }
        });
        holder.imageViewYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerYes(position, holder);
            }
        });
        holder.imageViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerNo(position,holder);
            }
        });
        //download image with customized class via imageId
        simpleLoader.loadImage(items.get(position).getImageId(), Image_M.SMALL, Image_M.ImageType.USER, holder.imageViewImage);
        holder.textViewUserIdentifier.setText(items.get(position).getTitle());

        return view;
    }


    private void answerNo(int position,Holder holder) {
        new AnswerRequestFriendship(context,LoginInfo.getUserId(context), items.get(position).getId(), false).execute();
        items.remove(position);
        notifyDataSetChanged();

    }

    private void answerYes(final int position, Holder holder) {
        holder.imageViewYes.setImageResource(R.drawable.ic_check_green);
        holder.imageViewNo.setVisibility(View.GONE);
        acceptedUsers.add(items.get(position));
        new AnswerRequestFriendship(context,LoginInfo.getUserId(context), items.get(position).getId(), true).execute();
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                int p=Integer.valueOf(integers[0]);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                items.remove(p);
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                notifyDataSetChanged();
            }
        }.execute(position);
    }

    public ArrayList<BaseAdapterItem> getAcceptedUsers() {
        return acceptedUsers;
    }

    private class Holder {
        ImageViewCircle imageViewImage;
        TextViewFont textViewUserIdentifier;
        ImageView imageViewYes;
        ImageView imageViewNo;
    }

}
