package com.example.firstapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.firstapplication.databinding.ActivityMainBinding;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean lastNumeric = false;
    private boolean stateError = false;
    private boolean lastDot = false;

    private Expression expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onAllClearClick(View view) {
        binding.solutionTv.setText("");
        binding.resultTv.setText("");
        stateError = false;
        lastDot = false;
        lastNumeric = false;
        binding.resultTv.setVisibility(View.GONE);
    }

    public void onEqualClick(View view) {
        OnEqual();
        String resultText = binding.resultTv.getText().toString();
        if (resultText.length() > 1) {
            binding.solutionTv.setText(resultText.substring(1));
        } else {
            binding.solutionTv.setText("");
        }
    }

    public void onDigitClick(View view) {
        if (stateError) {
            binding.solutionTv.setText(((Button) view).getText());
            stateError = false;
        } else {
            binding.solutionTv.append(((Button) view).getText());
        }
        lastNumeric = true;
        OnEqual();
    }

    public void onOperatorClick(View view) {
        if (!stateError && lastNumeric) {
            binding.solutionTv.append(((Button) view).getText());
            lastDot = false;
            lastNumeric = false;
            OnEqual();
        }
    }

    public void onBackClick(View view) {
        String solutionText = binding.solutionTv.getText().toString();
        if (!solutionText.isEmpty()) {
            binding.solutionTv.setText(solutionText.substring(0, solutionText.length() - 1));
            try {
                char lastChar = solutionText.charAt(solutionText.length() - 1);
                if (Character.isDigit(lastChar)) {
                    OnEqual();
                }
            } catch (Exception e) {
                binding.resultTv.setText("");
                binding.resultTv.setVisibility(View.GONE);
                Log.e("last char error", e.toString());
            }
        }
    }

    public void onClearClick(View view) {
        binding.solutionTv.setText("");
        lastNumeric = false;
        stateError = false;
        lastDot = false;
        binding.resultTv.setVisibility(View.GONE);
    }

    public void OnEqual() {
        if (lastNumeric && !stateError) {
            String txt = binding.solutionTv.getText().toString();
            try {
                expression = new ExpressionBuilder(txt).build();
                double result = expression.evaluate();
                binding.resultTv.setVisibility(View.VISIBLE);
                binding.resultTv.setText(""+result);
            } catch (ArithmeticException ex) {
                Log.e("evaluate error", ex.toString());
                binding.resultTv.setText("Error");
                stateError = true;
                lastNumeric = false;

            }
        }
    }
}
