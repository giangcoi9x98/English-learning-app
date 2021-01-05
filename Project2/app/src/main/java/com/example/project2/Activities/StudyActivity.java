package com.example.project2.Activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project2.Models.TopicModel;
import com.example.project2.Models.WordModels;
import com.example.project2.R;
import com.example.project2.databases.DataBaseUtils;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyActivity extends AppCompatActivity {
    public static String KEY_TOPIC = "key_topic";
    private static final String TAG = "StudyActivity";
    @BindView(R.id.img_speak)
    ImageView btnSpeak;
    private WordModels wordModels;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name_topic)
    TextView tvNameTopic;
    @BindView(R.id.tv_origin)
    TextView tvOrigin;
    @BindView(R.id.tv_pronun)
    TextView tvPronun;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.tv_explain)
    TextView tvExplain;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.iv_word)
    ImageView ivWord;
    @BindView(R.id.tv_example)
    TextView tvExample;
    @BindView(R.id.tv_example_trans)
    TextView tvExampleTrans;
    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.tv_didnt_know)
    TextView tvDidntKnow;
    @BindView(R.id.tv_knew)
    TextView tvKnew;
    @BindView(R.id.cl_detail_part)
    ConstraintLayout clDetailPart;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.cl_full)
    ConstraintLayout clFull;
    @BindView(R.id.cv_word)
    CardView cvWord;
    @BindView(R.id.rl_background)
    RelativeLayout rlBackground;
    int preWordId = -1;
    public AnimatorSet animatorSet;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this);
        textToSpeech = new TextToSpeech(StudyActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.UK);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e(TAG, "onInit:Language not supported ");
                    } else {
                        btnSpeak.setEnabled(true);
                    }
                }else{
                    Log.e(TAG, "onInit: Failed" );
                }
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =tvOrigin.getText().toString();
                textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        loadData();
    }

    private void nextWord(boolean isKnown) {
        setupAnimation(R.animator.animation_move_to_left);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                DataBaseUtils.getInstance(StudyActivity.this).updateWordLevel(wordModels, isKnown);
                loadData();
                clFull.setLayoutTransition(null);
                changeContent(true);
                setupAnimation(R.animator.animation_move_from_right);

            }
        });
    }

    private void setupAnimation(int animatorId) {
        animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(StudyActivity.this, animatorId);
        animatorSet.setTarget(cvWord);
        animatorSet.start();
    }

    private void loadData() {

        TopicModel topicModel = (TopicModel) getIntent().getSerializableExtra(KEY_TOPIC);
        Log.d(TAG, "onCreate: " + TopicModel.class.getName());
        tvNameTopic.setText(topicModel.getName());
        rlBackground.setBackgroundColor(Color.parseColor(topicModel.getColor()));
        wordModels = DataBaseUtils.getInstance(this).getRandomWord(topicModel.getId(), preWordId);
        preWordId = wordModels.getId();
        tvOrigin.setText(wordModels.getOrigin());
        tvPronun.setText(wordModels.getPronunciation());
        tvType.setText(wordModels.getType());
        tvExample.setText(wordModels.getExample());
        tvExplain.setText(wordModels.getExplanation());
        tvExampleTrans.setText(wordModels.getExample_trans());

        Glide.with(this).load(wordModels.getImageUrl()).into(ivWord);
        switch (wordModels.getLevel()) {
            case 0:
                tvLevel.setText("New Word");
                break;
            case 1:
            case 2:
            case 3:
                tvLevel.setText("Review");
                break;
            case 4:
                tvLevel.setText("Master");
        }

    }


    @OnClick({R.id.iv_back, R.id.tv_details, R.id.tv_didnt_know, R.id.tv_knew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_details:
                clFull.setLayoutTransition(new LayoutTransition());
                changeContent(false);
                break;
            case R.id.tv_didnt_know:
                // changeContent(true);
                // DataBaseUtils.getInstance(this).updateWordLevel(wordModels,false);
                //  loadData();
                nextWord(false);
                break;
            case R.id.tv_knew:
                //DataBaseUtils.getInstance(this).updateWordLevel(wordModels,true);
                //loadData();
                nextWord(true);
                //  changeContent(true);
                break;
        }
    }

    private void changeContent(boolean isExpanded) {
        if (isExpanded) {
            clDetailPart.setVisibility(View.GONE);
            tvDetails.setVisibility(View.VISIBLE);
        } else {
            clDetailPart.setVisibility(View.VISIBLE);
            tvDetails.setVisibility(View.GONE);
        }
    }


}
