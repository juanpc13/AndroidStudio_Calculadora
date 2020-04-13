package ues.fmocc.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResultado = (TextView)findViewById(R.id.txtResultado);

        for (int i = 0; i <= 9; i++) {
            int id = getResources().getIdentifier("btn"+i, "id", getPackageName());
            ((Button) findViewById(id)).setOnClickListener(this);
        }
        ((Button) findViewById(R.id.btnDot)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSuma)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnResta)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnMultiplicacion)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnDivision)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnClear)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnPower)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnCuadrado)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnRaiz)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnIgual)).setOnClickListener(this);

    }

    private boolean letDecimal(String cadena){
        if(cadena.length() == 0){
            return false;
        }

        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) == '.'){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn0:
                txtResultado.setText(txtResultado.getText() + "0");
                break;
            case R.id.btn1:
                txtResultado.setText(txtResultado.getText() + "1");
                break;
            case R.id.btn2:
                txtResultado.setText(txtResultado.getText() + "2");
                break;
            case R.id.btn3:
                txtResultado.setText(txtResultado.getText() + "3");
                break;
            case R.id.btn4:
                txtResultado.setText(txtResultado.getText() + "4");
                break;
            case R.id.btn5:
                txtResultado.setText(txtResultado.getText() + "5");
                break;
            case R.id.btn6:
                txtResultado.setText(txtResultado.getText() + "6");
                break;
            case R.id.btn7:
                txtResultado.setText(txtResultado.getText() + "7");
                break;
            case R.id.btn8:
                txtResultado.setText(txtResultado.getText() + "8");
                break;
            case R.id.btn9:
                txtResultado.setText(txtResultado.getText() + "9");
                break;
            case R.id.btnDot:
                if(letDecimal(txtResultado.getText().toString())){
                    txtResultado.setText(txtResultado.getText() + ".");
                }
                break;
            case R.id.btnClear:
                txtResultado.setText("");
                break;
        }
    }
}
