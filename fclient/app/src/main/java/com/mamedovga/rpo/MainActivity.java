package com.mamedovga.rpo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    //static {
     //   System.loadLibrary("native-lib");
    //}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onButtonClick(View view) throws Exception {
        TestHttpClient();
        //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    public String getPageTitle(String html) {
        // A compiled representation of a regular expression.
        // A regular expression, specified as a string, must first be compiled into an instance of this class.
        // The resulting pattern can then be used to create a Matcher object that can match
        // arbitrary character sequences against the regular expression.
        // Expression "X+?" means X, one or more times
        // "." means any character
        // Pattern.DOTALL enables dotall mode.
        // In dotall mode, the expression "." matches any character, including a line terminator.
        // By default this expression does not match line terminators.
        Pattern pattern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        String p;
        if(matcher.find())
            p = matcher.group(1);
        else
            p = "Not found";
        return p;
    }

    //метод создаёт и отправляет запрос web серверу
    //получает ответ, извлекает из него тег <title>
    //отображает его на экране
    protected void TestHttpClient() {
        //HTTP запрос - в отдельном потоке, чтобы не блокировать пользовательский интерфейс
        new Thread(() -> {
            try {
                //10.0.2.2 вместо localhost потому, что приложение запускается из эмулятора, а из
                //него доступ к хосту сделан по этой сети (так решили разработчики). Но, если вы
                //запускаете приложение на телефоне, укажите IP компьютера, на котором работает
                //приложение Spring boot и не забудьте настроить Firewall на этом компьютере, так чтобы
                //он пропускал HTTP траффик на порт 8081.
                HttpURLConnection uc = (HttpURLConnection) new URL(
                        "http://10.0.2.2:8081/api/v1/title").openConnection();
                InputStream inputStream = uc.getInputStream(); //результат GET запроса
                String html = IOUtils.toString(inputStream);
                String title = getPageTitle(html);
                runOnUiThread(() -> { //не можем отобразить результат в фоновом потоке, идем в пользовательский поток
                    Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
                });
            } catch (Exception ex) {
                Log.e("tag", "Http client fails", ex);
            }
        }).start();
    }
}
