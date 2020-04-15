package ues.fmocc.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtResultado;
    private TextView txtHistorico;

    //Datos para recordar seleccion
    private String operator;
    private String firstOperator;
    private String secondOperator;
    private int lastBnt;
    private boolean clearEquals = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResultado = (TextView)findViewById(R.id.txtResultado);
        txtHistorico = (TextView)findViewById(R.id.txtHistorico);

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
        ((Button) findViewById(R.id.btnBorrar)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnCos)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSen)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnTan)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnInvert)).setOnClickListener(this);

        operator = "";
        lastBnt = 0;

    }

    public void showMessage(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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

    public String removeLastCharacter(String str) {
        String result = "";
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

    //Metodo valida que no exista un valor con punto decimal a medias (. NO)-(5. NO)-(5.5 SI)
    public boolean validInput(String cadena){
        if(cadena.length() == 0){
            return false;
        }

        if(cadena.indexOf('.') == cadena.length() - 1){
            return false;
        }
        return true;
    }

    public String removeFromString(String str, int times){
        String result = "";
        for (int i = 0; i < str.length() - times; i++) {
            result += str.charAt(i);
        }
        return result;
    }

    public boolean isButtonOperator(int lastButton){
        int operatations[] = {R.id.btnSuma, R.id.btnResta, R.id.btnMultiplicacion, R.id.btnDivision, R.id.btnCuadrado};
        for (int i = 0; i < operatations.length; i++) {
            if(operatations[i] == lastButton){
                return true;
            }
        }
        return false;
    }

    public boolean isButtonEspecial(int lastButton){
        int operatations[] = {R.id.btnCuadrado, R.id.btnRaiz, R.id.btnCos, R.id.btnSen, R.id.btnTan};
        for (int i = 0; i < operatations.length; i++) {
            if(operatations[i] == lastButton){
                return true;
            }
        }
        return false;
    }

    public boolean isButtonNumber(int idBtn){
        for (int i = 0; i <= 9; i++) {
            int id = getResources().getIdentifier("btn"+i, "id", getPackageName());
            if(id == idBtn){
                return true;
            }
        }
        return false;
    }

    public void operation(String historico, int currentBtn, int lastBnt, String operator){
        if(clearEquals){
            historico = txtResultado.getText().toString();
            historico += operator;
            clearEquals = false;
        }else if(lastBnt != currentBtn){            //Si el boton se repite no hacer nada

            //Si el anterior y el nuevo son botones operador solo sustituir la operacion
            if(isButtonOperator(lastBnt) && isButtonOperator(currentBtn) || isButtonEspecial(lastBnt)){
                historico = removeFromString(historico, 3);
                if(!historico.isEmpty()){
                    historico += operator;
                }
            }else if(validInput(txtResultado.getText().toString())){
                historico += txtResultado.getText().toString();
                historico += operator;
            }

        }
        txtHistorico.setText(historico);
    }

    public void operationEspecial(String historico, int currentBtn, int lastBnt, String firstOperator, String secondOperator){
        if(txtResultado.getText().toString().isEmpty()){
            showMessage("Favor Ingrese algun valor numerico");
            currentBtn = 0;
            return;
        }
        historico = removeFromString(historico, 3);
        if(!txtHistorico.getText().toString().isEmpty()){
            historico += " + ";
        }
        historico += firstOperator + txtResultado.getText().toString() + secondOperator;
        historico += " + ";
        txtHistorico.setText(historico);
    }

    public double solveString(final String str){
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('X')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    @Override
    public void onClick(View v) {
        int currentBtn  = v.getId();

        //Necesita refrescar el campo de resultado needsToClearInput
        if(isButtonOperator(lastBnt) && isButtonNumber(currentBtn)){
            txtResultado.setText("");
        }

        //Vaciar el Resultado si ha presionado el boton de IGUAL antes y el nuevo es un numero
        if(lastBnt == R.id.btnIgual && isButtonNumber(currentBtn)){
            txtResultado.setText("");
        }

        String historico = txtHistorico.getText().toString();

        switch (currentBtn) {
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
            case R.id.btnRaiz:
                firstOperator = " sqrt(";
                secondOperator = ")";
                operationEspecial(historico, currentBtn, lastBnt, firstOperator, secondOperator);
                break;
            case R.id.btnCuadrado:
                firstOperator = " (";
                secondOperator = ")^2";
                operationEspecial(historico, currentBtn, lastBnt, firstOperator, secondOperator);
                break;
            case R.id.btnCos:
                firstOperator = " cos(";
                secondOperator = ")";
                operationEspecial(historico, currentBtn, lastBnt, firstOperator, secondOperator);
                break;
            case R.id.btnSen:
                firstOperator = " sin(";
                secondOperator = ")";
                operationEspecial(historico, currentBtn, lastBnt, firstOperator, secondOperator);
                break;
            case R.id.btnTan:
                firstOperator = " tan(";
                secondOperator = ")";
                operationEspecial(historico, currentBtn, lastBnt, firstOperator, secondOperator);
                break;
            case R.id.btnClear:
                txtResultado.setText("");
                txtHistorico.setText("");
                break;
            case R.id.btnBorrar:
                txtResultado.setText(removeLastCharacter(txtResultado.getText().toString()));
                break;
            case R.id.btnSuma:
                operator = " + ";
                operation(historico, currentBtn, lastBnt, operator);
                break;
            case R.id.btnResta:
                operator = " - ";
                operation(historico, currentBtn, lastBnt, operator);
                break;

            case R.id.btnMultiplicacion:
                operator = " X ";
                operation(historico, currentBtn, lastBnt, operator);
                break;
            case R.id.btnDivision:
                operator = " / ";
                operation(historico, currentBtn, lastBnt, operator);
                break;
            case R.id.btnIgual:
                operator = " = ";
                String ecuacion = "";

                //Evitar que este vacio
                if(txtResultado.getText().toString().isEmpty()){
                    currentBtn = 0;
                    showMessage("Favor Ingrese algun valor numerico");
                    break;
                }

                //Esto hace que se agrege el ultimo valor del campo resultado si lo amerita
                if(clearEquals){
                    if (isButtonEspecial(lastBnt)){
                        historico = firstOperator + txtResultado.getText().toString() + secondOperator;
                        historico += operator;
                    }else{
                        historico = txtResultado.getText().toString();
                        historico += operator;
                    }
                    clearEquals = false;
                }else if(isButtonNumber(lastBnt)){
                    historico += txtResultado.getText().toString();
                    historico += operator;
                }else{
                    historico = removeFromString(historico, 3);
                    historico += operator;
                }
                txtHistorico.setText(historico);
                ecuacion = removeFromString(historico, 3);
                Double result = (Double) solveString(ecuacion);
                ecuacion =  String.format("%.4f", result);
                if (ecuacion.equals("Infinity")){
                    showMessage("El resultado tiende al Infinito");
                    txtResultado.setText("");
                }else if (ecuacion.equals("NaN")){
                    showMessage("El resultado es Indefinido");
                    txtResultado.setText("");
                }else{
                    txtResultado.setText(ecuacion);
                }

                clearEquals = true;
                break;
        }

        lastBnt = currentBtn;
    }
}
