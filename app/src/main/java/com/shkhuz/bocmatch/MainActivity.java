package com.shkhuz.bocmatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Random rand = new Random();
    private ImageView[] bottles = new ImageView[7];
    private int[] bottle_colors;
    private HashMap<Integer, Integer> idToIdx = new HashMap<>();
    private Drawable empty_image_border;
    private Drawable image_border;
    private TextView match_text;
    private ImageButton check_button;

    List<Drawable> target_to_match = new ArrayList<>();
    int swapCount = 0;
    int checkCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottle_colors = new int[]{
                R.drawable.bocmatch_bottle_green,
                R.drawable.bocmatch_bottle_lightblue,
                R.drawable.bocmatch_bottle_offwhite,
                R.drawable.bocmatch_bottle_orange,
                R.drawable.bocmatch_bottle_pink,
                R.drawable.bocmatch_bottle_red,
                R.drawable.bocmatch_bottle_yellow,
        };

        bottles[0] = findViewById(R.id.bottle0);
        bottles[1] = findViewById(R.id.bottle1);
        bottles[2] = findViewById(R.id.bottle2);
        bottles[3] = findViewById(R.id.bottle3);
        bottles[4] = findViewById(R.id.bottle4);
        bottles[5] = findViewById(R.id.bottle5);
        bottles[6] = findViewById(R.id.bottle6);

        for (int i = 0; i < 7; i++) {
            idToIdx.put(bottles[i].getId(), i);
            bottles[i].setOnClickListener(this);
        }

        empty_image_border = ContextCompat.getDrawable(this, R.drawable.empty_image_border);
        image_border = ContextCompat.getDrawable(this, R.drawable.image_border);

        match_text = findViewById(R.id.match_text);
        match_text.setText("");

        check_button = findViewById(R.id.check);
        check_button.setOnClickListener(this);

        newGame();
    }

    public void newGame() {
        {
            List<Integer> bottle_colors_list = Arrays.stream(bottle_colors)
                    .boxed()
                    .collect(Collectors.toList());
            for (ImageView bottle : bottles) {
                int randidx = rand.nextInt(bottle_colors_list.size());
                bottle.setImageDrawable(ContextCompat.getDrawable(this, bottle_colors_list.get(randidx)));
                bottle_colors_list.remove(randidx);
            }
        }

        target_to_match.clear();
        {
            List<Integer> bottle_colors_list = Arrays.stream(bottle_colors)
                    .boxed()
                    .collect(Collectors.toList());
            for (int i = 0; i < 7; i++) {
                int randidx = rand.nextInt(bottle_colors_list.size());
                target_to_match.add(ContextCompat.getDrawable(this, bottle_colors_list.get(randidx)));
                bottle_colors_list.remove(randidx);
            }
        }

        swapCount = 0;
        checkCount = 0;
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (isIdBottle(vid)) {
            int idx = idToIdx.get(vid);

            int currentBottle = getCurrentlySelectedBottle();
            if (currentBottle == -1) selectBottle(idx);
            else {
                Drawable tmp = bottles[currentBottle].getDrawable();
                bottles[currentBottle].setImageDrawable(bottles[idx].getDrawable());
                bottles[idx].setImageDrawable(tmp);
                selectBottle(-1);
                swapCount++;
            }
        } else if (vid == R.id.check) {
            int matched = 0;
            for (int i = 0; i < 7; i++) {
                if (bottles[i].getDrawable().getConstantState().equals(target_to_match.get(i).getConstantState())) {
                    matched++;
                }
            }
            checkCount++;

            match_text.setText(String.format("%d matched\nchecked %d times\nswapped %d times", matched, checkCount, swapCount));
        }
    }

    private int getCurrentlySelectedBottle() {
        for (int i = 0; i < 7; i++) {
            if (!bottles[i].getBackground().getConstantState().equals(empty_image_border.getConstantState())) {
                return i;
            }
        }
        return -1;
    }

    private void selectBottle(int idx) {
        int currentBottle = getCurrentlySelectedBottle();
        if (currentBottle != -1) {
            bottles[currentBottle].setBackground(empty_image_border);
        }

        if (idx != -1) {
            bottles[idx].setBackground(image_border);
        }
    }

    public boolean isIdBottle(int vid) {
        return vid == R.id.bottle0 ||
               vid == R.id.bottle1 ||
               vid == R.id.bottle2 ||
               vid == R.id.bottle3 ||
               vid == R.id.bottle4 ||
               vid == R.id.bottle5 ||
               vid == R.id.bottle6;
    }
}