package cn.rosen.sticktextview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.rosen.sticktextview.view.StickerTextView;
import cn.rosen.sticktextview.view.TextViewItem;
import cn.rosen.sticktextview.utils.CommonUtils;
import cn.rosen.sticktextview.utils.PrintUtils;

public class MainActivity extends AppCompatActivity implements StickerTextView.OnStickerTextTouchListener {

    RelativeLayout contentLayout;
    View editLayout;
    View doButton;
    private EditText editText;
    private StickerTextView stickerTextView;
    private TextViewItem textViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        contentLayout = (RelativeLayout) findViewById(R.id.content_layout);
        editLayout = findViewById(R.id.do_reset_text_layout);
        doButton = findViewById(R.id.do_reset_finish);
        editText = (EditText) findViewById(R.id.edit_text);
        addStikerTextView();
        doButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.GONE);
                CommonUtils.hideInputMethod(MainActivity.this, editText);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int nums = textViewItem.getNums();
                if(s.length()<=nums){
                    textViewItem.getTextView().setText(s.toString(),TextView.BufferType.NORMAL);
                    stickerTextView.updateTextDraw(textViewItem.getTextView(), textViewItem.isSingleLine());
                }else {
                    Toast.makeText(MainActivity.this, "字数限制"+nums, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addStikerTextView() {
        stickerTextView = new StickerTextView(getBaseContext());
        stickerTextView.setOnStickerTextTouchListener(this);
        //设置显示位置
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.CENTER_IN_PARENT);
        //设置背景
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_test);
        stickerTextView.setBackgroundBitmap(bitmap);
        //添加文字元素
        TextView s1 = new TextView(getBaseContext());
        s1.setText("静", TextView.BufferType.NORMAL);
        s1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        s1.setBackgroundColor(Color.parseColor("#00000000"));
        s1.setTextColor(Color.BLACK);
        s1.setTextSize(getTextSize(10));

        TextView s2 = new TextView(getBaseContext());
        s2.setText("默", TextView.BufferType.NORMAL);
        s2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        s2.setBackgroundColor(Color.parseColor("#00000000"));
        s2.setTextColor(Color.BLACK);
        s2.setTextSize(getTextSize(10));

        TextView s3 = new TextView(getBaseContext());
        s3.setText("时", TextView.BufferType.NORMAL);
        s3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        s3.setBackgroundColor(Color.parseColor("#00000000"));
        s3.setTextColor(Color.BLACK);
        s3.setTextSize(getTextSize(10));

        TextView s4 = new TextView(getBaseContext());
        s4.setText("光", TextView.BufferType.NORMAL);
        s4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        s4.setBackgroundColor(Color.parseColor("#00000000"));
        s4.setTextColor(Color.BLACK);
        s4.setTextSize(getTextSize(10));

        TextView s5 = new TextView(getBaseContext());
        s5.setText("我们在一起/\n不需要孤独/\n独处时的静默时光/", TextView.BufferType.NORMAL);
        s5.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        s5.setBackgroundColor(Color.parseColor("#00000000"));
        s5.setTextColor(Color.WHITE);
        s5.setLines(5);
        s5.setLineSpacing(1.0f,2.0f);
        s5.setTextSize(getTextSize(5));


        stickerTextView.addTextDraw(s1, 10, 10, 50, 50,true,1);
        stickerTextView.addTextDraw(s2, 60, 10, 100, 50,true,1);
        stickerTextView.addTextDraw(s3, 110, 10, 150, 50,true,1);
        stickerTextView.addTextDraw(s4, 160, 10, 200, 50,true,1);
        stickerTextView.addTextDraw(s5, 15, 60, 180, 200,false,30);

        contentLayout.addView(stickerTextView, rl);
    }

    private float getTextSize(float size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
    }

    @Override
    public void onTextCopy(StickerTextView stickerView) {
        PrintUtils.println("点击了复制");
    }

    @Override
    public void onTextDelete(StickerTextView stickerView) {
        PrintUtils.println("点击了删除");
        contentLayout.removeView(stickerView);
    }

    @Override
    public void onTextMoveToHead(StickerTextView stickerView) {
        PrintUtils.println("触摸到控件时会调用该方法");
    }

    @Override
    public void onTextClickCurrentText(TextViewItem textViewItem) {
        this.textViewItem = textViewItem;
        PrintUtils.println("单击调用");
        editLayout.setVisibility(View.VISIBLE);
        editText.setText(textViewItem.getTextView().getText());
        CommonUtils.showInputMethod(MainActivity.this, editText);
    }
}
