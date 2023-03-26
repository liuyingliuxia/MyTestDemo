package com.lingong.mytest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.lingong.mytest.R;

public class CalculatorActivity extends AppCompatActivity {

    private EditText editText;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private Button buttonAdd, buttonSubtract, buttonMultiply, buttonDivide;
    private Button buttonDot, buttonEqual, buttonClear;

    private float num1 = 0, num2 = 0;
    private String operator = "";
    private boolean isDotPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        editText = findViewById(R.id.editText);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonMultiply = findViewById(R.id.buttonMultiply);
        buttonDivide = findViewById(R.id.buttonDivide);

        buttonDot = findViewById(R.id.buttonDot);
        buttonEqual = findViewById(R.id.buttonEqual);
        buttonClear = findViewById(R.id.buttonClear);

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String text = button.getText().toString();
                editText.append(text);
            }
        };

        button0.setOnClickListener(numberClickListener);
        button1.setOnClickListener(numberClickListener);
        button2.setOnClickListener(numberClickListener);
        button3.setOnClickListener(numberClickListener);
        button4.setOnClickListener(numberClickListener);
        button5.setOnClickListener(numberClickListener);
        button6.setOnClickListener(numberClickListener);
        button7.setOnClickListener(numberClickListener);
        button8.setOnClickListener(numberClickListener);
        button9.setOnClickListener(numberClickListener);

        View.OnClickListener operatorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String text = button.getText().toString();
                operator = text;
                num1 = Float.parseFloat(editText.getText().toString());
                editText.setText("");
                isDotPressed = false;
            }
        };

        buttonAdd.setOnClickListener(operatorClickListener);
        buttonSubtract.setOnClickListener(operatorClickListener);
        buttonMultiply.setOnClickListener(operatorClickListener);
        buttonDivide.setOnClickListener(operatorClickListener);

        buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDotPressed) {
                    editText.append(".");
                    isDotPressed = true;
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                num1 = 0;
                num2 = 0;
                isDotPressed = false;
            }
        });

        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num2 = Float.parseFloat(editText.getText().toString());
                float result = 0;
                switch (operator) {
                    case "+":
                        result = num1 + num2;
                        break;
                    case "-":
                        result = num1 - num2;
                        break;
                    case "*":
                        result = num1 * num2;
                        break;
                    case "/":
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            editText.setText("Cannot divide by zero");
                            return;
                        }
                        break;
                }
                editText.setText(String.valueOf(result));
                isDotPressed = false;
            }
        });
    }
}