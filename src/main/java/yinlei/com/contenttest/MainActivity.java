package yinlei.com.contenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private String url = "http://xue.hitui.com/index.php?m=api&c=index&a=api_video_tiwen_list&vid=5";

    private List<ContentBean.DataBean> mDataBeen = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private OneAdapter mOneAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManagerPlus(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        OkHttpUtils.post()
                .addParams("uid", "7538")
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String jstr = response;
                        Type type = new TypeToken<ContentBean>() {
                        }.getType();
                        ContentBean contentBean = new Gson().fromJson(jstr, type);
                        mDataBeen = contentBean.getData();
                        if (mDataBeen != null && mDataBeen.size() > 0) {
                            mOneAdapter = new OneAdapter(mDataBeen,MainActivity.this);
                            mRecyclerView.setAdapter(mOneAdapter);
                        }
                    }
                });
    }
}
