package com.example.onlinequiz.Adapter.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.onlinequiz.Common.Message;
import com.example.onlinequiz.Model.Question;
import com.example.onlinequiz.Model.QuestionInTest;
import com.example.onlinequiz.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TestQuestionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<QuestionInTest> questionInTestArrayList;

    public TestQuestionAdapter(Context context, int layout, ArrayList<QuestionInTest> questionInTestArrayList) {
        this.context = context;
        this.layout = layout;
        this.questionInTestArrayList = questionInTestArrayList;
    }

    @Override
    public int getCount() {
        return questionInTestArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        RelativeLayout rltMessage, rltMain, textAnswerContainer, pictureAnswerContainer;
        TextView txtQuestion, txtA, txtB, txtC, txtD, txtMessage;
        ImageView imgA, imgB, imgC, imgD, imgStatus, imgQuestion;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestQuestionAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new TestQuestionAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
            holder.txtA = (TextView) convertView.findViewById(R.id.txtA);
            holder.txtB = (TextView) convertView.findViewById(R.id.txtB);
            holder.txtC = (TextView) convertView.findViewById(R.id.txtC);
            holder.txtD = (TextView) convertView.findViewById(R.id.txtD);

            holder.imgA = (ImageView) convertView.findViewById(R.id.imgA);
            holder.imgB = (ImageView) convertView.findViewById(R.id.imgB);
            holder.imgC = (ImageView) convertView.findViewById(R.id.imgC);
            holder.imgD = (ImageView) convertView.findViewById(R.id.imgD);
            holder.imgStatus = (ImageView) convertView.findViewById(R.id.imgStatus);
            holder.imgQuestion = (ImageView) convertView.findViewById(R.id.imgQuestion);

            holder.txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
            holder.rltMessage = (RelativeLayout) convertView.findViewById(R.id.rltMessage);
            holder.rltMain = (RelativeLayout) convertView.findViewById(R.id.rltMain);
            holder.textAnswerContainer = (RelativeLayout) convertView.findViewById(R.id.textAnswerContainer);
            holder.pictureAnswerContainer = (RelativeLayout) convertView.findViewById(R.id.pictureAnswerContainer);
            convertView.setTag(holder);
        } else
            holder = (TestQuestionAdapter.ViewHolder) convertView.getTag();

        QuestionInTest questionInTest = questionInTestArrayList.get(position);
        int no = position + 1;
        if (questionInTest.getQuestion() != null) {
            solveAnswerContainerVisible(questionInTest, holder);
            // display question
            displayQuestion(position, questionInTest, holder);

            // display answer
            displayAnswer(questionInTest, holder);

            // set status image
            setStatusImage(questionInTest.getUserAnswer().equals(questionInTest.getQuestion().getCorrectAnswer()), holder);
            holder.rltMain.setVisibility(View.VISIBLE);
            holder.rltMessage.setVisibility(View.GONE);
        } else {
            String message = no + ". " + Message.failedToDisplayQuestion;
            holder.txtMessage.setText(message);
            holder.rltMessage.setVisibility(View.VISIBLE);
            holder.rltMain.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void displayQuestion(int index, QuestionInTest questionInTest, ViewHolder holder) {
        Question question = questionInTest.getQuestion();
        if (question.getIsImageQuestion().equals("true")) {
            Picasso.with(context)
                    .load(question.getQuestion())
                    .into(holder.imgQuestion);
            holder.imgQuestion.setVisibility(View.VISIBLE);
            holder.txtQuestion.setVisibility(View.GONE);
        } else {
            String questionString = (index + 1) + ". " + questionInTest.getQuestion().getQuestion();
            holder.txtQuestion.setText(questionString);
            holder.txtQuestion.setVisibility(View.VISIBLE);
            holder.imgQuestion.setVisibility(View.GONE);
        }
    }

    // TEXT
    private TextView getTextViewByLetter(String letter, ViewHolder holder) {
        TextView result = null;
        TextView[] textViewArr = new TextView[]{holder.txtA, holder.txtB, holder.txtC, holder.txtD};
        int index = Question.getLetterIndex(letter);
        if (index >= 0) result = textViewArr[index];
        return result;
    }

    private void displayAnswer(QuestionInTest questionInTest, ViewHolder holder) {
        if (questionInTest.getQuestion().getIsImageAnswer().equals("true")) {
            displayPictureAnswer(questionInTest, holder);
        } else {
            displayTextAnswer(questionInTest, holder);
        }
    }

    private void displayTextAnswer(QuestionInTest questionInTest, ViewHolder holder) {
        ArrayList<String> answerOrder = questionInTest.getAnswerOrder();
        Question question = questionInTest.getQuestion();
        displayTextAnswerByLetter(answerOrder.get(0), question.getA(), questionInTest, holder);
        displayTextAnswerByLetter(answerOrder.get(1), question.getB(), questionInTest, holder);
        displayTextAnswerByLetter(answerOrder.get(2), question.getC(), questionInTest, holder);
        displayTextAnswerByLetter(answerOrder.get(3), question.getD(), questionInTest, holder);
    }

    @SuppressLint("ResourceAsColor")
    private void displayTextAnswerByLetter(String letter, String answer, QuestionInTest questionInTest, ViewHolder holder) {
        Question question = questionInTest.getQuestion();
        TextView txt = getTextViewByLetter(letter, holder);
        boolean isCorrectAnswerChosen = false;

        // format background
        if (txt != null) {
            int backgroundDrawable;
            String textColorHexCode = null;
            if (isCorrectAnswerSelected(questionInTest, answer)) {
                backgroundDrawable = R.drawable.choose_correct_test_quetsion_answer_button;
                isCorrectAnswerChosen = true;
            } else if (questionInTest.getUserAnswer().equals(answer))
                // choose wrong answer
                backgroundDrawable = R.drawable.choose_wrong_test_quetsion_answer_button;
            else if (question.getCorrectAnswer().equals(answer))
                // correct answer
                backgroundDrawable = R.drawable.choose_correct_test_quetsion_answer_button;
            else {
                textColorHexCode = "#757575";
                backgroundDrawable = R.drawable.test_quetsion_answer_button;
            }
            // set text color
            if (textColorHexCode != null) txt.setTextColor(Color.parseColor(textColorHexCode));
            else txt.setTextColor(Color.WHITE);

            // set background
            txt.setBackground(ContextCompat.getDrawable(context, backgroundDrawable));

            // set text
            txt.setText(answer);
        }
    }

    // PICTURE
    private ImageView getImageViewByLetter(String letter, ViewHolder holder) {
        ImageView result = null;
        ImageView[] textViewArr = new ImageView[]{holder.imgA, holder.imgB, holder.imgC, holder.imgD};
        int index = Question.getLetterIndex(letter);
        if (index >= 0) result = textViewArr[index];
        return result;
    }

    private void displayPictureAnswer(QuestionInTest questionInTest, ViewHolder holder) {
        Question question = questionInTest.getQuestion();
        ArrayList<String> answerOrder = questionInTest.getAnswerOrder();
        displayPictureAnswerByLetter(answerOrder.get(0), question.getA(), questionInTest, holder);
        displayPictureAnswerByLetter(answerOrder.get(1), question.getB(), questionInTest, holder);
        displayPictureAnswerByLetter(answerOrder.get(2), question.getC(), questionInTest, holder);
        displayPictureAnswerByLetter(answerOrder.get(3), question.getD(), questionInTest, holder);
    }

    private void displayPictureAnswerByLetter(String letter, String answer, QuestionInTest questionInTest, ViewHolder holder) {
        Question question = questionInTest.getQuestion();
        ImageView img = getImageViewByLetter(letter, holder);
        boolean isCorrectAnswerChosen = false;
        if (img != null) {
            Picasso.with(context)
                    .load(answer)
                    .into(img);

            // format
            int backgroundDrawable;
            if (isCorrectAnswerSelected(questionInTest, answer)) {
                backgroundDrawable = R.drawable.quize_light_success;
                isCorrectAnswerChosen = true;
            } else if (questionInTest.getUserAnswer().equals(answer))
                // choose wrong answer
                backgroundDrawable = R.drawable.quize_danger;
            else if (question.getCorrectAnswer().equals(answer))
                // correct answer
                backgroundDrawable = R.drawable.quize_light_success;
            else {
                backgroundDrawable = R.drawable.quize_light;
            }

            // set background
            img.setBackground(ContextCompat.getDrawable(context, backgroundDrawable));

        }

    }

    private void setStatusImage(boolean isCorrect, ViewHolder holder) {
        if (isCorrect) {
            holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.correct));
        } else
            holder.imgStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.incorrect));
    }

    private void solveAnswerContainerVisible(QuestionInTest questionInTest, ViewHolder holder) {
        if (questionInTest.getQuestion().getIsImageAnswer().equals("true")) {
            holder.pictureAnswerContainer.setVisibility(View.VISIBLE);
            holder.textAnswerContainer.setVisibility(View.GONE);
        } else {
            holder.textAnswerContainer.setVisibility(View.VISIBLE);
            holder.pictureAnswerContainer.setVisibility(View.GONE);
        }
    }

    boolean isCorrectAnswerSelected(QuestionInTest questionInTest, String answer) {
        Question question = questionInTest.getQuestion();
        return (question.getCorrectAnswer().equals(questionInTest.getUserAnswer())) && (question.getCorrectAnswer().equals(answer));
    }
}
