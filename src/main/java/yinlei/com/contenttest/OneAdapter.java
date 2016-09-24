package yinlei.com.contenttest;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: OneAdapter.java
 * @author: 若兰明月
 * @date: 2016-09-24 22:00
 */

public class OneAdapter extends RecyclerView.Adapter<OneAdapter.ViewHolder> {

    private Context mContext;
    private List<ContentBean.DataBean> mDataBeen = new ArrayList<>();
    private List<ContentBean.DataBean.AnswerBean> mItemAnswerBeen = new ArrayList<>();
    private SecondAdapter mSecondAdapter;

    public OneAdapter(List<ContentBean.DataBean> dataBeen, Context context) {
        this.mDataBeen = dataBeen;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContentBean.DataBean bean = mDataBeen.get(position);
        mItemAnswerBeen = bean.getAnswer();
        Long addtime = Long.valueOf(bean.getAddtime());
        Long result_time = addtime * 1000;
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(result_time);
        // SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");//等价于
        Glide.with(mContext).load(bean.getUser_face()).asBitmap().transform(new GlideCircleTransform(mContext)).into(holder.iv_tour);
        holder.tv_name.setText(bean.getUsername());

        holder.tv_content.setText(bean.getQuestion());

        holder.tv_time.setText(date);

        if (mItemAnswerBeen != null && mItemAnswerBeen.size() > 0) {
            holder.second_recycler_view.setLayoutManager(new LinearLayoutManagerPlus(mContext));
            holder.second_recycler_view.setItemAnimator(new DefaultItemAnimator());
            holder.second_recycler_view.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
            mSecondAdapter = new SecondAdapter(mItemAnswerBeen,bean.getUsername());
            holder.second_recycler_view.setAdapter(mSecondAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return mDataBeen == null ? 0 : mDataBeen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView iv_tour, image_talk, img_love;
        private TextView tv_name, tv_time, tv_content;
        private RecyclerView second_recycler_view;

        public ViewHolder(View itemView) {
            super(itemView);
            image_talk = (ImageView) itemView.findViewById(R.id.image_talk);
            img_love = (ImageView) itemView.findViewById(R.id.img_love);
            iv_tour = (ImageView) itemView.findViewById(R.id.iv_tour);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            second_recycler_view = (RecyclerView) itemView.findViewById(R.id.content_second_list);
        }
    }


    public class SecondAdapter extends RecyclerView.Adapter<SecondViewHolder> {

        private List<ContentBean.DataBean.AnswerBean> mAnswerBeen = new ArrayList<>();
        private String mUserName;

        public SecondAdapter(List<ContentBean.DataBean.AnswerBean> answerBeen,String username) {
            this.mAnswerBeen = answerBeen;
           this.mUserName = username;
        }

        @Override
        public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         /*   //解决方式：
            在填充该item布局的时候viewgroup填写null

            原因：填写viewgroup的时候会利用viewgroup的根节点参数去填充布局，不填写则会默认用空的参数去填充，这样才能展示完整。

            如果以上方式还是显示不全，那就在createViewHolder（）的时候去直接计算并指定内部RecyclerView的Height。

            PS：
            在自定义的布局中，重写
            onTouchEvent（）
            onInterceptHoverEvent（）
            直接返回false，会一定的提升页面流畅度
            （减少计算事件分发代码计算量）*/
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_content_item, null, false);
            return new SecondViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SecondViewHolder holder, int position) {
            ContentBean.DataBean.AnswerBean answerBean = mAnswerBeen.get(position);
            StringBuffer sb = new StringBuffer();
            sb.append(answerBean.getUsername());
            sb.append(":@");
            sb.append(mUserName);
            sb.append(" "+answerBean.getAnswer());
            int length = mUserName.length() + answerBean.getUsername().length() +2;
            SpannableString sp = new SpannableString(sb.toString());
            sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.Blue)),0,length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mTvContent.setText(sp);
        }


        @Override
        public int getItemCount() {
            return mAnswerBeen == null ? 0 : mAnswerBeen.size();
        }
    }

    public class SecondViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvContent;

        public SecondViewHolder(View itemView) {
            super(itemView);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
