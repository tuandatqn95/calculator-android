package com.tuandat.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    TextView tvResult, tvExpression;

    Stack<String> stackExpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);
        stackExpression = new Stack<String>();
        stackExpression.push("0");
    }

    public void btnNum_Click(View view) {
        String expElement = stackExpression.pop();
        if (isOperator(expElement)) {
            stackExpression.push(expElement);
            tvResult.setText(((Button) view).getText());

        } else if (expElement == "0") {

            tvResult.setText(((Button) view).getText());
        } else {

            tvResult.setText(expElement + ((Button) view).getText());
        }

        stackExpression.push(tvResult.getText().toString());
        tvExpression.setText(getExpression());
    }

    public void btnAC_Click(View view) {
        tvResult.setText("0");
        tvExpression.setText("0");
        while (!stackExpression.isEmpty()) stackExpression.pop();
        stackExpression.push("0");
    }


    public void btnOperator_Click(View view) {

        String operator = ((Button) view).getText().toString();
        if (isOperator(stackExpression.peek())) {
            stackExpression.pop();
        }
        if(stackExpression.size() == 1){
            stackExpression.pop();
            stackExpression.push(String.valueOf(tvResult.getText().toString()));
        }

        stackExpression.push(operator);
        tvResult.setText(getResultString(getResult()));
        tvExpression.setText(getExpression());
    }


    private String getExpression() {
        String exp = "";
        for (String expElement : stackExpression) {
            exp += " " + expElement;
        }
        return exp;
    }

    private boolean isOperator(String operator) {
        if (operator.length() != 1 || "+-x/=".indexOf(operator) < 0) {
            return false;
        }
        return true;
    }

    private double getResult() {
        double result = 0;
        char operator = '+';
        for (String expElement : stackExpression) {
            if (expElement == "=")
                return result;
            if (isOperator(expElement)) {
                operator = expElement.charAt(0);
            } else {

                double operand;
                if (expElement.indexOf("%") >= 0) {
                    expElement = expElement.substring(0, expElement.length() - 1);
                    operand = result * Double.parseDouble(expElement) / 100;
                } else {
                    operand = Double.parseDouble(expElement);
                }
                switch (operator) {
                    case '+':
                        result += operand;
                        break;
                    case '-':
                        result -= operand;
                        break;
                    case 'x':
                        result *= operand;
                        break;
                    case '/':
                        result /= operand;
                        break;
                }
            }
        }
        return result;
    }

    public void btnEqual_Click(View view) {
        if (stackExpression.size() == 1)
            return;
        if (isOperator(stackExpression.peek())) stackExpression.pop();
        stackExpression.push("=");

        tvExpression.setText(getExpression());
        tvResult.setText(getResultString(getResult()));
        while (!stackExpression.isEmpty()) stackExpression.pop();
        stackExpression.push("0");

    }

    private String getResultString(double result) {

        if (result % 1 == 0) {
            return String.valueOf((int) result);
        }
        return String.valueOf(result);
    }

    public void btnNegative_Click(View view) {
        double result = 0;
        if (!isOperator(stackExpression.peek())) {
            result = Double.parseDouble(stackExpression.pop());
        } else {
            result = Double.parseDouble(tvResult.getText().toString());
        }
        result *= -1;

        stackExpression.push(getResultString(result));
        tvResult.setText(getResultString(result));
        tvExpression.setText(getExpression());
    }

    public void btnDot_Click(View view) {
        if (isOperator(stackExpression.peek())) {
            tvResult.setText("0.");
        } else {
            String expElement = stackExpression.pop();
            double tempNum = Double.parseDouble(expElement);
            if (tempNum % 1 != 0) {
                stackExpression.push(expElement);
                return;
            }

            tvResult.setText(String.valueOf((int) tempNum) + ".");
        }
        stackExpression.push(tvResult.getText().toString());
        tvExpression.setText(getExpression());
    }

    public void btnPercent_Click(View view) {
        if (isOperator(stackExpression.peek()))
            return;
        String expElement = stackExpression.pop();
        stackExpression.push(expElement+"%");
        tvResult.setText(getResultString(getResult()));
        tvExpression.setText(getExpression());
    }
}
